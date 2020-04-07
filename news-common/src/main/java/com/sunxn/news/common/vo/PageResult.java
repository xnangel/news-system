package com.sunxn.news.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @description: 分页信息结果
 * @data: 2020/4/6 19:25
 * @author: xiaoNan
 */
@Data
public class PageResult<T> {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 当前页数据
     */
    private List<T> items;

    public PageResult() {

    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this(total, items);
        this.totalPage = totalPage;
    }
}
