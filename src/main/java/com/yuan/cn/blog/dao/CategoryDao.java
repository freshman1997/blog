package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryDao {

    int insert(Category category);

    int delete(int id);

    List<Category> fetchAllCategory(@Param("s") int start, @Param("e") int end);

    String fetchCategoryNameByCategoryId(int id);

    int totalCategoryNumber();

    int update(@Param("newV") String newV, @Param("oldV") String oldV);

    Category getByName(String categoryName);

    int countCategory(String name);
}
