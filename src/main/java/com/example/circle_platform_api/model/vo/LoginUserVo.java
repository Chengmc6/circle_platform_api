package com.example.circle_platform_api.model.vo;

public record LoginUserVo(
        Long userId,
        String username,
        String nickname,
        String avatar,
        Integer role
) {
}
