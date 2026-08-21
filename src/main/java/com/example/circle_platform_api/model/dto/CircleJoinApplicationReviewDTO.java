package com.example.circle_platform_api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CircleJoinApplicationReviewDTO {
    @NotNull
    private Long reviewId;
    private String reviewMark;
}
