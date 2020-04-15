package com.yuan.cn.blog.vo;

public class CommentDetailsVo {
    private Integer id;
    private Integer replyId;
    private String username;
    private String replyUsername;
    private String commentContent;
    private String userHeadPhotoUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReplyUsername() {
        return replyUsername;
    }

    public void setReplyUsername(String replyUsername) {
        this.replyUsername = replyUsername;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getUserHeadPhotoUrl() {
        return userHeadPhotoUrl;
    }

    public void setUserHeadPhotoUrl(String userHeadPhotoUrl) {
        this.userHeadPhotoUrl = userHeadPhotoUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    @Override
    public String toString() {
        return "CommentDetailsVo{" +
                "username='" + username + '\'' +
                ", replyUsername='" + replyUsername + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", userHeadPhotoUrl='" + userHeadPhotoUrl + '\'' +
                '}';
    }
}
