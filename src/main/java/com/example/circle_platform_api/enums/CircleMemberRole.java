package com.example.circle_platform_api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CircleMemberRole {
    MEMBER(0, "普通成员"),
    VICE_PRESIDENT(1, "副社长"),
    PRESIDENT(2, "社长");

    @EnumValue
    private final int code;
    private final String desc;
}
