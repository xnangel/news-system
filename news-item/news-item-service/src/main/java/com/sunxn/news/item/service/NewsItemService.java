package com.sunxn.news.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.utils.DateUtil;
import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.item.mapper.CarouselNewsMapper;
import com.sunxn.news.item.mapper.CategoryMapper;
import com.sunxn.news.item.mapper.NewsDetailMapper;
import com.sunxn.news.item.mapper.NewsItemMapper;
import com.sunxn.news.pojo.CarouselNews;
import com.sunxn.news.pojo.Category;
import com.sunxn.news.pojo.NewsDetail;
import com.sunxn.news.pojo.NewsItem;
import com.sunxn.news.ro.NewsRequest;
import com.sunxn.news.vo.NewsItemVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @data: 2020/3/28 19:57
 * @author: xiaoNan
 */
@Service
public class NewsItemService {

    @Autowired
    private NewsItemMapper newsItemMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private NewsDetailMapper newsDetailMapper;
    @Autowired
    private CarouselNewsMapper carouselNewsMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据新闻类目id查询 非垃圾箱的 新闻（如：客户端的新闻浏览，需要满足发布并且是今日更新）
     * @param categoryId    新闻分类id
     * @param isSent        是否已经发布
     * @param isTodayUpdate 是否是今日更新
     */
    public List<NewsItem> findNewsItemsByCategoryId(Long categoryId, Boolean isSent, Boolean isTodayUpdate) {
        Example example = new Example(NewsItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId", categoryId);
        // 排除掉在垃圾箱中的
        criteria.andEqualTo("isDelete", false);
        // 是否已经发布
        if (isSent != null) {
            criteria.andEqualTo("status", isSent);
        }
        // 是否是今日更新，即是否是今日新闻
        if (isTodayUpdate != null && isTodayUpdate) {
            Date nowTime = new Date();
            Date timeStart = DateUtil.getStartOfDay(nowTime);
            Date timeEnd = DateUtil.getEndOfDay(nowTime);
            criteria.andGreaterThanOrEqualTo("updateTime", DateUtil.parseDateToStr(timeStart, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS));
            criteria.andLessThanOrEqualTo("updateTime", DateUtil.parseDateToStr(timeEnd, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS));
        }
        List<NewsItem> newsItems = newsItemMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(newsItems)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_ITEM);
        }
        return newsItems;
    }

    /**
     * 根据条件查询newsItem分页结果
     * @param page  当前页
     * @param rows  每页行数
     * @param key   关键词
     * @param sortBy    排序字段
     * @param desc     是否降序
     * @param isSend    是否发布
     * @param isToday   是否是今日新闻
     * @param isDelete  是否是被删除到垃圾箱中的数据
     * @return
     */
    public PageResult<NewsItemVo> findNewsItemsByCondition(Integer page, Integer rows, String key, String sortBy, Boolean desc, Boolean isSend, Boolean isToday, Boolean isDelete) {
        // 分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(NewsItem.class);
        Example.Criteria criteria = example.createCriteria();
        // 是否在垃圾箱中
        criteria.andEqualTo("isDelete", isDelete);
        // 搜索字段过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (isSend != null) {
            criteria.andEqualTo("status", isSend);
        }
        if (isToday) {
            Date nowTime = new Date();
            Date timeStart = DateUtil.getStartOfDay(nowTime);
            Date timeEnd = DateUtil.getEndOfDay(nowTime);
            criteria.andGreaterThanOrEqualTo("createTime", DateUtil.parseDateToStr(timeStart, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS));
            criteria.andLessThanOrEqualTo("createTime", DateUtil.parseDateToStr(timeEnd, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS));
        }
        // 排序
        String orderStr = "";
        if ("createTime".equals(sortBy)) {
            orderStr = "create_time ";
        } else if ("updateTime".equals(sortBy)) {
            orderStr = "update_time";
        }
        if (StringUtils.isNotBlank(orderStr)) {
            orderStr += desc ? " DESC" : " ASC";
            example.setOrderByClause(orderStr);
        }
        // 查询
        List<NewsItem> newsItems = newsItemMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(newsItems)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_ITEM);
        }

        // 封装categoryName属性
        List<NewsItemVo> newsItemVoList = new ArrayList<>();
        newsItems.forEach(newsItem -> {
            NewsItemVo newsItemVo = new NewsItemVo();
            BeanUtils.copyProperties(newsItem, newsItemVo);
            Category category = categoryMapper.selectByPrimaryKey(newsItem.getCategoryId());
            newsItemVo.setCategoryName(category.getName());
            newsItemVo.setCreateTimeStr(DateUtil.parseDateToStr(newsItem.getCreateTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS));
            newsItemVo.setUpdateTimeStr(DateUtil.parseDateToStr(newsItem.getUpdateTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS));
            newsItemVoList.add(newsItemVo);
        });
        // 解析newsItemVo
        PageInfo<NewsItem> newsItemPageInfo = new PageInfo<>(newsItems);
        return new PageResult<>(newsItemPageInfo.getTotal(), newsItemPageInfo.getPages(), newsItemVoList);
    }

    /**
     * 根据newsId更新newsItem发布状态
     * @param newsId
     */
    public void updateNewsItemStatus(Integer newsId) {
        NewsItem newsItem = newsItemMapper.selectByPrimaryKey(newsId);
        if (newsItem == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_STATUS_UPDATE_ERROR);
        }
        newsItem.setStatus(!newsItem.getStatus());
        newsItem.setUpdateTime(new Date());
        if (newsItemMapper.updateByPrimaryKeySelective(newsItem) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_STATUS_UPDATE_ERROR);
        }

        // 发送mq消息，发布状态的添加到es数据库，非发布状态的从es数据库中删除
        if (newsItem.getStatus()) {
            amqpTemplate.convertAndSend("item.insert", newsId);
        } else {
            amqpTemplate.convertAndSend("item.delete", newsId);
        }
    }

    /**
     * 通过id删除对应的newsItem和newsDetail，永久性的删除，彻底删除
     * @param newsId
     */
    @Transactional
    public void deleteNewsItemPermanentlyById(Long newsId) {
        NewsDetail newsDetail = new NewsDetail();
        newsDetail.setNewsId(newsId);
        if (newsDetailMapper.delete(newsDetail) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_DETAIL_DELETE_ERROR);
        }
        if (newsItemMapper.deleteByPrimaryKey(newsId) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_DELETE_ERROR);
        }
        CarouselNews record = new CarouselNews();
        record.setNewsId(newsId);
        CarouselNews carouselNews = carouselNewsMapper.selectOne(record);
        if (carouselNews != null) {
            if (carouselNewsMapper.deleteByPrimaryKey(carouselNews) != 1) {
                throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_PERMANENT_ERROR);
            }
        }

        amqpTemplate.convertAndSend("item.delete", newsId);
    }

    /**
     * 保存news信息
     * @param newsRequest
     */
    @Transactional
    public void saveNews(NewsRequest newsRequest) {
        NewsItem newsItem = new NewsItem();
        BeanUtils.copyProperties(newsRequest, newsItem);
        newsItem.setId(null);
        newsItem.setCreateTime(new Date());
        newsItem.setUpdateTime(new Date());
        if (newsItemMapper.insert(newsItem) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_SAVE_FAIL);
        }
        NewsDetail newsDetail = new NewsDetail();
        BeanUtils.copyProperties(newsRequest.getDetails(), newsDetail);
        newsDetail.setId(null);
        newsDetail.setNewsId(newsItem.getId());
        if (newsDetail.getNewsId() == null || newsDetailMapper.insert(newsDetail) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_DETAILS_SAVE_FAIL);
        }

        amqpTemplate.convertAndSend("item.insert", newsRequest.getId());
    }

    /**
     * 更新、编辑news信息
     * @param newsRequest
     */
    @Transactional
    public void updateNews(NewsRequest newsRequest) {
        NewsItem newsItem = new NewsItem();
        BeanUtils.copyProperties(newsRequest, newsItem);
        if (newsItemMapper.updateByPrimaryKeySelective(newsItem) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_UPDATE_ERROR);
        }

        NewsDetail newsDetail = new NewsDetail();
        BeanUtils.copyProperties(newsRequest.getDetails(), newsDetail);
        if (newsDetailMapper.updateByPrimaryKeySelective(newsDetail) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_DETAILS_UPDATE_ERROR);
        }

        amqpTemplate.convertAndSend("item.insert", newsRequest.getId());
    }

    /**
     * 根据id查询newsItem
     * @param id
     * @return
     */
    public NewsItem findNewsItemById(Long id) {
        NewsItem newsItem = newsItemMapper.selectByPrimaryKey(id);
        if (newsItem == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_ITEM);
        }
        return newsItem;
    }

    /**
     * 根据newsItem ids 永久 批量删除 newsItem和newsDetail信息,不可还原
     * @param ids
     */
    @Transactional
    public void deleteNewsItemListPermanently(List<Long> ids) {
        ids.forEach(id -> {
            deleteNewsItemPermanentlyById(id);
        });
    }

    /**
     * 根据newsItem id 把newsItem删除到垃圾箱中，可还原的
     * @param id
     */
    public void deleteNewsItemTemporarilyById(Long id) {
        NewsItem newsItem = newsItemMapper.selectByPrimaryKey(id);
        if (newsItem == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_ITEM);
        }
        newsItem.setIsDelete(true);
        if (newsItemMapper.updateByPrimaryKeySelective(newsItem) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_DELETE_TEMPORARILY_ERROR);
        }

        amqpTemplate.convertAndSend("item.delete", id);
    }

    /**
     * 根据id 把newsItem从垃圾箱中还原
     * @param id
     */
    public void reductionNewsItemById(Long id) {
        NewsItem newsItem = newsItemMapper.selectByPrimaryKey(id);
        if (newsItem == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_ITEM);
        }
        newsItem.setIsDelete(false);
        if (newsItemMapper.updateByPrimaryKeySelective(newsItem) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_REDUCTION_ERROR);
        }

        amqpTemplate.convertAndSend("item.insert", id);
    }

    /**
     * 根据ids 批量把newsItem从垃圾相中还原
     * @param ids
     */
    @Transactional
    public void reductionNewsItemList(List<Long> ids) {
        ids.forEach(id -> {
            reductionNewsItemById(id);
        });
    }
}
