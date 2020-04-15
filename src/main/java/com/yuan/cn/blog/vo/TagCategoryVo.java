package com.yuan.cn.blog.vo;

import com.yuan.cn.blog.pojo.TagCategory;

import java.util.List;

public class TagCategoryVo {
    private List<TagCategory> tagCategoryList;
    private Integer total;

    public TagCategoryVo(List<TagCategory> tagCategoryList, Integer total) {
        this.tagCategoryList = tagCategoryList;
        this.total = total;
    }

    public TagCategoryVo() {
    }

    public List<TagCategory> getTagCategoryList() {
        return tagCategoryList;
    }

    public void setTagCategoryList(List<TagCategory> tagCategoryList) {
        this.tagCategoryList = tagCategoryList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "TagCategoryVo{" +
                "tagCategoryList=" + tagCategoryList +
                ", total=" + total +
                '}';
    }
}
