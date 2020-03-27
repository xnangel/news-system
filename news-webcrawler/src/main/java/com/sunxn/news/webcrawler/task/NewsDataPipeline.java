package com.sunxn.news.webcrawler.task;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @description: 新闻数据结果持久化类
 * @data: 2020/3/26 21:58
 * @author: xiaoNan
 */
@Component
public class NewsDataPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

    }
}
