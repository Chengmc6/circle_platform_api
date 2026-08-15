package com.example.circle_platform_api.model.dto;

import lombok.Data;

@Data
public class CircleCreateApplicationUpdateDTO {
    private String name;
    private String description;
    private Long categoryId;
    private String applyReason;
}
