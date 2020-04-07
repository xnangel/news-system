package com.sunxn.news.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @description:
 * @data: 2020/4/7 16:02
 * @author: xiaoNan
 */
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 1.1 允许的域，不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("http://localhost:8080");
        // 1.2 是否发送Cookie信息
        config.setAllowCredentials(true);
        // 1.3 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        // 1.4 允许的头信息
        config.addAllowedHeader("*");
        // 1.5 有效时长
        config.setMaxAge(3600L);

        // 2. 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", config);

        // 3. 返回新的CorsFilter
        return new CorsFilter(configurationSource);
    }
}
