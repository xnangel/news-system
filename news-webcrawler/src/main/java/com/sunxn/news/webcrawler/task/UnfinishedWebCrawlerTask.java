package com.sunxn.news.webcrawler.task;

import com.sunxn.news.webcrawler.pojo.TaskScheduler;
import com.sunxn.news.webcrawler.service.TaskExecuteRecordService;
import com.sunxn.news.webcrawler.service.TaskSchedulerService;
import com.sunxn.news.webcrawler.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 定时扫描任务执行记录表，完成爬虫任务定时爬取
 * @data: 2020/4/25 14:23
 * @author: xiaoNan
 */
@Slf4j
@Component
public class UnfinishedWebCrawlerTask {

    @Autowired
    private TaskSchedulerService taskSchedulerService;
    @Autowired
    private TaskExecuteRecordService taskExecuteRecordService;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ScheduledFuture<?> future;

    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        taskScheduler.setThreadNamePrefix("Scheduler -- ");
        // 线程池对拒绝任务（无线程可用）的处理策略。目前支持AbortPolicy、CallerRunsPolicy等
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 调度器shutdown被调用时等待当前被调度的任务完成
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时长
        taskScheduler.setAwaitTerminationSeconds(60);
        return taskScheduler;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        String name = "tb_task_scheduler";
        System.out.println("ceshi...");
        if (!taskExecuteRecordService.isExecuteToday(name)) {
            System.out.println("execute.......");
            List<TaskScheduler> startUpTaskList = taskSchedulerService.findAllStartUpTask();
            startUpTaskList.forEach(startUpTask -> {
                Runnable taskRunnable = this.createTaskRunnable(startUpTask);
                threadPoolTaskScheduler.schedule(taskRunnable, new CronTrigger(startUpTask.getCronExpression()));
            });

            taskExecuteRecordService.updateTimeByName(name);
        }
    }

    /**
     * 创建一个runnable对象
     * @param taskScheduler
     * @return
     */
    private Runnable createTaskRunnable(TaskScheduler taskScheduler) {
        Runnable taskRunnable = new Runnable() {
            @Override
            public void run() {
                Object bean = SpringContextUtil.getBean(taskScheduler.getClassName());
                Class<?> beanClass = bean.getClass();
                if (beanClass != null) {
                    try {
                        Method method = beanClass.getMethod(taskScheduler.getMethodName());
                        if (method != null) {
                            method.invoke(bean);
                        }
                    } catch (NoSuchMethodException e) {
                        log.error("【UnfinishedWebCrawlerTask】 class.getMethod(name)没有获取成功，异常信息为：", e);
                    } catch (Exception e) {
                        log.error("【UnfinishedWebCrawlerTask】 method.invoke(object)方法执行失败，异常信息为：", e);
                    }
                }
            }
        };
        return taskRunnable;
    }

}
