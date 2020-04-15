package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.pojo.Blog;
import com.yuan.cn.blog.pojo.Tag;
import com.yuan.cn.blog.vo.BlogDetailsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogDao {

    Blog findBlogByMark(String mark);

    List<Blog> findBlogByTitleUsingLike(String title);

    Blog findBlogById(int blogId);

    int countBlogNumber();

    int deleteBlog(int blogId);

    int insertBlog(Blog blog);

    List<Blog> fetchAllBlog(@Param("s") int s, @Param("e") int e);

    List<Blog> getTheMostLove4();

    List<Blog> getTheMostLove40();

    int countUserBlog(int userId);

    int deleteTagBlogMapAndLoveItem(int blogId);

    int hasTheSameTitleMarker(String marker);

    String getBlogPicture(int blogId);

    int setBlogPicture(@Param("bid") int blogId, @Param("picUrl") String picUrl);

    int addTags(@Param("blogId") int blogId, @Param("tagIds") List<Integer> tagIds);

    int updateBlogLoved(int blogId);

    List<Blog> fullSnapshot(Integer categoryId);

    List<Integer>  fetchBlogTagsByTagId(Integer blogId);
}
