package com.sunxn.news.item.service;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.item.mapper.NewsDetailMapper;
import com.sunxn.news.pojo.NewsDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @data: 2020/3/28 19:58
 * @author: xiaoNan
 */
@Service
public class NewsDetailService {

    @Autowired
    private NewsDetailMapper newsDetailMapper;

    /**
     * 根据新闻id查询新闻详情
     * @param newsId
     * @return
     */
    public NewsDetail findNewsDetailByNewsId(Long newsId) {
        NewsDetail newsDetail = new NewsDetail();
        newsDetail.setNewsId(newsId);
        NewsDetail detail = newsDetailMapper.selectOne(newsDetail);
        if (detail == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_DETAIL);
        }
        return detail;
    }
}
