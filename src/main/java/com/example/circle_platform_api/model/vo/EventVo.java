package com.example.circle_platform_api.model.vo;

import java.time.LocalDateTime;

public record EventVo(
        Long id,
        String title,
        Long circleId,
        String circleName,
        String location,
        LocalDateTime startTime,
        Integer remain
) {
}
