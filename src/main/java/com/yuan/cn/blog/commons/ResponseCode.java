package com.yuan.cn.blog.commons;

public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(1001, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(1002, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
