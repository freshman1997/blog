package com.yuan.cn.blog.service.impl;

import com.yuan.cn.blog.dao.CommentDao;
import com.yuan.cn.blog.pojo.Comment;
import com.yuan.cn.blog.service.CommentService;
import com.yuan.cn.blog.service.enums.CommentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public int addComment(int type, Comment comment) {
        if (type == CommentType.JUST_ADD.getType()){
            return commentDao.addComment(comment.getContent(), comment.getUserId(), comment.getBlogId());
        }
        return commentDao.addReplyComment(comment.getContent(), comment.getUserId(), comment.getBlogId(), comment.getReplyId());
    }

    @Override
    public int deleteComment(int commentId) {
        return commentDao.deleteComment(commentId);
    }

    @Override
    public List<Comment> fetchAllComment(int start, int end) {
        return commentDao.fetchAllComment(start, end);
    }

    @Override
    public List<Comment> fetchCommentByBlogId(int blogId) {
        return commentDao.fetchCommentByBlogId(blogId);
    }

    @Override
    public int deleteCommentByBlogId(int blogId) {
        return 0;
    }

    @Override
    public int totalNumber() {
        return commentDao.totalCommentNumber();
    }

    @Override
    public List<Comment> listReplyComments(int commentId) {
        return commentDao.getCommentReplyList(commentId);
    }

    @Override
    public Comment getCommentById(int commentId) {
        return commentDao.fetchById(commentId);
    }

    @Override
    public Comment findLastOne() {
        return commentDao.fetchLastOne();
    }
}
