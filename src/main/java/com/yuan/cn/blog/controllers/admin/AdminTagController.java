package com.yuan.cn.blog.controllers.admin;

import com.yuan.cn.blog.commons.*;
import com.yuan.cn.blog.pojo.Tag;
import com.yuan.cn.blog.service.TagCategoryService;
import com.yuan.cn.blog.service.TagService;
import com.yuan.cn.blog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminTagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagCategoryService tagCategoryService;

    @PostMapping(value = "/tag/fetch-all-tag", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BlogResponse<TagVo> fetchAllTag(int page) {

        List<Tag> tagList = tagService.getAllTag((page - 1) * 8, page * 8);
        System.out.println(tagList);
        for (Tag tag : tagList) {
            Integer categoryId = tag.getTagCategoryId();
            String categoryName = tagCategoryService.findById(categoryId).getTagCategoryName();
            tag.setCategory(categoryName);
            JsonSetNullWithAnnotation.excludeFieldByAnnotation(tag);
        }
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, new TagVo(tagList, tagService.totalTag()));
    }

    @PostMapping("/tag/add")
    public BlogResponse<Integer> insertTag(String tagCategoryName, String tagName) {
        if (!tagCategoryName.equals("") && !tagName.equals("")) {
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            tag.setTagCategoryId(tagCategoryService.findByName(tagCategoryName.trim()).getId());
            int ret = tagService.insert(tag);
            if (ret > 0) {
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
            }
        }

        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "增加标签: " + tagName + "失败！", null);
    }

    @DeleteMapping("/tag/delete/{tagId}")
    public BlogResponse<Integer> deleteTag(@PathVariable("tagId") int tagId) {
        int ret = tagService.delete(tagId);
        if (ret > 0) {
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "删除标签: " + tagId + "失败！", null);
    }

    @PutMapping("/tag/update/{new-tag-name}/{old-tag-name}/{tag-category-name}")
    public BlogResponse<Integer> updateTag(@PathVariable("new-tag-name") String newTagName, @PathVariable("old-tag-name") String oldTagName, @PathVariable("tag-category-name") String tagCategoryName) {

        if (newTagName == null || newTagName.equals("") || oldTagName == null || oldTagName.equals("") || tagCategoryName == null || tagCategoryName.equals("") || tagCategoryService.countTagCategoryName(tagCategoryName) == 0){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "更新失败！", null);
        }

        int ret = tagService.update(newTagName, oldTagName, tagCategoryName);

        if (ret > 0) {
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "标签:" + oldTagName + "更新为标签: " + newTagName + "失败！", null);
    }

}
