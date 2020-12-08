package com.luchuang.fileImport.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.luchuang.common.UUID;
import com.luchuang.fileImport.mapper.MissionMapper;
import com.luchuang.fileImport.mapper.WltsjMapper;
import com.luchuang.fileImport.pojo.Mission;
import com.luchuang.fileImport.pojo.PostGroup;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.pojo.Wltsj;
import com.luchuang.fileImport.service.IFileWordService;
import com.luchuang.fileImport.service.MissionService;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.util.Export;
import com.luchuang.fileImport.util.FileWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version 1.0
 * @ClassName MissionServiceImpl
 * @Author PPPL
 * @Date 2019/8/1 10:22
 **/

@Service
public class MissionServiceImpl implements MissionService {

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WltsjMapper wltsjMapper;

    @Autowired
    private IFileWordService iFileWordService;

    private List<Mission> getJson(){

        List<Mission> missions = missionMapper.selectAll();

        return missions;
    }

    private int getWorkNum(){
        String date = new SimpleDateFormat("yyyy-MM").format(new Date()).replaceAll("-","");
        List<Wltsj> wltsjs = wltsjMapper.selectAll();
        int num = Integer.parseInt(date+"000");
        for(Wltsj wltsj : wltsjs){
            String createTime = new SimpleDateFormat("yyyy-MM-dd").format(wltsj.getCreateAt()).replaceAll("-","");
            if(createTime.indexOf(date)!=-1){
                String wnum = wltsj.getWork_num();
                if("".equals(wnum)){
                    wnum = "0";
                }
                int wlWordNum = Integer.parseInt(wnum);
                if(num < wlWordNum){
                    num = wlWordNum;
                }
            }
        }
        return num;
    }

    private void createLog(Mission mission){
//        List<Mission> missions = JSON.parseArray(FileWord.readJson("mission"),Mission.class);

//        int workNum = getWorkNum();
//        for(int i = 0; i<mission.getNum();i++){
//            Wltsj wltsj = new Wltsj();
//            wltsj.setMtype(mission.getMtype());
//            wltsj.setFilename(mission.getId() +"-"+mission.getMtype()+i);
//            wltsj.setPrice(mission.getPrice());
//            wltsj.setMission_id(mission.getId());
//            wltsj.setWork_num(++workNum + "");
//            Export export = new Export(wltsj);
//            export.createLog();
//        }
    }

    @Override
    public List<Mission> getMissions(String search) {
        List<Mission> list = new ArrayList();
        Set<Mission> set = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Example miex = new Example(Mission.class);

        miex.createCriteria().andLike("mtype","%"+search+"%");
                miex.or().andLike("Executive","%"+search+"%");
        miex.or().andLike("creator","%"+search+"%");
        miex.or().andLike("create_time","%"+search+"%");
        miex.or().andLike("price","%"+search+"%");
        miex.or().andLike("project_name","%"+search+"%");
        List<Mission> missions = missionMapper.selectByExample(miex);
        for(Mission mission : missions){

            Example example = new Example(Wltsj.class);
            example.createCriteria().andEqualTo("mission_id",mission.getId());
            mission.setWltsjs(wltsjMapper.selectByExample(example));
            set.add(mission);
        }
        {
            Example wlex = new Example(Wltsj.class);

            wlex.createCriteria().andLike("bh","%"+search+"%");
            wlex.or().andLike("wtdw","%"+search+"%");
            wlex.or().andLike("phone","%"+search+"%");
            wlex.or().andLike("name","%"+search+"%");
            wlex.or().andLike("sydw","%"+search+"%");
            wlex.or().andLike("azdw","%"+search+"%");
            wlex.or().andLike("gcmc","%"+search+"%");
            wlex.or().andLike("gcdz","%"+search+"%");
            wlex.or().andLike("sbxh","%"+search+"%");
            wlex.or().andLike("babh","%"+search+"%");
            wlex.or().andLike("sccj","%"+search+"%");
            wlex.or().andLike("ccbh","%"+search+"%");
            wlex.or().andLike("ccri","%"+search+"%");
            wlex.or().andLike("filename","%"+search+"%");
            wlex.or().andLike("mtype","%"+search+"%");
            wlex.or().andLike("date","%"+search+"%");
            wlex.or().andLike("createAt","%"+search+"%");
            List<Wltsj> wltsjs = wltsjMapper.selectByExample(wlex);
            for(Wltsj wltsj : wltsjs){

                Mission mission = missionMapper.selectByPrimaryKey(wltsj.getMission_id());
                Example example = new Example(Wltsj.class);
                example.createCriteria().andEqualTo("mission_id",mission.getId());
                mission.setWltsjs(wltsjMapper.selectByExample(example));
                set.add(mission);
            }
        }
        return new ArrayList<>(set);
    }

