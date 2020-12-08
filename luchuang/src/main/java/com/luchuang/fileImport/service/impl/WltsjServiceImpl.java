package com.luchuang.fileImport.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.luchuang.fileImport.mapper.MissionMapper;
import com.luchuang.fileImport.mapper.WltsjMapper;
import com.luchuang.fileImport.pojo.Mission;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.pojo.Wltsj;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.service.WltsjService;
import com.luchuang.fileImport.util.FileWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @version 1.0
 * @ClassName WltsjServiceImpl
 * @Author PPPL
 * @Date 2019/8/7 15:48
 **/
@Service
public class WltsjServiceImpl implements WltsjService {

    @Autowired
    private WltsjMapper wltsjMapper;

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private UserService userService;

    @Override
    public int delWltsj(String id, String pwd) {

        User user = userService.selUserByPwd(pwd);
        if(user==null||!user.hasRule("del")){return 0;}

        Wltsj wl = wltsjMapper.selectByPrimaryKey(id);


        String path = FileWord.file_path.replaceAll("classes", "cache") + wl.getDate() + "/" + wl.getFilename();
        FileWord.del(new File(path));

        String mid = wl.getMission_id();
        Mission mis = missionMapper.selectByPrimaryKey(mid);

        if(mis.getId().equals(mid)){
            if(!"防坠器".equals(mis.getMtype())){
                int count = mis.getNum();
                if(--count==0){
                    missionMapper.deleteByPrimaryKey(mid);
                }
                mis.setNum(count);
                missionMapper.updateByPrimaryKey(mis);
            }else{
                missionMapper.deleteByPrimaryKey(mid);
            }

        }
        wltsjMapper.delete(wl);
        return 1;
    }
}
