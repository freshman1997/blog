package com.yuan.cn.blog.service.impl;

import com.yuan.cn.blog.dao.TagCategoryDao;
import com.yuan.cn.blog.pojo.TagCategory;
import com.yuan.cn.blog.service.TagCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagCategoryServiceImpl implements TagCategoryService {

    @Autowired
    private TagCategoryDao tagCategoryDao;

    @Override
    public int insert(TagCategory tagCategory) {
        if (this.countTagCategoryName(tagCategory.getTagCategoryName()) > 0){
            return -1;
        }
        return tagCategoryDao.insert(tagCategory);
    }

    @Override
    public int delete(int id) {
        return tagCategoryDao.delete(id);
    }

    @Override
    public TagCategory findByName(String name) {
        return tagCategoryDao.findByName(name);
    }

    @Override
    public List<TagCategory> getAllTagCategory(int start, int end) {
        return tagCategoryDao.fetchAll(start, end);
    }

    @Override
    public int countTagCategoryName(String name) {
        return tagCategoryDao.detectContainsTagCategoryName(name);
    }

    @Override
    public int update(String newV, String oldV) {
        return tagCategoryDao.update(oldV, newV);
    }

    @Override
    public int totalNumber() {
        return tagCategoryDao.getTotalNumber();
    }

    @Override
    public TagCategory findById(int tagCategoryId) {
        return tagCategoryDao.findById(tagCategoryId);
    }

    @Override
    public List<TagCategory> getAllTagCategory() {
        return tagCategoryDao.fetchAll(0, totalNumber());
    }
}
