package com.sunxn.news.webcrawler.task;

import com.sunxn.news.webcrawler.pipeline.NewsDataPipeline;
import com.sunxn.news.webcrawler.pojo.NewsDetail;
import com.sunxn.news.webcrawler.pojo.NewsItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
 * @description: 军事新闻 人民网   新闻内容
 * @data: 2020/4/15 19:39
 * @author: xiaoNan
 */
@Slf4j
@Component
public class MilitaryNewsTextProcessor implements PageProcessor {

    private static final String MILITARY_NEWS_URL = "http://military.people.com.cn/";

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> textNodes = html.css("div.news_box a").nodes();
        if (CollectionUtils.isEmpty(textNodes)) {
            this.handleTextNews(page);
        } else {
            List<String> textStr = textNodes.stream().map(Selectable::links).map(Selectable::toString).collect(Collectors.toList());
            page.addTargetRequests(textStr);
        }
    }

    private void handleTextNews(Page page) {
        Html html = page.getHtml();
        NewsItem newsItem = new NewsItem();
        NewsDetail newsDetail = new NewsDetail();

        newsItem.setCategoryName("军事");
        newsItem.setUpdateTime(new Date());
        newsItem.setCreateTime(new Date());
        newsItem.setUrl(page.getUrl().toString());
        newsItem.setTitle(html.css("div.text_title h1", "text") + html.css("div.text_title h4.sub", "text").toString());

        newsDetail.setCome(html.css("div.text_title div.box01 div.fl a", "text").toString());
        newsDetail.setContent(html.css("div.text_con div.text_con_left div.box_con").toString());

        System.out.println(newsItem.toString());
        System.out.println(newsDetail.toString());
        page.putField("newsItem", newsItem);
        page.putField("newsDetail", newsDetail);
    }

    private Site site = Site.me()
            .setCharset("GBK")
            .setTimeOut(10*1000)
            .setRetrySleepTime(3*1000)
            .setRetryTimes(3);

    @Override
    public Site getSite() {
        return this.site;
    }

    @Autowired
    private NewsDataPipeline newsDataPipeline;

    public void MilitaryNewsTextProcess() {
        Spider.create(new MilitaryNewsTextProcessor())
                .addUrl(MILITARY_NEWS_URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
                .addPipeline(new ConsolePipeline())
                .addPipeline(newsDataPipeline)
                .thread(20)
                .run();
    }
}
