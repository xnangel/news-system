package com.sunxn.news.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:  jwt 载荷对象
 * @data: 2020/4/22 16:06
 * @author: xiaoNan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long id;
    private String username;
}
