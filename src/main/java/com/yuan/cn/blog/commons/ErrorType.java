package com.yuan.cn.blog.commons;

public enum ErrorType {
    NORMAL_ERROR(1),
    ERROR_WITH_MESSAGE(2),
    ERROR_WITH_CODE_MESSAGE(3),
    ERROR_WITH_DATA_MESSAGE(4),
    ERROR_NEED_LOGIN(5);


    private int type;
    ErrorType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
