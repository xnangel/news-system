package com.sunxn.news.gateway.config;

import com.sunxn.news.utils.RsaUtils;
import lombok.Data;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @description:
 * @data: 2020/4/22 21:21
 * @author: xiaoNan
 */
@Data
@ConfigurationProperties(prefix = "sunxn.jwt")
public class JwtProperties {

    private String publicKeyPath;
    private String cookieName;

    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(this.publicKeyPath);
    }
}
