package com.example.circle_platform_api.model.dto;

import lombok.Data;

@Data
public class CircleCreateApplicationPageDTO {
    private Integer status;
    private Long pageNum=1L;
    private Long pageSize=10L;
}
