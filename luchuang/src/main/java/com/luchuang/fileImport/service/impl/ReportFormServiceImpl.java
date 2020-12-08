package com.luchuang.fileImport.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luchuang.fileImport.pojo.FileAttr;
import com.luchuang.fileImport.pojo.Mission;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.pojo.Wltsj;
import com.luchuang.fileImport.service.IReportFormService;
import com.luchuang.fileImport.service.MissionService;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.util.ExcelUtil;
import com.luchuang.fileImport.util.FileWord;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportFormServiceImpl implements IReportFormService {

    @Autowired
    private UserService userService;

    @Autowired
    private MissionService missionService;

    private Logger log = Logger.getLogger(ReportFormServiceImpl.class);

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     *  获取列表 可以导出的月有
     * */
    @Override
    public Map getReportList(String searchkey) {

        FileWordServiceImpl fileWordService = new FileWordServiceImpl();
        List mon_list = fileWordService.wordList(searchkey);

        Map date_count = new HashMap();
        for(Object list_obj : mon_list){
            Map list_map = (Map)list_obj;
            String file_name = list_map.get("date").toString();
            String date = file_name.substring(0,file_name.lastIndexOf("-"));
            Object date_val = date_count.get(date);
            List date_list = new ArrayList();
            if(date_val!=null){
                date_list = (ArrayList)date_val;
            }
            date_list.add(list_map);
            date_count.put(date,date_list);
        }
        Iterator it = date_count.keySet().iterator();
        List real_list = new ArrayList();
        while(it.hasNext()){
            String key = (String)it.next();
            Map real_map = new HashMap();
            real_map.put("date",key);
            List files_list = (List)date_count.get(key);
            Collections.sort(files_list, new DateFilesColler());
            real_map.put("files",files_list);
            real_list.add(real_map);
        }
        date_count.clear();
        Collections.sort(real_list, new DateFilesColler());
        date_count.put("data",real_list);
        return date_count;
    }

    /**
     *  按月导出小组汇总表
     * */
    @Override
    public HSSFWorkbook getReportFile(String date,String pwd) {
        User user = userService.selUserByPwd(pwd);
        if(user==null||user.getStatus().indexOf("11")==-1){
            return null;
        }
        this.date = date;

        String[] title = {"小组","塔吊","升降机","井架","吊篮","防坠器","三宝钢管扣件"};
        String sheetName = "小组汇总表";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Date date1 = null;
        try {
            date1 = lastDayOfMonth(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(date1).split("-")[2];
        String less = date+"-"+lastDay;
        String greater = date+"-"+1;
        List<Mission> missions = missionService.getMissions(less,greater);
        System.out.println("任务数：："+missions.size());
        List<Mission> tempMissions = new ArrayList<>();
        for(Mission mission : missions){
            if(sdf.format(mission.getCreate_time()).equals(date)){
                tempMissions.add(mission);
            }
        }
        String[][] content = getContent(tempMissions,title.length);
        System.out.println(content);
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName,title,content,null,null);
        return wb;
    }

    private List<Mission> getMissionsByDate(String date){
        MissionServiceImpl missionService = new MissionServiceImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Date date1 = null;
        try {
            date1 = lastDayOfMonth(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(date1).split("-")[2];
        String less = date+"-"+lastDay;
        String greater = date+"-"+1;
        List<Mission> missions = missionService.getMissions(less,greater);
        List<Mission> missions1 = new ArrayList<>();
        for(Mission mission : missions){
            if(new SimpleDateFormat("yyyy-MM-dd").format(mission.getCreate_time().getTime()).indexOf(date)!=-1){
                missions1.add(mission);
            }
        }
        return missions1;
    }

    //机器汇总
    private Map<String,String[][]> getContent1(List<Mission> list,int len){
        String[][] content = new String[999][len];
        String[] mtypeArr = {"塔吊","升降机","井架","吊篮","防坠器","三宝钢管扣件"};
        Map<String,String[][]> contentMap = new HashMap<>();
        String[][] tjArr = new String[999][len],
                jjArr = new String[999][len],
                sjjArr = new String[999][len],
                dlArr = new String[999][len],
                fzqArr= new String[999][len],
                sbArr = new String[999][len];

        int[] indexArr = {0,0,0,0,0,0};
        for(Mission mission : list){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if(!this.date.equals(sdf.format(mission.getCreate_time()))){
                continue;
            }
            int zindex = 0;
            String mtype = mission.getMtype();
            switch(mtype){
                case "塔吊":
                    tjArr = makeContent(tjArr,indexArr[0],mission);
                    indexArr[0] += mission.getWltsjs().size();
                    break;
                case "升降机":
                    sjjArr = makeContent(sjjArr,indexArr[1],mission);
                    indexArr[1] += mission.getWltsjs().size();
                    break;
                case "井架":
                    jjArr = makeContent(jjArr,indexArr[2],mission);
                    indexArr[2] += mission.getWltsjs().size();
                    break;
                case "吊篮":
                    dlArr = makeContent(dlArr,indexArr[3],mission);
                    indexArr[3] += mission.getWltsjs().size();
                    break;
                case "防坠器":
                    fzqArr = makeContent(fzqArr,indexArr[4],mission);
                    indexArr[4] += mission.getWltsjs().size();
                    break;
                case "三宝钢管扣件":
                    sbArr = makeContent(sbArr,indexArr[5]++,mission);
                    break;
            }
        }
        tjArr = this.sortArr(tjArr,indexArr[0]);
        sjjArr = this.sortArr(sjjArr,indexArr[1]);
        jjArr = this.sortArr(jjArr,indexArr[2]);
        dlArr = this.sortArr(dlArr,indexArr[3]);
        fzqArr = this.sortArr(fzqArr,indexArr[4]);
//        sbArr = this.sortArr(sbArr,indexArr[5]);


        tjArr[indexArr[0]][20] = "SUM(U3:U"+ (indexArr[0]+2)+")";
        tjArr[indexArr[0]][21] = "SUM(V3:V"+ (indexArr[0]+2)+")";
        tjArr[indexArr[0]][22] = "SUM(W3:W"+ (indexArr[0]+2)+")";
        tjArr[indexArr[0]][23] = "SUM(X3:X"+ (indexArr[0]+2)+")";
        tjArr[indexArr[0]][0] = "合计";
        contentMap.put("塔吊",tjArr);
        sjjArr[indexArr[1]][20] = "SUM(U3:U"+ (indexArr[1]+2)+")";
        sjjArr[indexArr[1]][21] = "SUM(V3:V"+ (indexArr[1]+2)+")";
        sjjArr[indexArr[1]][22] = "SUM(W3:W"+ (indexArr[1]+2)+")";
        sjjArr[indexArr[1]][23] = "SUM(X3:X"+ (indexArr[1]+2)+")";
        sjjArr[indexArr[1]][0] = "合计";
        contentMap.put("升降机",sjjArr);
        jjArr[indexArr[2]][20] = "SUM(U3:U"+ (indexArr[2]+2)+")";
        jjArr[indexArr[2]][21] = "SUM(V3:V"+ (indexArr[2]+2)+")";
        jjArr[indexArr[2]][22] = "SUM(W3:W"+ (indexArr[2]+2)+")";
        jjArr[indexArr[2]][23] = "SUM(X3:X"+ (indexArr[2]+2)+")";
        jjArr[indexArr[2]][0] = "合计";
        contentMap.put("井架",jjArr);
        dlArr[indexArr[3]][20] = "SUM(U3:U"+ (indexArr[3]+2)+")";
        dlArr[indexArr[3]][21] = "SUM(V3:V"+ (indexArr[3]+2)+")";
        dlArr[indexArr[3]][22] = "SUM(W3:W"+ (indexArr[3]+2)+")";
        dlArr[indexArr[3]][23] = "SUM(X3:X"+ (indexArr[3]+2)+")";
        dlArr[indexArr[3]][0] = "合计";
        contentMap.put("吊篮",dlArr);
        fzqArr[indexArr[4]][13] = "SUM(N3:N"+ (indexArr[4]+2)+")";
        fzqArr[indexArr[4]][14] = "SUM(O3:O"+ (indexArr[4]+2)+")";
        fzqArr[indexArr[4]][15] = "SUM(P3:P"+ (indexArr[4]+2)+")";
        fzqArr[indexArr[4]][16] = "SUM(Q3:Q"+ (indexArr[4]+2)+")";
        fzqArr[indexArr[4]][0] = "合计";
        contentMap.put("防坠器",fzqArr);
        sbArr[indexArr[5]][13] = "SUM(N3:N"+ (indexArr[5]+2)+")";
        sbArr[indexArr[5]][14] = "SUM(O3:O"+ (indexArr[5]+2)+")";
        sbArr[indexArr[5]][15] = "SUM(P3:P"+ (indexArr[5]+2)+")";
        sbArr[indexArr[5]][16] = "SUM(Q3:Q"+ (indexArr[5]+2)+")";
        sbArr[indexArr[5]][0] = "合计";
        contentMap.put("三宝钢管扣件",sbArr);


        return contentMap;
    }

    private String[][] sortArr(String[][] arr ,  int index){

        String[][] newArr = new String[999][30];
        Map<String,List<String[]>> map = new HashMap<>();
        int len = 0;
        for(int i = 0 ; i < index; i++){
            String bh = arr[i][1].replaceAll(" ","");
            try {
                String key = bh.substring(2,4);
                List<String[]> cityList = map.get(key);
                if(cityList==null)cityList = new ArrayList<>();
                cityList.add(arr[i]);
                map.put(key,cityList);
            }catch (Exception e){
                List<String[]> cityList = map.get("err1");
                if(cityList==null) cityList = new ArrayList<>();
                cityList.add(arr[i]);
                map.put("err1",cityList);
            }
        }

        for(String key : map.keySet()){
            List<String[]> cityList = map.get(key);
            String[][] tempArr = new String[cityList.size()][30];
            for(int i = 0 ; i < cityList.size() ; i++){
                tempArr[i] = cityList.get(i);
            }
            for(int i = 0 ; i < cityList.size()-1; i++){
                for(int j = i+1; j< cityList.size();j++){
                    int tempNum = wtbhSub(tempArr[i][1]);
                    int tempNum1 = wtbhSub(tempArr[j][1]);
                    if(tempNum > tempNum1){
                        String[] temp = tempArr[i];
                        tempArr[i] = tempArr[j];
                        tempArr[j] = temp;
                    }
                }
            }
            for(int i = 0 ; i < cityList.size(); i++){
                newArr[len++] = tempArr[i];
            }
        }

        return newArr;
    }

    /**
     *  数据整理 待完成
     *
     *  收款需要额外的整理？
     *
     * */
    private String[][] makeContent(String[][] strArr,int zindex,Mission mission){

        List<Wltsj> wltsjs = mission.getWltsjs();
        if("塔吊,升降机,井架,吊篮".indexOf(mission.getMtype())!=-1){
            for(Wltsj wltsj : wltsjs){
                strArr[zindex][0] = zindex+1+"";
                strArr[zindex][1] = wltsj.getBh();
                strArr[zindex][2] = mission.getDetect_company();
                strArr[zindex][3] = mission.getDetect_contact();
                strArr[zindex][4] = mission.getDetect_contact_number();
                strArr[zindex][5] = mission.getCreator();
                strArr[zindex][6] = wltsj.getSydw();
                strArr[zindex][7] = wltsj.getAzdw();
                strArr[zindex][8] = wltsj.getProperty_unit();
                strArr[zindex][9] = wltsj.getWtdw();
                strArr[zindex][10] = wltsj.getName();
                strArr[zindex][11] = wltsj.getGcmc();
                strArr[zindex][12] = wltsj.getGcdz();
                strArr[zindex][13] = wltsj.getBabh();
                strArr[zindex][14] = wltsj.getCcbh();
                strArr[zindex][15] = wltsj.getSbxh();
                strArr[zindex][16] = mission.getExecutive();
                strArr[zindex][17] = "";
                strArr[zindex][18] = wltsj.getReport_msg()!=null?"合格":"";
                strArr[zindex][19] = wltsj.getReview()!=null?"已审核":"";
                strArr[zindex][20] = wltsj.getSeal()!=null?"已盖章":"";
                String price = new java.text.DecimalFormat("#.00").format(mission.getPrice());
                strArr[zindex][21] = price;
                strArr[zindex][22] = wltsj.getSettle()!=null?price:"";
                strArr[zindex][23] = wltsj.getCredit()!=null?price:"";
                strArr[zindex][24] = wltsj.getSettle()==null?price:"";
                strArr[zindex][25] = wltsj.getReport_send()!=null?"已传达":"";
                {
                    if(wltsj.getInvoiceNum()!=null){
                        Map<String,String> map = JSON.parseObject(wltsj.getInvoiceNum(),HashMap.class);
                        strArr[zindex][26] = map.get("text");
                    }else{
                        strArr[zindex][26] = "";
                    }
                }
                {
                    if(wltsj.getSettle_remark()!=null){
                        Map<String,String> map = JSON.parseObject(wltsj.getSettle_remark(),HashMap.class);
                        strArr[zindex][27] = map.get("text");
                    }else{
                        strArr[zindex][27] = "";
                    }
                }
                {
                    if(wltsj.getPhone_visit()!=null){
                        Map<String,String> map = JSON.parseObject(wltsj.getPhone_visit(),HashMap.class);
                        strArr[zindex][28] = map.get("text");
                    }else{
                        strArr[zindex][28] = "";
                    }
                }
                zindex++;
            }
        }else if("防坠器".equals(mission.getMtype())){

            for(Wltsj wltsj : wltsjs){
                strArr[zindex][0] = zindex+1+"";
                strArr[zindex][1] = wltsj.getBh();
                strArr[zindex][2] = mission.getDetect_company();
                strArr[zindex][3] = mission.getDetect_contact();
                strArr[zindex][4] = mission.getDetect_contact_number();
                strArr[zindex][5] = mission.getCreator();
                strArr[zindex][6] = wltsj.getWtdw();
                strArr[zindex][7] = wltsj.getName();
                strArr[zindex][8] = wltsj.getPhone();
                strArr[zindex][9] = wltsj.getReview()!=null?"已审核":"";
                strArr[zindex][10] = wltsj.getSeal()!=null?"已盖章":"";
                strArr[zindex][11] = mission.getNum() + "";
                String mPrice = new java.text.DecimalFormat("#.00").format(mission.getPrice()*mission.getNum());
                strArr[zindex][12] = mPrice;
                strArr[zindex][13] = new java.text.DecimalFormat("#.00").format(mission.getNum()*mission.getPrice());
                strArr[zindex][14] = wltsj.getSettle()!=null?mPrice:"";
                strArr[zindex][15] = wltsj.getCredit()!=null?mPrice:"";
                strArr[zindex][16] = wltsj.getSettle()==null?mPrice:"";
                strArr[zindex][17] = wltsj.getReport_send()!=null?"已传达":"";
                {
                    if(wltsj.getInvoiceNum()!=null){
                        Map<String,String> map = JSON.parseObject(wltsj.getInvoiceNum(),HashMap.class);
                        strArr[zindex][18] = map.get("text");
                    }else{
                        strArr[zindex][18] = "";
                    }
                }
                {
                    if(wltsj.getSettle_remark()!=null){
                        Map<String,String> map = JSON.parseObject(wltsj.getSettle_remark(),HashMap.class);
                        strArr[zindex][19] = map.get("text");
                    }else{
                        strArr[zindex][19] = "";
                    }
                }
                {
                    if(wltsj.getPhone_visit()!=null){
                        Map<String,String> map = JSON.parseObject(wltsj.getPhone_visit(),HashMap.class);
                        strArr[zindex][20] = map.get("text");
                    }else{
                        strArr[zindex][20] = "";
                    }
                }
                zindex++;
            }
        }else{
            Wltsj wltsj1 = wltsjs.get(0);
            strArr[zindex][0] = zindex+1+"";
            strArr[zindex][1] = mission.getDetect_company();
            strArr[zindex][2] = mission.getDetect_contact();
            strArr[zindex][3] = mission.getDetect_contact_number();
            strArr[zindex][4] = mission.getCreator();
            strArr[zindex][5] = wltsj1.getBh();
            if(wltsjs.size()==2){
                strArr[zindex][5] += "\r\n" + wltsjs.get(1).getBh();
            }
            strArr[zindex][6] = wltsj1.getWtdw();
            strArr[zindex][7] = wltsj1.getName();
            strArr[zindex][8] = wltsj1.getPhone();
            strArr[zindex][9] = wltsj1.getGcmc();
            strArr[zindex][10] = wltsj1.getGcdz();
            strArr[zindex][11] = wltsj1.getReview()!=null?"已审核":"";
            strArr[zindex][12] = wltsj1.getSeal()!=null?"已盖章":"";
            String mPrice = "";
            {
                double price = mission.getCount();
                mPrice = price + "";
            }
//            mPrice = new java.text.DecimalFormat("#.00").format(mission.getPrice());
            System.out.println("mppp:"+mPrice);
            strArr[zindex][13] = mPrice;
            strArr[zindex][14] = wltsj1.getSettle()!=null?mPrice:"";
            strArr[zindex][15] = wltsj1.getCredit()!=null?mPrice:"";
            strArr[zindex][16] = wltsj1.getSettle()==null?mPrice:"";
            strArr[zindex][17] = wltsj1.getReport_send()!=null?"已传达":"";
            {
                if(wltsj1.getPhone_visit()!=null){
                    Map<String,String> map = JSON.parseObject(wltsj1.getPhone_visit(),HashMap.class);
                    strArr[zindex][18] = map.get("text");
                }else{
                    strArr[zindex][18] = "";
                }
            }
//            {
//                if(wltsj.getInvoiceNum()!=null){
//                    Map<String,String> map = JSON.parseObject(wltsj.getInvoiceNum(),HashMap.class);
//                    strArr[zindex][18] = map.get("text");
//                }else{
//                    strArr[zindex][18] = "";
//                }
//            }
//            {
//                if(wltsj.getSettle_remark()!=null){
//                    Map<String,String> map = JSON.parseObject(wltsj.getSettle_remark(),HashMap.class);
//                    strArr[zindex][19] = map.get("text");
//                }else{
//                    strArr[zindex][19] = "";
//                }
//            }
        }

        return strArr;
    }

    private Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Override
    public HSSFWorkbook getMTypeReportFile(String date,String pwd) {
        System.out.println("dada---"+date);
        User user = userService.selUserByPwd(pwd);
        if(user==null||user.getStatus().indexOf("11")==-1){
            return null;
        }
        this.date = date;
        String[] tJTitle = {"序号","委托单编号","检测费负责单位","检测费负责人","负责人电话","下单人","使用单位","安装单位","产权单位","委托单位","送样人","工程名称","工程地址","备案编号","出厂编号","设备型号","检验员","检验备注","合格","审核","盖章","检测费","已收款","赊账","未收款","报告发放","发票号码","备注","电话回访"};
        String[] fzqTitle = {"序号","委托单编号","检测费负责单位","检测费负责人","负责人电话","下单人","委托单位","送样人","联系电话","审核","盖章","数量","单价","检测费(总计)","已收款","赊账","未收款","报告发放","发票号码","备注","电话回访"};
        String[] sbTitle = {"序号","检测费负责单位","检测费负责人","负责人电话","下单人","委托单编号","委托单位","送样人","联系电话","工程名称","工程地址","审核","盖章","检测费","已收款","赊账","未收款","报告发放","发票号码","备注","电话回访"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Date date1 = null;
        try {
            date1 = lastDayOfMonth(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(date1).split("-")[2];
        String less = date+"-"+lastDay;
        String greater = date+"-"+1;
        Map<String,String[][]> map = getContent1(missionService.getMissions(less,greater),tJTitle.length);
        System.out.println("----");
        HSSFWorkbook wb = null;
        for(String key : map.keySet()){
            if("塔吊,升降机,井架,吊篮".indexOf(key)!=-1){
                wb = ExcelUtil.getHSSFWorkbook(key,tJTitle,map.get(key),wb,date);
            }else if("防坠器".equals(key)){
                wb = ExcelUtil.getHSSFWorkbook(key,fzqTitle,map.get(key),wb,date);
            }else{
                wb = ExcelUtil.getHSSFWorkbook(key,sbTitle,map.get(key),wb,date);
            }

        }
        return wb;
    }

    @Override
    public Date getSearchFile(List list) {
        final Date now_date = new Date();
        List file_list = new ArrayList();
        log.info(list.size());
        for(int i=0 ; i<list.size(); i++){
            FileAttr fileAttr = (FileAttr)list.get(i);
            String filename = fileAttr.getFilename();
            String date = fileAttr.getDate();
            BufferedReader bfr = null;
            try{
                bfr = new BufferedReader(new FileReader(FileWord.file_path.replaceAll("classes","cache")+date+"/"+filename+"/"+filename+".txt"));
                String line = null;
                String text = "";
                while((line=bfr.readLine())!=null){
                    text += line;
                }
                Map attr_map = (Map)JSONObject.parse(text);
                if(attr_map.get("date")==null){
                    attr_map.put("date",date);
                }
                file_list.add(attr_map);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
        String[] title = {"序号","任务日期","任务单编号","委托单编号","样品名称","样品编号","产值","委托单位","联系人","联系电话","安装单位","使用单位","工地名称","工程地址","产权单位","设备型号","备案编号","出厂编号","已盖章","已收款","设备高度","检验日期","签发日期","检验类别","检验结论","检验员","备注","电话回访"};

        String fileName = "lala报表.xls";

        String sheetName = "报表";
        String[][] content = getContent(file_list,title.length);
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName,title,content,null,null);
        FileOutputStream output = null;
        try{
            output = new FileOutputStream(FileWord.file_path.replaceAll("classes","xls_c")+now_date.getTime()+".xls");
            wb.write(output);
            output.flush();
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            if(output!=null){
                try {
                    output.close();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            log.info("已执行,删除结果为：");
                            log.info(FileWord.del(new File(FileWord.file_path.replaceAll("classes","xls_c")+now_date.getTime()+".xls")));
                        }
                    },18000);
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
        return now_date;
    }

    private int wtbhSub(String wtbh){

        try{
            return Integer.parseInt(wtbh.toLowerCase().replaceAll("[^0-9]",""));
        }catch (Exception e){
//            e.printStackTrace();
        }
        return 0;
    }

    private Map<String,String[][]> getEarningsReportMap(String date){

        List<Wltsj> wltsjs = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Date date1 = null;
        try {
            date1 = lastDayOfMonth(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(date1).split("-")[2];
        String less = date+"-"+lastDay;
        String greater = date+"-"+1;
        List<Mission> missions = missionService.getMissions(less,greater);


        for(Mission mission : missions){
            for(Wltsj wltsj : mission.getWltsjs()){
                String settle = wltsj.getSettle();
                if(settle==null)continue;
                Map<String,String> map = JSON.parseObject(settle,HashMap.class);
                String exTime = sdf.format(map.get("execution_time"));
                if(exTime.equals(date)){
                    String type = wltsj.getMtype();
                    if(type.indexOf("三宝")!=-1){
                        wltsj.setPrice((mission.getCount()+0.0)/mission.getWltsjs().size());
                    }else{
                        wltsj.setPrice(mission.getPrice());
                    }
                    if("防坠器".equals(type)){
                        wltsj.setNum(mission.getNum());
                    }
                    wltsjs.add(wltsj);
                }
            }
        }


        Map<String,List<Wltsj>> map = sortingWltsjByBho(wltsjs);
        Map<String,String[][]> resultMap = new HashMap<>();

        int[] indexArr = {0,0,0,0,0,0};

        for(String key : map.keySet()){
            int index = 0;
            String[][] tempArr = new String[999][9];
            switch(key){
                case "塔吊":
                {
                    index = 0;
                }
                break;
                case "升降机":
                {
                    index = 1;
                }
                break;
                case "井架":
                {
                    index = 2;
                }
                break;
                case "吊篮":
                {
                    index = 3;
                }
                break;
                case "防坠器":
                {
                    index = 4;
                }
                break;
                case "三宝钢管扣件":
                {
                    index = 5;
                }
                break;
            }
            for(Wltsj wltsj : map.get(key)){
                tempArr[indexArr[index]++] = makeEarningsReportArr(wltsj,indexArr[index]+1);
            }
            resultMap.put(key,tempArr);
        }

        return resultMap;
    }

    private String[] makeEarningsReportArr(Wltsj wltsj,int index){
        String[] strs = {};
        switch(wltsj.getMtype()){
            case "塔吊":
            case "升降机":
            case "井架":
                strs = new String[9];
                strs[1] = wltsj.getBh();
                strs[2] = wltsj.getBabh();
                strs[3] = wltsj.getWtdw();
                strs[4] = wltsj.getGcmc();
                strs[5] = "";
                strs[6] = wltsj.getPrice()+"";
                {
                    String jsonStr = wltsj.getInvoiceNum();
                    if(jsonStr!=null){
                        try {
                            Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                            strs[7] = map.get("text");
                        }catch (Exception e){
                            strs[7] = "";
                        }
                    }
                }
                {
                    String jsonStr = wltsj.getSettle_remark();
                    if(jsonStr!=null){
                        try {
                            Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                            strs[8] = map.get("text");
                        }catch (Exception e){
                            strs[8] = "";
                        }
                    }
                }
                break;
            case "吊篮":
                strs = new String[8];
                strs[1] = wltsj.getBh();
                strs[2] = wltsj.getWtdw();
                strs[3] = wltsj.getGcmc();
                strs[4] = "";
                strs[5] = wltsj.getPrice()+"";
                {
                    String jsonStr = wltsj.getInvoiceNum();
                    try {
                        Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                        strs[6] = map.get("text");
                    }catch (Exception e){
                        strs[6] = "";
                    }
                }
                {
                    String jsonStr = wltsj.getSettle_remark();
                    try {
                        Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                        strs[7] = map.get("text");
                    }catch (Exception e){
                        strs[7] = "";
                    }
                }
                break;
            case "防坠器":
                strs = new String[7];
                strs[1] = wltsj.getBh();
                strs[2] = wltsj.getWtdw();
                strs[3] = wltsj.getNum()+"";
                strs[4] = wltsj.getPrice()+"";
                {
                    String jsonStr = wltsj.getInvoiceNum();
                    try {
                        Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                        strs[5] = map.get("text");
                    }catch (Exception e){
                        strs[5] = "";
                    }
                }
                {
                    String jsonStr = wltsj.getSettle_remark();
                    try {
                        Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                        strs[6] = map.get("text");
                    }catch (Exception e){
                        strs[6] = "";
                    }
                }
                break;
            case "三宝钢管扣件":
                strs = new String[8];
                strs[1] = wltsj.getBh();
                strs[2] = wltsj.getWtdw();
                strs[3] = wltsj.getGcmc();
                strs[4] = wltsj.getRemark().substring(0,wltsj.getRemark().lastIndexOf(","));
                {
                    String jsonStr = wltsj.getInvoiceNum();
                    try {
                        Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                        strs[5] = map.get("text");
                    }catch (Exception e){
                        strs[5] = "";
                    }
                }
                strs[6] = wltsj.getPrice()+"";
                {
                    String jsonStr = wltsj.getSettle_remark();
                    try {
                        Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                        strs[7] = map.get("text");
                    }catch (Exception e){
                        strs[7] = "";
                    }
                }
                break;
        }
        strs[0] = index+"";
        return strs;
    }


    private String[] makeAcceptsReportArr(Wltsj wltsj,int index){
        String[] strs = {};
        switch(wltsj.getMtype()){
            case "塔吊":
            case "升降机":
            case "井架":
                strs = new String[9];
                strs[1] = wltsj.getBh();
                strs[2] = wltsj.getBabh();
                strs[3] = wltsj.getWtdw();
                strs[4] = wltsj.getGcmc();
                strs[5] = "";
                strs[6] = wltsj.getPrice()+"";
            {
                String jsonStr = wltsj.getSettle_remark();
                if(jsonStr!=null){
                    try {
                        Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                        strs[7] = map.get("text");
                    }catch (Exception e){
                        strs[7] = "";
                    }
                }
            }
            break;
            case "吊篮":
                strs = new String[8];
                strs[1] = wltsj.getBh();
                strs[2] = wltsj.getWtdw();
                strs[3] = wltsj.getGcmc();
                strs[4] = "";
                strs[5] = wltsj.getPrice()+"";
            {
                String jsonStr = wltsj.getSettle_remark();
                try {
                    Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                    strs[6] = map.get("text");
                }catch (Exception e){
                    strs[6] = "";
                }
            }
            break;
            case "防坠器":
                strs = new String[9];
                strs[1] = wltsj.getBh();
                strs[2] = "";
                strs[3] = wltsj.getWtdw();
                strs[4] = "";
                strs[5] = "";
                strs[6] = wltsj.getNum()+"";
                strs[7] = wltsj.getPrice()+"";
            {
                String jsonStr = wltsj.getSettle_remark();
                try {
                    Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                    strs[8] = map.get("text");
                }catch (Exception e){
                    strs[8] = "";
                }
            }
            break;
            case "三宝钢管扣件":
                strs = new String[8];
                strs[1] = wltsj.getBh();
                strs[2] = wltsj.getWtdw();
                strs[3] = wltsj.getGcmc();
                strs[4] = wltsj.getRemark().substring(0,wltsj.getRemark().lastIndexOf(","));
                strs[5] = wltsj.getPrice()+"";
            {
                String jsonStr = wltsj.getSettle_remark();
                try {
                    Map<String,String> map = JSON.parseObject(jsonStr,Map.class);
                    strs[6] = map.get("text");
                }catch (Exception e){
                    strs[6] = "";
                }
            }
            break;
        }
        strs[0] = index+"";
        boolean isNull = true;
        for(int i = 0 ; i < strs.length ; i++){

            if(strs[i]!=null&&strs[i]!="null"){
                isNull = false;
                break;
            }

        }
        if(isNull)return null;
        return strs;
    }

    private Map<String,List<Wltsj>> sortingWltsjByBho(List<Wltsj> wltsjs){

        Map<String,List<Wltsj>> map = new HashMap<>();

        for(Wltsj wltsj : wltsjs){
            String mtype = wltsj.getMtype();
            List<Wltsj> list = map.get(mtype);
            if(list==null)list = new ArrayList<>();
            list.add(wltsj);
            map.put(mtype,list);
        }

        for(String key : map.keySet()){

            Map<String,List<Wltsj>> cityMap = new HashMap<>();
            for(Wltsj wltsj : map.get(key)){
                try{
                    String bho = wltsj.getBh().replaceAll(" ","").substring(2,4);
                    List<Wltsj> cityList = cityMap.get(bho);
                    if(cityList==null) cityList = new ArrayList<>();
                    cityList.add(wltsj);
                    cityMap.put(bho,cityList);
                }catch (Exception e){
                    List<Wltsj> cityList = cityMap.get("err1");
                    if(cityList==null) cityList = new ArrayList<>();
                    cityList.add(wltsj);
                    cityMap.put("err1",cityList);
                }

            }

            List<Wltsj> vals = new ArrayList<>();
            for(String cityKey : cityMap.keySet()){

                List<Wltsj> cityList = cityMap.get(cityKey);
                Collections.sort(cityList, new Comparator<Wltsj>() {

                    @Override
                    public int compare(Wltsj o1, Wltsj o2) {
                        try {
                            return wtbhSub(o1.getBh()) - wtbhSub(o2.getBh());
                        }catch (Exception e){
                            return 0;
                        }
                    }
                });
                vals.addAll(cityList);
            }
            map.put(key,vals);
        }

        return map;
    }


    private Map<String,String[][]> getAcceptReportMap(String date){

        List<Wltsj> wltsjs = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Date date1 = null;
        try {
            date1 = lastDayOfMonth(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(date1).split("-")[2];
        String less = date+"-"+lastDay;
        String greater = date+"-"+1;
        List<Mission> missions = missionService.getMissions(less,greater);


        for(Mission mission : missions){
            String time = sdf.format(mission.getCreate_time());
            if(!date.equals(time)){
                continue;
            }
            for(Wltsj wltsj : mission.getWltsjs()){
                String type = wltsj.getMtype();
                if(type.indexOf("三宝")!=-1){
                    wltsj.setPrice((mission.getCount()+0.0)/mission.getWltsjs().size());
                }else{
                    wltsj.setPrice(mission.getPrice());
                }
                if("防坠器".equals(type)){
                    wltsj.setNum(mission.getNum());
                }
                wltsjs.add(wltsj);
            }
        }

        Map<String,List<Wltsj>> map = sortingWltsjByBho(wltsjs);
        Map<String,String[][]> resultMap = new HashMap<>();

        int[] indexArr = {0,0,0,0,0,0};

        for(String key : map.keySet()){
            int index = 0;
            String[][] tempArr = new String[400][9];
            switch(key){
                case "塔吊":
                {
                    index = 0;
                }
                break;
                case "升降机":
                {
                    index = 1;
                }
                break;
                case "井架":
                {
                    index = 2;
                }
                break;
                case "吊篮":
                {
                    index = 3;
                }
                break;
                case "防坠器":
                {
                    index = 4;
                }
                break;
                case "三宝钢管扣件":
                {
                    index = 5;
                }
                break;
            }
            int count = map.get(key).size();
            for(Wltsj wltsj : map.get(key)){
                String[] strArr = makeAcceptsReportArr(wltsj,indexArr[index]+1);
//                System.out.println(strArr);
                if(strArr!=null){
                    tempArr[indexArr[index]++] = strArr;
                }
            }
            resultMap.put(key,tempArr);
            System.out.println("cc:"+count);
        }

        return resultMap;
    }



    //受理汇总
    @Override
    public HSSFWorkbook getAcceptReportFile(String date, String pwd) {

        User user = userService.selUserByPwd(pwd);
        if(user==null||user.getStatus().indexOf("11")==-1){
            return null;
        }

        Map<String,String[][]> acceptMap = getAcceptReportMap(date);
        System.out.println("size::"+acceptMap.size());
        System.out.println(JSON.toJSONString(acceptMap));
        HSSFWorkbook hssfWorkbook = ExcelUtil.getAcceptReport("塔吊",new String[]{"序号","受托编号","备案编号","委托单位","工程名称","检验类别","检测费","备注"},acceptMap.get("塔吊"),null,date);
        hssfWorkbook = ExcelUtil.getAcceptReport("施工梯",new String[]{"序号","受托编号","备案编号","委托单位","工程名称","检验类别","检测费","备注"},acceptMap.get("升降机"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getAcceptReport("井架",new String[]{"序号","受托编号","备案编号","委托单位","工程名称","检验类别","检测费","备注"},acceptMap.get("井架"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getAcceptReport("吊篮",new String[]{"序号","受托编号","委托单位","工程名称","检验类别","检测费","备注"},acceptMap.get("吊篮"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getAcceptReport("防坠器",new String[]{"序号","委托编号","样品编号","委托单位","生产厂家","出厂编号","数量","金额（元）","备注"},acceptMap.get("防坠器"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getAcceptReport("三宝钢管扣件",new String[]{"序号","委托编号","委托单位","工程名称","样品名称","检测费(元)","备注"},acceptMap.get("三宝钢管扣件"),hssfWorkbook,date);
        return hssfWorkbook;

    }
    //收入汇总
    @Override
    public HSSFWorkbook getEarningsReportFile(String date, String pwd) {

        User user = userService.selUserByPwd(pwd);
        if(user==null||user.getStatus().indexOf("11")==-1){
            return null;
        }

        Map<String,String[][]> earningMap = getEarningsReportMap(date);
        HSSFWorkbook hssfWorkbook = ExcelUtil.getEarningsReport("塔吊",new String[]{"序号","受托编号","备案编号","委托单位","工程名称","检验类别","金额","发票号码","备注"},earningMap.get("塔吊"),null,date);
        hssfWorkbook = ExcelUtil.getEarningsReport("施工梯",new String[]{"序号","受托编号","备案编号","委托单位","工程名称","检验类别","金额","发票号码","备注"},earningMap.get("升降机"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getEarningsReport("井架",new String[]{"序号","受托编号","备案编号","委托单位","工程名称","检验类别","金额","发票号码","备注"},earningMap.get("井架"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getEarningsReport("吊篮",new String[]{"序号","受托编号","委托单位","工程名称","检验类别","金额","发票号码","备注"},earningMap.get("吊篮"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getEarningsReport("防坠器",new String[]{"序号","受托编号","委托单位","数量(台)","金额","发票号码","备注"},earningMap.get("防坠器"),hssfWorkbook,date);
        hssfWorkbook = ExcelUtil.getEarningsReport("三宝钢管扣件",new String[]{"序号","委托编号","委托单位","工程名称","样品名称","发票号码","检测费(元)","备注"},earningMap.get("三宝钢管扣件"),hssfWorkbook,date);
        return hssfWorkbook;
    }

    /**
     *  把展示的数据整理成 一个二维数组
     * */
    private String[][] getContent(List<Mission> list,int len){
        String[][] content = new String[999][len];
        int index = -1;
        Map<String,Map<String,Integer>> map = getMonthDataCount();
        System.out.println("map--");
        System.out.println(JSON.toJSONString(map));
        String[] typeArr = {"塔吊","升降机","井架","吊篮","防坠器","三宝钢管扣件"};
        for(String group : map.keySet()){
            Map<String,Integer> countMap = map.get(group);
            content[++index][0] = group;
            for(int i = 0 ;i<typeArr.length;i++){
                Integer typeNum = countMap.get(typeArr[i]);
                content[index][i+1] = typeNum==null?"0":typeNum.toString();
            }
        }

        return content;
    }

    private Map<String,Map<String,Integer>> getMonthDataCount(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Date date1 = null;
        try {
            date1 = lastDayOfMonth(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(date1).split("-")[2];
        String less = date+"-"+lastDay;
        String greater = date+"-"+1;
        List<Mission> missions = missionService.getMissions(less,greater);
        Map<String,Map<String,Integer>> mapCount = new HashMap();
        for(Mission mis : missions){
            String group = mis.getExecutive();
            if(group!=null&&!"".equals(group)&&new SimpleDateFormat("yyyy-MM-dd").format(mis.getCreate_time()).indexOf(this.date)!=-1){
                String mtype = mis.getMtype();
                Map<String,Integer> mTypeCount = mapCount.get(group);
                Integer count = mis.getWltsjs().size();
                if(mTypeCount==null){
                    mTypeCount = new HashMap();
                }else{
                    Integer countNum = mTypeCount.get(mtype);
                    if(countNum==null){
                        countNum = 0;
                    }
                    count = countNum + mis.getWltsjs().size();
                }
                mTypeCount.put(mtype,count);
                mapCount.put(group,mTypeCount);
            }
        }
        return mapCount;
    }

    /**
 *
 * */
    private List getMonthData(String date){
        List list = new ArrayList();
        List<Wltsj> wltsjs = JSON.parseArray(FileWord.readJson("fileAttr"),Wltsj.class);
        for(Wltsj wltsj : wltsjs){
            Date create_time = wltsj.getCreateAt();
            String create_time_str = new SimpleDateFormat("yyyy-MM-dd").format(create_time);
            log.info("createTime--"+create_time_str);
            if(create_time_str.indexOf(date)!=-1){
                Map map = JSON.parseObject(JSON.toJSONString(wltsj),Map.class);
                list.add(map);
            }
        }
        return list;
    }

    class MonthFilesColler implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Map map1 = (Map)o1;
            Map map2 = (Map)o2;
            Long unit1 = (long)0;
            Long unit2 = (long)0;
            if(map1.get("work_num")==null){
                 return 1;
            }
            if(map2.get("work_num")==null){
                return -1;
            }
//            unit1 = Long.parseLong(map1.get("work_num").toString());
//            unit2 = Long.parseLong(map2.get("work_num").toString());
            if(unit1>unit2){
                return 1;
            }
            return -1;
        }
    }

    class DateFilesColler implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            Map map1 = (Map)o1;
            Map map2 = (Map)o2;
            long date1 = Long.parseLong(map1.get("date").toString().replaceAll("-",""));
            long date2 = Long.parseLong(map2.get("date").toString().replaceAll("-",""));
            if(date1>date2){
                return -1;
            }
            return 1;
        }
    }

}

