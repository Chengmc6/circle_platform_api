package com.example.circle_platform_api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RegistrationStatus {
    CANCELED(0, "已取消"),
    REGISTERED(1, "已报名");

    @EnumValue
    private final int code;
    private final String desc;
}
