package com.yuan.cn.blog.vo;

import com.yuan.cn.blog.pojo.Tag;

import java.util.Date;
import java.util.List;

public class BlogDetailsVo {

    private String title;
    private Date createTime;
    private String picUrl;
    private List<String> tags;
    private String smallContent;
    private String mask;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSmallContent() {
        return smallContent;
    }

    public void setSmallContent(String smallContent) {
        this.smallContent = smallContent;
    }
}
