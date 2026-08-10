package com.example.circle_platform_api.security.cookie;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public ResponseCookie generateCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)          //使用https安全请求设置为true，个人开发测试使用false，本地开发localhost + secure=true 是允许的
                .path("/")              //cookie生效路径，“/”全路径
                .sameSite("Lax")
                .maxAge(Duration.ofHours(1))        //生效时间1小时
                .build();
    }

    public ResponseCookie clearCookie() {
        return ResponseCookie.from("jwt", null)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)          //立即过期
                .build();
    }

}
