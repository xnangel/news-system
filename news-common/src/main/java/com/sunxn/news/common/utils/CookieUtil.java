package com.sunxn.news.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @description: Cookie工具类
 * @data: 2020/4/22 19:06
 * @author: xiaoNan
 */
@Slf4j
public class CookieUtil {

    /**
     * 设置Cookie的值，并使其在指定时间内生效
     * @param request       请求
     * @param response      响应
     * @param cookieName    cookie的名字
     * @param cookieValue   cookie对应的值
     * @param cookieMaxAge  cookie生效的最大秒数
     * @param encodeString  编码
     * @param httpOnly      设置该cookie能否被JavaScript读取
     */
    public static final void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxAge, String encodeString, Boolean httpOnly) {
        try {
            if (StringUtils.isBlank(encodeString)) {
                encodeString = "utf-8";
            }

            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }

            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxAge != null && cookieMaxAge > 0) {
                cookie.setMaxAge(cookieMaxAge);
            }
            // 设置cookie的域名
            // domain的含义为域，分为子域和父域，子域可以访问本级域名及父级域名下的cookie
            // 例子：f.com.cn 是 com.cn 的子域名
            // 设置domain为 b.e.f.com.cn 或 .b.e.f.com.cn 没有区别，注意前面的点，即只要是为cookie显示的声明domain，前面带不带点没有区别
            if (request != null) {
//                cookie.setDomain(getDomainName(request));
                cookie.setDomain("localhost");
            }
            cookie.setPath("/");

            if (httpOnly != null) {
                cookie.setHttpOnly(httpOnly);
            }

            response.addCookie(cookie);

        } catch (UnsupportedEncodingException e) {
            log.error("Cookie Encode Error. ", e);
        }
    }

    /**
     * 得到cookie的域名
     * @param request
     * @return
     */
    private static String getDomainName(HttpServletRequest request) {
        String domainName = null;
        String serverName = request.getRequestURL().toString();
        if (StringUtils.isBlank(serverName)) {
            domainName = "";
        } else {
            // http://域名:端口号/uri
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] array = domainName.split("\\:");
            domainName = array[0];
        }
        return domainName;
    }

    /**
     * 得到cookie的值，不解码
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 得到cookie的值
     * @param request
     * @param cookieName
     * @param isDecoder
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i=0; i<cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Cookie Decode Error. ", e);
        }
        return retValue;
    }

}
