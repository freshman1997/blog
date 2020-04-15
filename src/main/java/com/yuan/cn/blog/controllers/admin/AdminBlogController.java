package com.yuan.cn.blog.controllers.admin;

import com.yuan.cn.blog.commons.*;
import com.yuan.cn.blog.dao.BlogReadDao;
import com.yuan.cn.blog.pojo.Blog;
import com.yuan.cn.blog.service.BlogService;
import com.yuan.cn.blog.service.CategoryService;
import com.yuan.cn.blog.service.CommentService;
import com.yuan.cn.blog.vo.BlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminBlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogReadDao blogReadDao;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/blog/fetch-all-blog")
    public BlogResponse<BlogVo> fetchAllBlog(int page){
        //todo 获取每个博客的阅读量，然后再装填数据
        List<Blog> blogs = blogService.fetchAllBlog((page - 1) * 8, page * 8);
        for (Blog blog : blogs) {
            blog.setCommentNumber(commentService.fetchCommentByBlogId(blog.getId()).size());
            blog.setReadNumber(blogReadDao.countReadNumberInBlog(blog.getId()));
            blog.setCategory(categoryService.fetchCategoryNameByCategoryId(blog.getCategoryId()));
        }
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, new BlogVo(blogs, blogService.getTotalBlogNumber()));
    }


    @DeleteMapping("/blog/delete/{id}")
    public BlogResponse<Integer> deleteBlogById(@PathVariable("id") int id){
        int ret = blogService.deleteBlog(id);
        if (ret > 0){
            ThreadUtil.submitTask(() -> {
                // 可能没有结果， 因为有点博客是没有点赞和评论的
                blogService.deleteTagBlogMapAndLoveItem(id);
                commentService.deleteCommentByBlogId(id);
            });
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "博客："+id+"删除失败！", null);
    }
}
