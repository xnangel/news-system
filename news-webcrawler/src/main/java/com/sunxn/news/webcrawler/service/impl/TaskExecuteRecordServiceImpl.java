package com.sunxn.news.webcrawler.service.impl;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.utils.DateUtil;
import com.sunxn.news.webcrawler.dao.TaskExecuteRecordRepository;
import com.sunxn.news.webcrawler.pojo.TaskExecuteRecord;
import com.sunxn.news.webcrawler.service.TaskExecuteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @description:
 * @data: 2020/4/25 16:29
 * @author: xiaoNan
 */
@Service
public class TaskExecuteRecordServiceImpl implements TaskExecuteRecordService {

    @Autowired
    private TaskExecuteRecordRepository taskExecuteRecordRepository;

    @Override
    public boolean isExecuteToday(String name) {
        Example example = new Example(TaskExecuteRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        Date now = new Date();
        String startOfDayStr = DateUtil.parseDateToStr(DateUtil.getStartOfDay(now), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MI_SS);
        criteria.andLessThan("executeTime", startOfDayStr);
        TaskExecuteRecord taskExecuteRecord = taskExecuteRecordRepository.selectOneByExample(example);
        // 表名为name的执行时间小于今天，有taskExecuteRecord，返回false；否则返回true
        return taskExecuteRecord == null;
    }

    @Override
    public void updateTimeByName(String name) {
        TaskExecuteRecord record = new TaskExecuteRecord();
        record.setName(name);
        TaskExecuteRecord taskExecuteRecord = taskExecuteRecordRepository.selectOne(record);
        if (taskExecuteRecord == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_TASK_EXECUTE_RECORD);
        }
        taskExecuteRecord.setExecuteTime(new Date());
        if (taskExecuteRecordRepository.updateByPrimaryKeySelective(taskExecuteRecord) != 1) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.TASK_EXECUTE_RECORD_UPDATE_ERROR);
        }
    }
}
