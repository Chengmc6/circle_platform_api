package com.example.circle_platform_api.model.vo;

import com.example.circle_platform_api.enums.UserRole;

public record UserVo(
        Long id,
        String username,
        String email,
        String nickname,
        String avatar,
        UserRole role
) {
}
