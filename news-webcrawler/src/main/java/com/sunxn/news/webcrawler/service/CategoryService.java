package com.sunxn.news.webcrawler.service;

import com.sunxn.news.webcrawler.pojo.Category;

import java.util.List;

/**
 * @description:
 * @data: 2020/3/27 19:49
 * @author: xiaoNan
 */
public interface CategoryService {

    /**
     * 查询所有
     * @return
     */
    public List<Category> findAll();
}
