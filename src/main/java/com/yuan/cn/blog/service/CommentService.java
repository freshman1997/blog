package com.yuan.cn.blog.service;

import com.yuan.cn.blog.pojo.Comment;

import java.util.List;

public interface CommentService {

    int addComment(int type, Comment comment);

    int deleteComment(int commentId);

    List<Comment> fetchAllComment(int start, int end);

    List<Comment> fetchCommentByBlogId(int blogId);

    int deleteCommentByBlogId(int blogId);

    int totalNumber();

    List<Comment> listReplyComments(int commentId);

    Comment getCommentById(int commentId);

    Comment findLastOne();
}
