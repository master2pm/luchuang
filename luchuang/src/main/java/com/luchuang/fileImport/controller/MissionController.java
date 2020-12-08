package com.luchuang.fileImport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.luchuang.common.ResultBean;
import com.luchuang.fileImport.pojo.*;
import com.luchuang.fileImport.service.MissionService;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.service.impl.UserServiceImpl;
import com.luchuang.fileImport.util.FileWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version 1.0
 * @ClassName MissionController
 * @Author PPPL
 * @Date 2019/8/1 10:17
 **/
@RestController
public class MissionController {

    @Autowired
    private MissionService missionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/mission/fastOption",method = RequestMethod.POST)
    public ResultBean fastOption(@RequestParam(value = "text")String text){
        int flag = missionService.fastOption(text);
        return new ResultBean(flag==-1?"输入有误":"操作成功~");
    }

    @RequestMapping(value = "/mission/executive",method = RequestMethod.POST)
    public ResultBean updateMission(@RequestBody Mission mission,@RequestParam(value = "pwd")String pwd){
        ResultBean resultBean = null;
        int flag = missionService.updateMission(mission,pwd);
        String msg = null;
        switch (flag){
            case -1 : msg = "密码错误或权限不够"; break;
            case 0 : msg = "修改失败"; break;
            case 1 : msg = "修改成功"; break;
        }
        resultBean = new ResultBean(msg);
        return resultBean;
    }

    @RequestMapping(value = "/mission/price",method = RequestMethod.POST)
    public ResultBean editPrice(@RequestParam(value = "id") String id,@RequestParam(value = "price") String price,@RequestParam(value = "pwd") String pwd){
        ResultBean resultBean = new ResultBean(Rule.getName(missionService.editPrice(id,price,pwd)));
        return resultBean;
    }

    @RequestMapping(value = "/mission",method = RequestMethod.POST)
    public ResultBean addMission(@RequestBody Mission mission){
        ResultBean resultBean = null;
        boolean flag = missionService.addMission(mission)!=0;
        resultBean = new ResultBean(flag);
        return resultBean;
    }

    @RequestMapping(value = "/mission",method = RequestMethod.GET)
    public ResultBean getMission(@RequestParam(value = "isGroup",required = false,defaultValue = "false")Boolean isGroup,@RequestParam(value = "date",required = false)String date,@RequestParam(value = "sort",required = false)String sort,@RequestParam(value = "search",required = false)String search,@RequestParam(value = "pwd",required = false)String pwd)throws Exception{
        ResultBean resultBean = null;
        Boolean isGm = false;
        Boolean hasPhoneVisit = false;
        if(pwd!=null){
            try{
                User user = userService.selUserByPwd(pwd);
                String status = user.getStatus();
                isGm = status.indexOf("16")!=-1;
                hasPhoneVisit = status.indexOf("20")!=-1;
            }catch (Exception e){
//                e.printStackTrace();
            }

        }
        List<Mission> missions = null;
        if(search!=null){
            missions = missionService.getMissions(search);
        }else{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

            Date date1 = lastDayOfMonth(sdf.parse(date));

            String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(date1).split("-")[2];
            String less = date+"-"+lastDay;
            String greater = date+"-"+1;
            if(isGroup){
                String[] dates = date.split("-");
                Integer month = Integer.valueOf(dates[1]);
                less = dates[0] +"-"+ (month+1)+"-"+ lastDay;
                greater = dates[0] +"-"+ (month-1)+"-"+1;
            }
            System.out.println(less);
            System.out.println(greater);
            missions = missionService.getMissions(less,greater);
        }
        if(sort==null){
            resultBean = new ResultBean(missions);
        }else{
            Map <String,Map<String,Object>> map = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        return Integer.parseInt(o2.replaceAll("-","")) - Integer.parseInt(o1.replaceAll("-",""));
                    }catch (Exception e){
                        return 1;
                    }

                }
            });
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(Mission mission : missions){
                String key = sdf.format(mission.getCreate_time());
                Map tempMap = map.get(key);
                List<Mission> tempList;
                int typeCount = 0;
                double priceCount = 0,
                       settleCount = 0;
                if(tempMap==null){
                    tempMap = new HashMap();
                    tempList = new ArrayList();
                    tempMap.put("tdCount",0);
                    tempMap.put("sjjCount",0);
                    tempMap.put("jjCount",0);
                    tempMap.put("dlCount",0);
                    tempMap.put("fzqCount",0);
                    tempMap.put("sbCount",0);
                    if(isGm){
                        tempMap.put("priceCount",0);
                        tempMap.put("settleCount",0);
                    }
                }else{
                    switch(mission.getMtype()){
                        case "塔吊": typeCount = (int)tempMap.get("tdCount");break;
                        case "升降机": typeCount = (int)tempMap.get("sjjCount");break;
                        case "井架": typeCount = (int)tempMap.get("jjCount");break;
                        case "吊篮": typeCount = (int)tempMap.get("dlCount");break;
                        case "防坠器": typeCount = (int)tempMap.get("fzqCount");break;
                        case "三宝钢管扣件": typeCount= (int)tempMap.get("sbCount"); break;
                    }
                    tempList = (ArrayList)tempMap.get("list");
                    if(isGm){
                        priceCount = (Double) tempMap.get("priceCount");
                        settleCount = (Double) tempMap.get("settleCount");
                    }
                }
                switch(mission.getMtype()){
                    case "塔吊":
                        tempMap.put("tdCount",typeCount + mission.getNum());
                    break;
                    case "升降机":
                        tempMap.put("sjjCount",typeCount + mission.getNum());
                    break;
                    case "井架":
                        tempMap.put("jjCount",typeCount + mission.getNum());
                    break;
                    case "吊篮":
                        tempMap.put("dlCount",typeCount + mission.getNum());
                    break;
                    case "防坠器":
                        tempMap.put("fzqCount",typeCount + mission.getNum());
                    break;
                    case "三宝钢管扣件":
                        tempMap.put("sbCount",typeCount + mission.getWltsjs().size());
                    break;
                }
                if(!isGm){
                    mission.setPrice(0);
                }
                if(!"三宝钢管扣件".equals(mission.getMtype())){
                    priceCount += mission.getPrice() * mission.getNum();
                }else{
                    priceCount += mission.getCount();
                }
                for(Wltsj wltsj : mission.getWltsjs()){
                    long time = (new Date().getTime() - wltsj.getCreateAt().getTime())/86400000;
                    long time1 = 0;
                    if(wltsj.getSeal()!=null){
                        try {
                            Map<String,String> map1 = JSON.parseObject(wltsj.getSeal(),HashMap.class);
                            time1 = (new Date().getTime() - Long.parseLong(map1.get("execution_time")))/86400000;
                        }catch (Exception e){
//                            e.printStackTrace();
                        }
                    }

                    if(time > 6 && wltsj.getSeal()==null){
                        Object ids = tempMap.get("redWarn");
                        if(ids==null){
                            tempMap.put("redWarn",mission.getId());
                        }else if(ids.toString().indexOf(mission.getId().toString())!=-1){
                            tempMap.put("redWarn",ids +"," + mission.getId());
                        }
                    }
                    if(time1 > 10 && wltsj.getReport_send()==null){
                        Object ids = tempMap.get("purpleWarn");
                        if(ids==null){
                            tempMap.put("purpleWarn",mission.getId());
                        }else if(ids.toString().indexOf(mission.getId().toString())!=-1){
                            tempMap.put("purpleWarn",ids +"," + mission.getId());
                        }
                    }
                    if(wltsj.getSettle()==null){
                        if("三宝钢管扣件-防坠器".indexOf(mission.getMtype())==-1){
                            settleCount += mission.getPrice();
                        }else if("防坠器".equals(mission.getMtype())){
                            settleCount += mission.getNum()*mission.getPrice();
                        }else{
                            settleCount += mission.getCount();
                            if(!isGm){
                                wltsj.setPrice(0);
                            }
                            break;
                        }
                    }
                    if(!isGm){
                        wltsj.setPrice(0);
                    }
                    if(!hasPhoneVisit){
                        String phoneVisitStr = wltsj.getPhone_visit();
                        if(phoneVisitStr!=null){
                            Map<String,String> phoneVisit = JSON.parseObject(phoneVisitStr,Map.class);
                            phoneVisit.put("text","没有权限");
                            wltsj.setPhone_visit(JSON.toJSONString(phoneVisit));
                        }
                    }
                }
                if(isGm){
                    tempMap.put("priceCount",priceCount);
                    tempMap.put("settleCount",settleCount);
                }
                tempList.add(mission);
                tempMap.put("list",tempList);
                map.put(key,tempMap);
            }
