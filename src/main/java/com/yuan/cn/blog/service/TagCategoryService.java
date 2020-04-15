package com.yuan.cn.blog.service;

import com.yuan.cn.blog.pojo.TagCategory;

import java.util.List;

public interface TagCategoryService {

    int insert(TagCategory tagCategory);

    int delete(int id);

    TagCategory findByName(String name);

    List<TagCategory> getAllTagCategory(int start, int end);

    List<TagCategory> getAllTagCategory();

    int countTagCategoryName(String name);

    int update(String newV, String oldV);

    int totalNumber();

    TagCategory findById(int tagCategoryId);
}
