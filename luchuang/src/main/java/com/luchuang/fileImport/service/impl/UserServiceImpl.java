package com.luchuang.fileImport.service.impl;

import com.alibaba.fastjson.JSON;
import com.luchuang.fileImport.mapper.UserMapper;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.util.FileWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @version 1.0
 * @ClassName UserServiceImpl
 * @Author PPPL
 * @Date 2019/8/3 13:45
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUserList(){
        List<User> users = userMapper.selectAll();
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                try {
                    return Integer.parseInt(o2.getCreate_time().getTime()-o1.getCreate_time().getTime()+"");
                }catch (Exception e){
                    return -1;
                }
            }
        });
        return users;

    }

    @Override
    public int addUser(User user) {
        /**
         *  返回值的问题 -1root密码不正确 0密码已存在或姓名已存在 1成功
         * */
        Set<User> users = new HashSet<>(getUserList());
        Set<User> temp_users = new HashSet<>();
        temp_users.add(user);
        String name = user.getName();
        String pwd = user.getPwd();
        for(User user1 : users){
            if(name.equals(user1.getName())||pwd.equals(user1.getPwd()))return 0;
            temp_users.add(user1);
        }
        FileWord.writeJson("user",JSON.toJSONString(temp_users));
        return 1;
    }

    @Override
    public int delUser(String id) {

        return userMapper.deleteByPrimaryKey(id);

    }

    @Override
    public User selUserByPwd(String pwd) {

        if ("".equals("pwd") || pwd == null) {
            /**
             *  没有找到密码对应的名字和权限
             * */
            return null;
        }

        Example userExample = new Example(User.class);
        userExample.createCriteria().andEqualTo("pwd",pwd);
        List<User> users = userMapper.selectByExample(userExample);

        if(users.size()!=0){
            return users.get(0);
        }
        return null;
    }
}
