package com.sunxn.news.api;

import com.sunxn.news.pojo.NewsDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @data: 2020/4/20 15:03
 * @author: xiaoNan
 */
public interface NewsDetailApi {

    @GetMapping("/newsDetail/find/{newsId}")
    NewsDetail findNewsDetailByNewsId(@PathVariable("newsId")Long newsId);
}
