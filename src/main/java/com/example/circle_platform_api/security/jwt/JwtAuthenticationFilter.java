package com.example.circle_platform_api.security.jwt;

import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.enums.UserStatus;
import com.example.circle_platform_api.security.service.SecurityUser;
import com.example.circle_platform_api.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {
        try{
            // 获取token
            String token= jwtUtil.extractToken(request);

            // token 为空 → 放行
            if (token==null){
                filterChain.doFilter(request,response);
                return;
            }

            // 已认证 → 放行（避免重复解析）
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null
                    && authentication.isAuthenticated()
                    && !(authentication instanceof AnonymousAuthenticationToken)){
                filterChain.doFilter(request,response);
                return;
            }

            // 尝试解析token
            Claims claims= jwtUtil.parseToken(token);
            String userIdStr=claims.getSubject();
            if(!StringUtils.hasText(userIdStr)){
                throw new BusinessException(ResultCode.TOKEN_INVALID);
            }

            // 解析查询用户信息
            User user=userService.getById(Long.valueOf(userIdStr));
            if (user==null){
                throw new BusinessException(ResultCode.USER_NOT_FOUND);
            }
            // 构建LoginUser
            SecurityUser loginUser=new SecurityUser(user.getId(), user.getUsername(), user.getRole(), user.getStatus()== UserStatus.ENROLLED);
            // 构建spring security认证Authentication
            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
            // 设置认证信息，用于授权
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            // token 解析失败（无效、签名错误、过期等）统一在这里处理，不抛异常中断流程
            log.debug("JWT Token 解析失败: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
        // 无论成功失败都继续过滤链
        filterChain.doFilter(request,response);
    }
}
