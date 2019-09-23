package com.daizening.filter;

import com.daizening.constant.Const;
import com.daizening.security.config.TokenProperties;
import com.daizening.security.model.token.RawAccessToken;
import io.jsonwebtoken.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

public class JwtCheckGatewayFilterFactory extends  AbstractGatewayFilterFactory<JwtCheckGatewayFilterFactory.Config> {

    @Resource
    private TokenProperties tokenProperties;

    public JwtCheckGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("----------------------");
            String path = exchange.getRequest().getURI().toString();
            System.out.println(path);
            if (path.matches(Const.NO_INTERCEPTOR_PATH)) {
                // 不需要拦截直接过
                System.out.println("sssssssssssssssssssss");
                return chain.filter(exchange);
            }
            String jwtToken = exchange.getRequest().getHeaders().getFirst(Const.TOKEN_HEADER_PARAM);
            // 校验jwtToken的合法性
            if (jwtToken != null) {
                // 合法，将用户id作为参数传递下去
                ServerHttpRequest request = exchange.getRequest();
//                System.out.println(exchange.getRequest().getHeaders().getFirst("userId"));
                // 传递的问题：如果用户在header中输入了userId，那么其转发程序收到的将会是用户发送的userId
                RawAccessToken accessToken = new RawAccessToken(jwtToken);
                System.out.println(jwtToken);
                Jws<Claims> claims = null;
                try {
                    claims = accessToken.parseClaims(tokenProperties.getSigningKey());
                } catch(UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
                    return setResponse(exchange, HttpStatus.UNAUTHORIZED);
                } catch(ExpiredJwtException expiredEx) {
                    return setResponse(exchange, HttpStatus.UNAUTHORIZED);
                }

                String subject = claims.getBody().getSubject();
                System.out.println(subject);

                ServerHttpRequest serverHttpRequest = request.mutate().header("userId", subject).build();
                ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).build();
//                HttpServletRequest httpRequest = (HttpServletRequest)exchange.getRequest();
                return chain.filter(serverWebExchange);
            }

            // 不合法（响应未登录异常）
            ServerHttpResponse response = exchange.getResponse();
            // 设置headers
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
            httpHeaders.add("Cache-Control", "no-store, no-cache, mush-revalidate, max-age=0");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            DataBuffer bodyDataBuffer = response.bufferFactory().wrap("未登录或登录超时".getBytes());
            return exchange.getResponse().setComplete();
//            return response.writeWith(Mono.just(bodyDataBuffer));
        };
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }

    public Mono<Void> setResponse(ServerWebExchange exchange, HttpStatus code) {
        // 不合法（响应未登录异常）
        ServerHttpResponse response = exchange.getResponse();
        // 设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, mush-revalidate, max-age=0");
        response.setStatusCode(code);
//        DataBuffer bodyDataBuffer = response.bufferFactory().wrap("content".getBytes());
        return exchange.getResponse().setComplete();
    }
}
