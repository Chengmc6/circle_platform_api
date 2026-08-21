package com.example.circle_platform_api.model.vo;

import com.example.circle_platform_api.enums.CircleMemberRole;

public record CircleMemberVo(
        Long id,
        Long circleId,
        Long userId,
        String username,
        CircleMemberRole role
) {
}
