package com.luchuang.fileImport.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @ClassName PostGroup
 * @Author PPPL
 * @Date 2019/8/1 16:15
 **/
public class PostGroup implements Serializable {

    private List<Mission> missions;
    private String pwd;
    private String group;
    private String ids;
    private String id;
    private int num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        group = group.replaceAll("，",",");
        this.group = group;
    }
}
