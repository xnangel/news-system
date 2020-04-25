package com.sunxn.news.webcrawler.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description:    任务执行记录表
 * @data: 2020/4/25 16:26
 * @author: xiaoNan
 */
@Data
@Table(name = "tb_task_execute_record")
public class TaskExecuteRecord {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Date executeTime;
}
