package com.yuan.cn.blog.controllers.admin;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.controllers.normal.LoginAndLogoutController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminLoginAndLogoutController extends LoginAndLogoutController {

    @Override
    @PostMapping("/usr/login")
    public BlogResponse<Boolean> login(String username, @RequestParam("password") String passwd, String email, HttpServletRequest request, HttpServletResponse response) {
        super.setCheckAdmin(true);
        return super.login(username, passwd, email, request, response);
    }
}
