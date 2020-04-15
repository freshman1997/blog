package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagDao {

    int insert(Tag tag);

    int delete(int id);

    List<Tag> findTagByTagCategoryId(int tagCategoryId);

    List<Tag> getAllTag(@Param("start") int start, @Param("end") int end);

    int update(@Param("newValue") String newTagName, @Param("oldValue") String oldTagName, @Param("tagCategoryName") String tagCategoryName);

    int getTagTotalNumber();

    Tag getTagByName(String name);

    Tag findById(int tagId);
}
