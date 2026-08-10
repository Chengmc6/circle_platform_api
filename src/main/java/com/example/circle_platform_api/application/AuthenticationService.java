package com.example.circle_platform_api.application;

import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.model.result.AuthResult;
import com.example.circle_platform_api.security.jwt.JwtUtil;
import com.example.circle_platform_api.security.service.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResult authenticate(String username, String password) {

        // 创建认证令牌
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        // 进行认证
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 检查认证主体是否为SecurityUser
        if (!(authentication.getPrincipal() instanceof SecurityUser securityUser)) {
            log.warn("用户 {} 认证失败", username);
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }
        // 生成JWT令牌
        String token = jwtUtil.generateToken(securityUser);

        return new AuthResult(token, securityUser.getUserId());
    }

}
