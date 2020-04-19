package com.sunxn.news.webcrawler.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @description: 新闻实体
 * @data: 2020/3/27 15:56
 * @author: XiaoNan
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
    private Date createTime;
    private Date updateTime;
    private String url;
    private Boolean isDelete;

    @Transient
    private String categoryName;
}
