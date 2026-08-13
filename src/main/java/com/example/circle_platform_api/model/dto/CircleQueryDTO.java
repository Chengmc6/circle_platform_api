package com.example.circle_platform_api.model.dto;

import lombok.Data;

@Data
public class CircleQueryDTO {
    private String keywords;
    private Long pageNum= 1L;
    private Long pageSize= 8L;
}
