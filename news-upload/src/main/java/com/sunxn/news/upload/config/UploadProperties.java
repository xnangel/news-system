package com.sunxn.news.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @description: 上传到Fdfs中的相关属性
 * @data: 2020/4/8 17:48
 * @author: xiaoNan
 */
@Data
@ConfigurationProperties(prefix = "sunxn.upload")
public class UploadProperties {

    private String baseUrl;
    private List<String> allowTypes;
}
