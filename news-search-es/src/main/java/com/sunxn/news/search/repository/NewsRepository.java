package com.sunxn.news.search.repository;

import com.sunxn.news.search.pojo.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @description:
 * @data: 2020/4/20 14:47
 * @author: xiaoNan
 */
public interface NewsRepository extends ElasticsearchRepository<News, Long> {
}