    private void editMissionJson(Mission mission){

//        missionMapper.insert(mission);

        if("防坠器".equals(mission.getMtype())){
            Wltsj wltsj = new Wltsj();
            wltsj.setMtype(mission.getMtype());
            wltsj.setFilename(mission.getId() +"-"+mission.getMtype());
            wltsj.setPrice(mission.getPrice());
            wltsj.setMission_id(mission.getId().toString());
            wltsj.setNum(mission.getNum());
            wltsjMapper.insert(wltsj);

        }else if("三宝钢管扣件".indexOf(mission.getMtype())==-1){
            for(int i = 0; i<mission.getNum();i++){
                Wltsj wltsj = new Wltsj();
                wltsj.setMtype(mission.getMtype());
                wltsj.setFilename(mission.getId() +"-"+mission.getMtype()+i);
                wltsj.setPrice(mission.getPrice());
                wltsj.setMission_id(mission.getId().toString());
                wltsjMapper.insert(wltsj);
            }
        }


    }

    @Override
    public int addMission(Mission mission) {

        User user = userService.selUserByPwd(mission.getPwd());

        mission.setPwd("");
        mission.setCreator(user.getName());
        try{
            if(!user.hasRule("add")){
                return 0;
            }
        }catch (NullPointerException e){
            return 0;
        }
        String mType = mission.getMtype();
        if("防坠器,三宝钢管扣件".indexOf(mType)!=-1){
//            createLog(mission);
            mission.setCategory("Auto");
            mission.setExecutive("实验室");

            if("三宝钢管扣件".equals(mType)){
                String[] typelist = mission.getType_list().split(",");
                boolean flag1 = false,
                        flag2 = false;
                StringBuilder remark = new StringBuilder(),
                        remark1 = new StringBuilder();
                double price = mission.getCount();
                mission.setNum(1);
                for(String str :typelist){
                    if("安全帽,安全网,安全带".indexOf(str)!=-1){
                        remark.append(str).append(",");
                        flag1 = true;
                    }
                    if("直角扣件,旋转扣件,对接扣件,钢管".indexOf(str)!=-1){
                        remark1.append(str).append(",");
                        flag2 = true;
                    }
                }
                if(flag1&&flag2){
                    price /= 2;
                    mission.setNum(2);
                }

                missionMapper.insert(mission);
                System.out.println("0000----");
                System.out.println(JSON.toJSONString(mission));

                if(flag1){
                    Wltsj wl = new Wltsj();
                    wl.setFilename("三宝");
                    remark.append("三宝");
                    wl.setRemark(remark.toString());
                    wl.setMission_id(mission.getId().toString());
                    wl.setPrice(price);
                    wl.setMtype(mission.getMtype());
                    wltsjMapper.insert(wl);
                }
                if(flag2){
                    Wltsj wl = new Wltsj();
                    wl.setMission_id(mission.getId().toString());
                    wl.setFilename("钢管");
                    remark1.append("钢-管");
                    wl.setRemark(remark1.toString());
                    wl.setPrice(price);
                    wl.setMtype(mission.getMtype());
                    wltsjMapper.insert(wl);
                }
            }

        }else{
            mission.setExecutive(null);
            missionMapper.insert(mission);
        }
//        List<Mission> missions = getJson();
//        missions.add(mission);
        editMissionJson(mission);
//        FileWord.write("",JSON.toJSONString(missions));
        return 1;
    }

    @Override
    public List<Mission> getMissions(String less,String greater) {

        List<Mission> missions;

        if(less!=null){

            Example example = new Example(Mission.class);
            example.createCriteria().andGreaterThanOrEqualTo("create_time",greater)
                    .andLessThanOrEqualTo("create_time",less);
//                    .andBetween("create_time",greater,less);
            missions = missionMapper.selectByExample(example);
        }else{
            missions = missionMapper.selectAll();
        }
        Collections.sort(missions, new Comparator<Mission>() {
            @Override
            public int compare(Mission o1, Mission o2) {

                return  (o2.getCreate_time().getTime()-o1.getCreate_time().getTime())>0?1:-1;
            }
        });
        if(less!=null){
            Map map = new HashMap<>();
            map.put("less",less);
            map.put("greater",greater);
            buildMissions(missions,map);
        }else{
            buildMissions(missions);
        }

        return missions;
    }

