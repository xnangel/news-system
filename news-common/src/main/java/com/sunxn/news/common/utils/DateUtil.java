package com.sunxn.news.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description: 日期处理工具类
 * @data: 2020/3/26 21:13
 * @author: xiaoNan
 */
public class DateUtil {

    /**
     * 日期格式，年份
     */
    public static final String DATE_FORMAT_YYYY = "YYYY";
    /**
     * 日期格式，年月日，用 - 分隔
     */
    public static final String DATE_FORMAT_YYYY_MM_DD = "YYYY-MM-dd";
    /**
     * 日期格式，年月日时分秒。年月日用 - 分隔，时分秒用 : 分隔
     */
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MI_SS = "YYYY-MM-dd HH:mm:ss";

    /**
     * 获取某日期的年份
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取某日期的月份
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某日期的日数
     * @param date
     * @return
     */
    public static Integer getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 格式化Date日期
     * @param date
     * @param pattern
     * @return
     */
    public static String parseDateToStr(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间的格式化数据，年月日时分秒
     * @return
     */
    public static String getDateNow() {
        return parseDateToStr(new Date(), DATE_FORMAT_YYYY_MM_DD_HH_MI_SS);
    }

}
