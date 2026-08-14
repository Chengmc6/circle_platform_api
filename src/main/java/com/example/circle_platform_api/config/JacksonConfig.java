package com.example.circle_platform_api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return builder-> {

            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("HH:mm:ss");

            // 序列化 (后端 -> 前端)
            builder.serializerByType(LocalDateTime.class,new LocalTimeSerializer(dateTimeFormatter));
            builder.serializerByType(LocalDate.class,new LocalDateSerializer(dateFormatter));
            builder.serializerByType(LocalTime.class,new LocalTimeSerializer(timeFormatter));
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            // 反序列化 (前端 -> 后端)
            builder.deserializerByType(LocalDateTime.class,new LocalTimeDeserializer(dateTimeFormatter));
            builder.deserializerByType(LocalDate.class,new LocalDateDeserializer(dateFormatter));
            builder.deserializerByType(LocalTime.class,new LocalTimeDeserializer(timeFormatter));

            // 禁用时间戳格式
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // 允许 JSON 包含 DTO 中没有的字段 (忽略未知属性，对接 AI 或前端时非常有用)
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            // 允许把浮点数写成整数，或者字符串写成数字等宽松解析
            builder.featuresToEnable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);

            // --- 时区配置 ---
            builder.timeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        };
    }
}
