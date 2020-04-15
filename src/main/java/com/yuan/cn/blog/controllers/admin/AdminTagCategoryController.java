package com.yuan.cn.blog.controllers.admin;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.ErrorType;
import com.yuan.cn.blog.commons.ResponseCode;
import com.yuan.cn.blog.commons.SuccessType;
import com.yuan.cn.blog.pojo.Tag;
import com.yuan.cn.blog.pojo.TagCategory;
import com.yuan.cn.blog.service.TagCategoryService;
import com.yuan.cn.blog.service.TagService;
import com.yuan.cn.blog.vo.TagCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tag-category")
public class AdminTagCategoryController {

    @Autowired
    private TagCategoryService tagCategoryService;

    @Autowired
    private TagService tagService;

    @PostMapping("/fetch-all-tag-category")
    public BlogResponse<TagCategoryVo> fetchAllTagCategory(int page){
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA,
                null, new TagCategoryVo(tagCategoryService.getAllTagCategory((page - 1) * 8, page * 8),
                        tagCategoryService.totalNumber()));
    }

    @PostMapping("/fetch-all-tag-category/all")
    public BlogResponse<List<TagCategory>> fetchAllNonPage(){
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, tagCategoryService.getAllTagCategory());
    }
    @DeleteMapping("/delete")
    public BlogResponse<Integer> deleteTagCategory(@RequestParam("tag-category-name") String tagCategoryName){
        if (!tagCategoryName.equals("")){

            if (tagCategoryService.countTagCategoryName(tagCategoryName) > 0) {
                Integer id = tagCategoryService.findByName(tagCategoryName).getId();
                List<Tag> tagListForDelete = tagService.findTagByTagCategoryId(id);
                int size = tagListForDelete.size();
                int sum = 0;
                // 删除对应的标签
                for (Tag tag : tagListForDelete) {
                    sum += tagService.delete(tag.getId());
                }

                int ret = tagCategoryService.delete(id);
                if (ret > 0 && sum == size) {
                    return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
                }
            }
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "名称为null或空字符串！", null);
    }

    @PostMapping("/add")
    public BlogResponse<Integer> insert(@RequestParam("tagCategoryName") String tagCategoryName){
        if (!tagCategoryName.equals("")){
            int ret = tagCategoryService.insert(new TagCategory(null, tagCategoryName));
            if (ret > 0) {
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
            }
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "添加"+tagCategoryName+"失败！", null);
    }

    @PutMapping("/update/{newValue}===>{oldValue}")
    public BlogResponse<Integer> update(@PathVariable("newValue") String newValue, @PathVariable("oldValue") String oldValue){
        if (!newValue.equals("") && !oldValue.equals("")){
            if (tagCategoryService.countTagCategoryName(oldValue) > 0){
                int ret = tagCategoryService.update(newValue, oldValue);
                if (ret > 0) {
                    return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
                }
            }
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), oldValue + "更新为"+newValue+"失败！", null);
    }
}
