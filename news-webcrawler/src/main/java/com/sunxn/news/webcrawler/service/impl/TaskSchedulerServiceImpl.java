package com.sunxn.news.webcrawler.service.impl;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.utils.DateUtil;
import com.sunxn.news.webcrawler.dao.TaskSchedulerRepository;
import com.sunxn.news.webcrawler.pojo.TaskScheduler;
import com.sunxn.news.webcrawler.service.TaskExecuteRecordService;
import com.sunxn.news.webcrawler.service.TaskSchedulerService;
import com.sunxn.news.webcrawler.vo.TaskSchedulerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @data: 2020/4/25 14:22
 * @author: xiaoNan
 */
@Service
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    @Autowired
    private TaskSchedulerRepository taskSchedulerRepository;
    @Autowired
    private TaskExecuteRecordService taskExecuteRecordService;

    @Override
    public List<TaskScheduler> findAllStartUpTask() {
        TaskScheduler taskScheduler = new TaskScheduler();
        taskScheduler.setJobStatus(true);
        List<TaskScheduler> taskSchedulerList = taskSchedulerRepository.select(taskScheduler);
        if (CollectionUtils.isEmpty(taskSchedulerList)) {
            new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_TASK);
        }
        return taskSchedulerList;
    }

    @Override
    public List<TaskSchedulerVo> findTaskScheduleVoList() {
        List<TaskScheduler> taskSchedulers = taskSchedulerRepository.selectAll();
        if (CollectionUtils.isEmpty(taskSchedulers)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_TASK);
        }
        List<TaskSchedulerVo> taskSchedulerVoList = new ArrayList<>();
        taskSchedulers.forEach(taskScheduler -> {
            TaskSchedulerVo taskSchedulerVo = new TaskSchedulerVo();
            taskSchedulerVo.setId(taskScheduler.getId());
            taskSchedulerVo.setName(taskScheduler.getName());
            taskSchedulerVo.setUpdateTime(DateUtil.parseDateToStr(taskScheduler.getUpdateTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS));
            taskSchedulerVo.setJobStatus(taskScheduler.getJobStatus());
            taskSchedulerVo.setExecute(taskExecuteRecordService.isExecuteToday("tb_task_scheduler"));
            String[] cronTimeArray = taskScheduler.getCronExpression().split(" ");
            taskSchedulerVo.setTime(cronTimeArray[2] + ":" + cronTimeArray[1]);
            taskSchedulerVoList.add(taskSchedulerVo);
        });
        return taskSchedulerVoList;
    }

    @Override
    public void updateTaskSchedulerById(Long id, String cronExpression) {
        String[] timeStrArray = cronExpression.split(":");
        int hour = Integer.parseInt(timeStrArray[0]);
        int minutes = Integer.parseInt(timeStrArray[1]);
        String cron = "0 " + minutes + " " + hour + " * * ?";
        TaskScheduler taskScheduler = taskSchedulerRepository.selectByPrimaryKey(id);
        if (taskScheduler == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_TASK);
        }
        taskScheduler.setCronExpression(cron);
        taskScheduler.setUpdateTime(new Date());
        if (taskSchedulerRepository.updateByPrimaryKeySelective(taskScheduler) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.TASK_EXECUTE_RECORD_UPDATE_ERROR);
        }
    }
}
