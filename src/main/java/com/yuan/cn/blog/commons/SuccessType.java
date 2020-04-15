package com.yuan.cn.blog.commons;

public enum SuccessType {
    JUST_SUCCESS(1),
    SUCCESS_WITH_MESSAGE(2),
    SUCCESS_WITH_DATA(3),
    SUCCESS_WITH_ALL(4),
    SUCCESS_WITH_ERROR(5);

    private int type;

    SuccessType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
