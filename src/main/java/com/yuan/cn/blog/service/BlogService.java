package com.yuan.cn.blog.service;

import com.yuan.cn.blog.pojo.Blog;
import com.yuan.cn.blog.vo.BlogDetailsVo;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface BlogService {

    String findBlogByMark(String mark, Model model, HttpServletRequest request);

    Blog findBlogByMark(String marker);

    List<Blog> searchBlogUsingLike(String title);

    Blog getBlogById(int id);

    int getTotalBlogNumber();

    int deleteBlog(int blogId);

    int addBlogFully(Blog blog);

    List<Blog> fetchAllBlog(int start, int end);

    List<Blog> getTheMostLove4();

    List<Blog> getTheMostLove40();

    int countUserBlogNumber(int userId);

    int deleteTagBlogMapAndLoveItem(int blogId);

    void writeBlogImage(String dir, String imageId, HttpServletResponse response);

    int saveBlogImage(String newName, String dir, InputStream in);

    int countTitleMarker(String marker);

    String blogPicture(int blogId);

    int addBlogPicture(int blogId, String picUrl);

    int addTagsBlogMap(int blogId, List<Integer> tagIds);

    int updateBlogLoveNumber(int blogId);

    List<Blog> fullSnapshot(Integer categoryId);

    List<Integer>  fetchBlogTagsByTagId(Integer blogId);

    List<BlogDetailsVo> snapshot(String categoryName);
}
