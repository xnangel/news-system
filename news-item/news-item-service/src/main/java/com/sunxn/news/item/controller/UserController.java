package com.sunxn.news.item.controller;

import com.sunxn.news.common.entity.UserInfo;
import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.common.utils.CookieUtil;
import com.sunxn.news.item.config.JwtProperties;
import com.sunxn.news.item.service.UserService;
import com.sunxn.news.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @data: 2020/4/21 21:49
 * @author: xiaoNan
 */
@RestController
@RequestMapping("/user")
@EnableConfigurationProperties(JwtProperties.class)
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = userService.login(username, password);
        CookieUtil.setCookie(request, response, jwtProperties.getCookieName(), token, null, null, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("SUNXN_TOKEN") String token,
            HttpServletRequest request, HttpServletResponse response
    ) {
        if (StringUtils.isBlank(token)) {
            // 如果没有token，证明未登录，返回403
            throw new SunxnNewsException(NewsSystemExceptionEnum.UNAUTHORIZED);
        }

        try {
            // 解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 刷新token，重新生成token
            String newToken = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            // 写入cookie
            CookieUtil.setCookie(request, response, jwtProperties.getCookieName(), newToken, null, null, true);
            // 已登录，返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            // token已过期，或者 token被篡改
            throw new SunxnNewsException(NewsSystemExceptionEnum.UNAUTHORIZED);
        }
    }
}
