package com.sunxn.news.utils;

import com.sunxn.news.common.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @description: 实现无状态登录授权生成的token
 *      JWT：Json Web Token，是JSON风格轻量级的授权和身份认证规范，可实现无状态、分布式的Web应用授权。
 *          数据格式：JWT包括三部分数据：
 *              * header：头部，通常头部有两部分消息：
 *                  - 声明类型，这里是JWT
 *                  - 加密算法，自定义
 *              * Payload：载荷，就是有效数据，一般包含下面信息：
 *                  - 用户身份信息（注意，这里因为采用base64加密，可解密，因此不要存放敏感信息）
 *                  - 注册声明：如token的签发时间，过期时间，签发人等
 *              * Signature：签名，是整个数据的认证信息。一般根据前两步的数据，再加上服务的密钥（不要泄露，最好周期性更换），
 *                  通过加密算法生成。用于验证整个数据完整和可靠性。
 * @data: 2020/4/21 23:02
 * @author: xiaoNan
 */
public class JwtUtils {
    // TODO 授权未完成

    /**
     * 私钥加密token
     * @param userInfo      载荷中的数据，包括用户id和用户名称
     * @param privateKey    私钥
     * @param expireMinutes 过期时间，单位秒
     * @return
     * @throws Exception
     */
    public static String generateToken(UserInfo userInfo, PrivateKey privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public static UserInfo getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstants.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstants.JWT_KEY_USER_NAME))
        );
    }

    /**
     * 公钥解析token
     * @param token
     * @param publicKey
     * @return
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }
}
