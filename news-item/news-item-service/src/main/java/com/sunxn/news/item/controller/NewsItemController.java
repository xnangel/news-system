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
            @RequestParam(value = "isToday", defaultValue = "false") Boolean isToday,
            @RequestParam(value = "isDelete", defaultValue = "false") Boolean isDelete
    ) {
        return ResponseEntity.ok(newsItemService.findNewsItemsByCondition(page, rows, key, sortBy, desc, isSend, isToday, isDelete));
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
     * 根据newsItem id删除newsItem和newsDetail信息 永久删除，即从数据库中删除
     */
    @RequestMapping(value = "/delete/permanently/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteNewsItemPermanentlyById(@PathVariable("id") Long newsId) {
        newsItemService.deleteNewsItemPermanentlyById(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据newsItem ids 永久 批量删除 newsItem和newsDetail信息,不可还原
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete/permanently/list/{ids}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteNewsItemListPermanently(@PathVariable("ids") List<Long> ids) {
        newsItemService.deleteNewsItemListPermanently(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据newsItem id 把newsItem删除到垃圾箱中，可还原的
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/temporarily/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteNewsItemTemporarilyById(@PathVariable("id") Long id) {
        newsItemService.deleteNewsItemTemporarilyById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据id 把newsItem从垃圾箱中还原
     * @param id
     * @return
     */
    @RequestMapping(value = "/reduction/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> reductionNewsItemById(@PathVariable("id") Long id) {
        newsItemService.reductionNewsItemById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据ids 批量把newsItem从垃圾相中还原
     * @param ids
     * @return
     */
    @RequestMapping(value = "/reduction/list/{ids}", method = RequestMethod.PUT)
    public ResponseEntity<Void> reductionNewsItemList(@PathVariable("ids") List<Long> ids) {
        newsItemService.reductionNewsItemList(ids);
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

    /**
     * 根据categoryId查询newsItem
     * @param categoryId
     * @return
     */
    @GetMapping("find/{categoryId}")
    public ResponseEntity<List<NewsItem>> findNewsItemsByCategoryId(
            @PathVariable("categoryId")Long categoryId,
            @RequestParam(value = "isSent", required = false) Boolean isSent,
            @RequestParam(value = "isTodayUpdate", required = false) Boolean isTodayUpdate
    ) {
        return ResponseEntity.ok(newsItemService.findNewsItemsByCategoryId(categoryId, isSent, isTodayUpdate));
    }

    /**
     * 根据id查询newsItem
     * @param id
     * @return
     */
    @GetMapping("/find/newsItem/{id}")
    public ResponseEntity<NewsItem> findNewsItemById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsItemService.findNewsItemById(id));
    }
}
