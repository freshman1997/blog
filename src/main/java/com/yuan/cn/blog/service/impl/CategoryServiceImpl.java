package com.yuan.cn.blog.service.impl;

import com.yuan.cn.blog.dao.CategoryDao;
import com.yuan.cn.blog.pojo.Category;
import com.yuan.cn.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    @Transactional(transactionManager = "transactionManager")
    public int insert(Category category) {
        try {
            return categoryDao.insert(category);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int delete(int id) {
        return categoryDao.delete(id);
    }

    @Override
    public List<Category> fetchAllCategory(int start, int end) {
        return categoryDao.fetchAllCategory(start, end);
    }

    @Override
    public String fetchCategoryNameByCategoryId(int categoryId) {
        return categoryDao.fetchCategoryNameByCategoryId(categoryId);
    }

    @Override
    public int countCategoryNumber() {
        return categoryDao.totalCategoryNumber();
    }

    @Override
    @Transactional(transactionManager = "transactionManager")
    public int update(String newV, String oldV) {
        return categoryDao.update(newV, oldV);
    }

    @Override
    public Category findByCategoryName(String name) {
        return categoryDao.getByName(name);
    }

    @Override
    public int countCategoryNameExists(String categoryName) {
        return categoryDao.countCategory(categoryName);
    }
}
