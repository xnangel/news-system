package com.sunxn.news.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sunxn.news.common.entity.UserInfo;
import com.sunxn.news.common.utils.CookieUtil;
import com.sunxn.news.gateway.config.FilterProperties;
import com.sunxn.news.gateway.config.JwtProperties;
import com.sunxn.news.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:    授权过滤器
 * @data: 2020/4/21 20:29
 * @author: xiaoNan
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        // 过滤类型：前置过滤
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 过滤器顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    /**
     * 是否过滤
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 获取请求的uri路径
        String path = request.getRequestURI();
        return !isAllowPath(path);
    }

    private boolean isAllowPath(String path) {
        for (String allowPath: filterProperties.getAllowPaths()) {
            // 判断是否允许
            if (path.startsWith(allowPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = currentContext.getRequest();
        // 获取token
        String token = CookieUtil.getCookieValue(request, jwtProperties.getCookieName());
        try {
            // 解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // TODO 验证权限
        } catch (Exception e) {
            // 解析token失败，未登录，拦截
            currentContext.setSendZuulResponse(false);
            // 返回状态码
            currentContext.setResponseStatusCode(403);
        }
        return null;
    }
}
