package com.yuan.cn.blog.service.impl;

import com.yuan.cn.blog.dao.TagDao;
import com.yuan.cn.blog.pojo.Tag;
import com.yuan.cn.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public int insert(Tag tag) {
        return tagDao.insert(tag);
    }

    @Override
    public int delete(int id) {
        return tagDao.delete(id);
    }

    @Override
    public List<Tag> findTagByTagCategoryId(int tagCategoryId) {
        return tagDao.findTagByTagCategoryId(tagCategoryId);
    }

    @Override
    public List<Tag> getAllTag(int start, int end) {
        return tagDao.getAllTag(start, end);
    }

    @Override
    public int update(String newTagName, String oldTagName, String tagCategoryName) {
        return tagDao.update(newTagName, oldTagName, tagCategoryName);
    }

    @Override
    public int totalTag() {
        return tagDao.getTagTotalNumber();
    }

    @Override
    public Tag findTagByTagName(String name) {
        return tagDao.getTagByName(name);
    }

    @Override
    public Tag findTagById(int tagId) {
        return tagDao.findById(tagId);
    }
}
