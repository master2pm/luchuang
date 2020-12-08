package com.luchuang.fileImport.pojo;

import com.luchuang.common.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "wltsj")
public class Wltsj implements Serializable ,Cloneable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String bh = "";
    private String wtdw = "";
    private String name = "";
    private String phone = "";
    private String azdw = "";
    private String sydw = "";
    private String gcmc = "";
    private String gcdz = "";
    private String sbxh = "";
    private String babh = "";
    private String sccj = "";
    private String ccbh = "";
    private String ccri = "";
    private String filename = "";
    private String mtype = "";
    private String date;
    private String edit_file_name = "";

    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt = new Date();
    private String work_num = "";
    private String property_unit = "";
    private double price = 0;
    private String photoRemark;
//    防坠器用的两个字段 num数量 ypbh样品编号
    private int num = 0;
    private String ypbh = "";
    //三宝里面的样品编号 用来存放样品编号的json
    //安全帽编号
    private String aqmbh = "";
    //安全网编号
    private String maqmbh = "";
    //安全带编号
    private String aqdbh = "";
    //直角扣件编号
    private String zjbh = "";
    //旋转扣件编号
    private String xzbh = "";
    //对接扣件
    private String djbh = "";
    //钢管
    private String ggbh = "";
    /**
     *  检验员合格
     * */
    private String examine;
    /**
     *  审核
     * */
    private String review;
    /**
     *  发票号码
     * */
    private String invoiceNum;
    /**
     *  盖章
     * */
    private String seal;
    /**
     *  收款
     * */
    private String settle;

    /**
     *  赊账
     * */
    private String credit;
    /**
     *  收款备注
     * */
    private String settle_remark;

    /**
     *  报告发送
     * */
    private String report_send;

    /**
     *  报告信息
     * */
    private String report_msg;
    /**
     *  电话回访
     * */
    private String phone_visit;

    /**
     *  照片审核
     * */
    private String photo_verify;


    private String mission_id;
    /**
     *  图片路径
     * */
    private String pics;

    private String remark;

    public String getPhone_visit() {
        return phone_visit;
    }

    public void setPhone_visit(String phone_visit) {
        this.phone_visit = phone_visit;
    }

    public String getPhoto_verify() {
        return photo_verify;
    }

    public void setPhoto_verify(String photo_verify) {
        this.photo_verify = photo_verify;
    }

    public String getPhotoRemark() {
        return photoRemark;
    }

    public void setPhotoRemark(String photoRemark) {
        this.photoRemark = photoRemark;
    }

    public String getAqmbh() {
        return aqmbh;
    }

    public void setAqmbh(String aqmbh) {
        this.aqmbh = aqmbh;
    }

    public String getMaqmbh() {
        return maqmbh;
    }

    public void setMaqmbh(String maqmbh) {
        this.maqmbh = maqmbh;
    }

    public String getAqdbh() {
        return aqdbh;
    }

    public void setAqdbh(String aqdbh) {
        this.aqdbh = aqdbh;
    }

    public String getZjbh() {
        return zjbh;
    }

    public void setZjbh(String zjbh) {
        this.zjbh = zjbh;
    }

    public String getXzbh() {
        return xzbh;
    }

    public void setXzbh(String xzbh) {
        this.xzbh = xzbh;
    }

    public String getDjbh() {
        return djbh;
    }

    public void setDjbh(String djbh) {
        this.djbh = djbh;
    }

    public String getGgbh() {
        return ggbh;
    }

    public void setGgbh(String ggbh) {
        this.ggbh = ggbh;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getYpbh() {
        return ypbh;
    }

    public void setYpbh(String ypbh) {
        this.ypbh = ypbh;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setSeal(String seal) {
        this.seal = seal;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getSettle_remark() {
        return settle_remark;
    }

    public void setSettle_remark(String settle_remark) {
        this.settle_remark = settle_remark;
    }

    public String getReport_send() {
        return report_send;
    }

    public void setReport_send(String report_send) {
        this.report_send = report_send;
    }

    public String getReport_msg() {
        return report_msg;
    }

    public void setReport_msg(String report_msg) {
        this.report_msg = report_msg;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getSettle() {
        return settle;
    }

    public void setSettle(String settle) {
        this.settle = settle;
    }

    public String getMission_id() {
        return mission_id;
    }

    public void setMission_id(String mission_id) {
        this.mission_id = mission_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeal() {
        return seal;
    }

    public String getProperty_unit() {
        return property_unit;
    }

    public void setProperty_unit(String property_unit) {
        this.property_unit = property_unit;
    }

    public String getWork_num() {
        return work_num;
    }

    public void setWork_num(String work_num) {
        this.work_num = work_num;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getEdit_file_name() {
        return edit_file_name;
    }

    public void setEdit_file_name(String edit_file_name) {
        this.edit_file_name = edit_file_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }



    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }


    public String getBh() {
        return this.bh;
    }

    public void setBh(String bh) {
        try {
            this.bh = URLDecoder.decode(bh, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.bh = bh;
        }

    }

    public String getWtdw() {
        return this.wtdw;
    }

    public void setWtdw(String wtdw) {
        try {
            this.wtdw = URLDecoder.decode(wtdw, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.wtdw = wtdw;
        }

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        try {
            this.name = URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.name = name;
        }

    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        try {
            this.phone = URLDecoder.decode(phone, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.phone = phone;
        }

    }

    public String getAzdw() {
        return this.azdw;
    }

    public void setAzdw(String azdw) {
        try {
            this.azdw = URLDecoder.decode(azdw, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.azdw = azdw;
        }

    }

    public String getSydw() {
        return this.sydw;
    }

    public void setSydw(String sydw) {
        try {
            this.sydw = URLDecoder.decode(sydw, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.sydw = sydw;
        }

    }

    public String getGcmc() {
        return this.gcmc;
    }

    public void setGcmc(String gcmc) {
        try {
            this.gcmc = URLDecoder.decode(gcmc, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.gcmc = gcmc;
        }

    }

    public String getGcdz() {
        return this.gcdz;
    }

    public void setGcdz(String gcdz) {
        try {
            this.gcdz = URLDecoder.decode(gcdz, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.gcdz = gcdz;
        }

    }

    public String getSbxh() {
        return this.sbxh;
    }

    public void setSbxh(String sbxh) {
        try {
            this.sbxh = URLDecoder.decode(sbxh, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.sbxh = sbxh;
        }

    }

    public String getBabh() {
        return this.babh;
    }

    public void setBabh(String babh) {
        try {
            this.babh = URLDecoder.decode(babh, "UTF-8").trim();
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.babh = babh;
        }

    }

    public String getSccj() {
        return this.sccj;
    }

    public void setSccj(String sccj) {
        try {
            this.sccj = URLDecoder.decode(sccj, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.sccj = sccj;
        }

    }

    public String getCcbh() {
        return this.ccbh;
    }

    public void setCcbh(String ccbh) {
        try {
            this.ccbh = URLDecoder.decode(ccbh, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.ccbh = ccbh;
        }

    }

    public String getCcri() {
        return this.ccri;
    }

    public void setCcri(String ccri) {
        try {
            this.ccri = URLDecoder.decode(ccri, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            this.ccri = ccri;
        }

    }

    public Wltsj() {

    }

    public Wltsj(String bh, String wtdw) {
        this.bh = bh;
        this.wtdw = wtdw;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Wltsj)super.clone();
    }

    @Override
    public boolean equals(Object obj){
        if(obj==null){
            return true;
        }
        Wltsj wl_obj = (Wltsj)obj;
        return wl_obj.getId().equals(this.id);
    }

    public String toString() {
        return "编号:" + this.bh + "\n委托单位:" + this.wtdw + "\n联系人:" + this.name + "\n电话:" + this.phone + "\n安装单位:" + this.azdw + "\n使用单位:" + this.sydw + "\n工程名称:" + this.gcmc + "\n设备型号:" + this.sbxh + "\n备案编号:" + this.babh + "\n生产厂家:" + this.sccj + "\n出厂编号:" + this.ccbh + "\n出厂日期:" + this.ccri;
    }

    public static void main(String[] args){
        Wltsj wltsj = new Wltsj();
        wltsj.setPrice(1);
        Set<Wltsj> set = new HashSet<>();
        set.add(wltsj);
        set.add(wltsj);
        System.out.println(set.size());
    }
}