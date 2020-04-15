package com.yuan.cn.blog.controllers.normal;

import com.yuan.cn.blog.commons.*;
import com.yuan.cn.blog.controllers.IndexController;
import com.yuan.cn.blog.dao.LoveDao;
import com.yuan.cn.blog.pojo.*;
import com.yuan.cn.blog.service.*;
import com.yuan.cn.blog.service.enums.CommentType;
import com.yuan.cn.blog.utils.MD5Util;
import com.yuan.cn.blog.vo.BlogDetailsVo;
import com.yuan.cn.blog.vo.BlogImageUploadVo;
import com.yuan.cn.blog.vo.CommentDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/blog")
public class BlogController extends IndexController{

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private LoveDao loveDao;

    @Autowired
    private CommentService commentService;

    @GetMapping("/article/{marker}")
    public String getBlog(@PathVariable("marker") String marker, HttpServletRequest request, Model model, HttpServletResponse response) {
        if (blogService.countTitleMarker(marker) < 1){
            return "/blog-404";
        }
        return blogService.findBlogByMark(marker, model, request);
    }

    @PostMapping("/accept")
    @ResponseBody // 这个接口一定是要登录了才可以使用
    public BlogResponse<String> accept(@RequestParam("title")String title,
                                       MultipartFile introducePhoto, @RequestParam("content") String content,
                                       @RequestParam("tags") List<String> tags, @RequestParam("articleType") String articleType,
                                       @RequestParam("token") String token, HttpServletRequest request){

        if (token == null || token.equals("") || !token.equals(request.getSession().getAttribute("blog-token")) ||
                title == null || title.equals("") || content == null || content.equals("") || tags == null ||
                tags.size() == 0 || articleType == null || articleType.equals("") )
        {
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误！", null);
        }
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        if (username == null)
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription(), null);

