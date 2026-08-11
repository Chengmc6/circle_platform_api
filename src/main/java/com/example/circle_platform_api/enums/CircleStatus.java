package com.example.circle_platform_api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CircleStatus {
    NORMAL(0, "正常"),
    DORMANT(1, "休眠"),
    DISSOLVED(2, "已解散");

    @EnumValue
    private final int code;
    private final String desc;
}
