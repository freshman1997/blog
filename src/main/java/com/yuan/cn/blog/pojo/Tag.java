package com.yuan.cn.blog.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuan.cn.blog.annotation.JsonIgnoredNotAdminAction;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag {
    private Integer id;
    private String name;
    @JsonIgnoredNotAdminAction
    private Integer tagCategoryId;
    private String category;

    public Tag(Integer id, String name, Integer tagCategoryId) {
        this.id = id;
        this.name = name;
        this.tagCategoryId = tagCategoryId;
    }

    public Tag() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTagCategoryId() {
        return tagCategoryId;
    }

    public void setTagCategoryId(Integer tagCategoryId) {
        this.tagCategoryId = tagCategoryId;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tagCategoryId=" + tagCategoryId +
                '}';
    }
}
