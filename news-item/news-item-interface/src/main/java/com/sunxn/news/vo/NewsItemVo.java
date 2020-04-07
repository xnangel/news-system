package com.sunxn.news.vo;

import com.sunxn.news.pojo.NewsItem;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: newsItem视图类
 * @data: 2020/4/7 17:12
 * @author: xiaoNan
 */
@Setter
@Getter
public class NewsItemVo extends NewsItem {

    private String categoryName;
    private String createTimeStr;
    private String updateTimeStr;
}
