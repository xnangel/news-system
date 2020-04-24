package com.sunxn.news.webcrawler.pipeline;

import com.sunxn.news.webcrawler.pojo.NewsDetail;
import com.sunxn.news.webcrawler.pojo.NewsItem;
import com.sunxn.news.webcrawler.service.NewsDetailService;
import com.sunxn.news.webcrawler.service.NewsItemService;
import com.sunxn.news.webcrawler.service.TextRankKeyword;
import com.sunxn.news.webcrawler.utils.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @description: 新闻数据结果持久化类
 * @data: 2020/3/26 21:58
 * @author: xiaoNan
 */
@Slf4j
@Component
public class NewsDataPipeline implements Pipeline {

    @Autowired
    private NewsItemService newsItemService;

    @Autowired
    private NewsDetailService newsDetailService;

    @Autowired
    private TextRankKeyword textRankKeyword;

    @Override
    @Transactional
    public void process(ResultItems resultItems, Task task) {
        // 获取封装好的NewsItem对象
        NewsItem newsItem = resultItems.get("newsItem");
        if (newsItem == null) {
            log.info("【爬虫服务】 newsItem为空");
            return;
        }
        // 保存newsItem到数据库中
        newsItemService.save(newsItem);
        // 获取封装好的newDetail对象
        NewsDetail newsDetail = resultItems.get("newsDetail");
        // newDetail不为空，就保存到数据库中
        if (newsDetail != null && newsItem.getId() != null) {
            newsDetail.setNewsId(newsItem.getId());
            String content = "";
            try {
                content = HtmlUtil.getText(newsDetail.getContent());
            } catch (ParserException e) {
                log.error("【爬虫数据处理管道】 纯文本内容处理失败，异常信息为：", e);
            }
            newsDetail.setKeyword(textRankKeyword.getKeyword(newsItem.getTitle(), content));
            newsDetailService.save(newsDetail);
        }
    }
}
