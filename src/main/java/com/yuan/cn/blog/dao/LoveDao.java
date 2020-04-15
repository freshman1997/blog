package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.pojo.Love;

import java.util.List;

@Mapper
public interface LoveDao {

    List<Love> findByBlogId(int blogId);

    List<Love> findByUserId(int userId);

    int insert(Love love);

    int delete(int id);
}
