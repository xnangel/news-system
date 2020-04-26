package com.sunxn.news.webcrawler.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: 定时任务表
 * @data: 2020/4/25 13:47
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_task_scheduler")
public class TaskScheduler {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private String className;
    private String methodName;
    private String cronExpression;
    private Date lastExecuteTime;
    private Date updateTime;
    private Boolean jobStatus;
    private String notes;
}
