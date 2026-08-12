package com.example.circle_platform_api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @NotNull
    private Long id;
    private String nickname;
    private String avatar;
}
