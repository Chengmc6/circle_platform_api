package com.example.circle_platform_api.application;

import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.mapstruct.UserConvert;
import com.example.circle_platform_api.model.dto.UserRegisterDTO;
import com.example.circle_platform_api.model.dto.UserUpdateDTO;
import com.example.circle_platform_api.model.vo.LoginUserVo;
import com.example.circle_platform_api.model.vo.UserVo;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final UserConvert userConvert;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginUserVo getLoginUserInfo(Long userId) {

        User user = userService.getById(userId);

        if (user == null) {
            log.debug("不存在的用户");
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return new LoginUserVo(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar(),
                user.getRole());
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO dto) {

        User user = userService.getUserByUsername(dto.getUsername());

        if (user != null) {
            log.warn("注册的用户名已存在");
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        User registerUser = userConvert.registerToUser(dto);

        // 密码加密
        String encodePwd = passwordEncoder.encode(dto.getPassword());

        registerUser.setPassword(encodePwd);

        userService.save(registerUser);

    }

    @Transactional(readOnly = true)
    public UserVo getUser(Long userId) {
        User user = userService.getById(userId);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return new UserVo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getNickname(),
                user.getAvatar(),
                user.getRole()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long currentUserId, UserUpdateDTO dto) {

        User user = userService.getById(currentUserId);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        boolean success = userService.lambdaUpdate()
                .eq(User::getId, currentUserId)
                .set(User::getNickname, dto.getNickname())
                .set(User::getAvatar, dto.getAvatar())
                .update();

        if (!success) {
            throw new BusinessException(ResultCode.USER_UPDATE_FAILED);
        }

    }
}
