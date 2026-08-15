package com.example.circle_platform_api.model.vo;

import com.example.circle_platform_api.enums.ApplicationStatus;

import java.time.LocalDateTime;

public record CircleCreateApplicationVo(
        Long id,
        Long userId,
        String applyUserName,
        Long categoryId,
        String name,
        ApplicationStatus status,
        LocalDateTime createdAt
) {
}