    private List<Mission> buildMissions(List<Mission> missions,Map dateMap){

        {
//            StringBuilder ids = new StringBuilder();
//            Map<String,Mission> missionMap = new HashMap();
//            for(int i = 0 ; i < missions.size() ; i++) {
//                Mission mission = missions.get(i);
//                ids.append(mission.getId());
//                ids.append(",");
//                missionMap.put(mission.getId(),mission);
//            }

            missions = new ArrayList<>(missions);

            for(Mission mission : missions){
                Example example = new Example(Wltsj.class);
                example.createCriteria().andEqualTo("mission_id",mission.getId());
                List<Wltsj> wltsjs = wltsjMapper.selectByExample(example);
                Collections.sort(wltsjs, new Comparator<Wltsj>() {
                    @Override
                    public int compare(Wltsj o1, Wltsj o2) {
                        return Integer.parseInt(o2.getCreateAt().getTime()-o1.getCreateAt().getTime()+"");
                    }
                });
                mission.setWltsjs(wltsjs);
            }



        }
        return missions;
    }

    private List<Mission> buildMissions(List<Mission> missions){

        {
            missions = new ArrayList<>(missions);

            for(Mission mission : missions){
                Example example = new Example(Wltsj.class);
                example.createCriteria().andEqualTo("mission_id",mission.getId());
                List<Wltsj> wltsjs = wltsjMapper.selectByExample(example);
                Collections.sort(wltsjs, new Comparator<Wltsj>() {
                    @Override
                    public int compare(Wltsj o1, Wltsj o2) {
                        return Integer.parseInt(o2.getCreateAt().getTime()-o1.getCreateAt().getTime()+"");
                    }
                });
                mission.setWltsjs(wltsjs);
            }
        }
        return missions;
    }

    private String[] arrDel(String[] arr,int index){
        String[] temp_arr = new String[arr.length-1];
        for(int i = 0 ;i < arr.length - 1;i++){
            if( i < index){
                temp_arr[i] = arr[i];
            }else{
                temp_arr[i] = arr[i+1];
            }
        }
        return temp_arr;
    }

    @Override
    public int postRandomGroup(PostGroup postGroup) {
        String pwd = postGroup.getPwd();
        List<User> users = JSON.parseArray(FileWord.readJson("user"),User.class);
        if("".equals("pwd")||pwd==null){
            /**
             *  没有找到密码对应的名字和权限
             * */
            return 0;
        }
        {
            boolean flag = true;
            for(User user : users){
                if(pwd.equals(user.getPwd())){
                    /**
                     * 没有权限
                     * */
                    if(user.hasRule("random")) {
                        flag = false;
                        break;
                    }
                }
            }
            if(flag)return 0;
        }

        String json = postGroup.getIds();
        /**
         *  任务种类 任务ids
         * */
        Map<String,String> map = JSON.parseObject(json, HashMap.class);
        String[] group_string = postGroup.getGroup().split(",");
        String[] randomArr = map.keySet().toArray(new String[0]);
        int len = randomArr.length;
        int group_len = group_string.length;
        String[] tempGroupString = group_string;
        Random random = new Random();
        Map<String,String> missionMap = new HashMap();
        for(int i = 0;i < len;i++){
            int mission_random_index = random.nextInt(len-i);
            int group_random_index = random.nextInt(tempGroupString.length);
            {
                String mission_random = randomArr[mission_random_index];
                String group_random = tempGroupString[group_random_index];

                /**
                 *  确定任务分配到小组
                 * */

                missionMap.put(mission_random,group_random);
            }


            /**
             *  删除随机到的group
             *
             * */
            randomArr = arrDel(randomArr,mission_random_index);
            tempGroupString = arrDel(tempGroupString,group_random_index);
            if(tempGroupString.length==0){
                tempGroupString = group_string;
            }
        }
        Map<String,String> valMap = new HashMap<>();
        {
            for(String key : map.keySet()){
                System.out.println("keyklk");
                System.out.println(key);
                System.out.println(JSON.toJSONString(map));
                String[] strs = map.get(key).toString().split(",");
                for(String str: strs){
                    valMap.put(str,key);
                }
            }
        }
        System.out.println("vvvvv=");
        System.out.println(JSON.toJSONString(valMap));
        {
            for(String key : valMap.keySet()){
                String num = valMap.get(key);
                Mission mis = missionMapper.selectByPrimaryKey(Long.valueOf(key));
                mis.setCategory(num);
                mis.setExecutive(missionMap.get(num));
                missionMapper.updateByPrimaryKeySelective(mis);
            }

        }
        return 1;
    }

