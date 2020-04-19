package com.sunxn.news.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.item.mapper.CarouselNewsMapper;
import com.sunxn.news.item.mapper.CategoryMapper;
import com.sunxn.news.item.mapper.NewsItemMapper;
import com.sunxn.news.pojo.CarouselNews;
import com.sunxn.news.pojo.NewsItem;
import com.sunxn.news.vo.CarouselNewsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.el.parser.BooleanNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @data: 2020/4/18 22:27
 * @author: xiaoNan
 */
@Service
@Slf4j
public class CarouselNewsService {

    @Autowired
    private CarouselNewsMapper carouselNewsMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private NewsItemMapper newsItemMapper;

    /**
     * 根据条件查询carouselNews
     * @param key   关键词
     * @param page  当前页
     * @param rows  每页大小
     * @return
     */
    public PageResult<CarouselNewsVo> findCarouselNewsListByCondition(String key, Integer page, Integer rows, Boolean isDelete) {
        PageHelper.startPage(page, rows);

        Example example = new Example(CarouselNews.class);
        Example.Criteria criteria = example.createCriteria();
        // 非在垃圾箱中的
        criteria.andEqualTo("isDelete", isDelete);
        // 关键词过滤
        if (!StringUtils.isBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        List<CarouselNews> carouselNews = carouselNewsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(carouselNews)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CAROUSEL_NEWS);
        }

        PageInfo<CarouselNews> carouselNewsPageInfo = new PageInfo<>(carouselNews);
        List<CarouselNewsVo> carouselNewsVoList = new ArrayList<>();
        carouselNews.forEach(carousel -> {
            CarouselNewsVo carouselNewsVo = new CarouselNewsVo();
            BeanUtils.copyProperties(carousel, carouselNewsVo);
            carouselNewsVo.setCategoryName(categoryMapper.selectByPrimaryKey(carousel.getCategoryId()).getName());
            carouselNewsVoList.add(carouselNewsVo);
        });
        return new PageResult<>(carouselNewsPageInfo.getTotal(), carouselNewsPageInfo.getPageNum(), carouselNewsVoList);
    }

    /**
     * 根据id暂时删除carouselNews，即删除到垃圾箱
     * @param id
     */
    public void deleteCarouselNewsTemporarily(Long id) {
        CarouselNews carouselNews = carouselNewsMapper.selectByPrimaryKey(id);
        if (carouselNews == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CAROUSEL_NEWS);
        }
        carouselNews.setIsDelete(true);
        if (carouselNewsMapper.updateByPrimaryKeySelective(carouselNews) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_TEMPORARILY_ERROR);
        }
    }

    /**
     * 保存carouselNews
     * @param carouselNews
     */
    @Transactional
    public void saveCarouselNews(CarouselNews carouselNews) {
        if (this.isSameCarouselNews(carouselNews)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_IS_SAME);
        }
        carouselNews.setId(null);
        this.disableNewsAndSetNewsTitle(carouselNews);
        if (carouselNewsMapper.insertSelective(carouselNews) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_SAVE_ERROR);
        }
    }

    /**
     * 根据categoryId和newsId判断carouselNews是否相同
     * @param carouselNews
     * @return
     */
    private Boolean isSameCarouselNews(CarouselNews carouselNews) {
        CarouselNews record = new CarouselNews();
        record.setCategoryId(carouselNews.getCategoryId());
        record.setNewsId(carouselNews.getNewsId());
        CarouselNews carousel = carouselNewsMapper.selectOne(record);
        return carousel != null;
    }

    /**
     * 禁用该新闻并且获取新闻标题
     * @param carouselNews
     */
    private void disableNewsAndSetNewsTitle(CarouselNews carouselNews) {
        NewsItem newsItem = newsItemMapper.selectByPrimaryKey(carouselNews.getNewsId());
        if (newsItem == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_ITEM);
        }
        newsItem.setStatus(false);
        if (newsItemMapper.updateByPrimaryKeySelective(newsItem) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_STATUS_UPDATE_ERROR);
        }
        carouselNews.setTitle(newsItem.getTitle());
    }

    /**
     * 更新编辑carouselNews
     * @param carouselNews
     */
    @Transactional
    public void updateCarouselNews(CarouselNews carouselNews) {
        this.disableNewsAndSetNewsTitle(carouselNews);

        if (carouselNewsMapper.updateByPrimaryKeySelective(carouselNews) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_UPDATE_ERROR);
        }
    }

    /**
     * 彻底删除carouselNews
     * @param id
     */
    public void deleteCarouselNewsPermanent(Long id) {
        if (carouselNewsMapper.deleteByPrimaryKey(id) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_PERMANENT_ERROR);
        }
    }

    /**
     * 根据ids 批量 删除carouselNews
     * @param ids
     */
    @Transactional
    public void deleteCarouselNewsListPermanent(List<Long> ids) {
        ids.forEach(id -> {
            deleteCarouselNewsPermanent(id);
        });
    }

    /**
     * 根据id 还原carouselNews
     * @param id
     */
    public void reductionCarouselNewsById(Long id) {
        CarouselNews carouselNews = carouselNewsMapper.selectByPrimaryKey(id);
        carouselNews.setIsDelete(false);
        if (carouselNewsMapper.updateByPrimaryKeySelective(carouselNews) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_REDUCTION_ERROR);
        }
    }

    /**
     * 根据ids 批量 还原carouselNews
     * @param ids
     */
    @Transactional
    public void reductionCarouselNewsList(List<Long> ids) {
        ids.forEach(id -> {
            reductionCarouselNewsById(id);
        });
    }

    /**
     * 根据categoryId查询分类下的轮播图新闻信息
     * @param categoryId
     * @return
     */
    public List<CarouselNews> findCarouselNewsListByCategoryId(Long categoryId) {
        CarouselNews carouselNews = new CarouselNews();
        carouselNews.setCategoryId(categoryId);
        carouselNews.setIsDelete(false);
        List<CarouselNews> carouselNewsList = carouselNewsMapper.select(carouselNews);
        if (CollectionUtils.isEmpty(carouselNewsList)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CAROUSEL_NEWS);
        }
        return carouselNewsList;
    }
}
