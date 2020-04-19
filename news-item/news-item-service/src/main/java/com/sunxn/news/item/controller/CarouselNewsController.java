package com.sunxn.news.item.controller;

import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.item.service.CarouselNewsService;
import com.sunxn.news.pojo.CarouselNews;
import com.sunxn.news.vo.CarouselNewsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @data: 2020/4/18 22:26
 * @author: xiaoNan
 */
@RestController
@RequestMapping("/carousel")
public class CarouselNewsController {

    @Autowired
    private CarouselNewsService carouselNewsService;

    /**
     * 根据条件查询carouselNews页信息
     * @param key   过滤关键词
     * @param page  当前页
     * @param rows  每页行数
     */
    @GetMapping("/find/condition")
    public ResponseEntity<PageResult<CarouselNewsVo>> findCarouselNewsListByCondition(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "isDelete", defaultValue = "false") Boolean isDelete
    ) {
        return ResponseEntity.ok(carouselNewsService.findCarouselNewsListByCondition(key, page, rows, isDelete));
    }

    /**
     * 根据id暂时删除carouselNews，即删除到垃圾箱
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/temporarily/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCarouselNewsTemporarily(@PathVariable("id") Long id) {
        carouselNewsService.deleteCarouselNewsTemporarily(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据id永久删除carouselNews，从数据库上彻底删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/permanent/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCarouselNewsPermanent(@PathVariable("id") Long id) {
        carouselNewsService.deleteCarouselNewsPermanent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据ids 永久 批量 删除carouselNews
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete/permanent/list/{ids}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCarouselNewsListPermanent(@PathVariable("ids")List<Long> ids) {
        carouselNewsService.deleteCarouselNewsListPermanent(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据id 还原carouselNews，即把垃圾箱中的该数据还原
     * @param id
     * @return
     */
    @RequestMapping(value = "/reduction/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> reductionCarouselNewsById(@PathVariable("id") Long id) {
        carouselNewsService.reductionCarouselNewsById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据ids 批量 还原carouselNews集合
     * @param ids
     * @return
     */
    @RequestMapping(value = "/reduction/list/{ids}", method = RequestMethod.PUT)
    public ResponseEntity<Void> reductionCarouselNewsList(@PathVariable("ids") List<Long> ids) {
        carouselNewsService.reductionCarouselNewsList(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 保存carouselNews轮播图新闻信息
     * @param carouselNews
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<Void> saveCarouselNews(@RequestBody CarouselNews carouselNews) {
        System.out.println(carouselNews);
        carouselNewsService.saveCarouselNews(carouselNews);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新carouselNews轮播图新闻信息
     * @param carouselNews
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCarouselNews(@RequestBody CarouselNews carouselNews) {
        System.out.println(carouselNews);
        carouselNewsService.updateCarouselNews(carouselNews);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据categoryId查询分类下的轮播图新闻信息
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/find/list/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<List<CarouselNews>> findCarouselNewsListByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(carouselNewsService.findCarouselNewsListByCategoryId(categoryId));
    }
}
