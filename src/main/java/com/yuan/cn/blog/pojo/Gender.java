package com.yuan.cn.blog.pojo;

public enum Gender {
    MAN(0, "男"), WOMAN(1, "女");
    private int gender;
    private String genderString;
    Gender(int gender, String genderString){
        this.gender = gender;
        this.genderString = genderString;
    }

    public int getGender() {
        return gender;
    }

    public String getGenderString() {
        return genderString;
    }
}
