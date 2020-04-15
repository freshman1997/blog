package com.yuan.cn.blog.vo;

import com.yuan.cn.blog.pojo.Category;

import java.util.List;

public class CategoryVo {
    private List<Category> categoryList;
    private Integer total;

    public CategoryVo(List<Category> categoryList, Integer total) {
        this.categoryList = categoryList;
        this.total = total;
    }

    public CategoryVo() {
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CategoryVo{" +
                "categoryList=" + categoryList +
                ", total=" + total +
                '}';
    }
}
