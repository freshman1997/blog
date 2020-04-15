package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.pojo.TagCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagCategoryDao {

    int insert(TagCategory tagCategory);

    int delete(int id);

    TagCategory findByName(String name);

    List<TagCategory> fetchAll(@Param("start") int start, @Param("end") int end);

    int detectContainsTagCategoryName(String name);

    int update(@Param("oldValue") String oldValue, @Param("newValue") String newValue);

    int getTotalNumber();

    TagCategory findById(int id);
}
