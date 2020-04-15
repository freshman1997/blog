package com.yuan.cn.blog.filter;

import com.yuan.cn.blog.commons.Const;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

@Order(1)
@WebFilter(urlPatterns = "/*", filterName = "cookieFilter")
public class CookieFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (Const.USER_CACHING_MAP.get(request.getSession().getId()) == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user-info")) {
                        Const.USER_CACHING_MAP.insert(request.getSession().getId(), URLDecoder.decode(cookie.getValue(), "UTF-8"), Const.USER_CACHING_TIMEOUT);
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
