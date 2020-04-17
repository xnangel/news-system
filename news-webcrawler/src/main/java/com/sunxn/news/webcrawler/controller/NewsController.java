package com.sunxn.news.webcrawler.controller;

import com.sunxn.news.webcrawler.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @data: 2020/3/28 10:39
 * @author: xiaoNan
 */
@Controller
@Component
@RequestMapping("/webcrawler")
public class NewsController {

    @Autowired
    private NewsHighlightsProcessor highlightsProcessor;
    @Autowired
    private HotNewsProcessor hotNewsProcessor;
    @Autowired
    private MilitaryNewsCarouselProcessor militaryNewsCarouselProcessor;
    @Autowired
    private MilitaryNewsTextProcessor militaryNewsTextProcessor;
    @Autowired
    private GlobalNewsProcessor globalNewsProcessor;

    /**
     * 每天7点执行一次定时器
     * 人民日报 要闻等新闻信息
     */
//    @Scheduled(cron = "0 0 7 * * ?")
    @GetMapping("/newsHighLights")
    public void newsHighLights() {
        highlightsProcessor.spiderProcess();
    }

    /**
     * 爬取 排行榜 新闻信息
     * 澎湃新闻 热新闻
     * @return
     */
    @GetMapping("/newsTopHit")
    public ResponseEntity<Void> newsTopHit() {
        hotNewsProcessor.HotNewsProcess();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 爬取 军事新闻
     * @return
     */
    @GetMapping("/newsMilitary")
    public ResponseEntity<Void> newsMilitary() {
        militaryNewsCarouselProcessor.MilitaryNewsCarouselProcess();
        militaryNewsTextProcessor.MilitaryNewsTextProcess();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 爬取 环球新闻
     * @return
     */
    @GetMapping("/newsGlobal")
    public ResponseEntity<Void> newsGlobal() {
        globalNewsProcessor.GlobalNewsProcess();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
