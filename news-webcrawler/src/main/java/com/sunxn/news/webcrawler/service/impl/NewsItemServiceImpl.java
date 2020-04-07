package com.sunxn.news.webcrawler.service.impl;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.webcrawler.dao.CategoryRepository;
import com.sunxn.news.webcrawler.dao.NewsItemRepository;
import com.sunxn.news.webcrawler.pojo.Category;
import com.sunxn.news.webcrawler.pojo.NewsItem;
import com.sunxn.news.webcrawler.service.NewsItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 新闻元素服务实现类
 * @data: 2020/3/28 11:07
 * @author: xiaoNan
 */
@Slf4j
@Service
public class NewsItemServiceImpl implements NewsItemService {

    @Autowired
    private NewsItemRepository newsItemRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void save(NewsItem newsItem) {
        if (StringUtils.isBlank(newsItem.getCategoryName())) {
            log.info("【爬虫服务】 新闻列表categoryName为空");
            return;
        }
        Category category = this.findCategoryByCategoryName(newsItem.getCategoryName());
        if (category == null) {
            log.info("【爬虫服务】 未找到categoryName={}的类别对象", newsItem.getCategoryName());
            return;
        }
        newsItem.setCategoryId(category.getId());
        if (newsItemRepository.insertSelective(newsItem) != 1) {
            // 保存失败，报异常
            log.error("【爬虫服务】 newsItem保存失败，newsItem title={}, categoryName={}", newsItem.getTitle(), newsItem.getCategoryName());
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_ITEM_SAVE_FAIL);
        }
    }

    /**
     * 根据名称查询Category
     * @param name
     * @return
     */
    private Category findCategoryByCategoryName(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.selectOne(category);
    }
}
