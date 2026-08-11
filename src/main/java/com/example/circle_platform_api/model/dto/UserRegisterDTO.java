package com.example.circle_platform_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 12, message = "请输入6-12位的密码")
    private String password;
    @Email(message = "邮箱格式不正确")
    private String email;
    private String nickname;
    private String avatar;
}
