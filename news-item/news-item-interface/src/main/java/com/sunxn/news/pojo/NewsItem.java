package com.sunxn.news.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description:
 * @data: 2020/3/28 17:16
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_news_item")
public class NewsItem {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String title;
    private Long categoryId;
    private Boolean status;
    private String url;
    private Date createTime;
    private Date updateTime;
}
