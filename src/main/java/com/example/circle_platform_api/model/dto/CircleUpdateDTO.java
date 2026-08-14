package com.example.circle_platform_api.model.dto;

import lombok.Data;

@Data
public class CircleUpdateDTO {
    private String name;
    private String description;
    private Long categoryId;
}
