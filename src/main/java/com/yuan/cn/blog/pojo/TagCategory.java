package com.yuan.cn.blog.pojo;

public class TagCategory {
    private Integer id;
    private String tagCategoryName;

    public TagCategory() {
    }

    public TagCategory(Integer id, String tagCategoryName) {
        this.id = id;
        this.tagCategoryName = tagCategoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagCategoryName() {
        return tagCategoryName;
    }

    public void setTagCategoryName(String tagCategoryName) {
        this.tagCategoryName = tagCategoryName;
    }

    @Override
    public String toString() {
        return "TagCategory{" +
                "id=" + id +
                ", tagCategoryName='" + tagCategoryName + '\'' +
                '}';
    }
}
