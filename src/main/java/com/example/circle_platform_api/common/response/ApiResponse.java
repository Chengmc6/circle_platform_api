package com.example.circle_platform_api.common.response;

public record ApiResponse<T>(
        int code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>(200,"SUCCESS",null);
    }

    public static <T> ApiResponse<T> success(String message){
        return new ApiResponse<>(200,message,null);
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(200,"SUCCESS",data);
    }

    public static <T> ApiResponse<T> success(String message,T data){
        return new ApiResponse<>(200,message,data);
    }

    public static <T> ApiResponse<T> fail(int code,String message){
        return new ApiResponse<>(code,message,null);
    }

    public static <T> ApiResponse<T> fail(ResultCode result){
        return new ApiResponse<>(result.getCode(), result.getMessage(), null);
    }

    public static <T> ApiResponse<T> fail(int code,String message,T data){
        return new ApiResponse<>(code,message,data);
    }
}
