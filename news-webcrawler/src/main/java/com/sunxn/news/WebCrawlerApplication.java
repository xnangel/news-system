package com.sunxn.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description: 爬虫服务 启动器
 * @data: 2020/3/26 20:56
 * @author: xiaoNan
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@MapperScan("com.sunxn.news.webcrawler.dao")
public class WebCrawlerApplication {

    // TODO 对爬取到的数据进行关键词抽取分析
    // TODO 客户端的新闻推荐
    // TODO 动态实现任务调度管理

    public static void main(String[] args) {
        SpringApplication.run(WebCrawlerApplication.class, args);
    }
}
