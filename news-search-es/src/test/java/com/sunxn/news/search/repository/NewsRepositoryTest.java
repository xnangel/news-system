package com.sunxn.news.search.repository;

import com.sunxn.news.common.vo.PageResult;
import com.sunxn.news.search.client.NewsItemClient;
import com.sunxn.news.search.pojo.News;
import com.sunxn.news.search.service.SearchService;
import com.sunxn.news.vo.NewsItemVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @data: 2020/4/20 16:20
 * @author: xiaoNan
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private NewsItemClient newsItemClient;
    @Autowired
    private SearchService searchService;

    /**
     * 创建sunxn_news索引库，类型为docs，并且设置映射关系
     */
    @Test
    public void testCreateIndex() {
        elasticsearchTemplate.createIndex(News.class);
        elasticsearchTemplate.putMapping(News.class);
    }

    /**
     * 把news数据存入索引库
     */
    @Test
    public void loadData() {
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询newsItem信息
            PageResult<NewsItemVo> result = newsItemClient.findNewsItemsByCondition(page, rows, null, null, false, true, false, false);
            List<NewsItemVo> newsItemVoList = result.getItems();
            if (CollectionUtils.isEmpty(newsItemVoList)) {
                break;
            }
            // 构建news对象
            List<News> newsList = newsItemVoList.stream().map(searchService::buildNews)
                    .collect(Collectors.toList());
            // 存入搜索库
            newsRepository.saveAll(newsList);
            //翻页
            page ++;
            size = newsItemVoList.size();
        } while (size == 100);

    }
}
