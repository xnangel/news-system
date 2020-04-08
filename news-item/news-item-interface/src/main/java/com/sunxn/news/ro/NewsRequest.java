package com.sunxn.news.ro;

import com.sunxn.news.pojo.NewsDetail;
import com.sunxn.news.pojo.NewsItem;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 新闻请求参数类
 * @data: 2020/4/8 16:41
 * @author: xiaoNan
 */
@Setter
@Getter
public class NewsRequest extends NewsItem {

    private NewsDetail details;
}
