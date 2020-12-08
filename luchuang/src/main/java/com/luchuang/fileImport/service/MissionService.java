package com.luchuang.fileImport.service;

import com.luchuang.fileImport.pojo.Mission;
import com.luchuang.fileImport.pojo.PostGroup;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName MissionService
 * @Author PPPL
 * @Date 2019/8/1 10:22
 **/
public interface MissionService {

    List<Mission> getMissions(String search);

    int addMission(Mission mission);

    List<Mission> getMissions(String less,String greater);

    int postRandomGroup(PostGroup postGroup);

    int delMission(String id,String pwd);

    int appendGroup(String id,int num,String pwd,String group);

    int updateMission(Mission mission,String pwd);

    int editPrice(String id,String price,String pwd);

    int fastOption(String text);
}
