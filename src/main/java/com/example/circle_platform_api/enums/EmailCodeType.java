package com.example.circle_platform_api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EmailCodeType {
    FORGOT_PASSWORD(0, "忘记密码"),
    OTHER(1, "其他用途");

    @EnumValue
    private final int code;
    private final String desc;
}
