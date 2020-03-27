package com.sunxn.news.webcrawler.test;

import com.sunxn.news.webcrawler.pojo.Category;
import com.sunxn.news.webcrawler.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description: 通用mapper测试
 * @data: 2020/3/27 19:56
 * @author:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findAll() {
        List<Category> list = categoryService.findAll();
        for (Category category : list) {
            System.out.println(category);
        }
    }
}
