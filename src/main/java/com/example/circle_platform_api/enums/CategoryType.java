package com.example.circle_platform_api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CategoryType {
    CIRCLE_TYPE(0, "社团分类"),
    FACILITY_TYPE(1, "设施分类");

    @EnumValue
    private final int code;
    private final String desc;
}
