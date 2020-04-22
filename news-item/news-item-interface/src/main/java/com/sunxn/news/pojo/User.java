package com.sunxn.news.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: 用户
 * @data: 2020/4/21 21:44
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_user")
public class User {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String username;
    private String password;
    private String mail;
    private String phone;
    private Integer auth;
    private Date createTime;
    private Date updateTime;
    private String notes;
}
