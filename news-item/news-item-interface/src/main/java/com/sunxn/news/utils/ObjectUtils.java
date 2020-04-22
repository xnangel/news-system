package com.sunxn.news.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @description:    从jwt解析得到的数据是Object类型，转换为具体类型可能出现空指针，这个工具类就是进行该转换用地
 * @data: 2020/4/22 16:15
 * @author: xiaoNan
 */
public class ObjectUtils {

    public static Long toLong(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Double || obj instanceof Float) {
            return Long.valueOf(StringUtils.substringBefore(obj.toString(), "."));
        }
        if (obj instanceof Number) {
            return Long.valueOf(obj.toString());
        }
        if (obj instanceof String) {
            return Long.valueOf(obj.toString());
        }
        return 0L;
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
