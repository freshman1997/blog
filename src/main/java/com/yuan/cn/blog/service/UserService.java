package com.yuan.cn.blog.service;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.pojo.User;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface UserService {

    BlogResponse getUserById(int id);

    User getUserByUsername(String username);

    int insertUserSelective(User user);

    int insertUser(User user);

    int countUser();

    boolean loginByUsername(String username, String password);

    boolean loginByEmail(String email, String password);

    int countUsername(String username);

    int countEmail(String email);

    void fetchHeadPhoto(String url, HttpServletResponse response) throws IOException;

    List<User> fetchAllUser(int start, int end);

    int deleteUserById(int userId);

    String getUserNameByEmail(String email);

    int update(User user);

    int checkIsAdminUser(int userId);
}
