package com.sunxn.news.search.client;

import com.sunxn.news.api.NewsDetailApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @description:
 * @data: 2020/4/20 14:57
 * @author: xiaoNan
 */
@FeignClient("item-service")
public interface NewsDetailClient extends NewsDetailApi {
}
