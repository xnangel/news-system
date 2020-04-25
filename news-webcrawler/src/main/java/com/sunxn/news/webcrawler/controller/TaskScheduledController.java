package com.sunxn.news.webcrawler.controller;

import com.sunxn.news.webcrawler.service.TaskSchedulerService;
import com.sunxn.news.webcrawler.vo.TaskSchedulerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @data: 2020/4/25 13:46
 * @author: xiaoNan
 */
@RestController
@RequestMapping("/task")
public class TaskScheduledController {

    @Autowired
    private TaskSchedulerService taskSchedulerService;

    /**
     * 查询所有爬虫任务
     * @return
     */
    @GetMapping("/find/list")
    public ResponseEntity<List<TaskSchedulerVo>> findTaskScheduleVoList() {
        return ResponseEntity.ok(taskSchedulerService.findTaskScheduleVoList());
    }

    @RequestMapping(value = "/update/cronExpression", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateTaskSchedulerById(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "cronExpression", defaultValue = "0 0 5 * * ?") String cronExpression
    ) {
        taskSchedulerService.updateTaskSchedulerById(id, cronExpression);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
