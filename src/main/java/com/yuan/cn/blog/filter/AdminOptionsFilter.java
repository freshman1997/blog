package com.yuan.cn.blog.filter;

import com.yuan.cn.blog.commons.Const;
import com.yuan.cn.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(2)
@WebFilter(urlPatterns = "/admin/*", filterName = "adminOptionsFiler")
public class AdminOptionsFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AdminOptionsFilter.class);

    private String[] ops = {"/admin/login", "/admin/usr/login", "/admin/usr/need-login"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("check out if there is an user in the caching map or not.");
    }

    private boolean checkAdminLogin(String uri){
        for (String op : ops) {
            if (op.equals(uri)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getServletPath();
        if (checkAdminLogin(uri)){
            // 放行
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        ServletContext context = request.getSession().getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
        assert webApplicationContext != null;
        UserService userService = webApplicationContext.getBean(UserService.class);

        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        if (username == null){
            if (request.getMethod().toUpperCase().equals("GET")){
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendRedirect("/admin/login");
                return;
            }
            request.getRequestDispatcher("/admin/usr/need-login").forward(servletRequest, servletResponse);
        }else {
            int isAdmin = userService.checkIsAdminUser(userService.getUserByUsername(username).getId());

            if (isAdmin == 0){
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendRedirect("/admin/login");
                return;
            }
            // 是管理员，直接放行
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
