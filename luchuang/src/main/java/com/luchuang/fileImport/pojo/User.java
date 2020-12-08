package com.luchuang.fileImport.pojo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.luchuang.common.UUID;
import com.luchuang.fileImport.util.FileWord;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @ClassName User
 * @Author PPPL
 * @Date 2019/8/3 13:12
 **/
@Table(name = "user")
@Data
public class User implements Serializable {

    @Transient
    private static final String superPwd = "SystemRoot";

//    @Id
    private String id = UUID.random();
    @Column(name = "name")
    private String name;
    @Column(name = "pwd")
    private String pwd;

    /**
     *  1添加任务 2随机分配 3删除 4盖章 5收款 6检验合格 7审核 8赊账 9报告信息 10报告发放
     *  11总结表导出 12列表价格是否显示 13下载报告 14收款备注 15发票号码 16价格显示 17图片上传
     *  18检验备注 19修改检验单价 20电话回访 21照片审核 22取消钩子
     * */
    private String status = "1";

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time = new Date();



    public boolean hasRule(String attr){
        String[] rules = "1add,2random,3del,4seal,5settle,6examine,7review,8credit,9report_msg,10report_send,15invoiceNum,14settle_remark,18photoRemark,19edit_price,20phone_visit,21photo_verify,22cancel".split(",");
        for(String rule : rules){
            String replacedRule = rule.replace(attr,"");
            String[] statusAttr = this.status.split("-");
            for(String statuss : statusAttr){
                if(replacedRule.equals(statuss)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasRule(String status,String attr){
        String[] rules = "1add,2random,3del,4seal,5settle,6examine,7review,8credit,9report_msg,10report_send,15invoiceNum,14settle_remark,18photoRemark,19edit_price,20phone_visit,21photo_verify,22cancel".split(",");
        for(String rule : rules){
            String replacedRule = rule.replace(attr,"");
            String[] statusAttr = status.split("-");
            for(String statuss : statusAttr){
                if(replacedRule.equals(statuss)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args){
        User user = new User();
        User user1 = new User();
        User user2 = new User();
        user.setName("测试1");
        user1.setName("测试2");
        user2.setName("测试3");
        user.setPwd("111");
        user1.setPwd("222");
        user2.setPwd("333");
        user.setStatus("1");
        user1.setStatus("1-2-3");
        user2.setStatus("3");
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);
        FileWord.writeJson("user", JSON.toJSONString(users));
    }
}
