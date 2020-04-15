package com.yuan.cn.blog.vo;

import com.yuan.cn.blog.pojo.Comment;

import java.util.List;

public class CommentVo {
    private List<Comment> commentList;
    private Integer total;

    public CommentVo(List<Comment> commentList, Integer total) {
        this.commentList = commentList;
        this.total = total;
    }

    public CommentVo() {
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CommentVo{" +
                "commentList=" + commentList +
                ", total=" + total +
                '}';
    }
}
