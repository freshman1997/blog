package com.yuan.cn.blog.controllers.admin;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.ErrorType;
import com.yuan.cn.blog.commons.ResponseCode;
import com.yuan.cn.blog.commons.SuccessType;
import com.yuan.cn.blog.pojo.Category;
import com.yuan.cn.blog.service.CategoryService;
import com.yuan.cn.blog.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/fetch-all-category")
    public BlogResponse<CategoryVo> fetchAll(int page){
        List<Category> categoryList = categoryService.fetchAllCategory((page - 1) * 8, page * 8);
        int size = categoryService.countCategoryNumber();
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, new CategoryVo(categoryList, size));
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BlogResponse<Integer> insert(@RequestBody Category category){
        int ret = categoryService.insert(category);
        if (ret > 0)
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(),"插入分类失败！", null);
    }

    @DeleteMapping("/delete/{categoryId}")
    public BlogResponse<Integer> delete(@PathVariable int categoryId){
        int ret = categoryService.delete(categoryId);
        if (ret > 0)
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(),"删除分类失败！", null);
    }

    @PutMapping(value = "/update/{newCategoryName}/{oldCategoryName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BlogResponse<Integer> updateCategory(@PathVariable String newCategoryName, @PathVariable String oldCategoryName){
        if (newCategoryName == null || newCategoryName.equals("") || oldCategoryName == null || oldCategoryName.equals("")){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, -1, "参数非法！", -1);
        }
        int ret = categoryService.update(newCategoryName, oldCategoryName);
        if (ret > 0)
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, -1, "更新失败！", -1);
    }
}
