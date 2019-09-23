package com.daizening.conf;

import com.daizening.filter.JwtCheckGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public JwtCheckGatewayFilterFactory jwtCheckGatewayFilterFactory() {
        return new JwtCheckGatewayFilterFactory();
    }
}
