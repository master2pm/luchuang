package com.luchuang.fileImport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luchuang.common.DownloadFile;
import com.luchuang.common.ResultBean;
import com.luchuang.fileImport.mapper.MissionMapper;
import com.luchuang.fileImport.mapper.WltsjMapper;
import com.luchuang.fileImport.pojo.Mission;
import com.luchuang.fileImport.pojo.Rule;
import com.luchuang.fileImport.pojo.User;
import com.luchuang.fileImport.pojo.Wltsj;
import com.luchuang.fileImport.service.IFileWordService;
import com.luchuang.fileImport.service.IReportFormService;
import com.luchuang.fileImport.service.UserService;
import com.luchuang.fileImport.service.impl.UserServiceImpl;
import com.luchuang.fileImport.util.DeleteFileIsTime;
import com.luchuang.fileImport.util.ExcelUtil;
import com.luchuang.fileImport.util.FileWord;
import com.luchuang.fileImport.util.Zip;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;

@Controller
public class FileWordImportController {

    @Value("#{configProperties['download.show']}")
    private Boolean downLoadshow;

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private WltsjMapper wltsjMapper;

    private Logger log = Logger.getLogger(FileWordImportController.class);
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request , HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    @Autowired
    private IFileWordService iFileWordService;

    @Autowired
    private IReportFormService iReportFormService;

    @RequestMapping("/")
    public String home(HttpServletRequest req, HttpServletResponse resp){
        log.info("主页");
        log.info(downLoadshow);
//        Map map = iReportFormService.getReportList("");
//        req.setAttribute("report_list", JSONObject.toJSONString(map));
        req.setAttribute("optionBtn",FileWord.readJson("optionBtn"));
        req.setAttribute("down_show",downLoadshow);
        Map<String,String> typeWord = new HashMap<>();
        typeWord.put("井架","任务单,委托单,原始记录,报告,检验整改意见,回执");
        typeWord.put("塔吊","任务单,委托单,原始记录,报告,检验整改意见,回执");
        typeWord.put("升降机","任务单,委托单,原始记录,报告,检验整改意见,回执");
        typeWord.put("防坠器","任务单,委托单,检验整改意见,回执");
        typeWord.put("吊篮","");
        typeWord.put("三宝钢管扣件","");
        req.setAttribute("type_list", JSONObject.toJSONString(typeWord));
        return "ku";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest req, HttpServletResponse resp){
        Map map = iReportFormService.getReportList("");
        req.setAttribute("report_list", JSONObject.toJSONString(map));
        return "ku";
    }

    @RequestMapping("/ku")
    public String ku(HttpServletRequest req, HttpServletResponse resp){
        Map map = iReportFormService.getReportList("");
        req.setAttribute("report_list", JSONObject.toJSONString(map));
        return "ku";
    }

