package com.sunxn.news.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 轮播图新闻对象
 * @data: 2020/4/16 11:27
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
