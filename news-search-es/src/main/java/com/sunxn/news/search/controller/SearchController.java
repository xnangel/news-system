package com.sunxn.news.search.controller;

import com.sunxn.news.search.pojo.News;
import com.sunxn.news.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @data: 2020/4/20 14:05
 * @author: xiaoNan
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 根据关键词进行搜索
     * @param key 关键词
     * @return
     */
    @RequestMapping(value = "/search/key", method = RequestMethod.GET)
    public ResponseEntity<List<News>> searchNewsByKey(
            @RequestParam("key") String key
    ) {
        return ResponseEntity.ok(searchService.searchNewsByKey(key));
    }
}
