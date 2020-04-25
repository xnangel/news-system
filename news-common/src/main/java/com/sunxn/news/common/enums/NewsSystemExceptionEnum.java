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
    NEWS_ITEM_UPDATE_ERROR(500, "新闻更新失败"),
    NEWS_ITEM_DELETE_TEMPORARILY_ERROR(500, "新闻删除到垃圾箱失败"),
    NEWS_ITEM_REDUCTION_ERROR(500, "新闻从垃圾箱中还原失败"),
    NEWS_DETAILS_UPDATE_ERROR(500, "新闻详情更新失败"),
    NOT_FOUND_CATEGORIES(404, "未找到新闻类目"),
    NOT_FOUND_NEWS_ITEM(404, "未找到相关新闻"),
    NOT_FOUND_NEWS_DETAIL(404, "未找到新闻详情"),
    NEWS_ITEM_STATUS_UPDATE_ERROR(404, "新闻发布状态更新失败"),
    NEWS_ITEM_DELETE_ERROR(500, "新闻永久删除失败"),
    NEWS_DETAIL_DELETE_ERROR(500, "新闻详情永久删除失败"),
    INVALID_FILE_TYPE(400, "无效的文件类型"),
    UPLOAD_FILE_ERROR(500, "文件上传失败"),
    CATEGORY_STATUS_UPDATE_ERROR(500, "分类启用状态更新失败"),
    CATEGORY_UPDATE_ERROR(500, "分类更新失败"),
    CATEGORY_SAVE_ERROR(500, "分类保存失败"),
    CATEGORY_NAME_SAME_ERROR(400, "分类名称唯一不能相同"),
    CAROUSEL_NEWS_SAVE_ERROR(500, "轮播图新闻信息保存失败"),
    CAROUSEL_NEWS_UPDATE_ERROR(500, "轮播图新闻信息更新失败"),
    NOT_FOUND_CAROUSEL_NEWS(404, "未找到轮播图新闻信息"),
    CAROUSEL_NEWS_TEMPORARILY_ERROR(500, "轮播图新闻删除到垃圾箱失败"),
    CAROUSEL_NEWS_PERMANENT_ERROR(500, "轮播图新闻永久删除失败"),
    CAROUSEL_NEWS_REDUCTION_ERROR(500, "轮播图新闻还原失败"),
    CAROUSEL_NEWS_IS_SAME(400, "该轮播图新闻类似，可以从已有的更新"),
    INVALID_USERNAME_PASSWORD(400, "用户名或密码错误"),
    UNAUTHORIZED(403, "未授权"),
    CREATE_TOKEN_ERROR(500, "用户凭证生成失败"),
    NOT_FOUND_TASK_EXECUTE_RECORD(404, "未找到任务执行记录"),
    TASK_EXECUTE_RECORD_UPDATE_ERROR(500, "任务执行记录更新失败"),
    NOT_FOUND_TASK(404, "未找到爬虫任务"),
    TASK_SCHEDULER_UPDATE_ERROR(500, "定时任务更新失败"),
    ;

    private int code;
    private String msg;
}
