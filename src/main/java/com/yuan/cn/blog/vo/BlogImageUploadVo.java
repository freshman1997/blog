package com.yuan.cn.blog.vo;

public class BlogImageUploadVo {
    //{
    //  "success": true/false,
    //  "msg": "error message", # optional
    //  "file_path": "[real file path]"
    //}

    private Boolean success;
    private String msg;
    private String file_path;

    public BlogImageUploadVo(Boolean success, String msg, String file_path) {
        this.success = success;
        this.msg = msg;
        this.file_path = file_path;
    }

    public BlogImageUploadVo() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    @Override
    public String toString() {
        return "BlogImageUploadVo{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", file_path='" + file_path + '\'' +
                '}';
    }
}
