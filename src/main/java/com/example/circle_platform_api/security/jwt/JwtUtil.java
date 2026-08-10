package com.example.circle_platform_api.security.jwt;

import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.security.service.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Duration expiration;
    private SecretKey signingKey;

    private SecretKey getSigningKey(){
        if (signingKey == null) {
            if (secretKey == null || secretKey.length() < 32) {
                throw new IllegalStateException("JWT secret key is too weak or not configured. Please set a strong key (at least 32 characters) in application.yml");
            }
            signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
        return signingKey;
    }

    public String generateToken(SecurityUser user){
        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .claim("username",user.getUsername())
                .claim("role",user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration.toMillis()))
                .signWith(getSigningKey(),Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseToken(String token){
        try{
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException e){
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        }catch (JwtException e){
            throw new BusinessException(ResultCode.TOKEN_INVALID.getCode(),e.getMessage());
        }
    }

    public String extractToken(HttpServletRequest request) {
        // 优先从 Header 取 Bearer Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // 再尝试从 Cookie 取（支持 jwt 或 access_token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName()) || "access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Long getUserId(String token){
        return Long.parseLong(parseToken(token).getSubject());
    }

    public String getUsername(String token){
        return parseToken(token).get("username",String.class);
    }

}
