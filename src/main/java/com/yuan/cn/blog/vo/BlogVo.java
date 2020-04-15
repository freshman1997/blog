package com.yuan.cn.blog.vo;

import com.yuan.cn.blog.pojo.Blog;

import java.util.List;

public class BlogVo {
    private List<Blog> blogList;
    private Integer total;

    public BlogVo() {
    }

    public BlogVo(List<Blog> blogList, Integer total) {
        this.blogList = blogList;
        this.total = total;
    }

    public List<Blog> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<Blog> blogList) {
        this.blogList = blogList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "BlogVo{" +
                "blogList=" + blogList +
                ", total=" + total +
                '}';
    }
}
