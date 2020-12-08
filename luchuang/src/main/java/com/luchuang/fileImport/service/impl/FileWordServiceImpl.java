package com.luchuang.fileImport.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luchuang.fileImport.mapper.MissionMapper;
import com.luchuang.fileImport.mapper.WltsjMapper;
import com.luchuang.fileImport.pojo.Mission;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.pojo.Wltsj;
import com.luchuang.fileImport.service.IFileWordService;
import com.luchuang.fileImport.service.MissionService;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.util.ExcelUtil;
import com.luchuang.fileImport.util.Export;
import com.luchuang.fileImport.util.FileWord;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Service
public class FileWordServiceImpl implements IFileWordService {

    private Export export;
    private Logger log = Logger.getLogger(FileWordServiceImpl.class);
    private final String superPwd = "SystemRoot";
    private boolean isNull = false;

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private WltsjMapper wltsjMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MissionService missionService;

    @Override
    public void addFile(Wltsj wl,boolean oneKey) {
        String editDate = wl.getDate();
        if(editDate!=null||"".equals(editDate)){

            Wltsj wltsj = wltsjMapper.selectByPrimaryKey(wl.getId());
            {
                String mid = wltsj.getMission_id();
                wl.setMission_id(mid);
            }
            {
                Object workNum = wltsj.getWork_num();
                if(workNum!=null){
//                            System.out.println("www--"+workNum);
                    wl.setWork_num(workNum.toString());
                }
                wl.setCreateAt(wltsj.getCreateAt());
            }
            {
                wl.setExamine(wltsj.getExamine());
                wl.setReview(wltsj.getReview());
                wl.setInvoiceNum(wltsj.getInvoiceNum());
                wl.setSeal(wltsj.getSeal());
                wl.setSettle(wltsj.getSettle());
                wl.setCredit(wltsj.getCredit());
                wl.setSettle_remark(wltsj.getSettle_remark());
                wl.setReport_send(wltsj.getReport_send());
                wl.setReport_msg(wltsj.getReport_send());
                wl.setMission_id(wltsj.getMission_id());
                wl.setRemark(wltsj.getRemark());
            }
            if(!oneKey)
            wltsjMapper.updateByPrimaryKeySelective(wl);


//            FileWord.del(new File(FileWord.file_path.replaceAll("classes","cache") + wl.getDate() + "/" + wl.getEdit_file_name()));
        }

        if(oneKey){
            Example example = new Example(Wltsj.class);
            example.createCriteria().andEqualTo("mission_id",wl.getMission_id());
            List<Wltsj> wltsjs = wltsjMapper.selectByExample(example);
            Integer bhNum = 0;
            String bhHead = "";
            {

                try {
                    String bh = wl.getBh();
                    bhHead = bh.substring(0,bh.length()-3);

                    bh = bh.substring(bh.length()-3);

                    bhNum = Integer.valueOf(bh);
                }catch (Exception e){
                    e.printStackTrace();
                    bhNum = 0;
                }
            }
            System.out.println("size::"+wltsjs.size());
            for(Wltsj wltsj : wltsjs){

                if("三宝钢管扣件".equals(wltsj.getMtype())){
                    wltsj.setWtdw(wl.getWtdw());
                    {
                        switch (bhNum.toString().length()){
                            case 1: wltsj.setBh(bhHead+"00"+bhNum++);
                            case 2: wltsj.setBh(bhHead+"0"+bhNum++);
                            case 3: wltsj.setBh(bhHead+bhNum++);
                        }
                    }
                    wltsj.setName(wl.getName());
                    wltsj.setPhone(wl.getPhone());
                    wltsj.setGcdz(wl.getGcmc());
                    wltsj.setGcmc(wl.getGcmc());
                    wltsjMapper.updateByPrimaryKeySelective(wltsj);
                    this.addFile(wltsj);
                }else{
                    try {
                        Wltsj wltsj1 = (Wltsj) wl.clone();
                        wltsj1.setId(wltsj.getId());
                        switch (bhNum.toString().length()){
                            case 1: wltsj1.setBh(bhHead+"00"+bhNum++);break;
                            case 2: wltsj1.setBh(bhHead+"0"+bhNum++);break;
                            case 3: wltsj1.setBh(bhHead+bhNum++);
                        }
                        wltsj1.setFilename(wltsj.getFilename());
                        wltsj1.setCreateAt(wltsj.getCreateAt());
                        wltsj1.setWork_num(wltsj.getWork_num());
                        System.out.println("bhbhbhbh:"+wltsj1.getBh());
                        wltsjMapper.updateByPrimaryKeySelective(wltsj1);
                        this.addFile(wltsj1);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        wltsjMapper.updateByPrimaryKeySelective(wl);
//        this.addFile(wl);
    }

    private boolean isComplete(Wltsj wltsj){
        boolean flag = true;
        //判断是否填写完整
        if(wltsj.getReport_msg()==null) {
            if (!"".equals(wltsj.getBh()) && !"".equals(wltsj.getWtdw()) && !"".equals(wltsj.getName()) && !"".equals(wltsj.getPhone())) {
                if ("塔吊,升降机,井架,吊篮".indexOf(wltsj.getMtype()) != -1) {
                    if ("".equals(wltsj.getAzdw()) ||
                            "".equals(wltsj.getSydw()) ||
                            "".equals(wltsj.getGcmc()) ||
                            "".equals(wltsj.getGcdz()) ||
                            "".equals(wltsj.getSbxh()) ||
                            "".equals(wltsj.getProperty_unit()) ||
                            "".equals(wltsj.getBabh()) ||
                            "".equals(wltsj.getSccj()) ||
                            "".equals(wltsj.getCcbh()) ||
                            "".equals(wltsj.getCcri()) ||
                            "".equals(wltsj.getPhoto_verify())) {
                        flag = false;
                    }
                } else if ("防坠器".equals(wltsj.getMtype())) {
                    if("".equals(wltsj.getPhoto_verify())){
                        flag = false;
                    }
                } else {
                    if ("".equals(wltsj.getGcmc()) || "".equals(wltsj.getGcdz())) {
                        flag = false;
                    } else {
                        String remark = wltsj.getRemark();
                        if (remark.indexOf("安全帽") != -1) {
                            if ("".equals(wltsj.getAqmbh())) {
                                flag = false;
                            }
                        }
                        if (remark.indexOf("安全网") != -1) {
                            if ("".equals(wltsj.getMaqmbh())) {
                                flag = false;
                            }
                        }
                        if (remark.indexOf("安全带") != -1) {
                            if ("".equals(wltsj.getAqdbh())) {
                                flag = false;
                            }
                        }
                        if (remark.indexOf("直角扣件") != -1) {
                            if ("".equals(wltsj.getZjbh())) {
                                flag = false;
                            }
                        }
                        if (remark.indexOf("旋转扣件") != -1) {
                            if ("".equals(wltsj.getXzbh())) {
                                flag = false;
                            }
                        }
                        if (remark.indexOf("对接扣件") != -1) {
                            if ("".equals(wltsj.getDjbh())) {
                                flag = false;
                            }
                        }
                        if (remark.indexOf("钢管") != -1) {
                            if ("".equals(wltsj.getGgbh())) {
                                flag = false;
                            }
                        }
                    }
                }
            }else{
                flag = false;
            }
        }
        return flag;
    }
    private void addFile(Wltsj wltsj){
//        if(this.isComplete(wltsj)){
//            Map<String,String> map = new HashMap();
//            map.put("executor","--");
//            map.put("execution_time",new Date().getTime()+"");
//            wltsj.setReport_msg(JSON.toJSONString(map));
////            editAttrList(wltsj,"report_msg",user,text);
//        }
        switch (wltsj.getMtype()){
            case "井架": this.createWltsj(wltsj);break;
            case "塔吊": this.createTj(wltsj);break;
            case "升降机":this.createSgt(wltsj);break;
            case "防坠器":this.createfzq(wltsj);break;
            case "吊篮":
            case "三宝钢管扣件":
                this.export = new Export(wltsj);
                this.export.createLog();
                break;
        }
    }

    @Override
    public boolean createCommonWord(Wltsj wl) {
        this.export = new Export(wl);
        this.export.common();
        return false;
    }

    @Override
    public boolean createWltsj(Wltsj wl) {
        this.export = new Export(wl);
        this.export.createWltsj();
        return false;
    }

    @Override
    public File createWltsj(Wltsj wl, String mtype) {
        this.export = new Export(wl);
        return this.export.createWltsj(mtype);
    }

    @Override
    public boolean createTj(Wltsj wl) {
        this.export = new Export(wl);
        this.export.createTj();
        return false;
    }

    @Override
    public File createTj(Wltsj wl, String mtype) {
        this.export = new Export(wl);
        return this.export.createTj(mtype);
    }

    @Override
    public boolean createSgt(Wltsj wl) {
        this.export = new Export(wl);
        this.export.createSgt();
        return false;
    }

    @Override
    public File createSgt(Wltsj wl, String mtype) {
        this.export = new Export(wl);
        return this.export.createSgt(mtype);
    }

    @Override
    public boolean createfzq(Wltsj wl) {
        this.export = new Export(wl);
        this.export.createfzq();
        return false;
    }

    @Override
    public File createfzq(Wltsj wl, String mtype) {
        this.export = new Export(wl);
        return this.export.createfzq(mtype);
    }

    @Override
    public boolean creategj(Wltsj wl) {
        this.export = new Export(wl);
        this.export.creategj();
        return false;
    }

    @Override
    public File creategj(Wltsj wl, String mtype) {
        this.export = new Export(wl);
        return this.export.creategj(mtype);
    }

    @Override
    public boolean createaqqc(Wltsj wl) {
        this.export = new Export(wl);
        this.export.createaqqc();
        return false;
    }

    @Override
    public File createaqqc(Wltsj wl, String mtype) {
        this.export = new Export(wl);
        return this.export.createaqqc(mtype);
    }

    @Override
    public File addFile(Wltsj wltsj, String mtype) {
        File file = null;
        switch (wltsj.getMtype()){
            case "井架": file = this.createWltsj(wltsj,mtype);break;
            case "塔吊": file = this.createTj(wltsj,mtype);break;
            case "升降机":file = this.createSgt(wltsj,mtype);break;
            case "防坠器":file = this.createfzq(wltsj,mtype);break;
            case "吊篮":
            case "三宝钢管扣件":
                this.export = new Export(wltsj);
                this.export.createLog();
                break;
        }
        return file;
    }

    @Override
    public List wordList(String searchkey) {
        List list = new ArrayList();
        File papa_file = new File(FileWord.file_path.replaceAll(FileWord.file_top,"").replaceAll("classes", "cache"));
        {

            for(File time_file:papa_file.listFiles()){
                Map map = new HashMap();
                map.put("date",time_file.getName());
                List data_file = new ArrayList<Map>();
                if(time_file.listFiles()!=null)
                for(File file : time_file.listFiles()){
                    Map map1 = new HashMap();
                    List file_list = new ArrayList();
                    if(file.isDirectory()){
                        for(File file1 : file.listFiles()){
                            String filename = "意见,回执,任务单,委托单,原始记录,报告";
                            String file1_name = file1.getName();
                            for(String str :filename.split(",")){
                                if(file1_name.indexOf(str)!=-1){
                                    file_list.add(str);
                                    break;
                                }
                                if(file1_name.indexOf(".txt")!=-1){
                                    String text = FileWord.read(file1);
                                    Map txt_map = JSON.parseObject(text);
                                    map1.put("attr",txt_map);
                                }
                            }
                        }
                        map1.put("file_name",file.getName());
                        map1.put("file_data",file_list);
                        data_file.add(map1);
                    }
                }
                Collections.sort(data_file,new FileComparator());
                map.put("files",data_file);
                list.add(map);
            }
            if(searchkey!=null&&!"".equals(searchkey)){
                List search_list = new ArrayList();
                for(Object obj :list){
                    Map map = (Map)obj;
                    List file_list = new ArrayList();
                    if(map.get("date").toString().indexOf(searchkey)!=-1){
                        search_list.add(map);
                    }else{
                        for(Object file_obj:(List)map.get("files")){
                            Map file_map = (Map)file_obj;
                            if(file_map.get("file_name").toString().indexOf(searchkey)!=-1){
                                file_list.add(file_map);
                            }else{
                                Map attr = (Map)file_map.get("attr");
                                if(attr!=null){
                                    for(Object attr_v:attr.values()){
                                        if(attr_v.toString().indexOf(searchkey)!=-1){
                                            file_list.add(file_map);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(file_list.size()!=0){
                        Map file_map = new HashMap();
                        file_map.put("files",file_list);
                        file_map.put("date",map.get("date").toString());
                        search_list.add(file_map);
                    }

                }
                list = search_list;
            }
        }
        return list;
    }

    @Override
    public Map readTxt(String file_name,String date) {
        Map map = null;
        String path = FileWord.file_path.replaceAll("classes","cache") + date + "/" + file_name + "/" + file_name + ".txt";
        BufferedReader bfr = null;
        try{
            bfr = new BufferedReader(new FileReader(path));
            String text = "";
            String jsonstr = "";
            while((text=bfr.readLine())!=null){
                jsonstr += text;
            }
            map = (Map)JSONObject.parse(jsonstr);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            try {
                bfr.close();
            }catch (Exception e){
                log.error(e.getMessage());
            }

        }
        return map;
    }

    @Override
    public HSSFWorkbook buildReportForm(String date) {
        String[] title = {"序号","任务日期","任务单编号","委托单编号","样品名称","样品编号","产值","委托单位","联系人","联系电话","安装单位","使用单位","工地名称","工程地址","产权单位","设备型号","出厂编号","审核、审批日期","盖公章日期","设备高度","检验日期","签发日期","检验类别","检验结论","检验员","备注"};

        String fileName = date+"报表.xls";

        String sheetName = "报表";

        String[][] content = new String[15][title.length];
        for(int i = 0 ; i < 15; i++){
            content[i][0] = i+1+"";
            content[i][1] = i+"gg";
            content[i][2] = i+"gg";
            content[i][3] = i+"gg";
            content[i][4] = i+"gg";
        }

        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName,title,content,null,null);
        return wb;
    }

    class FileComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            try {
                Map map1 = (Map)o1;
                Map map2 = (Map)o2;
                Map attr1 = (Map)map1.get("attr");
                Map attr2 = (Map)map2.get("attr");
//            log.info("o1:"+attr1);
//            log.info("o2:"+attr2);
                if(o1==null||attr1==null){
                    return -1;
                }
                if(o2==null||attr1==null){
                    return -1;
                }
                if(attr1.get("createAt")==null){
                    return -1;
                }
                if(attr2.get("createAt")==null){
                    return -1;
                }
//            log.info(attr1.get("createAt")+"-----"+attr2.get("createAt"));

                long date1 = (long)attr1.get("createAt");
                long date2 = (long)attr2.get("createAt");
                if(date1>date2){
                    return -1;
                }else{
                    return 1;
                }
            }catch (Exception e){
                return -1;
            }

        }
    }

    private int editFileAttr(String pwd, Long id, String attr,String text,boolean flag) throws Exception{
        if("".equals(pwd))return -1;
        User user = userService.selUserByPwd(pwd);
//        boolean isGm = user.hasRule("cancel");
//        boolean hasRule = user.hasRule(attr);
        if(user==null)return -1;
//        String[] rules = "4seal,5settle,6examine,7review,8credit,9report_msg,10report_send,15invoiceNum,14settle_remark,18photoRemark,20phone_visit,21photo_verify".split(",");
        List<Wltsj> wltsjs = JSON.parseArray(FileWord.readJson("fileAttr"),Wltsj.class);
        for(Wltsj wltsj : wltsjs){
            if(id.equals(wltsj.getId())){

                //判断是否填写完整
                if(wltsj.getReport_msg()==null){
                    boolean flag1 = this.isComplete(wltsj);
                    System.out.println("fflag--"+flag1);
                    if(!flag1)return 3;
                    editAttrList(wltsj,"report_msg",user,text,flag);

                }

                if("三宝钢管扣件".equals(wltsj.getMtype())&&"settle,credit".indexOf(attr)!=-1){
//                    String mid = wltsj.getMission_id();
                    for(Wltsj wltsj1 : wltsjs){
                        if(id.equals(wltsj1.getId())){
                            editAttrList(wltsj,attr,user,text,flag);
                            FileWord.writeJson("fileAttr",JSON.toJSONString(wltsjs));
                            return 1;
                        }
                    }
                }else{
                    editAttrList(wltsj,attr,user,text,flag);
                    FileWord.writeJson("fileAttr",JSON.toJSONString(wltsjs));
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public int editFileAttr(String pwd, Long id, String attr,String text,String mid) throws Exception{

        int result = editFileAttr(pwd,id,attr,text);
        //是否存在mid 如果有mid的话 就覆盖全部 不然只修改一个
        if("".equals(mid)||result!=1){
            return result;
        }
        Example wlExample = new Example(Wltsj.class);
        wlExample.createCriteria().andEqualTo("mission_id",mid);
        List<Wltsj> wltsjs = wltsjMapper.selectByExample(wlExample);
        {
            for(Wltsj wltsj : wltsjs){
                if(id.equals(wltsj.getId())){
                    continue;
                }
                editFileAttr(pwd,wltsj.getId(),attr,text);
            }
            return 1;
        }

    }


    @Override
    public int editFileAttr(String pwd, Long id, String attr,String text) throws Exception{

        if("".equals(pwd))return -1;
        User user = userService.selUserByPwd(pwd);
        System.out.println(JSON.toJSONString(user));
//        boolean isGm = user.hasRule("cancel");
//        boolean hasRule = user.hasRule(attr);
        if(user==null)return -1;
//        String[] rules = "4seal,5settle,6examine,7review,8credit,9report_msg,10report_send,15invoiceNum,14settle_remark,18photoRemark,20phone_visit,21photo_verify".split(",");
        Example wlExample = new Example(Wltsj.class);
        wlExample.createCriteria().andEqualTo("id",id);
        List<Wltsj> wltsjs = wltsjMapper.selectByExample(wlExample);
        if(wltsjs.size()!=0){
            Wltsj wltsj = wltsjs.get(0);
            if(id.equals(wltsj.getId())){
                //判断是否填写完整
                if(wltsj.getReport_msg()==null){
                    boolean flag1 = this.isComplete(wltsj);
                    if(!flag1)return 3;
                    editAttrList(wltsj,"report_msg",user,text);

                }

                if("三宝钢管扣件".equals(wltsj.getMtype())&&"settle,credit".indexOf(attr)!=-1){
                    System.out.println("扑街");
                    for(Wltsj wltsj1 : wltsjs){
                        if(id.equals(wltsj1.getId())){
                            editAttrList(wltsj,attr,user,text);
                            wltsjMapper.updateByPrimaryKey(wltsj);
                            return 1;
                        }
                    }
                }else{
                    editAttrList(wltsj,attr,user,text);
                    wltsjMapper.updateByPrimaryKey(wltsj);
                    return 1;
                }
            }
        }
        return 0;
    }

    private boolean attrIsNull(Wltsj wltsj){
        boolean flag = false;
        switch(wltsj.getMtype()){
            case "塔吊"        :
            case "升降机"      :
            case "井架"        :
            case "吊篮"        :  if(wltsj.getPhoto_verify()==null&&wltsj.getExamine()==null&&wltsj.getReview()==null&&wltsj.getSeal()==null&&wltsj.getSettle()==null&&wltsj.getCredit()==null)flag = true;break;
            case "防坠器"      :   if(wltsj.getPhoto_verify()==null&&wltsj.getReview()==null&&wltsj.getSeal()==null&&wltsj.getSettle()==null&&wltsj.getCredit()==null)flag = true;break;
            case "三宝钢管扣件" :   if(wltsj.getReview()==null&&wltsj.getSeal()==null&&wltsj.getSettle()==null&&wltsj.getCredit()==null)flag = true;break;
        }
        return flag;
    }

    private boolean editAttrList(Wltsj wltsj,String attr,User user,String text,boolean ... flag)throws Exception{
        //当flag1为false时 全部为空 为true时全部都填充 null正常操作
        Boolean flag1 = null;
        if(flag.length!=0){
            flag1 = flag[0];
        }

        switch (attr){
            case "seal":
                wltsj.setSeal(attrChecker(attr,wltsj.getSeal(),user));
                break;
            case "settle":
                {
                    String attr1 = wltsj.getSettle();
                    if(flag1!=null){
                        //当flag1为true时 全部填充 那么attr1就应当为null
                        if(flag1&&attr1!=null){
                            break;
                        }else if(!flag1&&attr1==null){
                            break;
                        }
                    }
                }
                wltsj.setSettle(attrChecker( attr,wltsj.getSettle(),user));
                break;
            case "examine":
                wltsj.setExamine(attrChecker( attr,wltsj.getExamine(),user));
                break;
            case "review":
                wltsj.setReview(attrChecker( attr,wltsj.getReview(),user));
                break;
            case "credit":
                {
                    String attr1 = wltsj.getCredit();
                    if(flag1!=null){
                        //当flag1为true时 全部填充 那么attr1就应当为null
                        if(flag1&&attr1!=null){
                            break;
                        }else if(!flag1&&attr1==null){
                            break;
                        }
                    }
                }
                wltsj.setCredit(attrChecker( attr,wltsj.getCredit(),user));
                break;
            case "report_msg":
                wltsj.setReport_msg(attrChecker( attr,wltsj.getReport_msg(),user));
                break;
            case "report_send":
            {
                String attr1 = wltsj.getReport_send();
                if(flag1!=null){
                    //当flag1为true时 全部填充 那么attr1就应当为null
                    if(flag1&&attr1!=null){
                        break;
                    }else if(!flag1&&attr1==null){
                        break;
                    }
                }
            }
                wltsj.setReport_send(attrChecker( attr,wltsj.getReport_send(),user));
                break;
            case "settle_remark":
                wltsj.setSettle_remark(attrChecker( attr,wltsj.getSettle_remark(),user,text));
                break;
            case "invoiceNum":
                wltsj.setInvoiceNum(attrChecker( attr,wltsj.getInvoiceNum(),user,text));
                break;
            case "photoRemark":
                wltsj.setPhotoRemark(attrChecker(attr,wltsj.getPhotoRemark(),user,text));
                break;
            case "phone_visit":
                wltsj.setPhone_visit(attrChecker( attr,wltsj.getPhone_visit(),user,text));
                break;
            case "photo_verify":
                wltsj.setPhoto_verify(attrChecker( attr,wltsj.getPhoto_verify(),user));
                break;
        }
        if(attrIsNull(wltsj)&&!"report_msg".equals(attr)){
//            System.out.println("---lala:---::"+attr);
            wltsj.setReport_msg(null);
        }
        return true;
    }

    private String attrChecker(String attr,User user,String text,boolean flag)throws Exception{
        boolean isGM = user.hasRule("cancel");
        Map<String,Object> attrMap = new HashMap();
        if(attr==null||flag){
            attrMap.put("execution_time",new Date().getTime());
            attrMap.put("executor",user.getName());
            if(text!=null){
                attrMap.put("text",text);
            }
            attr = JSON.toJSONString(attrMap);
            isNull = true;
        }else{
            if(attr!=null){
                if(!isGM){
                    throw new InstantiationException("noGm");
                }
                attr = null;
                isNull = false;
            }
        }
        return attr;
    }

    private String attrChecker(String property, String attr,User user,String ... text)throws Exception{
        System.out.println("pp>"+text.length+"---property::"+property);
        if((user.hasRule(property)&&(attr==null||text.length!=0))||(user.hasRule("cancel")&&attr!=null)||"report_msg".equals(property)){
            if(text.length==0){
                return attrChecker(attr,user,null,false);
            }else{
                return attrChecker(attr,user,text[0],true);
            }
        }
        throw new InstantiationException("nonono");
    }

}
