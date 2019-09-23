package com.daizening.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "daizening.security.token")
@Data
public class TokenProperties {
    /**
     * token 的过期时间
     */
    private Integer expirationTime;

    /**
     * 发行人
     */
    private String issuer;

    /**
     * 使用的签名KEY
     */
    private String signingKey;

    /**
     *  刷新过期时间
     */
    private Integer refreshExpTime;

}
