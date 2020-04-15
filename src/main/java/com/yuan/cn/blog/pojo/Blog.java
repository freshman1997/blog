package com.yuan.cn.blog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuan.cn.blog.annotation.JsonIgnoredNotAdminAction;

import java.util.Date;

public class Blog {
    private Integer id;
    private Integer love;
    private String mark;
    private String table;
    private String title;
    private String content;
    private Date createTime;
    @JsonIgnore
    private Integer userId;
    private Integer categoryId;
    private Integer readNumber;
    private Integer commentNumber;
    private String category;


    public Blog(Integer id, Integer love, String mark, String table, String title, String content, Date createTime, Integer userId, Integer categoryId, Integer readNumber,Integer commentNumber, String category) {
        this.id = id;
        this.love = love;
        this.mark = mark;
        this.table = table;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.userId = userId;
        this.categoryId = categoryId;
        this.readNumber = readNumber;
        this.commentNumber = commentNumber;
        this.category = category;
    }

    public Blog() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(Integer readNumber) {
        this.readNumber = readNumber;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", love=" + love +
                ", mark='" + mark + '\'' +
                ", table='" + table + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", readNumber=" + readNumber +
                ", commentNumber=" + commentNumber +
                ", category='" + category + '\'' +
                '}';
    }
}
