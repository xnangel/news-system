package com.sunxn.news.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description:
 * @data: 2020/3/28 17:16
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
