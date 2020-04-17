package com.sunxn.news.webcrawler.task;

import com.sunxn.news.webcrawler.pipeline.NewsCarouselDataPipeline;
import com.sunxn.news.webcrawler.pojo.CarouselNews;
import com.sunxn.news.webcrawler.pojo.NewsDetail;
import com.sunxn.news.webcrawler.pojo.NewsItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
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
 * @description: 军事新闻 人民网   轮播图新闻
 * @data: 2020/4/15 19:39
 * @author: xiaoNan
 */
@Slf4j
@Component
public class MilitaryNewsCarouselProcessor implements PageProcessor {

    private static final String MILITARY_NEWS_URL = "http://military.people.com.cn/";
    private static final String TAB_ENCODE = "\u00A0";
    private Map<String, String> map = new HashMap<>();

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> imgNodes = html.css("div#focus_list ul li a img", "src").nodes();
        List<Selectable> imgNodesAlt = html.css("div#focus_list ul li a img", "alt").nodes();
        if (!CollectionUtils.isEmpty(imgNodes)) {
            for (int i=0; i<imgNodes.size(); i++) {
                String value = MILITARY_NEWS_URL + imgNodes.get(i).toString();
                String key = imgNodesAlt.get(i).get().trim().replace(" ", "");
                // 标题为key，图片url为value
                this.map.put(key, value);
            }
        }

        List<Selectable> imgUrlNodes = html.css("div#focus_list ul li a").nodes();
        if (CollectionUtils.isEmpty(imgUrlNodes)) {
            this.handleImageNews(page);
        } else {
            List<String> imgUrlStr = imgUrlNodes.stream().map(Selectable::links).map(Selectable::get).collect(Collectors.toList());
            page.addTargetRequests(imgUrlStr);
        }
    }

    private void handleImageNews(Page page) {
        NewsItem newsItem = new NewsItem();
        NewsDetail newsDetail = new NewsDetail();
        CarouselNews carouselNews = new CarouselNews();

        String title = page.getHtml().css("div.title h1", "text").toString().trim();
        newsItem.setCategoryName("军事");
        newsItem.setCreateTime(new Date());
        newsItem.setUpdateTime(new Date());
        newsItem.setUrl(page.getUrl().toString());
        newsItem.setTitle(title);

        newsDetail.setCome(page.getHtml().css("div.pic_data div.page_c div.fr a", "text").toString());
        newsDetail.setContent(page.getHtml().css("div.content").toString());

        carouselNews.setImageUrl(this.map.get(this.removeSpaces(title)));
        carouselNews.setTitle(title);

        page.putField("newsItem", newsItem);
        page.putField("newsDetail", newsDetail);
        page.putField("carouselNews", carouselNews);
    }

    /**
     * page.getHtml().xpath()返回从html页面中的数据中，包含空格
     *  想取出空格，遇到有的空格无法去除，经过排查，空格中包含"tab"空格，
     *  一般的办法是无法去除的，tab空格在编码中是"\u00A0"，替换掉即可。
     * @param resource
     * @return
     */
    private String removeSpaces(String resource) {
        if (resource.indexOf(TAB_ENCODE) > 0) {
            return resource.replace(TAB_ENCODE, "");
        }
        return resource.replace("\\s", "");
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
    private NewsCarouselDataPipeline newsCarouselDataPipeline;

    public void MilitaryNewsCarouselProcess() {
        Spider.create(new MilitaryNewsCarouselProcessor())
                .addUrl(MILITARY_NEWS_URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
                .addPipeline(newsCarouselDataPipeline)
                .thread(20)
                .run();
    }
}
