package com.sunxn.news.item.config;

import com.sunxn.news.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @description:
 * @data: 2020/4/22 16:56
 * @author: xiaoNan
 */
@Data
@ConfigurationProperties(prefix = "sunxn.jwt")
public class JwtProperties {

    private String secret;
    private String publicKeyPath;
    private String privateKeyPath;
    private int expire;
    private String cookieName;

    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 私钥
     */
    private PrivateKey privateKey;

    /**
     * 对象一旦实例化【后】，就应该读取公钥和私钥
     * 被 @PostConstruct 修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次
     * 整个bean的初始化执行顺序：Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        // 公钥私钥如果不存在，先生成
        File pubPath = new File(this.publicKeyPath);
        File priPath = new File(this.privateKeyPath);
        if (!pubPath.exists() || !priPath.exists()) {
            // 生成公钥和私钥
            RsaUtils.generateKey(this.publicKeyPath, this.privateKeyPath, this.secret);
        }
        // 读取公钥和私钥
        this.publicKey = RsaUtils.getPublicKey(this.publicKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(this.privateKeyPath);
    }
}
