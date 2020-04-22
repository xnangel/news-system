package com.sunxn.news.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @description:
 * @data: 2020/4/22 22:00
 * @author: xiaoNan
 */
@Data
@ConfigurationProperties(prefix = "sunxn.filter")
public class FilterProperties {

    private List<String> allowPaths;
}
