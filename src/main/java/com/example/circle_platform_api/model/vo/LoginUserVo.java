package com.example.circle_platform_api.model.vo;

import com.example.circle_platform_api.enums.UserRole;

public record LoginUserVo(
        Long userId,
        String username,
        String nickname,
        String avatar,
        UserRole role
) {
}
