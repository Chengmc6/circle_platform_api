package com.example.circle_platform_api.common.exception;

import com.example.circle_platform_api.common.response.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code,String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode result) {
        super(result.getMessage());
        this.code = result.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.SERVER_ERROR.getCode();
    }
}
