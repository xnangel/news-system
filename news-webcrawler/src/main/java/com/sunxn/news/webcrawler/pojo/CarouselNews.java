package com.sunxn.news.webcrawler.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 新闻轮播对象
 * @data: 2020/4/16 16:39
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_news_carousel")
public class CarouselNews {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long categoryId;
    private Long newsId;
    private String title;
    private String imageUrl;
    private String notes;
    private Integer sorted;
    private Boolean isDelete;
}
