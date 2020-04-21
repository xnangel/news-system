package com.sunxn.news.search.client;

import com.sunxn.news.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @data: 2020/4/20 16:20
 * @author: xiaoNan
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void queryCategoryById() {
        Category category = categoryClient.findCategoryById(17L);
        System.out.println(category);
    }
}
