package com.sunxn.news.webcrawler.service.impl;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.webcrawler.dao.NewsDetailRepository;
import com.sunxn.news.webcrawler.pojo.NewsDetail;
import com.sunxn.news.webcrawler.service.NewsDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 新闻详情实现类
 * @data: 2020/3/28 11:01
 * @author: xiaoNan
 */
@Slf4j
@Service
public class NewsDetailServiceImpl implements NewsDetailService {

    @Autowired
    private NewsDetailRepository newsDetailRepository;

    @Override
    public void save(NewsDetail newsDetail) {
        if (newsDetailRepository.insertSelective(newsDetail) != 1) {
            log.error("【爬虫服务】 newsDetail保存失败，newsDetail={}", newsDetail);
            // 保存失败，抛异常
            throw new SunxnNewsException(NewsSystemExceptionEnum.NEWS_DETAILS_SAVE_FAIL);
        }
    }
}
