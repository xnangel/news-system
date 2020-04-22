package com.sunxn.news.item;

import com.sunxn.news.common.entity.UserInfo;
import com.sunxn.news.utils.JwtUtils;
import com.sunxn.news.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @description:    生成rsa密钥对测试类
 * @data: 2020/4/21 22:23
 * @author: xiaoNan
 */
public class JwtTest {

    private static final String PUBLIC_KEY_PATH = "E:\\prictice6_JavaEE\\projects\\web-crawler-news-system\\rsakey\\rsa.pub";
    private static final String PRIVATE_KEY_PATH = "E:\\prictice6_JavaEE\\projects\\web-crawler-news-system\\rsakey\\rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 生成rsa的私钥和公钥
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(PUBLIC_KEY_PATH, PRIVATE_KEY_PATH, "hello Xiao nan, you just your");
    }

    /**
     * 从私钥和公钥的文件中获取私钥和公钥
     */
    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(PUBLIC_KEY_PATH);
        this.privateKey = RsaUtils.getPrivateKey(PRIVATE_KEY_PATH);
    }

    /**
     * 私钥生成token
     */
    @Test
    public void testGenerateToken() throws Exception {
        String token = JwtUtils.generateToken(new UserInfo(1L, "xiao nan"), this.privateKey, 5);
        System.out.println("token = " + token);
    }

    /**
     * 公钥解析token，获取对象信息
     */
    @Test
    public void testParseToken() {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ4aWFvIG5hbiIsImV4cCI6MTU4NzU0NDE4N30.bAcNz3RJRNO5b92dvjN3w-MzqLyRw-lARzRo0ogiV9vEqvvnZOn3HkZef88jmVXM488QtnXkuSL0sb2E5005U5FpfLIGqrDcB9ho6VmMzMTFq7kvCWDAn96JonTUko7ZL33uaIwEWNMst-ziubWqWfSB_tjFsmRM_1f6HPeNAlo";
        // 解析token
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.publicKey);
        System.out.println("id: " + userInfo.getId());
        System.out.println("username: " + userInfo.getUsername());
    }
}
