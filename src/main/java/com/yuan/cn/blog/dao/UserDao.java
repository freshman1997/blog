package com.yuan.cn.blog.dao;

import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserDao {
    /**
     * 通过主键查找用户
     * @param id 主键id
     * @return user实体类
     */
    User findUserById(int id);

    /**
     * 通过用户名查找用户，因为用户名是唯一的，所以可以查找
     * @param username 用户名
     * @return user实体类
     */
    User findUserByUsername(String username);

    /**
     * 插入用户
     * @param user 用户信息实体类
     * @return 插入数量
     */
    int insertUser(User user);
    /**
     * 插入用户
     * @param user 用户信息实体类
     * @return 插入数量
     */
    int insertUserSelective(User user);

    /**
     * 更新用户信息
     * @param user 用户信息实体类
     * @return 更新数量
     */
    int updateUser(User user);

    /**
     * 通过主键删除用户
     * @param id 主键id
     * @return 删除数量
     */
    int deleteUser(int id);

    /**
     * 通过用户名删除用户，因为用户名是唯一的，所以可以执行删除
     * @param username 用户名（唯一）
     * @return 删除数量
     */
    int deleteUserByUsername(String username);

    /**
     * 获取所有的用户
     * @return 用户信息列表
     */
    List<User> fetchAllUser(@Param("start") int start, @Param("end") int end);

    /**
     * 获取用户数量
     * @return 用户数量
     */
    int countUserNumber();

    int login(@Param("username") String username, @Param("password") String password);

    int loginByEmail(@Param("email") String email, @Param("password") String password);

    int countUsername(String username);

    int countEmail(String email);

    int loginAdminByUsername(@Param("username") String username, @Param("password") String password);

    int loginAdminByEmail(@Param("email") String email, @Param("password") String password);

    List<User> getBanedUser();

    List<User> getNormalUser();

    List<User> getUserByConditionSearch(@Param("username") String username, @Param("email") String email, @Param("createTime") Date createTime, @Param("status") int status);

    String getUserNameByEmail(String email);

    int checkIsAdminUser(int userId);
}
