package com.sunxn.news.webcrawler.service.impl;

import com.sunxn.news.webcrawler.dao.CategoryRepository;
import com.sunxn.news.webcrawler.pojo.Category;
import com.sunxn.news.webcrawler.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @data: 2020/3/27 19:51
 * @author: xiaoNan
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> findAll() {
        return categoryRepository.selectAll();
    }
}
