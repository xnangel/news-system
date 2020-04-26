package com.sunxn.news.webcrawler.service.impl;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.utils.DateUtil;
import com.sunxn.news.webcrawler.dao.TaskSchedulerRepository;
import com.sunxn.news.webcrawler.pojo.TaskScheduler;
import com.sunxn.news.webcrawler.service.TaskSchedulerService;
import com.sunxn.news.webcrawler.vo.TaskSchedulerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

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

    @Override
    public List<TaskScheduler> findAllStartUpUnExecuteTask() {
        Example example = new Example(TaskScheduler.class);
        Example.Criteria criteria = example.createCriteria();
        // 启用状态
        criteria.andEqualTo("jobStatus", true);
        // 获取今天的最小时间，并转换成字符串
        Date date = new Date();
        Date startOfDay = DateUtil.getStartOfDay(date);
        String startOfDayStr = DateUtil.parseDateToStr(startOfDay, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS);
        // 上次执行时间小于今天的最小时间
        criteria.andLessThan("lastExecuteTime", startOfDayStr);
        return taskSchedulerRepository.selectByExample(example);
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
            Date startOfDay = DateUtil.getStartOfDay(new Date());
            taskSchedulerVo.setExecute(!taskScheduler.getLastExecuteTime().before(startOfDay));
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
        TaskScheduler taskScheduler = taskSchedulerRepository.selectByPrimaryKey(id);
        if (taskScheduler == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_TASK);
        }
        String[] cronArray = taskScheduler.getCronExpression().split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<cronArray.length; i++) {
            if (i == 1) {
                stringBuilder.append(minutes);
            } else if (i==2) {
                stringBuilder.append(hour);
            } else {
                stringBuilder.append(cronArray[i]);
            }
            stringBuilder.append(" ");
        }
        String cron = stringBuilder.toString().trim();
        taskScheduler.setCronExpression(cron);
        taskScheduler.setUpdateTime(new Date());
        if (taskSchedulerRepository.updateByPrimaryKeySelective(taskScheduler) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.TASK_SCHEDULER_UPDATE_ERROR);
        }
    }

    @Override
    public void updateTaskSchedulerById(Long id, Date executeTime) {
        TaskScheduler taskScheduler = new TaskScheduler();
        taskScheduler.setId(id);
        taskScheduler.setLastExecuteTime(executeTime);
        if (taskSchedulerRepository.updateByPrimaryKeySelective(taskScheduler) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.TASK_SCHEDULER_UPDATE_ERROR);
        }
    }
}
