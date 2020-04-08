package com.sunxn.news.item.controller;

import com.sunxn.news.item.service.CategoryService;
import com.sunxn.news.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/find/list")
    public ResponseEntity<List<Category>> findAllCategory() {
        return ResponseEntity.ok(categoryService.findAllCategory());
    }
}
