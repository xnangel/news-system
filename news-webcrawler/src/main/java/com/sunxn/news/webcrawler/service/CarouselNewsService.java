package com.sunxn.news.webcrawler.service;

import com.sunxn.news.webcrawler.pojo.CarouselNews;

/**
 * @description:
 * @data: 2020/4/16 22:41
 * @author: xiaoNan
 */
public interface CarouselNewsService {

    /**
     * 保存carouselNews信息
     * @param carouselNews
     */
    public void save(CarouselNews carouselNews);
}
