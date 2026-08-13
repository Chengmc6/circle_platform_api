package com.example.circle_platform_api.common.response;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ResultCode {

    // === HTTP 通用状态码 ===
    SUCCESS(200, "操作成功"),
    SERVER_ERROR(500, "系统异常"),
    BAD_REQUEST(400, "错误请求"),
    UNAUTHORIZED(401, "未登录"),
    FORBIDDEN(403, "无权限"),
    VALIDATION_ERROR(422, "参数校验失败"),
    TOKEN_EXPIRED(9001, "token已过期"),
    TOKEN_INVALID(9002, "token解析失败"),

    // === 业务错误码 ===
    // 用户相关 1000 ~ 1099
    OPERATION_FAILED(1000, "操作失败"),
    USER_NOT_FOUND(1001, "用户不存在"),
    LOGIN_ERROR(1002, "用户名或密码错误"),
    PASSWORD_REPETITION(1003, "新旧密码不能相同"),
    USERNAME_EXISTS(1004, "用户名已存在"),
    USER_DISABLED(1005, "用户已被禁用"),
    USER_DELETED(1006, "用户已被删除"),
    PASSWORD_INCORRECT(1007, "密码错误"),
    ROLE_NOT_FOUND(1008, "角色不存在"),
    USER_UPDATE_FAILED(1009,"用户更新失败"),

    PENDING_APPLICATION_EXISTS(2000,"已存在待审核的申请"),
    APPLICATION_PROCESSED(2001,"申请已处理"),
    APPLICATION_NOT_FOUND(2002,"申请不存在"),
    CIRCLE_NOT_FOUND(2003,"社团不存在"),
    CIRCLE_DISSOLVED(2004,"该社团已解散，无法查看详情")
    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private static final Map<Integer, ResultCode> CACHE =
            Arrays.stream(values()).collect(Collectors.toMap(ResultCode::getCode, r -> r));

    public static ResultCode fromCode(int code) {
        return CACHE.getOrDefault(code, ResultCode.SERVER_ERROR);
    }
}
