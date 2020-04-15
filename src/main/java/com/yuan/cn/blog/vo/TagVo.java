package com.yuan.cn.blog.vo;

import com.yuan.cn.blog.pojo.Tag;

import java.util.List;

public class TagVo {
    private List<Tag> tagList;
    private Integer total;

    public TagVo(List<Tag> tagList, Integer total) {
        this.tagList = tagList;
        this.total = total;
    }

    public TagVo() {
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "TagVo{" +
                "tagList=" + tagList +
                ", total=" + total +
                '}';
    }
}
