package com.sunxn.news.item.service;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.item.mapper.CategoryMapper;
import com.sunxn.news.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:
 * @data: 2020/3/28 19:56
 * @author: xiaoNan
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询所有新闻类目
     * @return
     */
    public List<Category> findAllCategory() {
        List<Category> categories = categoryMapper.selectAll();
        if (CollectionUtils.isEmpty(categories)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CATEGORIES);
        }
        return categories;
    }
}
