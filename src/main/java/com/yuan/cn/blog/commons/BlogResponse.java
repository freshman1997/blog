package com.yuan.cn.blog.commons;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @date 2020/2/15
 * 通用返回数据包装类
 * @param <T> 数据类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponse<T> implements Serializable {
    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BlogResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

    private BlogResponse(int status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public static <T> BlogResponse<T> SUCCESS(SuccessType type, String msg, T data){
        switch (type.getType()){
            case 1:
                return new BlogResponse<>(ResponseCode.SUCCESS.getCode(), null, null);
            case 2:
                return new BlogResponse<>(ResponseCode.SUCCESS.getCode(), msg, null);
            case 3:
                return new BlogResponse<>(ResponseCode.SUCCESS.getCode(), null, data);
            case 4:
                return new BlogResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
            case 5:
                return new BlogResponse<>(ResponseCode.ERROR.getCode(), msg, data);
            default:return null;
        }
    }

    public static <T> BlogResponse<T> ERROR(ErrorType type, int code, String msg, T data) {
        switch (type.getType()) {
            case 1:
                return new BlogResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription());
            case 2:
                return new BlogResponse<>(ResponseCode.ERROR.getCode(), msg);
            case 3:
                return new BlogResponse<>(code, msg);
            case 4:
                return new BlogResponse<>(ResponseCode.ERROR.getCode(), msg, data);
            case 5:
                return new BlogResponse<>(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription(), data);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "BlogResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
