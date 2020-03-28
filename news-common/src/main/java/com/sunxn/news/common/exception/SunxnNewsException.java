package com.sunxn.news.common.exception;

import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @data: 2020/3/28 14:38
 * @author: xiaoNan
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SunxnNewsException extends RuntimeException {

    private NewsSystemExceptionEnum newsSystemExceptionEnum;
}
