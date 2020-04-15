package com.yuan.cn.blog.service;

import com.yuan.cn.blog.pojo.Tag;

import java.util.List;

public interface TagService {

    int insert(Tag tag);

    int delete(int id);

    List<Tag> findTagByTagCategoryId(int tagCategoryId);

    List<Tag> getAllTag(int start, int end);

    int update(String newTagName, String oldTagName, String tagCategoryName);

    int totalTag();

    Tag findTagByTagName(String name);

    Tag findTagById(int tagId);
}
