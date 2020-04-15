package com.yuan.cn.blog.vo;

import com.yuan.cn.blog.pojo.User;

import java.util.List;

public class UserVo {
    private List<User> userList;
    private Integer total;

    public UserVo() {
    }

    public UserVo(List<User> userList, Integer total) {
        this.userList = userList;
        this.total = total;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "userList=" + userList +
                ", total=" + total +
                '}';
    }
}
