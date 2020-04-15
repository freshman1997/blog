package com.yuan.cn.blog.service.enums;

public enum CommentType {
    JUST_ADD(1), REPLY(2);

    int type;
    CommentType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
