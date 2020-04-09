package com.sunxn.news.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description:
 * @data: 2020/3/28 17:11
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_category")
public class Category {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Integer type;
    private Boolean status;
    private String notes;
}
