package com.example.circle_platform_api.model.vo;

import com.example.circle_platform_api.enums.ApplicationStatus;

import java.time.LocalDateTime;

public record CircleJoinApplicationVo(
        Long id,
        Long circleId,
        Long userId,
        String username,
        String applyReason,
        ApplicationStatus status,
        LocalDateTime createdAt
) {
}
