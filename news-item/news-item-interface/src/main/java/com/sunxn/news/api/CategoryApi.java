package com.sunxn.news.api;

import com.sunxn.news.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @data: 2020/4/20 15:03
 * @author: xiaoNan
 */
public interface CategoryApi {

    @GetMapping("/category/find/category/id")
    Category findCategoryById(@RequestParam("id") Long id);
}
