package com.luchuang.fileImport.service;

import com.luchuang.fileImport.pojo.User;

import java.util.List;

/**
 * @version 1.0
 * @ClassName UserService
 * @Author PPPL
 * @Date 2019/8/3 13:44
 **/
public interface UserService {

    int addUser(User user);

    int delUser(String id);

    List<User> getUserList();

    User selUserByPwd(String pwd);
}
