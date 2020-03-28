package com.sunxn.news.webcrawler.service;

import com.sunxn.news.webcrawler.pojo.NewsDetail;

/**
 * @description: 新闻详情服务类
 * @data: 2020/3/28 11:00
 * @author: xiaoNan
 */
public interface NewsDetailService {

    /**
     * 保存新闻详情对象
     * @param newsDetail
     * @return
     */
    void save(NewsDetail newsDetail);
}
