package com.sunxn.news.vo;

import com.sunxn.news.pojo.Category;
import com.sunxn.news.pojo.NewsItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description:
 * @data: 2020/4/15 11:12
 * @author: xiaoNan
 */
@Setter
@Getter
public class CategoryNewsItemVo extends Category {

    private List<NewsItem> newsItems;
}
