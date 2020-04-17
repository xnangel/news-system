package com.sunxn.news.webcrawler.service.impl;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.webcrawler.dao.CarouselNewsRepository;
import com.sunxn.news.webcrawler.pojo.CarouselNews;
import com.sunxn.news.webcrawler.service.CarouselNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @data: 2020/4/16 22:41
 * @author: xiaoNan
 */
@Slf4j
@Service
public class CarouselNewsServiceImpl implements CarouselNewsService {

    @Autowired
    private CarouselNewsRepository carouselNewsRepository;

    @Override
    public void save(CarouselNews carouselNews) {

        if (this.carouselNewsRepository.insertSelective(carouselNews) != 1) {
            log.error("【爬虫服务】carouselNews信息保存失败, carouselNews的title={}, imageUrl={}", carouselNews.getTitle(), carouselNews.getImageUrl());
            throw new SunxnNewsException(NewsSystemExceptionEnum.CAROUSEL_NEWS_SAVE_ERROR);
        }
    }
}
