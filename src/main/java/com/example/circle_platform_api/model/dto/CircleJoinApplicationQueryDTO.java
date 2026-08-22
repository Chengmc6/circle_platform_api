package com.example.circle_platform_api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CircleJoinApplicationQueryDTO {
    @NotNull
    private Integer Status;
    private Long pageNum=1L;
    private Long pageSize=10L;
}
