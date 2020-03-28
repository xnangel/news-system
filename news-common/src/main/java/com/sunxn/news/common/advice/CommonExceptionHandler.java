package com.sunxn.news.common.advice;

import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description: 公共异常处理类
 * @data: 2020/3/28 14:36
 * @author: xiaoNan
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(SunxnNewsException.class)
    public ResponseEntity<ExceptionResult> handleException(SunxnNewsException exception) {
        return ResponseEntity.status(exception.getNewsSystemExceptionEnum().getCode())
                .body(new ExceptionResult(exception.getNewsSystemExceptionEnum()));
    }

}
