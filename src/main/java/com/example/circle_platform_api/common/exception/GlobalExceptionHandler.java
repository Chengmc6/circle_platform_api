package com.example.circle_platform_api.common.exception;

import com.example.circle_platform_api.common.response.ApiResponse;
import com.example.circle_platform_api.common.response.ResultCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Object> HandleSystemException(BusinessException ex){
        return ApiResponse.fail(ResultCode.fromCode(ex.getCode()));
    }

    /**
     * Security 认证相关异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Object> handleAuthenticationException(AuthenticationException ex) {
        if (ex instanceof BadCredentialsException) {
            return ApiResponse.fail(ResultCode.LOGIN_ERROR);
        } else if (ex instanceof LockedException || ex instanceof DisabledException) {
            return ApiResponse.fail(ResultCode.USER_DISABLED);
        }
        return ApiResponse.fail(ResultCode.UNAUTHORIZED.getCode(),ResultCode.UNAUTHORIZED.getMessage(), ex.getMessage());
    }

    /**
     * 参数校验异常（@Valid / @Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse(ResultCode.VALIDATION_ERROR.getMessage());

        return ApiResponse.fail(ResultCode.VALIDATION_ERROR.getCode(), message);
    }

    /**
     * 参数校验异常（@Validated 在单个参数上）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return ApiResponse.fail(ResultCode.VALIDATION_ERROR.getCode(), ex.getMessage());
    }

    /**
     * 404 资源未找到
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("404 Not Found: {}", ex.getRequestURL());
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), "请求的资源不存在");
    }

    /**
     * JSON 格式解析失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Object> handleJsonParseError(HttpMessageNotReadableException ex) {
        log.warn("Not Readable: {}", ex.getMessage());
        return ApiResponse.fail(ResultCode.BAD_REQUEST.getCode(), "JSON 格式错误");
    }

    /**
     * 参数类型转换失败（例如 String 传给 Long）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type Mismatch: {}", ex.getMessage());
        return ApiResponse.fail(ResultCode.BAD_REQUEST.getCode(), "参数类型错误");
    }

    /**
     * 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception ex) {
        // 生产环境建议不要把真实异常信息返回给前端
        // ex.printStackTrace(); // 或使用日志框架记录
        log.error("系统捕获到未处理异常: ", ex);
        return ApiResponse.fail(ResultCode.SERVER_ERROR);
    }

}
