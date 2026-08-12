package com.example.circle_platform_api.controller;

import com.example.circle_platform_api.application.AuthenticationService;
import com.example.circle_platform_api.application.UserApplicationService;
import com.example.circle_platform_api.common.response.ApiResponse;
import com.example.circle_platform_api.model.dto.LoginRequestDTO;
import com.example.circle_platform_api.model.dto.UserRegisterDTO;
import com.example.circle_platform_api.model.dto.UserUpdateDTO;
import com.example.circle_platform_api.model.result.AuthResult;
import com.example.circle_platform_api.model.vo.LoginUserVo;
import com.example.circle_platform_api.model.vo.UserVo;
import com.example.circle_platform_api.security.cookie.CookieUtil;
import com.example.circle_platform_api.security.service.SecurityUser;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author GAOMING
 * @since 2026-08-09
 */
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserApplicationService userApplicationService;
    private final CookieUtil cookieUtil;

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody @Valid @NotNull UserRegisterDTO dto) {
        userApplicationService.register(dto);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    public ApiResponse<LoginUserVo> passwordLogin(@RequestBody @NotNull @Valid LoginRequestDTO dto, HttpServletResponse response) {
        AuthResult result = authenticationService.authenticate(dto.username(), dto.password());
        LoginUserVo vo = userApplicationService.getLoginUserInfo(result.userId());
        // 生成Cookie
        ResponseCookie cookie = cookieUtil.generateCookie(result.token());
        // 添加Cookie到响应头
        response.addHeader(HttpHeaders.SET_COOKIE, String.valueOf(cookie));

        return ApiResponse.success(vo);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        // 生成清除Cookie
        ResponseCookie cookie = cookieUtil.clearCookie();
        // 添加清除Cookie到响应头
        response.addHeader(HttpHeaders.SET_COOKIE, String.valueOf(cookie));
        // 清除安全上下文
        SecurityContextHolder.clearContext();

        return ApiResponse.success();
    }

    @GetMapping("/me")
    public ApiResponse<UserVo> getUserInfo(@AuthenticationPrincipal SecurityUser securityUser){
        return ApiResponse.success(userApplicationService.getUser(securityUser.getUserId()));
    }

    @PostMapping("/update")
    public ApiResponse<Void> updateUserInfo(@RequestBody @NotNull @Valid UserUpdateDTO dto){
        userApplicationService.updateUser(dto);
        return ApiResponse.success();
    }

}
