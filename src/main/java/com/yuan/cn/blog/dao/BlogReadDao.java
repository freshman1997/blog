package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;

@Mapper
public interface BlogReadDao {

    int addReadFirst(int blogId);

    Integer countReadNumberInBlog(int blogId);

    int updateReadNumber(int blogId);
}