    @Override
    public int delMission(String id,String pwd) {
        {
            if(pwd==null||"".equals(pwd)||id==null)return -1;
            User user = userService.selUserByPwd(pwd);
            System.out.println("pwd+++:"+pwd);
            if(user.getPwd().equals(pwd)){
                if(!user.hasRule("del")){
                    return 0;
                }
            }
        }
        missionMapper.deleteByPrimaryKey(id);
        Example wlE = new Example(Wltsj.class);
        wlE.createCriteria().andEqualTo("mission_id",id);
        Wltsj wltsj = wltsjMapper.selectByExample(wlE).get(0);
        wltsjMapper.deleteByExample(wlE);
        FileWord.delWordFile(wltsj.getDate(),wltsj.getFilename());
        return 1;
    }

    @Override
    public int appendGroup(String id, int num, String pwd, String group) {

        User user = userService.selUserByPwd(pwd);
        String[] grops = group.replaceAll("，",",").split(",");
        if(!user.hasRule("random"))return -1;
        Mission mis = missionMapper.selectByPrimaryKey(Long.valueOf(id));

        Random random = new Random();
        int num1 = mis.getNum();
        String[] tempGrops = grops;
        int misNum = Integer.parseInt(Math.round(num1/(num+1.0))+"");
        Example wlExample = new Example(Wltsj.class);
        wlExample.createCriteria().andEqualTo("mission_id",mis.getId().toString());
        List<Wltsj> wltsjs = wltsjMapper.selectByExample(wlExample);
        System.out.println("wlwlwl:size::"+wltsjs.size());
        for(int i = 0 ; i < num; i++){
            int randomNum = random.nextInt(tempGrops.length);
            try {
                Mission mission = (Mission) mis.clone();
                mission.setId(null);
                int shareNum = (i==0&&num==1)||num-i!=1?misNum:(num1-misNum*(i+1));
                mission.setNum(shareNum);
                mis.setNum(mis.getNum()-shareNum);
                mission.setExecutive(tempGrops[randomNum]);
                missionMapper.insert(mission);
                List<Wltsj> tempWltsjs = wltsjs.subList(0,shareNum);
                wltsjs = wltsjs.subList(shareNum,wltsjs.size());
                for(Wltsj wl : tempWltsjs){
                    //这里有问题 号码转存
                    wl.setMission_id(mission.getId().toString());
                    wltsjMapper.updateByPrimaryKey(wl);
                }
//                wltsjMapper.update(tempWltsjs);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            missionMapper.updateByPrimaryKey(mis);
            tempGrops = arrDel(tempGrops,randomNum);
            if(tempGrops.length==0){

                tempGrops = grops;
            }
        }
        System.out.println("修改过后的size："+wltsjs.size());
//                mis.setWltsjs(wltsjs);
//                saveMissionWltsj(missions);
        return 1;

    }

    @Override
    public int updateMission(Mission mission,String pwd) {
        User user = userService.selUserByPwd(pwd);
        try{
            if(!user.hasRule("random")){
                return -1;
            }
        }catch (NullPointerException e){
            return -1;
        }
        boolean flag = false;
        missionMapper.updateByPrimaryKeySelective(mission);
        return flag?1:0;
    }

    @Override
    public int editPrice(String id, String price, String pwd) {

        User user = userService.selUserByPwd(pwd);
        try{
            if(!user.hasRule("edit_price")){
                return -1;
            }
        }catch (NullPointerException e){
            return -1;
        }
        Mission mission = missionMapper.selectByPrimaryKey(id);

        if(id.equals(mission.getId())){
            switch(mission.getMtype()){
                case "塔吊" :
                case "升降机" :
                case "井架" :
                case "吊篮" :
                case "防坠器" : mission.setPrice(Double.parseDouble(price));break;
                case "三宝钢管扣件" : mission.setCount(Integer.parseInt(price));break;
            }
            missionMapper.updateByPrimaryKeySelective(mission);
            return 1;
        }


        return 0;
    }

    @Override
    public int fastOption(String text) {
        text = text.replaceAll("，",",");
        String[] texts = text.split(",");
        if(texts.length<4){
            return -1;
        }
        Map<String,String> map = new HashMap();
        map.put("A",texts[0]);
        map.put("B",texts[1]);
        map.put("C",texts[2]);
        map.put("D",texts[3]);
        FileWord.writeJson("optionBtn",JSON.toJSONString(map));
        return 1;
    }

    public void saveMissionWltsj(List<Mission> missions){
        List<Wltsj> wltsjs = new ArrayList<>();
        missionMapper.insertList(missions);

        for(Mission mis : missions){
            for(Wltsj wltsj : wltsjs){
                wltsj.setMission_id(mis.getId().toString());
            }
            wltsjs.addAll(mis.getWltsjs());
        }
        wltsjMapper.insertList(wltsjs);
    }

}
