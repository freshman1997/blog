package com.yuan.cn.blog.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuan.cn.blog.annotation.JsonIgnoredNotAdminAction;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    
    private Integer id;
    private String username;
    private Integer gender;
    private String headPhotoUrl;
    private Date birthday;
    private String email;
    @JsonIgnoredNotAdminAction
    private String password;
    private Integer status;
    private Date createTime;
    private Integer age;
    private Integer isAdmin;
    private int blogNumber;
    public User(){}

    public User(Integer id, String username, Integer gender, String headPhotoUrl, String email, String password,
                Integer status, Date createTime, Integer age, Date birthday, Integer isAdmin) {
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.headPhotoUrl = headPhotoUrl;
        this.email = email;
        this.password = password;
        this.status = status;
        this.createTime = createTime;
        this.age = age;
        this.birthday = birthday;
        this.isAdmin = isAdmin;
    }

    public int getBlogNumber() {
        return blogNumber;
    }

    public void setBlogNumber(int blogNumber) {
        this.blogNumber = blogNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getHeadPhotoUrl() {
        return headPhotoUrl;
    }

    public void setHeadPhotoUrl(String headPhotoUrl) {
        this.headPhotoUrl = headPhotoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", gender=" + gender +
                ", headPhotoUrl='" + headPhotoUrl + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", age=" + age +
                ", isAdmin=" + isAdmin +
                ", blogNumber=" + blogNumber +
                '}';
    }
}
