package com.luchuang.fileImport.controller;

import com.alibaba.fastjson.JSON;
import com.luchuang.fileImport.mapper.*;
import com.luchuang.fileImport.pojo.*;
import com.luchuang.fileImport.util.FileWord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WltsjMapper wltsjMapper;

    @Autowired
    private PicsMapper picsMapper;

    private Logger log = Logger.getLogger(TestController.class);

    @RequestMapping(value = "/test",method = {RequestMethod.GET})
    public Map delTest(@RequestParam(value = "date",required = false) String date){
        Map map = new HashMap();
        map.put("date",date);
        String file_path = "../cache/"+date;//FileWord.getCachePath(date);
        File file = new File(file_path);
        file.setWritable(true,false);
        System.out.println("是否存在"+file.exists());
        log.info(file.exists());
        Boolean mks = file.mkdirs();
        map.put("文件夹路径",file_path);
        map.put("创建成功",mks);
        if(!mks){
            map.put("啦啦啦",file.mkdir());
        }
        return map;
    }

    @RequestMapping(value  = "/cunzai",method = {RequestMethod.GET})
    public Map hash(@RequestParam(value="path")String path){
        Map map = new HashMap();
        map.put("path",path);
        File file = new File(path);
        map.put("是否存在",file.exists());
        map.put("是文件",file.isFile());
        map.put("是文件夹",file.isDirectory());
        map.put("是否创建成功",file.mkdirs());
        return map;
    }

    @RequestMapping(value = "/missionJson",method = RequestMethod.GET)
    public Map missionJson(){
        Map map = new HashMap();
        String text = FileWord.read("");
        List<Map> list  = JSON.parseArray(text,Map.class);
        list.stream().forEach(map1 -> {
            String id = map1.get("id").toString();
            map1.remove("id");
            Mission mission = JSON.parseObject(JSON.toJSONString(map1),Mission.class);
            missionMapper.insert(mission);
            Example wlEx = new Example(Wltsj.class);
            wlEx.createCriteria().andEqualTo("mission_id",id);
            List<Wltsj> wltsjs = wltsjMapper.selectByExample(wlEx);
            wltsjs.stream().forEach(wltsj -> {
                wltsj.setMission_id(mission.getId().toString());
                wltsjMapper.updateByExampleSelective(wltsj,wlEx);
            });
        });
        System.out.println("上传完毕");
        return map;
    }

    @RequestMapping(value = "/UserJson",method = RequestMethod.GET)
    public Map UserJson(){
        Map map = new HashMap();
        String text = FileWord.readJson("user");
        List<User> users = JSON.parseArray(text,User.class);
        userMapper.insertList(users);
        return map;
    }

    @RequestMapping(value = "/WltsjJson",method = RequestMethod.GET)
    public Map WltsjJSON(){
        Map map = new HashMap();
        String text = FileWord.readJson("fileAttr");
        List<Map> list  = JSON.parseArray(text,Map.class);
        list.stream().forEach(map1 -> {
            map1.remove("id");
        });
        List<Wltsj> wltsjs = JSON.parseArray(JSON.toJSONString(list), Wltsj.class);
        wltsjMapper.insertList(wltsjs);
        map.put("www",wltsjs);
        return map;
    }

    @RequestMapping(value = "/PicsJson",method = RequestMethod.GET)
    public Map PicsJSON(){
        Map map = new HashMap();
        String text = FileWord.readJson("pics");
        List<Pics> pics = JSON.parseArray(text, Pics.class);
        picsMapper.insertList(pics);
//        pics.stream().forEach(pics1 -> {
//            picsMapper.insert(pics1);
//        });
        return map;
    }

}
