package com.example.circle_platform_api.application;

import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.mapstruct.UserConvert;
import com.example.circle_platform_api.model.dto.UserRegisterDTO;
import com.example.circle_platform_api.model.vo.LoginUserVo;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final UserConvert userConvert;

    public LoginUserVo getLoginUserInfo(Long userId){

        User user= userService.getById(userId);

        if (user==null){
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return new LoginUserVo(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar(),
                user.getRole());
    }

    public void register(UserRegisterDTO dto){

        User user= userService.getUserByUsername(dto.getUsername());

        if(user!=null){
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        User registerUser=userConvert.registerToUser(dto);

        userService.save(registerUser);

    }

}
