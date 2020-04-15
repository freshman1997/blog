package com.yuan.cn.blog.controllers;

import com.yuan.cn.blog.commons.*;
import com.yuan.cn.blog.dao.LoveDao;
import com.yuan.cn.blog.pojo.*;
import com.yuan.cn.blog.service.*;
import com.yuan.cn.blog.service.enums.CommentType;
import com.yuan.cn.blog.utils.MD5Util;
import com.yuan.cn.blog.vo.BlogDetailsVo;
import com.yuan.cn.blog.vo.BlogImageUploadVo;
import com.yuan.cn.blog.vo.CommentDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @RequestMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model){
        List<Blog> theMostLove4 = blogService.getTheMostLove4();
        List<Blog> theMostLove40 = blogService.getTheMostLove40();
        model.addAttribute("theMostLove4", getTheMostLove(theMostLove4, 0));
        model.addAttribute("theMostLove40", getTheMostLove(theMostLove40, 1));
        model.addAttribute("categories", setUserAndCategories(model, request));
        return "/home";
    }

    protected List<List<BlogDetailsVo>> getTheMostLove(List<Blog> blogs, int type){
        List<BlogDetailsVo> vos = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogDetailsVo vo = new BlogDetailsVo();
            vo.setTitle(blog.getTitle());
            vo.setCreateTime(blog.getCreateTime());
            vo.setPicUrl(blogService.blogPicture(blog.getId()));
            if (type == 0) {
                String smaller = blog.getContent().substring(0, 100);
                smaller = smaller.replaceAll("\\\\s*|\\t|\\r|\\n", "");
                smaller = smaller.replaceAll("</?[^>]+>", "");
                vo.setSmallContent(smaller);
            }
            List<Integer> list = blogService.fetchBlogTagsByTagId(blog.getId());
            System.out.println("list = " + list);
            List<String> tags = new ArrayList<>();
            for (Integer integer : list) {
                tags.add(tagService.findTagById(integer).getName());
            }
            vo.setTags(tags);
            vo.setMask(blog.getMark());
            vos.add(vo);
        }
        List<List<BlogDetailsVo>> theMostLove = new ArrayList<>();
        List<BlogDetailsVo> detailsVos = null;
        if (type == 0) {
            for (int i = 0; i < vos.size(); i++) {
                if (i % 2 == 0) {
                    detailsVos = new ArrayList<>();
                    theMostLove.add(detailsVos);
                }
                detailsVos.add(vos.get(i));
            }
        }else {
            for (int i = 0; i < vos.size(); i++) {
                if (i % 4 == 0) {
                    detailsVos = new ArrayList<>();
                    theMostLove.add(detailsVos);
                }
                detailsVos.add(vos.get(i));
            }
        }
        return theMostLove;
    }

    @RequestMapping("/editor")
    public String editor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        if (username == null)
        {
            response.setStatus(302);
            response.sendRedirect("/login.html");
            return null;
        }
        return "/blog-editor";
    }
    private List<Category> setUserAndCategories(Model model, HttpServletRequest request){
        List<Category> categories = categoryService.fetchAllCategory(0, categoryService.countCategoryNumber());
        for (Category category : categories) {
            category.setName("<a href=\"/category/index/"+category.getName()+"\">"+category.getName()+"</a>");
        }
        Category home = new Category();
        home.setName("<a href=\"/home\">首页</a>");
        home.setId(-1);
        categories.add(home);
        Category loginPane = new Category();
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        if (username == null){
            loginPane.setName("<a href=\"/login.html\" title=\"登录账号\">登录</a>");
        }else {
            loginPane.setName("<div class=\"dropdown\" style=\"margin-top: 11px;padding-bottom:10px\">\n" +
                    "                            <span id=\"user\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\"\n" +
                    "                                  style=\"cursor:pointer;width: 50%\">\n" +
                    "                                <span class=\"user\">"+username+"</span>\n" +
                    "                                <img width=\"20px\" height=\"20px\" class=\"img-circle\" alt=\"头像\" src=\"/usr/head-photo\">\n" +
                    "                            </span>\n" +
                    "                        <ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenu1\" style=\"min-width: 120px\">\n" +
                    "                            <li class=\"dropdown-header\"><a href=\"javascript:\"\n" +
                    "                                                           onclick=\"location.href='/editor'\"><span><img\n" +
                    "                                    style=\"width: 18px;height: 18px;color: grey\" src=\"/static/image/写博客.png\"></span>写博客</a>\n" +
                    "                            </li>\n" +
                    "                            <li class=\"dropdown-header\"><a href=\"javascript:\" onclick=\"switchAccount()\">切换账号</a></li>\n" +
                    "                            <li class=\"dropdown-header\"><a href=\"javascript:\" onclick=\"userLogout()\"><span\n" +
                    "                                    class=\"glyphicon glyphicon-log-out\" style=\"color: grey\"></span> &nbsp;退&nbsp;&nbsp;出</a>\n" +
                    "                            </li>\n" +
                    "                        </ul>\n" +
                    "                    </div>");
        }
        loginPane.setId(1000);
        categories.add(loginPane);
        categories.sort((o1, o2) -> {
            if (o1.getId() < o2.getId())
                return 1;
            if (o1.getId() > o2.getId())
                return -1;
            return 0;
        });
        model.addAttribute("categories", categories);
        return categories;
    }

    @GetMapping("/login.html")
    public String forLogin(){
        return "/login";
    }

    @RequestMapping("/register.html")
    public String register(){
        return "/register";
    }


    @GetMapping("/admin/login")
    public String adminLogin(){
        return "/admin-login";
    }

    @GetMapping("/admin/pane")
    public String adminPane(Model model, HttpServletRequest request){
        model.addAttribute("username", Const.USER_CACHING_MAP.get(request.getSession().getId()));
        return "/admin";
    }

    @RequestMapping("/not-found")
    public String notFound(){
        return "404";
    }

    @GetMapping("/category/index/{name}")
    public String blogCategoryIndex(@PathVariable("name") String categoryName, Model model, HttpServletRequest request){
        if (categoryName == null || categoryName.equals("") || categoryService.countCategoryNameExists(categoryName) == 0)
            return "redirect:/not-found";
        model.addAttribute("categories", setUserAndCategories(model, request));
        //todo 根据不同的分类名称，查找不同的博客集合
        Category category = categoryService.findByCategoryName(categoryName);
        List<Blog> blogs = blogService.fullSnapshot(category.getId());
        model.addAttribute("details", getTheMostLove(blogs, 1));
        return "/blog-category";
    }
}
