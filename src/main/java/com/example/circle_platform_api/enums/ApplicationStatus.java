package com.example.circle_platform_api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplicationStatus {

    DRAFT(0, "草稿"),
    PENDING(1, "待审核"),
    APPROVED(2, "已通过"),
    REJECTED(3, "已拒绝");

    @EnumValue
    private final int code;
    private final String desc;

    private static final Map<Integer,ApplicationStatus> CACHE= Arrays.stream(values())
            .collect(Collectors.toMap(ApplicationStatus::getCode,a->a));
    public static ApplicationStatus fromCode(int code) {
        return CACHE.get(code);
    }
}
