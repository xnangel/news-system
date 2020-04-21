package com.sunxn.news.search.client;

import com.sunxn.news.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @description: Category远程调用客户端
 * @data: 2020/4/20 14:56
 * @author: xiaoNan
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
