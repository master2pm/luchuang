package com.luchuang.fileImport.pojo;

import com.luchuang.common.UUID;
import lombok.Data;

import javax.persistence.Table;
import java.util.Date;


/**
 * @version 1.0
 * @ClassName Pics
 * @Author PPPL
 * @Date 2019/9/4 16:34
 **/
@Table(name = "pics")
@Data
public class Pics {

    private String id = UUID.random();

    private String name;

    private Date create_time = new Date();

    private String wid;

    private String user_id;

    private String remark;

}
