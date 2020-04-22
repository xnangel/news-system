package com.sunxn.news.item.service;

import com.sunxn.news.common.entity.UserInfo;
import com.sunxn.news.common.enums.NewsSystemExceptionEnum;
import com.sunxn.news.common.exception.SunxnNewsException;
import com.sunxn.news.item.config.JwtProperties;
import com.sunxn.news.item.mapper.UserMapper;
import com.sunxn.news.pojo.User;
import com.sunxn.news.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @data: 2020/4/21 21:52
 * @author: xiaoNan
 */
@Slf4j
@Service
@EnableConfigurationProperties({JwtProperties.class})
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param username  用户名
     * @param password  密码
     * @return          token
     */
    public String login(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (user == null) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        // 校验密码
        if (!StringUtils.equals(user.getPassword(), password)) {
            throw new SunxnNewsException(NewsSystemExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        // 用户名和密码正确
        try {
            String token = getTokenByUser(user.getId(), username);
            return token;
        } catch (Exception e) {
            log.error("【用户登录授权】用户凭证生成失败");
            throw new SunxnNewsException(NewsSystemExceptionEnum.CREATE_TOKEN_ERROR);
        }
    }

    /**
     * 根据用户id和用户名生成token数据
     */
    private String getTokenByUser(Long id, String username) throws Exception {
        return JwtUtils.generateToken(new UserInfo(id, username), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
    }
}
