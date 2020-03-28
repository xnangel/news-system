package com.sunxn.news.common.vo;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import lombok.Data;

/**
 * @description: 异常结果
 * @data: 2020/3/28 14:39
 * @author: xiaoNan
 */
@Data
public class ExceptionResult {

    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(NewsSystemExceptionEnum exceptionEnum) {
        this.status = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
