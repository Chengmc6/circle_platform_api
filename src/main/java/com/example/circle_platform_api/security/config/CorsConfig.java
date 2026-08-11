package com.example.circle_platform_api.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final RequestProperties requestProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //允许的请求源
        configuration.setAllowedOriginPatterns(requestProperties.getAllowedOrigins());

        //允许所有请求方法，包括 OPTIONS, POST, GET, etc.
        configuration.setAllowedMethods(Collections.singletonList("*"));

        // 允许所有 Header，以便携带 Content-Type, Authorization 等
        configuration.setAllowedHeaders(List.of("*"));

        // 允许携带认证信息（JWT, Cookie等）
        configuration.setAllowCredentials(true);

        // 预检请求缓存时间（单位：秒）
        configuration.setMaxAge(Duration.ofHours(1));

        //注册配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对所有路径生效
        return source;
    }

}
