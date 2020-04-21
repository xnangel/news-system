package com.sunxn.news.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


/**
 * @description: 封装保存到索引库的数据
 * @data: 2020/4/20 14:08
 * @author: xiaoNan
 */
@Data
@Document(indexName = "sunxn_news", type = "docs", shards = 1, replicas = 0)
public class News {

    /**
     * newsId
     */
    @Id
    private Long id;

    /**
     * 所有需要被搜索的信息，包括标题、分类名称、来源、关键词
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;

    /**
     * 新闻标题
     */
    @Field(type = FieldType.Keyword, index = false)
    private String title;

    /**
     * 新闻详情id
     */
    @Field(type = FieldType.Long)
    private Long newsDetailId;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date)
    private Date updateTime;
}
