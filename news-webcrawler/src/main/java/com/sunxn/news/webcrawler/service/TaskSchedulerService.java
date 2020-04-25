package com.sunxn.news.webcrawler.service;

import com.sunxn.news.webcrawler.pojo.TaskScheduler;
import com.sunxn.news.webcrawler.vo.TaskSchedulerVo;

import java.util.List;

/**
 * @description:
 * @data: 2020/4/25 13:51
 * @author: xiaoNan
 */
public interface TaskSchedulerService {

    /**
     * 查询所有启动的爬虫任务
     * @return
     */
    List<TaskScheduler> findAllStartUpTask();

    /**
     * 查询所有爬虫任务
     * @return
     */
    List<TaskSchedulerVo> findTaskScheduleVoList();

    /**
     * 根据id更新爬虫任务的cronExpression表达式
     * @param id
     * @param cronExpression
     */
    void updateTaskSchedulerById(Long id, String cronExpression);
}
