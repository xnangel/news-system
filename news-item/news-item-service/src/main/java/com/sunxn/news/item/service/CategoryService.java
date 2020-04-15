package com.sunxn.news.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.item.mapper.CategoryMapper;
import com.sunxn.news.item.mapper.NewsItemMapper;
import com.sunxn.news.pojo.Category;
import com.sunxn.news.pojo.NewsItem;
import com.sunxn.news.vo.CategoryNewsItemVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
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
    @Autowired
    private NewsItemMapper newsItemMapper;

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

    /**
     * 根据条件查询category
     * @param key   搜索词
     * @param page  当前页
     * @param rows  每页行数
     * @param sortBy    排序字段
     * @param desc      是否降序
     * @return
     */
    public PageResult<Category> findCategoryByCondition(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Category.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%");
        }
        // 排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Category> categories = categoryMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(categories)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CATEGORIES);
        }
        // 解析分页结果
        PageInfo<Category> categoryPageInfo = new PageInfo<>(categories);
        return new PageResult<>(categoryPageInfo.getTotal(), categoryPageInfo.getPages(), categories);
    }

    /**
     * 更改category的status状态，禁用<--->启用
     * @param id
     */
    public void updateCategoryStatus(Long id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CATEGORY_STATUS_UPDATE_ERROR);
        }
        category.setStatus(!category.getStatus());
        if (categoryMapper.updateByPrimaryKeySelective(category) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CATEGORY_STATUS_UPDATE_ERROR);
        }
    }

    /**
     * 保存category
     * @param category
     */
    public void saveCategory(Category category) {
        // category的name是unique的
        this.isNameExist(category.getName());

        category.setId(null);
        if (categoryMapper.insert(category) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CATEGORY_SAVE_ERROR);
        }
    }

    /**
     * 更新category
     * @param category
     */
    public void updateCategory(Category category) {
        if (categoryMapper.updateByPrimaryKeySelective(category) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CATEGORY_UPDATE_ERROR);
        }
    }

    /**
     * 判断数据库中category的name是否相同, 相同抛出名称相同异常
     * @param name
     */
    private void isNameExist(String name) {
        Category record = new Category();
        record.setName(name);
        Category nameCategory = categoryMapper.selectOne(record);
        if (nameCategory != null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.CATEGORY_NAME_SAME_ERROR);
        }
    }

    /**
     * 根据type查询启用状态的category集合
     * @param type 类型
     * @return
     */
    public List<Category> findCategoriesByType(Integer type) {
        Category category = new Category();
        category.setStatus(true);
        category.setType(type);
        List<Category> categories = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(categories)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CATEGORIES);
        }
        return categories;
    }

    /**
     * 根据type查询category以及对应的newsItem集合
     * @param type
     * @return
     */
    public List<CategoryNewsItemVo> findCategoryNewsItemsListByType(Integer type) {
        Category category = new Category();
        category.setType(type);
        List<Category> categoryList = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CATEGORIES);
        }
        List<CategoryNewsItemVo> categoryNewsItemVoList = new ArrayList<>();
        NewsItem newsItem = new NewsItem();
        categoryList.forEach(c -> {
            CategoryNewsItemVo categoryNewsItemVo = new CategoryNewsItemVo();
            BeanUtils.copyProperties(c, categoryNewsItemVo);
            newsItem.setCategoryId(c.getId());
            List<NewsItem> newsItemList = newsItemMapper.select(newsItem);
            categoryNewsItemVo.setNewsItems(newsItemList);
            categoryNewsItemVoList.add(categoryNewsItemVo);
        });
        return categoryNewsItemVoList;
    }
}
