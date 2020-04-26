package com.sunxn.news.webcrawler.task;

import com.sunxn.news.webcrawler.pipeline.NewsDataPipeline;
import com.sunxn.news.webcrawler.pojo.NewsDetail;
import com.sunxn.news.webcrawler.pojo.NewsItem;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 环球 要闻
 * @data: 2020/4/15 21:31
 * @author: xiaoNan
 */
@Slf4j
@Component("GlobalNewsProcessor")
public class GlobalNewsProcessor implements PageProcessor {

    private static final String GLOBAL_NEWS_URL = "https://www.huanqiu.com/";

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> nodes = html.css("div.rightSec div.secNewsBlock div.secNewsList").nodes();
        if (nodes != null && nodes.size() == 3) {
            nodes.remove(1);
            nodes.forEach(node -> {
                List<Selectable> textNodes = node.css("div p.listp a").nodes();
                List<String> textLinks = textNodes.stream().map(Selectable::links).map(Selectable::toString).collect(Collectors.toList());
                page.addTargetRequests(textLinks);
            });
        } else {
            this.handleData(page);
        }
    }

    private void handleData(Page page) {
        NewsItem newsItem = new NewsItem();
        NewsDetail newsDetail = new NewsDetail();
        Html html = page.getHtml();

        newsItem.setCategoryName("环球");
        newsItem.setUrl(page.getUrl().toString());
        newsItem.setTitle(html.css("div.t-container div.t-container-title h3", "text").toString());
        newsItem.setUpdateTime(new Date());
        newsItem.setCreateTime(new Date());

        String come = html.css("div.t-container div.t-container-metadata div.metadata-info p span.source span", "text").toString();
        if (StringUtils.isBlank(come)) {
            come = html.css("div.t-container div.t-container-metadata div.metadata-info p span.source span a", "text").toString();
        }
        newsDetail.setCome(come);
        newsDetail.setContent(html.css("div.b-container div.l-container div.l-con").toString());
        if (StringUtils.isBlank(newsDetail.getContent())) {
            newsDetail.setContent("<div>不好意思，该新闻内容解析有误</div>");
        }

        page.putField("newsItem", newsItem);
        page.putField("newsDetail", newsDetail);
    }

    private Site site = Site.me()
            .setCharset("UTF-8")
            .setTimeOut(10*1000)
            .setRetrySleepTime(3000)
            .setRetryTimes(3);

    @Override
    public Site getSite() {
        return this.site;
    }

    @Autowired
    private NewsDataPipeline newsDataPipeline;

    public void globalNewsProcess() {
        Spider.create(new GlobalNewsProcessor())
                .addUrl(GLOBAL_NEWS_URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
                .addPipeline(new ConsolePipeline())
                .addPipeline(newsDataPipeline)
                .thread(20)
                .run();
    }
}
