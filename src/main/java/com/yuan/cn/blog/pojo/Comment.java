package com.yuan.cn.blog.pojo;

public class Comment {
    private Integer id;
    private String content;
    private Integer userId;
    private Integer blogId;
    private Integer replyId;
    private String username;

    public Comment(Integer id, String content, Integer userId, Integer blogId, Integer replyId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.blogId = blogId;
        this.replyId = replyId;
    }

    public Comment() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", blogId=" + blogId +
                ", replyId=" + replyId +
                '}';
    }
}
