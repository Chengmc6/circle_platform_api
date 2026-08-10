package com.example.circle_platform_api.security.config;

import com.example.circle_platform_api.security.handler.JwtAuthenticationEntryPoint;
import com.example.circle_platform_api.security.handler.UserAccessDeniedHandler;
import com.example.circle_platform_api.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RequestProperties requestProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CorsConfigurationSource corsConfigurationSource,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint entryPoint,
            UserAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        return httpSecurity
                // 1. 关闭 CSRF（JWT 无状态，不需要）
                .csrf(AbstractHttpConfigurer::disable)
                // 2. 启用 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // 3. jwt项目禁用表单登录（避免拦截自定义的登录接口）
                .formLogin(AbstractHttpConfigurer::disable)
                // 4. jwt项目禁用默认登出（避免session+重定向）
                .logout(AbstractHttpConfigurer::disable)
                // 5. 无状态 Session（JWT 核心）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 6. 请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 预检请求全部放行
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 公开接口
                        .requestMatchers(requestProperties.getPublicUrls().toArray(new String[0])).permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )
                // 7. 异常处理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(entryPoint)       // 未登录 → 401
                        .accessDeniedHandler(accessDeniedHandler)   // 已登录但无权限 → 403
                )
                // 8. 添加 JWT 过滤器（必须在 UsernamePasswordAuthenticationFilter 之前）
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
