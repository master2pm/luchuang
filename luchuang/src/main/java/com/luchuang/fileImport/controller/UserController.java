package com.luchuang.fileImport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luchuang.common.ResultBean;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.util.FileWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @ClassName UserController
 * @Author PPPL
 * @Date 2019/8/3 13:11
 **/
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private String rootPwd = "SystemRoot";

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public @ResponseBody ResultBean add(@RequestBody User user,@RequestParam(value = "rootPwd")String rootPwd){
        if(!this.rootPwd.equals(rootPwd)){
            return new ResultBean("超级密码不正确");
        }
        int flag = userService.addUser(user);
        if(flag == 0){
            return new ResultBean("姓名或密码已存在,无法添加");
        }else{
            return new ResultBean("添加成功");
        }
    }

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String user(HttpServletRequest req, HttpServletResponse resp){
        List<User> users = userService.getUserList();
        req.setAttribute("users", JSONObject.toJSONString(users));
        req.setAttribute("rootPwd",rootPwd);
        return "user";
    }

//    @RequestMapping(value = "/user",method = RequestMethod.GET)
//    public @ResponseBody ResultBean userList(){
//        ResultBean resultBean = null;
//        List<User> users = userService.getUserList();
//        resultBean = new ResultBean(users);
//        return resultBean;
//    }

    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public @ResponseBody ResultBean delUser(@PathVariable(value = "id")String id,@RequestParam(value = "rootPwd")String pwd){
        if(!this.rootPwd.equals(rootPwd)){
            return new ResultBean("超级密码不正确");
        }
        ResultBean resultBean = null;
        int flag = userService.delUser(id);
        resultBean = new ResultBean(flag==1);
        return resultBean;
    }
}
