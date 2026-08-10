package com.example.circle_platform_api.security.service;

import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user= userService.getUserByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException(username+ResultCode.USER_NOT_FOUND.getMessage());
        }

        return new SecurityUser(user.getId(), username,user.getRole(),user.getStatus()==1);
    }
}
