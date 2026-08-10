package com.example.circle_platform_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(String.class, LocalDateTime.class, source -> {
            if (!StringUtils.hasText(source)) {
                return null;
            }
            return LocalDateTime.parse(source, DATE_TIME_FORMATTER);
        });

        registry.addConverter(String.class, LocalDate.class, source -> {
            if (!StringUtils.hasText(source)) {
                return null;
            }
            return LocalDate.parse(source, DATE_FORMATTER);
        });

        registry.addConverter(String.class, LocalTime.class, source -> {
            if (!StringUtils.hasText(source)) {
                return null;
            }
            return LocalTime.parse(source, TIME_FORMATTER);
        });
    }

}
