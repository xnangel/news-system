package com.sunxn.news.webcrawler.service;

/**
 * @description:
 * @data: 2020/4/25 16:29
 * @author: xiaoNan
 */
public interface TaskExecuteRecordService {

    /**
     * 根据表名来判断该表是否是今日已执行
     * @param name
     * @return
     */
    public boolean isExecuteToday(String name);

    /**
     * 根据表名更新扫描任务表的执行时间
     * @param name
     */
    public void updateTimeByName(String name);
}
