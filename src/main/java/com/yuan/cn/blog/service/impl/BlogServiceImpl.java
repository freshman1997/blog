package com.yuan.cn.blog.service.impl;

import com.sun.management.OperatingSystemMXBean;
import com.yuan.cn.blog.commons.Const;
import com.yuan.cn.blog.commons.ThreadUtil;
import com.yuan.cn.blog.dao.BlogDao;
import com.yuan.cn.blog.dao.BlogReadDao;
import com.yuan.cn.blog.dao.LoveDao;
import com.yuan.cn.blog.pojo.Blog;
import com.yuan.cn.blog.pojo.Comment;
import com.yuan.cn.blog.pojo.Love;
import com.yuan.cn.blog.pojo.User;
import com.yuan.cn.blog.service.*;
import com.yuan.cn.blog.vo.BlogDetailsVo;
import com.yuan.cn.blog.vo.CommentDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogReadDao blogReadDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LoveDao loveDao;

    @Autowired
    private TagService tagService;

    @Override
    public String findBlogByMark(String mark, Model model, HttpServletRequest request) {
        Blog blog = blogDao.findBlogByMark(mark);
        if (blog == null) {
            return "/blog-404";
        }
        blog.setCategory(categoryService.fetchCategoryNameByCategoryId(blog.getCategoryId()));
        model.addAttribute("blog", blog);
        model.addAttribute("picUrl", blogDao.getBlogPicture(blog.getId()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(blog.getCreateTime());
        User user = (User) userService.getUserById(blog.getUserId()).getData();
        String date = "&nbsp;&nbsp;" + user.getUsername() + " · " + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日 · " + calendar.get(Calendar.YEAR) + "&nbsp;";
        model.addAttribute("info", date);
        List<Comment> comments = commentService.fetchCommentByBlogId(blog.getId());
        System.out.println(comments);
        model.addAttribute("commentNumber", comments.size());
        List<CommentDetailsVo> detailsVoList = new ArrayList<>();
        List<User> users = userService.fetchAllUser(0, userService.countUser());
        comments.parallelStream().forEach(item ->{
            CommentDetailsVo vo = new CommentDetailsVo();
            vo.setCommentContent(item.getContent());
            vo.setId(item.getId());
            comments.parallelStream().forEach(item1->{
                if (item1.getId().equals(item.getReplyId())){
                    for (User user1 : users) {
                        if (user1.getId().equals(item1.getUserId())){
                            vo.setReplyUsername(user1.getUsername());
                            vo.setReplyId(item.getReplyId());
                        }
                    }
                }
            });

            users.parallelStream().forEach(user1 -> {
                if (user1.getId().equals(item.getUserId())){
                    vo.setUserHeadPhotoUrl("/usr/head-photo-using-name?u=" + user1.getUsername());
                    vo.setUsername(user1.getUsername());

                }
            });
            detailsVoList.add(vo);
        });
        boolean hasLovedThisBlog = false;
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        User userByUsername = userService.getUserByUsername(username);
        for (Love love : loveDao.findByBlogId(blog.getId())) {
            if (userByUsername != null) {
                if (love.getUserId().equals(userByUsername.getId())) {
                    hasLovedThisBlog = true;
                }
            }
        }
        List<CommentDetailsVo> commentList = new ArrayList<>();
        List<CommentDetailsVo> commentReplyList = new ArrayList<>();
        for (CommentDetailsVo vo : detailsVoList) {
            if (vo.getReplyId() == null)
                commentList.add(vo);
            else
                commentReplyList.add(vo);
        }
        commentList.sort((o1, o2) -> {
            if (o1.getId() < o2.getId())
                return -1;
            else if (o1.getId() > o2.getId())
                return 1;
            else
                return 0;
        });
        commentReplyList.sort((o1, o2) -> {
            if (o1.getId() < o2.getId())
                return -1;
            else if (o1.getId() > o2.getId())
                return 1;
            else
                return 0;
        });
        List<Integer> tags = blogDao.fetchBlogTagsByTagId(blog.getId());
        model.addAttribute("tag", tagService.findTagById(tags.get(0)).getName());
        model.addAttribute("hasLovedThisBlog", hasLovedThisBlog);
        model.addAttribute("commentList", commentList);
        model.addAttribute("commentReplyList", commentReplyList);
        int updateReadNumber = -1;
        Integer readNumberInBlog = blogReadDao.countReadNumberInBlog(blog.getId());
        if (readNumberInBlog == null || readNumberInBlog == 0){
            blogReadDao.addReadFirst(blog.getId());
        }else
            updateReadNumber = blogReadDao.updateReadNumber(blog.getId());
        if (updateReadNumber != -1)
            readNumberInBlog++;
        model.addAttribute("readNumber", readNumberInBlog);
        return "/blog";
    }

    @Override
    public Blog findBlogByMark(String marker) {
        return blogDao.findBlogByMark(marker);
    }

    @Override
    public List<Blog> searchBlogUsingLike(String title) {
        return blogDao.findBlogByTitleUsingLike(title);
    }

    @Override
    public Blog getBlogById(int id) {
        return blogDao.findBlogById(id);
    }

    @Override
    public int getTotalBlogNumber() {
        return blogDao.countBlogNumber();
    }

    @Override
    public int deleteBlog(int blogId) {
        return blogDao.deleteBlog(blogId);
    }

    @Override
    public int addBlogFully(Blog blog) {
        return blogDao.insertBlog(blog);
    }

    @Override
    public List<Blog> fetchAllBlog(int start, int end) {
        return blogDao.fetchAllBlog(start, end);
    }

    @Override
    public List<Blog> getTheMostLove4() {
        return blogDao.getTheMostLove4();
    }

    @Override
    public List<Blog> getTheMostLove40() {
        return blogDao.getTheMostLove40();
    }

    @Override
    public int countUserBlogNumber(int userId) {
        return blogDao.countBlogNumber();
    }

    @Override
    public int deleteTagBlogMapAndLoveItem(int blogId) {
        return blogDao.deleteTagBlogMapAndLoveItem(blogId);
    }

    @Override
    public void writeBlogImage(String dir, String imageId, HttpServletResponse response) {
        File file = new File(dir + "/" + imageId);
        try {
            FileInputStream fis = new FileInputStream(file);
            if (imageId.substring(imageId.lastIndexOf(".")).equalsIgnoreCase(".png"))
                response.setHeader("Content-Type", "image/png");
            else if (imageId.substring(imageId.lastIndexOf(".")).equalsIgnoreCase(".jpg") || imageId.substring(imageId.lastIndexOf(".")).equalsIgnoreCase(".jpeg"))
                response.setHeader("Content-Type", "image/jpg");
            else
                response.setHeader("Content-Type", "image/gif");
            //attachment; 去掉可在右键另存为 可以有名字
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "fileName=" + imageId);
            OutputStream out = response.getOutputStream();

            int len;
            byte[] buf = new byte[512];
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            fis.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int saveBlogImage(String newName, String dir, InputStream in) {

        try {
            return ThreadUtil.submitTaskCallable(() -> {
                File parent = new File(dir);
                boolean createdFile = false;
                if (!parent.exists())
                    parent.mkdirs();

                File image = new File(dir + "/" + newName);
                if (!image.exists()) {
                    createdFile = image.createNewFile();
                }
                //todo 存之前确保内存足够 以及确保可以读写
                if (parent.canRead() && parent.canWrite()) {

                    File[] files = File.listRoots();

                    boolean isFullSpace = false;
                    String osName = System.getProperty("os.name");
                    if (osName.contains("Windows")) {
                        for (File file : files) {
                            if (file.getAbsolutePath().startsWith(dir.substring(0, dir.indexOf("/")))) {
                                if (file.getFreeSpace() - in.available() > 0)
                                    isFullSpace = true;
                            }
                        }
                    } else if (osName.contains("Linux")) {
                        OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                        if (systemMXBean.getFreePhysicalMemorySize() - in.available() > 0)
                            isFullSpace = true;
                    }

                    if (createdFile && isFullSpace) {
                        FileOutputStream fos = new FileOutputStream(image);
                        int len;
                        byte[] buf = new byte[512];
                        while ((len = in.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.close();
                        in.close();
                        return 0;
                    }
                }
                return -1;
            });
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int countTitleMarker(String marker) {
        return blogDao.hasTheSameTitleMarker(marker);
    }

    @Override
    public String blogPicture(int blogId) {
        return blogDao.getBlogPicture(blogId);
    }

    @Override
    public int addBlogPicture(int blogId, String picUrl) {
        return blogDao.setBlogPicture(blogId, picUrl);
    }

    @Override
    public int addTagsBlogMap(int blogId, List<Integer> tagIds) {
        return blogDao.addTags(blogId, tagIds);
    }

    @Override
    public int updateBlogLoveNumber(int blogId) {
        return blogDao.updateBlogLoved(blogId);
    }

    @Override
    public List<Blog> fullSnapshot(Integer categoryId) {
        return blogDao.fullSnapshot(categoryId);
    }

    @Override
    public List<BlogDetailsVo> snapshot(String categoryName) {
        return null;
    }

    @Override
    public List<Integer> fetchBlogTagsByTagId(Integer blogId) {
        return blogDao.fetchBlogTagsByTagId(blogId);
    }
}
