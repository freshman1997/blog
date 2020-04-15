package com.yuan.cn.blog.service.impl;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.ErrorType;
import com.yuan.cn.blog.commons.ResponseCode;
import com.yuan.cn.blog.commons.SuccessType;
import com.yuan.cn.blog.dao.UserDao;
import com.yuan.cn.blog.pojo.User;
import com.yuan.cn.blog.service.UserService;
import com.yuan.cn.blog.utils.MD5Util;
import com.yuan.cn.blog.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public BlogResponse<User> getUserById(int id) {
        User userById = userDao.findUserById(id);
        boolean flag = userById != null;
        return flag ? BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_ALL, ResponseCode.SUCCESS.getDescription(), userById)
                : BlogResponse.ERROR(ErrorType.ERROR_WITH_MESSAGE, ResponseCode.ERROR.getCode(), "用户不存在", null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public int insertUser(User user) {
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        return userDao.insertUser(user);
    }

    @Override
    public int insertUserSelective(User user) {
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        System.out.println(user);
        return userDao.insertUserSelective(user);
    }

    @Override
    public int countUser() {
        return userDao.countUserNumber();
    }

    @Override
    public boolean loginByUsername(String username, String password) {
        String encode = MD5Util.MD5EncodeUtf8(password);
        return  userDao.login(username, encode) == 1;
    }

    @Override
    public boolean loginByEmail(String email, String password) {
        String encode = MD5Util.MD5EncodeUtf8(password);
        return  userDao.loginByEmail(email, encode) == 1;
    }

    @Override
    public int countUsername(String username) {
        return userDao.countUsername(username);
    }

    @Override
    public int countEmail(String email) {
        return userDao.countEmail(email);
    }

    @Override
    public void fetchHeadPhoto(String url, HttpServletResponse response) throws IOException {
        FileInputStream fis = new FileInputStream(PropertiesUtil.getProperty("head-photo-for-save") + "/" + url);
        if (url.substring(url.lastIndexOf(".")).equals(".jpg") || url.substring(url.lastIndexOf(".")).equals(".jpeg")){
            response.setHeader("Content-Type", "image/jpg");
        }else
            response.setHeader("Content-Type", "image/png");
        response.setCharacterEncoding("UTF-8");
        //attachment; 去掉可在右键另存为 可以有名字
        response.setHeader("Content-Disposition", "fileName=" + url);
        int len;
        byte[] buf = new byte[512];
        while ( (len = fis.read(buf)) != -1 ){
            response.getOutputStream().write(buf, 0, len);
        }
        fis.close();
    }

    @Override
    public List<User> fetchAllUser(int start, int end) {
        return userDao.fetchAllUser(start, end);
    }

    @Override
    public int deleteUserById(int userId) {
        return userDao.deleteUser(userId);
    }

    @Override
    public String getUserNameByEmail(String email) {
        return userDao.getUserNameByEmail(email);
    }

    @Override
    public int update(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public int checkIsAdminUser(int userId) {
        return userDao.checkIsAdminUser(userId);
    }
}
