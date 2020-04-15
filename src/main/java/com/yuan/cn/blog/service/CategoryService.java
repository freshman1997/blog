package com.yuan.cn.blog.service;

import com.yuan.cn.blog.pojo.Category;

import java.util.List;

public interface CategoryService {

    int insert(Category category);

    int delete(int id);

    List<Category> fetchAllCategory(int start, int end);

    String fetchCategoryNameByCategoryId(int categoryId);

    int countCategoryNumber();

    int update(String newV, String oldV);

    Category findByCategoryName(String name);

    int countCategoryNameExists(String categoryName);
}
