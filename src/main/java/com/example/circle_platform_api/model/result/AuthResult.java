package com.example.circle_platform_api.model.result;

public record AuthResult(
        String token,
        Long userId
) {
}
