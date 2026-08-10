package com.example.circle_platform_api.security.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class SecurityUser implements UserDetails {

    private final Long userId;
    private final String username;
    private final Integer role;
    private final Boolean enabled;

    public SecurityUser(Long userId, String username, Integer role,Boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.enabled=enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role == 1 ? "ROLE_ADMIN" : "ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null; //JWT 不需要密码
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