//            Map<String,List> map = new TreeMap<>(new Comparator<String>() {
//                @Override
//                public int compare(String o1, String o2) {
//                    return Integer.parseInt(o2.replaceAll("-","")) - Integer.parseInt(o1.replaceAll("-",""));
//                }
//            });
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            resultBean = new ResultBean(map);
        }
        return resultBean;
    }

    private Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @RequestMapping(value = "/mission/{id}/{pwd}",method = RequestMethod.DELETE)
    public ResultBean delMission(@PathVariable(value = "id")String id,@PathVariable(value = "pwd")String pwd){
        ResultBean resultBean = null;
        String msg = "";
        int num = missionService.delMission(id,pwd);
        switch(num){
            case -1 : msg = "id或密码不能为空";break;
            case 0 :  msg = "密码错误，找不到密码对应的信息";break;
            case 1 : msg = "删除成功";break;
        }
        resultBean = new ResultBean(msg);
        return resultBean;
    }

    @RequestMapping(value = "/random_group",method = RequestMethod.POST)
    public ResultBean postRandomGroup(@RequestBody PostGroup postGroup){
        ResultBean resultBean = null;
        boolean flag = missionService.postRandomGroup(postGroup)==1?true:false;
        resultBean = new ResultBean(flag?"成功":"密码不正确或者没有权限");
        return resultBean;
    }

    @RequestMapping(value = "/group",method = RequestMethod.POST)
    public ResultBean postGroup(@RequestBody PostGroup postGroup){
        ResultBean resultBean = null;
        resultBean = new ResultBean(postGroup);
        return resultBean;
    }

    @RequestMapping(value = "/groupMissionList",method = RequestMethod.GET)
    public ResultBean getGroup(){
        ResultBean resultBean = null;
        return resultBean;
    }

    /**
     *  小组追加
     * */
    @RequestMapping(value = "/appendGroup", method = RequestMethod.POST)
    public ResultBean appendGroup(@RequestBody PostGroup postGroup){
        ResultBean resultBean = null;
        int flag = missionService.appendGroup(postGroup.getId(),postGroup.getNum(),postGroup.getPwd(),postGroup.getGroup());
        if(flag == -1){
            return new ResultBean("账号没有权限");
        }
        if(flag == 0){
            return new ResultBean("没有找到该任务");
        }
        if(flag == 1){
            return new ResultBean("添加成功");
        }
        resultBean = new ResultBean(flag);
        return resultBean;
    }

}
