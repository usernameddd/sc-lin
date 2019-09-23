package com.daizening.security.model.token;

import com.daizening.model.base.BaseUser;
import com.daizening.security.config.TokenProperties;
import com.daizening.security.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * Token 创建工厂类
 */
@Component
public class TokenFactory {

    private final TokenProperties properties;

    @Autowired
    public TokenFactory(TokenProperties properties) {
        this.properties = properties;
    }

    /**
     * 利用JWT， 生成token
     * @param context
     * @return
     */
    public AccessToken createAccessToken(UserContext context) {
        Optional.ofNullable(context.getUsername()).orElseThrow(() -> new IllegalArgumentException("Cannot create Token without username"));
//        Optional.ofNullable(context.getAuthorities()).orElseThrow(() -> new IllegalArgumentException("User doesn't have any privileges"));
        Claims claims = Jwts.claims().setSubject(context.getUsername());
        claims.put("scopes", context);
//        claims.put("scopes", context.getAuthorities().stream().map(Object::toString).collect(toList()));
        LocalDateTime currentTime = LocalDateTime.now();
        String token = Jwts.builder().setClaims(claims)
                .setIssuer(properties.getIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(properties.getExpirationTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, properties.getSigningKey())
                .compact();
        return new AccessToken(token, claims);
    }

    public AccessToken createAccessToken(BaseUser context) {
        Optional.ofNullable(context.getId()).orElseThrow(() -> new IllegalArgumentException("Cannot create Token without username"));
//        Optional.ofNullable(context.getAuthorities()).orElseThrow(() -> new IllegalArgumentException("User doesn't have any privileges"));
        Claims claims = Jwts.claims().setSubject(context.getId() + "");
        claims.put("scopes", context);
//        claims.put("scopes", context.getAuthorities().stream().map(Object::toString).collect(toList()));
        LocalDateTime currentTime = LocalDateTime.now();
        Date expireTime = Date.from(currentTime.plusMinutes(properties.getExpirationTime()).atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTime = Date.from(currentTime.plusMinutes(properties.getRefreshExpTime()).atZone(ZoneId.systemDefault()).toInstant());
        String token = Jwts.builder().setClaims(claims)
                .setIssuer(properties.getIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, properties.getSigningKey())
                .compact();
        return new AccessToken(token, claims, expireTime, refreshTime);
    }

    public Token createRefreshToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create Token without username");
        }
        LocalDateTime currentTime = LocalDateTime.now();
        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", userContext);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(properties.getIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(properties.getRefreshExpTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, properties.getSigningKey())
                .compact();
        return new AccessToken(token, claims);
    }
}
