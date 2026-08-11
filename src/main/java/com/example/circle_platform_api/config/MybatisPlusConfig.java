package com.example.circle_platform_api.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus分页配置
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor pagination = new PaginationInnerInterceptor(DbType.MYSQL);

        // 可选配置
        pagination.setMaxLimit(500L);           // 单页最大限制，防止一次查太多
        pagination.setOverflow(false);         // 页码溢出时是否处理（false 返回空，true 继续查）

        interceptor.addInnerInterceptor(pagination);

        return interceptor;
    }

}
