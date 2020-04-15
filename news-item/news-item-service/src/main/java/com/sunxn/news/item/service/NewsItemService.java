package com.sunxn.news.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.utils.DateUtil;
import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.item.mapper.CategoryMapper;
import com.sunxn.news.item.mapper.NewsDetailMapper;
import com.sunxn.news.item.mapper.NewsItemMapper;
import com.sunxn.news.pojo.Category;
import com.sunxn.news.pojo.NewsDetail;
import com.sunxn.news.pojo.NewsItem;
import com.sunxn.news.ro.NewsRequest;
import com.sunxn.news.vo.NewsItemVo;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 根据新闻类目id查询新闻
     * @param categoryId
     * @return
     */
    public List<NewsItem> findNewsItemsByCategoryId(Long categoryId) {
        NewsItem newsItem = new NewsItem();
        newsItem.setCategoryId(categoryId);
        List<NewsItem> newsItems = newsItemMapper.select(newsItem);
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
     * @return
     */
    public PageResult<NewsItemVo> findNewsItemsByCondition(Integer page, Integer rows, String key, String sortBy, Boolean desc, Boolean isSend, Boolean isToday) {
        // 分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(NewsItem.class);
        Example.Criteria criteria = example.createCriteria();
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
    }

    /**
     * 通过id删除对应的newsItem和newsDetail
     * @param newsId
     */
    @Transactional
    public void deleteNewsItemById(Long newsId) {
        NewsDetail newsDetail = new NewsDetail();
        newsDetail.setNewsId(newsId);
        if (newsDetailMapper.delete(newsDetail) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_DETAIL_DELETE_ERROR);
        }
        if (newsItemMapper.deleteByPrimaryKey(newsId) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_DELETE_ERROR);
        }
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
}
