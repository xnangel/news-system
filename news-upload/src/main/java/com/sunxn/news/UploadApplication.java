package com.sunxn.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description: 文件上传服务 启动类
 * @data: 2020/4/8 17:05
 * @author: xiaoNan
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}
