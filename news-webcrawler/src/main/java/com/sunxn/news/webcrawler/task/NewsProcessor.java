package com.sunxn.news.webcrawler.task;

import com.sunxn.news.webcrawler.pojo.NewsDetail;
import com.sunxn.news.webcrawler.pojo.NewsItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 新闻页面处理器
 * @data: 2020/3/26 21:06
 * @author: xiaoNan
 */
@Slf4j
@Component
public class NewsProcessor implements PageProcessor {

    private Map<String, String> map = new HashMap<>();

    /**
     * 人民日报 ip地址+端口号 公共前缀
     */
    public static final String PEOPLE_PAGE_URL = "http://paper.people.com.cn/rmrb";

    private String peoplePageUrl = "http://paper.people.com.cn/rmrb/html/2020-03/27/nbs.D110000renmrb_01.htm";

    @Override
    public void process(Page page) {
        // 获取当日人民日报-人民网 版面目录url 并加入到任务队列中
        List<Selectable> nodes = page.getHtml().css("a#pageLink").nodes();
        List<String> text = page.getHtml().css("a#pageLink", "text").all();
        text.forEach(s -> {
            String key = s.substring(1,3);
            String value = StringUtils.substringAfter(s, "：");
            this.map.put(key, value);
        });
        List<String> links = nodes.stream().map(Selectable::links).map(Selectable::toString).collect(Collectors.toList());
        page.addTargetRequests(links);
        // 获取 xx版本中全部新闻
        List<Selectable> titleNodes = page.getHtml().css("div#titleList ul li a").nodes();
        if (titleNodes.size() == 0) {
            // 每个新闻页的数据处理
            handleNewsData(page);
        } else {
            List<String> titleLinks = titleNodes.stream().map(Selectable::links).map(Selectable::get).collect(Collectors.toList());
            page.addTargetRequests(titleLinks);
        }
    }

    private void handleNewsData(Page page) {
        NewsItem newsItem = new NewsItem();
        NewsDetail newsDetail = new NewsDetail();
        Html html = page.getHtml();
        // 封装新闻对象
        newsItem.setTitle(html.css("div.text_c h1", "text").toString());
        String url = page.getUrl().toString();
        newsItem.setUrl(url);
        String key = StringUtils.substringBefore(StringUtils.substringAfterLast(url, "-"), ".");
        newsItem.setCategoryName(this.map.get(key));
        newsItem.setCreateTime(new Date());
        newsItem.setUpdateTime(new Date());
        newsDetail.setCome(html.css("div.text_c div.lai", "text").toString());
        newsDetail.setContent(html.css("div.text_c div.c_c").all().stream().collect(Collectors.joining(" ")).replace("../../..", PEOPLE_PAGE_URL));
        // 把结果保存起来
        page.putField("newsItem", newsItem);
        page.putField("newsDetail", newsDetail);
    }

    /**
     * 设置gbk编码格式
     * 设置超时时间   10秒
     * 设置重试的间隔时间    3秒
     * 设置重试的次数      3
     */
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

    @Scheduled(initialDelay = 1000, fixedDelay = 2*60*60*1000)
    public void spiderProcess() {
        Spider.create(new NewsProcessor())
                .addUrl(peoplePageUrl)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
                .thread(10)
                .addPipeline(this.newsDataPipeline)
                .run();
    }

}
