package com.sunxn.news.webcrawler.controller;

import com.sunxn.news.webcrawler.task.NewsHighlightsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @data: 2020/3/28 10:39
 * @author: xiaoNan
 */
@Component
public class NewsController {

    @Autowired
    private NewsHighlightsProcessor highlightsProcessor;

    /**
     * 每天7点执行一次定时器
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void newsHighLights() {
        highlightsProcessor.spiderProcess();
    }
}
