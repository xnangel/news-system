package com.sunxn.news.webcrawler.task;

import com.sunxn.news.webcrawler.pojo.TaskScheduler;
import com.sunxn.news.webcrawler.service.TaskSchedulerService;
import com.sunxn.news.webcrawler.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ConcurrentHashMap<String, ScheduledFuture<?>> map = new ConcurrentHashMap<>();

    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(30);
        taskScheduler.setThreadNamePrefix("Scheduler -- ");
        // 线程池对拒绝任务（无线程可用）的处理策略。目前支持AbortPolicy、CallerRunsPolicy等
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 调度器shutdown被调用时等待当前被调度的任务完成
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时长
        taskScheduler.setAwaitTerminationSeconds(60);
        return taskScheduler;
    }

    @Transactional
    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() {
        List<TaskScheduler> startUpUnExecuteTaskList = taskSchedulerService.findAllStartUpUnExecuteTask();
        startUpUnExecuteTaskList.forEach(startUpUnExecuteTask -> {
            // 把定时任务加入线程池中
            this.startCron(startUpUnExecuteTask);
        });
    }

    /**
     * 处理定时任务
     * @param startUpUnExecuteTask
     */
    @Transactional
    void startCron(TaskScheduler startUpUnExecuteTask) {
        ScheduledFuture<?> future = null;
        if (this.map.containsKey(startUpUnExecuteTask.getClassName())) {
            future = this.map.get(startUpUnExecuteTask.getClassName());
            this.stopCron(future);
        }
        future = threadPoolTaskScheduler.schedule(this.createTaskRunnable(startUpUnExecuteTask), new CronTrigger(startUpUnExecuteTask.getCronExpression()));
        this.map.put(startUpUnExecuteTask.getClassName(), future);
        // 修改定时任务的执行时间
        taskSchedulerService.updateTaskSchedulerById(startUpUnExecuteTask.getId(), new Date());
    }

    /**
     * 如果定时任务不为null，则暂停定时任务
     * @param future
     */
    private void stopCron(ScheduledFuture<?> future) {
        if (future != null) {
            future.cancel(true);
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
