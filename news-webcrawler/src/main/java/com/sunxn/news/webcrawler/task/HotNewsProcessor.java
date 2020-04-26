package com.sunxn.news.webcrawler.task;

import com.sunxn.news.webcrawler.pipeline.NewsDataPipeline;
import com.sunxn.news.webcrawler.pojo.NewsDetail;
import com.sunxn.news.webcrawler.pojo.NewsItem;
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
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:    热新闻 爬取 澎湃新闻
 * @data: 2020/4/15 15:20
 * @author: xiaoNan
 */
@Slf4j
@Component(value = "HotNewsProcessor")
public class HotNewsProcessor implements PageProcessor {

    /**
     * 澎湃新闻首页url
     */
    private static final String HOT_NEWS_URL = "https://www.thepaper.cn/";

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().css("ul#listhot0 li a").nodes();
        List<String> links = nodes.stream().map(Selectable::links).map(Selectable::toString).collect(Collectors.toList());
        page.addTargetRequests(links);
        if (!HOT_NEWS_URL.equals(page.getUrl().toString())) {
            this.handleData(page);
        }
    }

    private void handleData(Page page) {
        Selectable contentDiv = page.getHtml().css("div.newscontent");

        NewsItem newsItem = new NewsItem();
        NewsDetail newsDetail = new NewsDetail();
        newsItem.setCategoryName("排行榜");
        newsItem.setCreateTime(new Date());
        newsItem.setUpdateTime(new Date());
        newsItem.setUrl(page.getUrl().toString());
        if (contentDiv == null || StringUtils.isBlank(contentDiv.toString())) {
            contentDiv = page.getHtml().css("div.video_txt_detail");
            newsItem.setTitle(contentDiv.css("div.video_txt_t h2", "text").toString());
            String come = contentDiv.css("div.t_source_l div.video_info_first div.video_info_left span.oriBox", "text").toString();
            if (StringUtils.isNotBlank(come)) {
                come = come.substring(come.indexOf("："));
            }
            newsDetail.setCome(come);
            newsDetail.setContent(contentDiv.css("div.video_txt_l p", "text").toString());
            newsDetail.setNotes("视频");
        } else {
            newsItem.setTitle(contentDiv.css("h1.news_title", "text").toString());
            newsDetail.setCome(contentDiv.css("div.news_about p", "text").nodes().get(0).toString());
            newsDetail.setContent(contentDiv.css("div.news_txt").all().stream().collect(Collectors.joining()));
        }

        if (StringUtils.isBlank(newsItem.getTitle())) {
            return;
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

    public void hotNewsProcess() {
        Spider.create(new HotNewsProcessor())
                .addUrl(HOT_NEWS_URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
                .thread(20)
                .addPipeline(new ConsolePipeline())
                .addPipeline(newsDataPipeline)
                .run();
    }
}
