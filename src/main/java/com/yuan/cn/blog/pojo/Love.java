package com.yuan.cn.blog.pojo;

public class Love {
    private Integer id;
    private Integer userId;
    private Integer blogId;

    public Love() {
    }

    public Love(Integer id, Integer userId, Integer blogId) {
        this.id = id;
        this.userId = userId;
        this.blogId = blogId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    @Override
    public String toString() {
        return "Love{" +
                "id=" + id +
                ", userId=" + userId +
                ", blogId=" + blogId +
                '}';
    }
}
