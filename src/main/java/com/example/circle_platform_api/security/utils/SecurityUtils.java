package com.example.circle_platform_api.security.utils;

import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.security.service.SecurityUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static SecurityUser getCurrentUser(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        // 未认证 或 是匿名用户
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof SecurityUser securityUser)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        return securityUser;
    }

    public static Long getCurrentUserId(){
        return getCurrentUser().getUserId();
    }
}
