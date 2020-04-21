package com.sunxn.news.search.mq;

import com.sunxn.news.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:    item新闻的增删改的监听类
 * @data: 2020/4/21 16:23
 * @author: xiaoNan
 */
@Component
public class ItemListener {

    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "search.item.insert.queue", durable = "true"),
            exchange = @Exchange(value = "sunxn.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void listenInsertOrUpdate(Long newsId) {
        if (newsId == null) {
            return;
        }

        // 处理消息，对搜索库id=newsId的数据进行新增或修改
        searchService.createOrUpdateIndex(newsId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "search.item.delete.queue", durable = "true"),
            exchange = @Exchange(value = "sunxn.item.exchange", type = ExchangeTypes.TOPIC),
            key = "item.delete"
    ))
    public void listenDelete(Long newsId) {
        if (newsId == null) {
            return;
        }

        searchService.deleteIndex(newsId);
    }
}
