package com.sunxn.news.webcrawler.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 新闻类目类
 * @data: 2020/3/27 19:42
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_category")
public class Category {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Integer sort;
}
