package com.yuan.cn.blog.controllers.admin;

import com.yuan.cn.blog.commons.*;
import com.yuan.cn.blog.controllers.normal.UserController;
import com.yuan.cn.blog.pojo.User;
import com.yuan.cn.blog.service.BlogService;
import com.yuan.cn.blog.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController extends UserController {

    @Autowired
    private BlogService blogService;

    @PostMapping("/usr/fetch-user/page")
    public BlogResponse<UserVo> getAllUserInformation(int page){
        List<User> users = userService.fetchAllUser((page - 1) * 8,  page * 8);
        int size = userService.countUser();
        for (User user : users) {
            user.setBlogNumber(blogService.countUserBlogNumber(user.getId()));
            JsonSetNullWithAnnotation.excludeFieldByAnnotation(user);
        }
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, new UserVo(users, size));
    }

    @DeleteMapping("/usr/delete/{userId}")
    public BlogResponse<Integer> deleteUser(@PathVariable("userId") int userId){
        int ret = userService.deleteUserById(userId);
        if (ret == 0){
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "删除失败", null);
        }
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
    }


    public BlogResponse<Integer> updateUser(User user){
        if (user != null){
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, userService.update(user));
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "非法参数！", null);
    }

    @RequestMapping("/usr/need-login")
    public BlogResponse<String> needLogin(){
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription(), "/admin/login");
    }
}
