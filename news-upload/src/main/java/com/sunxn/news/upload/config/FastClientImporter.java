package com.sunxn.news.upload.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @description: FastDFS客户端的引入
 * @data: 2020/4/8 17:46
 * @author: xiaoNan
 * 解决jmx重复注册bean的问题
 *      @EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClientImporter {
}
