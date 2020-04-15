package com.yuan.cn.blog.controllers.normal;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.SuccessType;
import com.yuan.cn.blog.pojo.TagCategory;
import com.yuan.cn.blog.service.TagCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/category")
public class TagCategoryController {

    @Autowired
    private TagCategoryService tagCategoryService;

    @PostMapping("/all-tag-category")
    @ResponseBody
    public BlogResponse<List<TagCategory>> getAllTagCategory(){
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, tagCategoryService.getAllTagCategory());
    }
}
