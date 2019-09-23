package com.daizening.security.model.token;

import io.jsonwebtoken.Claims;

import java.util.Date;

public class AccessToken implements Token {

    private final String rawToken;
    private Date expireTime;
    private Date refreshTime;
    private Claims claims;

    protected AccessToken(final String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
    }

    protected AccessToken(final String token, Claims claims, Date expireTime, Date refreshTime) {
        this.rawToken = token;
        this.claims = claims;
        this.expireTime = expireTime;
        this.refreshTime = refreshTime;
    }

    @Override
    public String getToken() {
        return this.rawToken;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public Claims getClaims() {
        return claims;
    }
}
