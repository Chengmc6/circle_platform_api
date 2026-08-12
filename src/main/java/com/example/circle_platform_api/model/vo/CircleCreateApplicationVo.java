package com.example.circle_platform_api.model.vo;

import com.example.circle_platform_api.enums.ApplicationStatus;

public record CircleCreateApplicationVo(
        Long userId,
        String name,
        Long categoryId,
        String categoryName,
        ApplicationStatus status
) {
}
