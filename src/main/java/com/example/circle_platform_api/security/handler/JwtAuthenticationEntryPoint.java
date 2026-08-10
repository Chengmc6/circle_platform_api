package com.example.circle_platform_api.security.handler;

import com.example.circle_platform_api.common.response.ApiResponse;
import com.example.circle_platform_api.common.response.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * 认证失败时调用返回401
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn("未认证访问: {} {}", request.getMethod(), request.getRequestURI());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<?> body = ApiResponse.fail(ResultCode.UNAUTHORIZED.getCode(), authException.getMessage());

        try {
            response.getWriter().write(objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            log.error("写入认证失败响应失败", e);
            // 兜底：直接写简单字符串
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
        }
    }
}
