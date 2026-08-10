package com.example.circle_platform_api.security.handler;

import com.example.circle_platform_api.common.response.ApiResponse;
import com.example.circle_platform_api.common.response.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * 权限认证失败返回403
     * @param request that resulted in an <code>AccessDeniedException</code>
     * @param response so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("权限不足访问: {} {}", request.getMethod(), request.getRequestURI());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ApiResponse<?> body=ApiResponse.fail(ResultCode.FORBIDDEN.getCode(), accessDeniedException.getMessage());

        try{
            response.getWriter().write(objectMapper.writeValueAsString(body));
        }catch (Exception e){
            log.error("写入权限拒绝响应失败", e);
            response.getWriter().write("{\"code\":403,\"message\":\"无权限访问\"}");
        }

    }
}
