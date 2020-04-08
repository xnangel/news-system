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
    NOT_FOUND_CATEGORIES(404, "未找到新闻类目"),
    NOT_FOUND_NEWS_ITEM(404, "未找到相关新闻"),
    NOT_FOUND_NEWS_DETAIL(404, "未找到新闻详情"),
    NEWS_ITEM_STATUS_UPDATE_ERROR(404, "新闻发布状态更新失败"),
    NEWS_ITEM_DELETE_ERROR(500, "新闻删除失败"),
    NEWS_DETAIL_DELETE_ERROR(500, "新闻详情删除失败"),
    INVALID_FILE_TYPE(400, "无效的文件类型"),
    UPLOAD_FILE_ERROR(500, "文件上传失败"),
    ;

    private int code;
    private String msg;
}