        Blog blog = new Blog();
        blog.setCategory(articleType);
        blog.setContent(content);
        blog.setCreateTime(new Date());
        Category category = categoryService.findByCategoryName(articleType);
        System.out.println(category);
        blog.setCategoryId(category.getId());
        blog.setTitle(title);
        blog.setUserId(userService.getUserByUsername(username).getId());
        blog.setMark(MD5Util.MD5EncodeUtf8(title));
        if (blogService.countTitleMarker(blog.getMark()) > 0){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "已有相同标题博客。", null);
        }
        int ret = blogService.addBlogFully(blog);
        if (ret > 0){
            blog = blogService.findBlogByMark(blog.getMark());
            List<Integer> tagIds = new ArrayList<>();
            System.out.println(tags);
            for (String tag : tags) {
                tagIds.add(tagService.findTagByTagName(tag).getId());
            }
            //todo 存博客展示的图片
            BlogImageUploadVo uploadVo = acceptUploadImage(introducePhoto);
            int picture = blogService.addBlogPicture(blog.getId(), uploadVo.getFile_path());
            int blogMap = blogService.addTagsBlogMap(blog.getId(), tagIds);
            if (uploadVo.getSuccess() && picture > 0 && blogMap > 0){
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, "/blog/article/" + blog.getMark());
            }
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "发布博客失败！", null);
    }

    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public String searchBlogTitle(@RequestParam("kw") String keyword, Model model){
        if (!keyword.equals(""))
            model.addAttribute("details", getTheMostLove(blogService.searchBlogUsingLike(keyword), 1));
        else
            model.addAttribute("details", new ArrayList<List<BlogDetailsVo>>());
        return "/search";
    }

    @RequestMapping(value = "/query", method = {RequestMethod.POST})
    @ResponseBody
    public BlogResponse<List<List<BlogDetailsVo>>> searchBlogTitle(@RequestParam("kw") String keyword){
        if (keyword.equals(""))
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法请求！", null);
        List<List<BlogDetailsVo>> rets = getTheMostLove(blogService.searchBlogUsingLike(keyword), 1);

        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, rets);
    }

    @GetMapping("/images/{imageId:.+}")
    public void getBlogImage(@PathVariable String imageId, HttpServletResponse response) {
        if (imageId == null || imageId.equals(""))
            return;
        blogService.writeBlogImage(Const.BLOG_IMAGES_SAVING_PATH, imageId, response);
    }

    @PostMapping("/images")
    @ResponseBody
    public BlogImageUploadVo acceptUploadImage(@RequestPart("image") MultipartFile image){
        BlogImageUploadVo blogImageUploadVo = new BlogImageUploadVo();
        if (image == null){
            blogImageUploadVo.setSuccess(false);
            blogImageUploadVo.setMsg("上传失败，参数错误！");
            blogImageUploadVo.setFile_path("***");
            return blogImageUploadVo;
        }

        String originalFilename = image.getOriginalFilename();
        if (originalFilename == null){
            blogImageUploadVo.setSuccess(false);
            blogImageUploadVo.setMsg("上传失败，所上传的文件错误！");
            blogImageUploadVo.setFile_path("***");
            return blogImageUploadVo;
        }
        originalFilename = originalFilename.toLowerCase();
        if (originalFilename.endsWith(".png") || originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg") || originalFilename.endsWith(".gif")){

            String newFilename = MD5Util.MD5EncodeUtf8(originalFilename + System.currentTimeMillis()) + originalFilename.substring(originalFilename.lastIndexOf("."));
            try {
                int ret = blogService.saveBlogImage(newFilename, Const.BLOG_IMAGES_SAVING_PATH, image.getInputStream());
                if (ret == 0){
                    blogImageUploadVo.setMsg("上传图片成功！");
                    blogImageUploadVo.setSuccess(true);
                    blogImageUploadVo.setFile_path("/blog/images/" + newFilename);
                    return blogImageUploadVo;
                }else {
                    blogImageUploadVo.setFile_path("***");
                    blogImageUploadVo.setSuccess(false);
                    blogImageUploadVo.setMsg("文件系统出错！");
                    return blogImageUploadVo;
                }
            } catch (IOException e) {
                e.printStackTrace();
                blogImageUploadVo.setFile_path("***");
                blogImageUploadVo.setSuccess(false);
                blogImageUploadVo.setMsg("文件系统出错！");
                return blogImageUploadVo;
            }
        }

        blogImageUploadVo.setFile_path("***");
        blogImageUploadVo.setSuccess(false);
        blogImageUploadVo.setMsg("文件格式非法！");
        return blogImageUploadVo;
    }

    @PostMapping("/generate-blog-token")
    @ResponseBody
    public BlogResponse<String> generateToken(HttpServletRequest request){
        String token = UUID.randomUUID().toString();
        String header = request.getHeader("Generate-Token");
        if (header != null && !header.equals("")){
            if (header.equals("true")){
                request.getSession().setAttribute("blog-token", token);
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, token);
            }
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法请求！", null);
    }

    @PostMapping("/love")
    @ResponseBody
    @Transactional
    public BlogResponse<Integer> addLove(@RequestParam("bm") String blogMask, HttpServletRequest request){
        if (blogMask == null || blogMask.equals("")){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法请求!", null);
        }
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        if (username == null)
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription(), null);

        if (blogService.countTitleMarker(blogMask) == 0){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误!", null);
        }

        Love love = new Love();
        int userId = userService.getUserByUsername(username).getId();

        boolean hasLovedThisBlog = false;
        Blog blog = blogService.findBlogByMark(blogMask);
        for (Love l : loveDao.findByBlogId(blog.getId())) {
            if (l.getUserId().equals(userId))
                hasLovedThisBlog = true;
        }
        if (hasLovedThisBlog){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "您已经点过赞了！", null);
        }
        love.setBlogId(blogService.findBlogByMark(blogMask).getId());
        love.setUserId(userId);
        int blogLoved = blogService.updateBlogLoveNumber(blog.getId());
        int ret = loveDao.insert(love);
        if (ret > 0 && blogLoved > 0)
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "后台错误！请通知管理员维护，qq:1905269298", null);
    }

    @PostMapping("/comment")
    @ResponseBody // 返回的对象要包含1、头像路径 2、用户名 3、评论内容
    public BlogResponse<CommentDetailsVo> addComment(@RequestParam("c") String content, @RequestParam("bm") String blogMask,@RequestParam(value = "id", required = false) Integer commentId,
                                                     @RequestParam(value = "run", required = false) String replyUsername, HttpServletRequest request){
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        //todo 添加评论
        if (replyUsername == null){
            if (content == null || content.equals("") || blogMask == null || blogMask.equals("")){
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法请求！", null);
            }
            BlogResponse<CommentDetailsVo> commentCheck = commentCheck(blogMask);
            if (commentCheck != null)
                return commentCheck;


            if (username == null)
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription(), null);
            Comment comment = new Comment();
            User user = userService.getUserByUsername(username);
            comment.setUserId(user.getId());
            comment.setBlogId(blogService.findBlogByMark(blogMask).getId());
            comment.setContent(content);
            int ret = commentService.addComment(CommentType.JUST_ADD.getType(), comment);
            if (ret > 0){
                CommentDetailsVo vo = new CommentDetailsVo();
                vo.setId(commentService.findLastOne().getId());
                vo.setUsername(username);
                vo.setUserHeadPhotoUrl("/usr/head-photo-using-name?u=" + username);
                vo.setCommentContent(content);
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, vo);
            }
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "后台错误！请通知管理员维护，qq:1905269298", null);
        }

        //todo 添加回复
        if (content == null || content.equals("") || blogMask == null || blogMask.equals("") || replyUsername.equals("") || commentId == null || commentId <= 0){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法请求！", null);
        }

        BlogResponse<CommentDetailsVo> commentCheck = commentCheck( blogMask);
        if (commentCheck != null)
            return commentCheck;

        if (userService.countUsername(replyUsername) == 0)
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "无法回复，所回复用户评论不存在！", null);

        if (username == null)
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription(), null);
        Comment comment = new Comment();
        User user = userService.getUserByUsername(username);
        comment.setUserId(user.getId());
        Blog blog = blogService.findBlogByMark(blogMask);
        comment.setBlogId(blog.getId());
        comment.setContent(content);
        comment.setReplyId(commentId);
        int ret = commentService.addComment(CommentType.REPLY.getType(), comment);
        if (ret > 0)
        {
            CommentDetailsVo vo = new CommentDetailsVo();
            vo.setId(commentService.findLastOne().getId());
            vo.setUsername(username);
            vo.setReplyUsername(replyUsername);
            vo.setUserHeadPhotoUrl("/usr/head-photo-using-name?u=" + username);
            vo.setCommentContent(content);
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, vo);
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "后台错误！请通知管理员维护，qq:1905269298", null);
    }

    private BlogResponse<CommentDetailsVo> commentCheck( String blogMask){

        if (blogService.countTitleMarker(blogMask) == 0){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误!", null);
        }
        return null;
    }
}
