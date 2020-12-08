package com.luchuang.fileImport.service.impl;

import com.alibaba.fastjson.JSON;
import com.luchuang.common.UUID;
import com.luchuang.fileImport.mapper.PicsMapper;
import com.luchuang.fileImport.pojo.Pics;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.service.PicsService;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.util.FileWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @version 1.0
 * @ClassName PicsServiceImpl
 * @Author PPPL
 * @Date 2019/9/4 16:30
 **/
@Service
public class PicsServiceImpl implements PicsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PicsMapper picsMapper;

    private User user;

    private int checkRule(String pwd){
        if("".equals(pwd))return -1;
        User user = userService.selUserByPwd(pwd);
        if(user==null)return -1;
        if(user.getStatus().indexOf("17")==-1)return 0;
        this.user = user;
        return 1;
    }

    @Override
    public List<Pics> getPics(String wid) {
        Example example = new Example(Pics.class);
        example.createCriteria().andEqualTo("wid",wid);
        List<Pics> list = picsMapper.selectByExample(example);
        return list;
    }

    @Override
    public int addPics(List<Pics> pics, String pwd) {
        int flag = checkRule(pwd);
        if(flag!=1)return flag;
        String wid = pics.get(0).getWid();
//        List<Pics> list = JSON.parseArray(FileWord.readJson("pics"),Pics.class);
        List<Pics> list = new LinkedList<>();

        List<String> remarks = new ArrayList<>();
        for(Pics pics1 : pics){
            remarks.add(pics1.getRemark());
            pics1.setName(UUID.random() + new Date().getTime());
            pics1.setUser_id(this.user.getId());
            pics1.setRemark(null);
            list.add(pics1);
        }
        picsMapper.insertList(list);
        for(int i = 0 ;i<pics.size();i++){
            FileWord.GenerateImage(remarks.get(i),pics.get(i).getName());
        }
//        FileWord.GenerateImage(pics);
        return 1;
    }

    @Override
    public int delPics(String wid,String id, String pwd) {
        int flag = checkRule(pwd);
        if(flag!=1)return flag;
        String filePath = FileWord.file_path.replaceAll("classes", "pics");

        Pics pic = picsMapper.selectByPrimaryKey(id);
        if(FileWord.del(filePath + pic.getName() + ".png")){
            picsMapper.deleteByPrimaryKey(id);
        }
        return 1;
    }
}
