package com.yuan.cn.blog.controllers.normal;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.SuccessType;
import com.yuan.cn.blog.pojo.Tag;
import com.yuan.cn.blog.pojo.TagCategory;
import com.yuan.cn.blog.service.TagCategoryService;
import com.yuan.cn.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/t-tc")
public class FixedTagAndTagCategoryController {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagCategoryService tagCategoryService;


    @PostMapping("/all")
    public BlogResponse<Map<String, List<Tag>>> getFixedTCDataFully(){
        Map<String, List<Tag>> data = new HashMap<>();
        List<TagCategory> allTagCategory = tagCategoryService.getAllTagCategory();
        List<Tag> allTag = tagService.getAllTag(0, tagService.totalTag());
        for (TagCategory tagCategory : allTagCategory) {
            List<Tag> tagList = new ArrayList<>();
            for (Tag tag : allTag) {
                if (tag.getTagCategoryId().equals(tagCategory.getId())){
                    tagList.add(tag);
                }
            }
            data.put(tagCategory.getTagCategoryName(), tagList);
        }
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, data);
    }
}
