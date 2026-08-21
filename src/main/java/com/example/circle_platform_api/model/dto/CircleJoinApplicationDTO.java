package com.example.circle_platform_api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CircleJoinApplicationDTO {
    @NotNull
    private Long userId;
    private String applyReason;
}
