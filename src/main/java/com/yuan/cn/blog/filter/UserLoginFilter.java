package com.yuan.cn.blog.filter;

import com.yuan.cn.blog.commons.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(2)
@WebFilter(urlPatterns = "/usr/*", filterName = "loginFiler")
public class UserLoginFilter implements Filter {


    private static final Logger logger = LoggerFactory.getLogger(UserLoginFilter.class);

    private String[] pass = new String[]{"/usr/check-unique-username",
            "/usr/check-unique-email", "/usr/login", "/usr/register", "/usr/need-login",
            "/usr/head-photo-using-name", "/usr/send-email-code",
            "/usr/code/generate-user-register-token"};
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("check out if there is an user in the caching map or not.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getServletPath();
        if (isPassUri(uri)){
            System.out.println("放行。。。");
            // 放行
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String username = Const.USER_CACHING_MAP.get(request.getSession().getId());
        if (username == null){
            if (request.getMethod().toUpperCase().equals("GET")){
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendRedirect("/login.html");
                return;
            }
            request.getRequestDispatcher("/usr/need-login").forward(servletRequest, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isPassUri(String uri){
        for (String s : pass) {
            if (uri.equals(s)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
