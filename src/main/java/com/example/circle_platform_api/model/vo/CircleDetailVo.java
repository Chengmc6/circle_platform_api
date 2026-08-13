package com.example.circle_platform_api.model.vo;

public record CircleDetailVo(
        Long id,
        String name,
        String description,
        Long ownerId,
        String ownerName,
        Integer memberCount
) {
}
