package com.sunxn.news.webcrawler.service;

import com.sunxn.news.webcrawler.pojo.NewsItem;

/**
 * @description: 新闻元素服务类
 * @data: 2020/3/27 16:43
 * @author: xiaoNan
 */
public interface NewsItemService {

    /**
     * 保存新闻元素对象
     * @param newsItem
     * @return
     */
    void save(NewsItem newsItem);
}
