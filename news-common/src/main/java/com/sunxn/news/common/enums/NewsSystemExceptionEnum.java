package com.sunxn.news.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 新闻系统 异常枚举类
 * @data: 2020/3/28 14:30
 * @author: xiaoNan
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum NewsSystemExceptionEnum {

    NEWS_ITEM_SAVE_FAIL(500, "新闻保存失败"),
    NEWS_DETAILS_SAVE_FAIL(500, "新闻详情保存失败"),
    ;

    private int code;
    private String msg;
}
