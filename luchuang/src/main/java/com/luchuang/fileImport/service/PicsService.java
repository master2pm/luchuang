package com.luchuang.fileImport.service;

import com.luchuang.fileImport.pojo.Pics;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName PicsService
 * @Author PPPL
 * @Date 2019/9/4 16:30
 **/
public interface PicsService {


    /**
     *
     * */

    List<Pics> getPics(String wid);

    int addPics(List<Pics> pics, String pwd);

    int delPics(String wid,String id,String pwd);

}
