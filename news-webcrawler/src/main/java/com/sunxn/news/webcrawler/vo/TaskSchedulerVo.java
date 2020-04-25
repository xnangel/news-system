package com.sunxn.news.webcrawler.vo;

import lombok.Data;

/**
 * @description:
 * @data: 2020/4/25 19:33
 * @author: xiaoNan
 */
@Data
public class TaskSchedulerVo {

    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 每天执行时间
     */
    private String time;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 状态：启动，暂停
     */
    private boolean jobStatus;
    /**
     * 今日是否已执行
     */
    private boolean execute;
}
