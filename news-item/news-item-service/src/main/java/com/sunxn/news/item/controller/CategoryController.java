package com.sunxn.news.item.controller;

import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.item.service.CategoryService;
import com.sunxn.news.pojo.Category;
import com.sunxn.news.vo.CategoryNewsItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @data: 2020/3/28 17:08
 * @author: xiaoNan
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有category
     * @return
     */
    @GetMapping("/find/list")
    public ResponseEntity<List<Category>> findAllCategory() {
        return ResponseEntity.ok(categoryService.findAllCategory());
    }

    /**
     * 根据类型查询category集合，注意：category是启用状态的
     * @param type
     * @return
     */
    @GetMapping("/find/{type}")
    public ResponseEntity<List<Category>> findCategoriesByType(@PathVariable("type") Integer type) {
        return ResponseEntity.ok(categoryService.findCategoriesByType(type));
    }

    /**
     * 根据page，key等条件查询category
     * @return
     */
    @GetMapping("/find/page")
    public ResponseEntity<PageResult<Category>> findCategoryByCondition(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc
    ) {
        return ResponseEntity.ok(categoryService.findCategoryByCondition(key, page, rows, sortBy, desc));
    }

    /**
     * 根据id更新category的status状态
     * @param id
     * @return
     */
    @RequestMapping(value = "update/status/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCategoryStatus(@PathVariable("id") Long id) {
        categoryService.updateCategoryStatus(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 保存category
     * @param category
     * @return
     */
    @RequestMapping(value = "/save/category", method = RequestMethod.POST)
    public ResponseEntity<Void> saveCategory(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新category
     * @param category
     * @return
     */
    @RequestMapping(value = "/save/category", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据type查询category和对应的newsItem集合
     * @param type
     * @return
     */
    @GetMapping("/find/categoryNewsItem/{type}")
    public ResponseEntity<List<CategoryNewsItemVo>> findCategoryNewsItemsListByType(@PathVariable("type") Integer type) {
        return ResponseEntity.ok(categoryService.findCategoryNewsItemsListByType(type));
    }
}
