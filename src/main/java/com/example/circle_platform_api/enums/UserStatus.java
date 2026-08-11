package com.example.circle_platform_api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserStatus {
    ENROLLED(0, "在籍"),
    GRADUATED(1, "毕业/休学");

    @EnumValue
    private final int code;
    private final String desc;
}
