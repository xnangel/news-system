package com.sunxn.news.search.service;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.pojo.Category;
import com.sunxn.news.pojo.NewsDetail;
import com.sunxn.news.pojo.NewsItem;
import com.sunxn.news.search.client.CategoryClient;
import com.sunxn.news.search.client.NewsDetailClient;
import com.sunxn.news.search.client.NewsItemClient;
import com.sunxn.news.search.pojo.News;
import com.sunxn.news.search.repository.NewsRepository;
import com.sunxn.news.vo.NewsItemVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @data: 2020/4/20 14:46
 * @author: xiaoNan
 */
@Service
@Slf4j
public class SearchService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private NewsDetailClient newsDetailClient;
    @Autowired
    private NewsItemClient newsItemClient;
    @Autowired
    private CategoryClient categoryClient;

    /**
     * 构建news对象
     * @param newsItemVo
     * @return
     */
    public News buildNews(NewsItemVo newsItemVo) {
        // 查询newsDetail
        NewsDetail newsDetail = newsDetailClient.findNewsDetailByNewsId(newsItemVo.getId());
        if (newsDetail == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_DETAIL);
        }

        // 搜索字段
        String all = newsItemVo.getTitle() + " " + newsDetail.getCome()
                + " " + newsDetail.getKeyword() + " " + newsItemVo.getCategoryName();

        News news = new News();
        news.setId(newsItemVo.getId());
        news.setTitle(newsItemVo.getTitle());
        news.setCreateTime(newsItemVo.getCreateTime());
        news.setUpdateTime(newsItemVo.getUpdateTime());
        news.setAll(all);
        news.setNewsDetailId(newsDetail.getId());
        return news;
    }

    /**
     * 根据newsId创建或更新索引库id=newsId的数据
     * @param newsId
     */
    public void createOrUpdateIndex(Long newsId) {
        // 封装newsItemVo对象
        NewsItem newsItem = newsItemClient.findNewsItemById(newsId);
        if (newsItem == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_NEWS_ITEM);
        }

        // 查询分类名称
        Category category = categoryClient.findCategoryById(newsItem.getCategoryId());
        if (category == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.NOT_FOUND_CATEGORIES);
        }

        NewsItemVo newsItemVo = new NewsItemVo();
        BeanUtils.copyProperties(newsItem, newsItemVo);
        newsItemVo.setCategoryName(category.getName());

        // 构建news对象
        News news = buildNews(newsItemVo);

        // 存入搜索库
        newsRepository.save(news);
    }

    /**
     * 根据newsId删除索引库id=newsId的数据
     * @param newsId
     */
    public void deleteIndex(Long newsId) {
        newsRepository.deleteById(newsId);
    }

    /**
     * 根据关键词查询news
     * @param key
     * @return
     */
    public List<News> searchNewsByKey(String key) {
        // 判断是否有搜索关键词，如果没有，直接返回null，不允许搜索全部news
        if (StringUtils.isBlank(key)) {
            return null;
        }

        // 创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "title", "newsDetailId"}, null));
        // 过滤 搜索条件
        MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all", key);
        queryBuilder.withQuery(basicQuery);
        // 查询
        Page<News> result = newsRepository.search(queryBuilder.build());
        // 解析结果
        List<News> newsList = result.getContent();
        return newsList;
    }
}
