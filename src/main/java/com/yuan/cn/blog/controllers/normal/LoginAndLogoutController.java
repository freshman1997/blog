package com.yuan.cn.blog.controllers.normal;

import com.yuan.cn.blog.commons.*;
import com.yuan.cn.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/usr")
public class LoginAndLogoutController {

    @Autowired
    UserService userService;

    private boolean checkAdmin = false;

    protected void setCheckAdmin(boolean checkAdmin) {
        this.checkAdmin = checkAdmin;
    }

    /**
     * 通过用户名或邮箱登录
     *
     * @param username 用户名
     * @param passwd   密码
     * @param email    邮箱
     * @return 返回登录状态json
     */
    @PostMapping("/login")
    public BlogResponse<Boolean> login(@RequestParam(name = "username") String username,
                                       @RequestParam("password") String passwd,
                                       @RequestParam(name = "email") String email, HttpServletRequest request, HttpServletResponse response) {


        if (passwd == null) {
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误！", false);
        }

        if (username == null && email == null) {
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误！", false);
        } else {
            if (username != null && username.equals("")) {
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误！", false);
            }
            if (email != null && email.equals("")) {
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "参数错误！", false);
            }
        }

        // todo 重复登录将在线一方挤掉线
        String signedInUsername = Const.USER_CACHING_MAP.get(request.getSession().getId());
        if (signedInUsername != null) {
            if (signedInUsername.equals(userService.getUserNameByEmail(email)) || signedInUsername.equals(username)) {
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "您已登录，无需重复登录！", null);
            } else {
                if (Const.USER_CACHING_MAP.values().contains(signedInUsername)) {
                    // todo 删除在线的，重新加入集合
                    Const.USER_CACHING_MAP.remove(Const.USER_CACHING_MAP.values().indexOf(signedInUsername));
                }
            }
        }

        // 确认是否存在用户或邮箱
        int usernameCount = userService.countUsername(username);
        int emailCount = userService.countEmail(email);

        if (usernameCount != 0) {
            boolean loginState = userService.loginByUsername(username, passwd);
            int isAdmin = userService.checkIsAdminUser(userService.getUserByUsername(username).getId());
            if (checkAdmin) {
                if (isAdmin == 0) {
                    loginState = false;
                }
            }
            checkAdmin = false;

            if (loginState) {
                Const.USER_CACHING_MAP.insert(request.getSession().getId(), username, Const.USER_CACHING_TIMEOUT);
                setCookie(response, username);
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_ALL, "登录成功！", true);
            } else {
                if (isAdmin == 0) {
                    return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, -1, "非管理员拒绝登录！", false);
                }
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, -1, "用户名或密码错误", false);
            }
        }

        if (emailCount != 0) {
            boolean loginState = userService.loginByEmail(email, passwd);
            int ret = userService.checkIsAdminUser(userService.getUserByUsername(userService.getUserNameByEmail(email)).getId());
            if (checkAdmin) {
                if (ret == 0) {
                    loginState = false;
                }
            }
            checkAdmin = false;

            if (loginState) {
                String fetchedUsername = userService.getUserNameByEmail(email);
                Const.USER_CACHING_MAP.insert(request.getSession().getId(), fetchedUsername, Const.USER_CACHING_TIMEOUT);
                setCookie(response, fetchedUsername);
                return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_ALL, "登录成功！", true);
            } else {
                if (ret == 0) {
                    return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, -1, "非管理员拒绝登录！", false);
                }
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_DATA_MESSAGE, -1, "邮箱或密码错误", false);
            }
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, -1, "用户不存在!", false);
    }
    //jackyuan199706@gmail.com

    private void setCookie(HttpServletResponse response, String username) {
        Cookie cookie = null;   //(key,value)
        try {
            cookie = new Cookie("user-info", URLEncoder.encode(username, "UTF-8"));
            cookie.setPath("/");// 这个要设置
            cookie.setHttpOnly(true);
            // cookie.setDomain(".aotori.com");//这样设置，能实现两个网站共用
            cookie.setMaxAge(7 * 24 * 60 * 60);// 不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位
            response.addCookie(cookie); //添加第二个Cookie
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void removeCookie(HttpServletRequest request, HttpServletResponse response){
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("user-info")){
                Cookie  userCookie = new Cookie(cookie.getName(), null);
                userCookie.setMaxAge(0);
                userCookie.setPath("/");
                response.addCookie(userCookie);
                break;
            }
        }
    }

    @DeleteMapping(value = "/logout/{username}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BlogResponse<Boolean> logout(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        String value = request.getHeader("Do-Logout-Action");
        if (value == null || value.equals("")) {
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, 1, "非法请求！", false);
        } else if (value.equals("true")) {
            if (username == null || username.equals("")) {
                return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "请传入正确的用户名！", false);
            }
            String signedInUsername = Const.USER_CACHING_MAP.get(request.getSession().getId());
            if (signedInUsername != null) {
                if (signedInUsername.equals(username)) {
                    Const.USER_CACHING_MAP.remove(request.getSession().getId());
                    removeCookie(request, response);
                    return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, true);
                }
            }
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "用户不存在，无法执行下线操作！", false);
        } else if (value.equals("false")) {
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, false);
        } else {
            return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, 1, "参数错误！", false);
        }
    }
}
