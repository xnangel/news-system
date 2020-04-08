package com.sunxn.news.item.controller;

import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.item.service.NewsItemService;
import com.sunxn.news.pojo.NewsItem;
import com.sunxn.news.ro.NewsRequest;
import com.sunxn.news.vo.NewsItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @data: 2020/3/28 19:59
 * @author: xiaoNan
 */
@RestController
@RequestMapping("/newsItem")
public class NewsItemController {

    @Autowired
    private NewsItemService newsItemService;

    /**
     * 查询newsItem列表，根据分页查询、关键字等过滤
     */
    @GetMapping("find/list")
    public ResponseEntity<PageResult<NewsItemVo>> findNewsItemsByCondition(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "true") Boolean desc,
            @RequestParam(value = "isSend", required = false) Boolean isSend,
            @RequestParam(value = "isToday", defaultValue = "false") Boolean isToday
    ) {
        return ResponseEntity.ok(newsItemService.findNewsItemsByCondition(page, rows, key, sortBy, desc, isSend, isToday));
    }

    /**
     * 修改newsItem发布状态
     */
    @RequestMapping(value = "status/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateNewsItemStatus(@PathVariable("id") Integer newsId) {
        newsItemService.updateNewsItemStatus(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据newsItem id删除newsItem和newsDetail信息
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteNewsItemById(@PathVariable("id") Long newsId) {
        newsItemService.deleteNewsItemById(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * news新增
     * @return
     */
    @PostMapping("/news")
    public ResponseEntity<Void> saveNews(@RequestBody NewsRequest newsRequest) {
        newsItemService.saveNews(newsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * news更新
     * @return
     */
    @PutMapping("/news")
    public ResponseEntity<Void> updateNews(@RequestBody NewsRequest newsRequest) {
        newsItemService.updateNews(newsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("find/{categoryId}")
    public ResponseEntity<List<NewsItem>> findNewsItemsByCategoryId(@PathVariable("categoryId")Long categoryId) {
        return ResponseEntity.ok(newsItemService.findNewsItemsByCategoryId(categoryId));
    }
}
