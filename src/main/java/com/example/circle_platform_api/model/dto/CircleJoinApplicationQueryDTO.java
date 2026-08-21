package com.example.circle_platform_api.model.dto;

import lombok.Data;

@Data
public class CircleJoinApplicationQueryDTO {
    private Integer Status;
    private Long pageNum=1L;
    private Long pageSize=10L;
}
