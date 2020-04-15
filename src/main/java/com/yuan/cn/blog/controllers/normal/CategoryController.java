package com.yuan.cn.blog.controllers.normal;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.SuccessType;
import com.yuan.cn.blog.pojo.Category;
import com.yuan.cn.blog.service.CategoryService;
import com.yuan.cn.blog.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/fetch-all-category")
    public BlogResponse<CategoryVo> fetchAll(){
        int size = categoryService.countCategoryNumber();
        List<Category> categoryList = categoryService.fetchAllCategory(0, size);

        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, new CategoryVo(categoryList, size));
    }
}