    @RequestMapping(value = "/file_attr",method = RequestMethod.POST)
    public @ResponseBody ResultBean editFileAttr(@RequestParam(value = "mid",required = false,defaultValue = "")String mid,@RequestParam(value = "pwd")String pwd,@RequestParam(value = "attr")String attr,@RequestParam(value = "id")Long id,@RequestParam(value = "text",required = false)String text){
        String msg = null;
        try {
            int flag = iFileWordService.editFileAttr(pwd,id,attr,text,mid);
            System.out.println("flag::"+flag);
            if(flag!=2){
                msg = Rule.getName(flag);
            }else{
                msg = "内容不完整 不能勾选";
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("---:"+e.getMessage());
            if(e.getMessage().indexOf("noGm")!=-1){
                msg = "不是管理员 不能取消钩子";
            }else if(e.getMessage().indexOf("nonono")!=-1){
                msg = "没有权限";
            }
        }

        return new ResultBean(msg);
    }

    @RequestMapping(value = "/word_file",method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> word_file(@RequestParam(value = "searchkey" , required = false) String searchkey){
        Map map = new HashMap();
        List list = iFileWordService.wordList(searchkey);
        Map map1 = new HashMap();
        Collections.sort(list,new DateComparator());
        map1.put("data", list);
        map.put("results",map1);
        map.put("status",1);
        map.put("path",Thread.currentThread().getContextClassLoader().getResource("").toString());
        return map;
    }

    /**
     *  如果有接收到 Edit_file_name就是修改
     * */
    @RequestMapping(value = "/add_file",method = RequestMethod.POST)
    public @ResponseBody Map add_file(@RequestBody Wltsj wl,@RequestParam(value = "oneKey",required = false,defaultValue = "false")boolean oneKey){
        Map map = new HashMap();
        map.put("status",1);
        if("".equals(wl.getFilename())||wl.getFilename()==null){
            map.put("msg","不能为空");
            return map;
        }
        iFileWordService.addFile(wl,oneKey);
        return map;
    }

    @RequestMapping(value = "/delfile/{filepath}",method = RequestMethod.DELETE)
    public @ResponseBody Map delfile(@PathVariable(value = "filepath") String filepath){
        Map map = new HashMap();
        map.put("status",1);
        String path = FileWord.file_path.replaceAll("classes","cache")+filepath;
        FileWord.del(new File(path));
        return map;
    }

    @RequestMapping(value = "/delfile/{filepath}/{filename}",method = RequestMethod.DELETE)
    public @ResponseBody Map delfile(@PathVariable(value = "filepath") String filepath,@PathVariable(value = "filename") String filename){
        Map map = new HashMap();
        map.put("status",1);
        try{
            filename = URLDecoder.decode(filename,"UTF-8");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        String path = FileWord.file_path.replaceAll("classes","cache")+filepath+"/"+filename;
        FileWord.del(new File(path));
        System.out.println(path);
        System.out.println(filename);
        return map;
    }

    @RequestMapping(value = "/readfile",method = RequestMethod.GET)
    public @ResponseBody Map readfile(){
        Map map = new HashMap();
        map.put("lala","213");
//        File file = new File("");
        Wltsj wl = new Wltsj();
        String path = FileWord.file_path;
        path = path.substring(0,path.indexOf("classes"))+"ftl/";

        return map;
    }

    //下载一个文件或者是一个压缩包
    @RequestMapping(value = "/downFile",method = RequestMethod.GET)
    public ResponseEntity<byte[]> downFile(@RequestParam(value = "pwd")String pwd,@RequestParam(value = "date") String date,@RequestParam(value = "filepath") String filepath,@RequestParam(value = "mtype") String mtype,HttpServletResponse response) throws Exception{
        User user = userService.selUserByPwd(pwd);
        if(user==null||user.getStatus().indexOf("13")==-1){
            return null;
        }
        date = URLDecoder.decode(date,"UTF-8").trim();
        filepath = URLDecoder.decode(filepath,"UTF-8").trim();
        filepath = URLDecoder.decode(filepath,"UTF-8").trim();
        mtype = URLDecoder.decode(mtype,"UTF-8").trim();
        mtype = URLDecoder.decode(mtype,"UTF-8").trim();
//        filepath = filepath.substring(0,filepath.indexOf("-"));

        Example example = new Example(Wltsj.class);
        example.createCriteria().andEqualTo("filename",filepath).andEqualTo("date",date);
        Wltsj wltsj = wltsjMapper.selectByExample(example).get(0);
        File file = iFileWordService.addFile(wltsj,mtype);

        ExecutorService executor = Executors.newCachedThreadPool();
        DeleteFileIsTime task = new DeleteFileIsTime();
        task.setFile(file);
        task.setWltsj(wltsj);
        Future<Integer> result = executor.submit(task);
        executor.shutdown();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(file.getName(),"UTF-8"));
        FileUtils.readFileToByteArray(file);
        ResponseEntity responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
        if("".equals(mtype)){
            file.delete();
        }
        return responseEntity;
//        return null;
    }

    @RequestMapping(value = "/getfilemsg",method = RequestMethod.GET)
    public @ResponseBody Map getFileMsg(@RequestParam(value = "filename")String filename,@RequestParam(value = "date")String date){
        Map map = new HashMap();
        try{
            filename = URLDecoder.decode(filename,"UTF-8");
            date = URLDecoder.decode(date,"UTF-8");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        Map file_map = iFileWordService.readTxt(filename,date);
        map.put("data",file_map);
        map.put("status",1);
        return map;
    }

    @RequestMapping(value = "/setCheck",method = RequestMethod.POST)
    public @ResponseBody Map setCheck(@RequestParam(value = "filename")String filename,@RequestParam(value = "type")String type,@RequestParam(value = "val")String bool){
        Map map = new HashMap();
        String text = FileWord.read(new File(FileWord.getCachePath(filename)));
        Map textMap = JSON.parseObject(text);
        switch (type){
            case "photo": break;
            case "seal": break;
            case "examine": break;
            case "submit": break;
        }
        System.out.println(text);
        return map;
    }

    class DateComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            try{
                Map map1 = (Map)o1;
                Map map2 = (Map)o2;
                if(map1.get("date")==null){
                    return -1;
                }
                if(map2.get("date")==null){
                    return 1;
                }
                String[] date1 = map1.get("date").toString().split("-");
                String[] date2 = map2.get("date").toString().split("-");
                int year = Integer.parseInt(date1[0])-Integer.parseInt(date2[0]);
                if(year==0){
                    int mon = Integer.parseInt(date1[1])-Integer.parseInt(date2[1]);
                    if(mon==0){
                        int day = Integer.parseInt(date1[2])-Integer.parseInt(date2[2]);
                        if(day==0){
                            return -1;
                        }else{
                            return day>0?-1:1;
                        }
                    }else{
                        return mon>0?-1:1;
                    }
                }else{
                    return year>0?-1:1;
                }
            }catch (Exception e){
                log.error(e.getMessage());
                return -1;
            }

        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = URLDecoder.decode("%25E5%25A7%2594%25E6%2589%2598%25E5%258D%2595","UTF-8").trim();
        System.out.println(str);
    }
}
