package com.sunxn.news.webcrawler.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 新闻详情类
 * @data: 2020/3/27 15:59
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_news_detail")
public class NewsDetail {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long newsId;
    private String come;
    private String content;
    private String keyword;
    private String notes;
}
