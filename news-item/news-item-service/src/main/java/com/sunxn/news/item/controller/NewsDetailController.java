package com.sunxn.news.item.controller;

import com.sunxn.news.item.service.NewsDetailService;
import com.sunxn.news.pojo.NewsDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @data: 2020/3/28 20:00
 * @author: xiaoNan
 */
@RestController
@RequestMapping("/newsDetail")
public class NewsDetailController {

    @Autowired
    private NewsDetailService newsDetailService;

    @GetMapping("find/{newsId}")
    public ResponseEntity<NewsDetail> findNewsDetailByNewsId(@PathVariable("newsId")Long newsId) {
        return ResponseEntity.ok(newsDetailService.findNewsDetailByNewsId(newsId));
    }
}
