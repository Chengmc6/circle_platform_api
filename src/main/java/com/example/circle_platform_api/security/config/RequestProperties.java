package com.example.circle_platform_api.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "req")
public class RequestProperties {
    private List<String> publicUrls;
    private List<String> allowedOrigins;
}
