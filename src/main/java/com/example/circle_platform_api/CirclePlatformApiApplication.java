package com.example.circle_platform_api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@MapperScan("com.example.circle_platform_api.mapper")
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class CirclePlatformApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CirclePlatformApiApplication.class, args);
    }

}
