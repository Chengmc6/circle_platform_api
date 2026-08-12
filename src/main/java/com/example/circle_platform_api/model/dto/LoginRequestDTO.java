package com.example.circle_platform_api.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "请输入用户名") String username,
        @NotBlank(message = "请输入密码") String password
) {
}
