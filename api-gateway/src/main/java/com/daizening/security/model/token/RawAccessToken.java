package com.daizening.security.model.token;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawAccessToken implements Token {

    private static Logger logger = LoggerFactory.getLogger(RawAccessToken.class);

    private String token;

    public RawAccessToken(String token) {
        this.token = token;
    }

    public Jws<Claims> parseClaims(String signingKey) {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
//        try {
//            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
//        } catch(UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
//            logger.error("token 错误（Invalid Token", ex);
//            throw new RuntimeException("Invalid token: ", ex);
//        } catch(ExpiredJwtException expiredEx) {
//            logger.info("Token is expired(token已经过期了)", expiredEx);
//            throw new RuntimeException("Token expired", expiredEx);
//        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
