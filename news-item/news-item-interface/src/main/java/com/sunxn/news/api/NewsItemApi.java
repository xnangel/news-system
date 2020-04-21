package com.sunxn.news.api;

import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.pojo.NewsItem;
import com.sunxn.news.vo.NewsItemVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @data: 2020/4/20 15:04
 * @author: xiaoNan
 */
public interface NewsItemApi {

    @GetMapping("/newsItem/find/list")
    PageResult<NewsItemVo> findNewsItemsByCondition(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "true") Boolean desc,
            @RequestParam(value = "isSend", required = false) Boolean isSend,
            @RequestParam(value = "isToday", defaultValue = "false") Boolean isToday,
            @RequestParam(value = "isDelete", defaultValue = "false") Boolean isDelete
    );

    @GetMapping("/newsItem/find/newsItem/{id}")
    NewsItem findNewsItemById(@PathVariable("id") Long id);
}
