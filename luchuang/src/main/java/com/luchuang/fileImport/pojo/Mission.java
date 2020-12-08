package com.luchuang.fileImport.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luchuang.common.UUID;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @ClassName Mission
 * @Author PPPL
 * @Date 2019/8/1 10:11
 **/
@Table(name = "mission")
@Data
public class Mission implements Serializable ,Cloneable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String mtype;

    private String detect_company;

    private String detect_contact;

    private String detect_contact_number;

    private String scene_contact;

    private String scene_contact_number;

    private Integer num;

    private String project_name;

    private double price;

    private String wtdw;

    private Integer count;

    private String type_list;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time = new Date();

    private String creator;

    @Transient
    private List<Wltsj> wltsjs = new ArrayList<>();

    private String pwd;

    private Integer status;

    private String Executive;

    private String category;

    /**
     *  任务出现6天后未盖章
     * */
    private boolean red_warn;

    /**
     *  10天未发放报告
     * */
    private boolean purple_warn;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Mission)super.clone();
    }

//    public Object clone1() throws CloneNotSupportedException{
//        return this.clone();
//    }

}
