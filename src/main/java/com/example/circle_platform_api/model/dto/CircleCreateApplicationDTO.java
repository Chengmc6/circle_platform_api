package com.example.circle_platform_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CircleCreateApplicationDTO {
    @NotNull
    private Long userId;
    @NotBlank(message = "请输入社团名称")
    private String name;
    private String description;
    private Long categoryId;
    private String applyReason;
}
