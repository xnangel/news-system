package com.sunxn.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description: 新闻服务启动类
 * @data: 2020/3/24 19:43
 * @author: xiaoNan
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.sunxn.news.item.mapper")
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }
}
