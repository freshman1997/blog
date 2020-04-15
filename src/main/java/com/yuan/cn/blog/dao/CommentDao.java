package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDao {

    int addComment(@Param("content") String content, @Param("userId") int userId, @Param("blogId") int blogId);

    int addReplyComment(@Param("content")String content, @Param("userId")int userId, @Param("blogId")int blogId, @Param("replyId") int replyUserId);

    int deleteComment(int id);

    List<Comment> fetchAllComment(@Param("start") int start, @Param("end") int end);

    List<Comment> fetchCommentByBlogId(int blogId);

    int deleteCommentByBlogId(int blogId);

    int totalCommentNumber();

    List<Comment> getCommentReplyList(int commentId);

    Comment fetchById(int commentId);

    Comment fetchLastOne();
}
