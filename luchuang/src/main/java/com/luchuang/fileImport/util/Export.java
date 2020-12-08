package com.luchuang.fileImport.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luchuang.fileImport.mapper.WltsjMapper;
import com.luchuang.fileImport.pojo.Wltsj;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Export {
    private Configuration configuration = null;
    private String name;
    private Wltsj wl;
    private Map<String, String> dataMap;
    private Logger log = Logger.getLogger(Export.class);
    private String file_top;

    @Autowired
    private WltsjMapper wltsjMapper;

    public Export(){}

    //创建文件夹
    public Export(Wltsj wl) {
        this.configuration = new Configuration();
        this.configuration.setDefaultEncoding("UTF-8");
        this.name = wl.getFilename();
        this.wl = wl;
        this.dataMap = new HashMap();
        this.file_top = FileWord.file_top;
        if(this.wl.getCreateAt()==null){
            this.wl.setCreateAt(new Date());
        }
        {
            String date = this.wl.getDate();
            if(date==null)
            date = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
            {
                if(this.wl.getDate()==null){
                    File file = new File(FileWord.file_path.replaceAll("classes","cache") + date + "/");
                    if(!file.exists()){
                        log.info(file.getAbsolutePath());
                        file.setWritable(true,false);
                        log.info("文件夹创建是否成功"+file.mkdirs());
                    }
                }
            }
            String path = FileWord.file_path.replaceAll("classes", "cache") + date + "/" + this.name;
            FileWord.del(new File(path));
            File file = new File(path);
            if(this.wl.getBh()!=null&&!"".equals(this.wl.getBh())){
                log.info("lalalla:---"+file.getAbsolutePath().toString());
                try {
                    file.setWritable(true,false);
                    log.info("文件夹创建是否成功"+file.mkdirs());
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
            log.info(file.getAbsolutePath());
        }
        this.getData();
    }

    public void common() {
        String file_name = "检验整改意见,回执";
        for(String str :file_name.split(",")){
            this.create(str);
        }
        if(this.wl.getEdit_file_name()!=null&&!this.wl.getEdit_file_name().equals(this.wl.getFilename())){
            String path = FileWord.getCachePath(this.wl.getDate()+"/"+this.wl.getEdit_file_name());
            System.out.println(path);
            FileWord.del(new File(path));
        }
//        createLog();
    }

    public void createWltsj() {
        String file_name = "物料提升机任务单,物料提升机委托单,物料提升机原始记录,物料提升机报告";
        for(String str :file_name.split(",")){
            this.create(str);
        }
        this.common();
    }

    public File createWltsj(String mtype) {
        String file_name = "物料提升机任务单,物料提升机委托单,物料提升机原始记录,物料提升机报告,检验整改意见,回执";
        for(String str :file_name.split(",")){
            if(str.indexOf(mtype)!=-1)
            return this.create(str);
        }
        return null;
//        this.common();
    }

    public void createTj() {
        String file_name = "塔机任务单,塔机检验委托单,塔机原始记录,塔机报告";
        for(String str :file_name.split(",")){
            this.create(str);
        }
        this.common();
    }

    public File createTj(String mtype) {
        String file_name = "塔机任务单,塔机检验委托单,塔机原始记录,塔机报告,检验整改意见,回执";
        for(String str :file_name.split(",")){
            if(str.indexOf(mtype)!=-1){
                return this.create(str);
            }
        }
        return null;
    }

    public void createSgt() {
        String file_name = "施工升降机任务单,施工升降机委托单,施工升降机原始记录,施工升降机报告";
        for(String str :file_name.split(",")){
            this.create(str);
        }
        this.common();
    }

    public File createSgt(String mtype) {
        String file_name = "施工升降机任务单,施工升降机委托单,施工升降机原始记录,施工升降机报告,检验整改意见,回执";
        for(String str :file_name.split(",")){
            if(str.indexOf(mtype)!=-1){
                return this.create(str);
            }
        }
        return null;
    }

    public void createfzq() {
        String file_name = "防坠器任务单,防坠器检验委托单";
        for(String str :file_name.split(",")){
            this.create(str);
        }
        this.common();
    }

    public File createfzq(String mtype) {
        String file_name = "防坠器任务单,防坠器检验委托单,检验整改意见,回执";
        for(String str :file_name.split(",")){
            if(str.indexOf(mtype)!=-1){
                return this.create(str);
            }
        }
        return null;
    }

    public void creategj() {
        String file_name = "钢管、脚手架扣件检验任务单,钢管、脚手架扣件检验委托单";
        for(String str :file_name.split(",")){
            this.create(str);
        }
        this.common();
    }
    public File creategj(String mtype) {
        String file_name = "钢管、脚手架扣件检验任务单,钢管、脚手架扣件检验委托单,检验整改意见,回执";
        for(String str :file_name.split(",")){
            if(str.indexOf(mtype)!=-1){
                return this.create(str);
            }
        }
        return null;
    }

    public void createaqqc() {
        String file_name = "安全建材检验任务单,安全建材检验委托单";
        for(String str :file_name.split(",")){
            this.create(str);
        }
        this.common();
    }

    public File createaqqc(String mtype) {
        String file_name = "安全建材检验任务单,安全建材检验委托单,检验整改意见,回执";
        for(String str :file_name.split(",")){
            if(str.indexOf(mtype)!=-1){
                return this.create(str);
            }
        }
        return null;
    }


    public void createLog(){

        Wltsj wltsj = wltsjMapper.selectByPrimaryKey(wl.getId());
        if(wltsj!=null){
            wltsjMapper.updateByPrimaryKeySelective(wl);
        }else{
            wltsjMapper.insert(wl);
        }
//        String file_path = FileWord.file_path.replaceAll("classes","json") + "fileAttr.json";
//
//        String read_json = FileWord.read(file_path);
//        List<Wltsj> wltsjs = !"".equals(read_json)?JSON.parseArray(read_json,Wltsj.class):new ArrayList<Wltsj>();
//        Set<Wltsj> set = new HashSet<>(wltsjs);
//        Set<Wltsj> temp_set = new HashSet<>();
//        temp_set.add(wl);
//        System.out.println("log---"+JSON.toJSONString(wl));
//        for(Wltsj wltsj: set){
//            temp_set.add(wltsj);
//        }

//        System.out.println("写入是否---"+FileWord.write(file_path,JSONObject.toJSONString(temp_set)));

    }

    private void addk(String type, int num) {
        String tt = (String)this.dataMap.get(type);
        if (tt.length() < num) {
            String temp = "";

            for(int i = 1; i <= num - tt.length(); ++i) {
                temp = temp + " ";
            }

            this.dataMap.put(type, tt + temp);
        }

    }

    public File create(String ftlname) {
        log.info("-------->>>>创建文档方法被调用");
        log.info("----------->>>正在生成" + ftlname + ".doc");
        this.configuration = new Configuration();
        this.configuration.setDefaultEncoding("utf-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dd = sdf.format(new Date());
        if (ftlname.equals("回执")) {
            this.addk("sydw", 33);
            this.addk("gcmc", 43);
            this.addk("sbxh", 12);
            this.addk("ccbh", 12);
            this.addk("baxh", 10);
            log.info(this.dataMap);
        }
        String name = "";
        if(this.dataMap.get("bho")==null){

        }else{
            name = (String)this.dataMap.get("bho") + (String)this.dataMap.get("wtdw") + "（" + ((String)this.dataMap.get("gcmc")).trim() + "）" + ftlname + " " + dd.replaceAll("-", ".");
        }

//        String[] tsfh = new String[]{"\\", "/", ":", "*", "?", "\"", "<", ">", "|"};
//        String[] var9 = tsfh;
//        int var8 = tsfh.length;

        String date;
//        for(int var7 = 0; var7 < var8; ++var7) {
//            date = var9[var7];
//            name = name.replaceAll(date, "");
//        }
        date = this.wl.getDate();
        if(date==null){
            date = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        }


        try {
            this.configuration.setDirectoryForTemplateLoading(new File(FileWord.file_path.replaceAll("classes", "ftl")));
            File outFile = new File(FileWord.file_path.replaceAll("classes", "cache") + date + "/" + this.name.trim() + "/" + name + ".doc");
            log.info("lalalalal="+outFile.getAbsolutePath());
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
            Template t = this.configuration.getTemplate(ftlname + ".ftl", "utf-8");
            t.process(this.dataMap, out);
            out.close();
            return outFile;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }

    private void getData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] date = sdf.format(new Date()).split("-");
        this.dataMap.put("da1", date[0]);
        this.dataMap.put("da2", date[1]);
        this.dataMap.put("da3", date[2]);
        this.dataMap.put("wtdw", this.wl.getWtdw());
        this.dataMap.put("name", this.wl.getName());
        this.dataMap.put("phone", this.wl.getPhone());
        this.dataMap.put("azdw", this.wl.getAzdw());
        this.dataMap.put("sydw", this.wl.getSydw());
        this.dataMap.put("gcmc", this.wl.getGcmc());
        this.dataMap.put("gcdz", this.wl.getGcdz());
        this.dataMap.put("sbxh", this.wl.getSbxh());
        System.out.println("备案编号----"+this.wl.getBabh());
        this.dataMap.put("baxh", this.wl.getBabh());
        this.dataMap.put("sccj", this.wl.getSccj());
        this.dataMap.put("ccbh", this.wl.getCcbh());
        switch(this.wl.getMtype()){
            case "塔吊": this.dataMap.put("mty", "塔式起重机");break;
            case "升降机": this.dataMap.put("mty", "施工升降机");break;
            case "井架": this.dataMap.put("mty", "物料提升机");break;
            case "防坠器": this.dataMap.put("mty","防坠安全器");break;
            default:this.dataMap.put("mty", this.wl.getMtype());
        }

        this.dataMap.put("propertyunit",this.wl.getProperty_unit());
        if(this.wl.getCcri()!=null){
            String data = this.wl.getCcri().trim();
            try{
                String year = data.substring(0, 4);
                String month = data.substring(4, 6);
                if(month.charAt(0)=='0'){
                    month = month.substring(1);
                }
                String day = "";
                if(data.length()==8){
                    day = data.substring(6, 8);
                    if(day.charAt(0)=='0'){
                        day = day.substring(1);
                    }
                    day += "日";
                }
                data = year + "年" + month + "月" + day;
            }catch(Exception e){
                e.printStackTrace();
            }
            this.dataMap.put("ccri", data);
        }

        String num = this.wl.getBh();
        if (num!=null&&num.length() == 14) {
            num = num.substring(0, 2) + " " + num.substring(2, 4) + " " + num.substring(4, 10) + " " + num.substring(10);
        }

        this.dataMap.put("bho", num);
    }

    public static void main(String[] args){
        FileInputStream input = null;
        try {
            input = new FileInputStream("E:\\tete\\tete.data");
            byte[] buffer = new byte[1024];
            while (true) {
                int len = input.read(buffer);
                if (len == -1) {
                    break;
                }
                String str = new String(buffer, 0, len);
                System.out.println(str);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}