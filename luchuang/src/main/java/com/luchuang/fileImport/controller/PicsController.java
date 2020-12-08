package com.luchuang.fileImport.controller;

import com.alibaba.fastjson.JSON;
import com.luchuang.common.ResultBean;
import com.luchuang.fileImport.pojo.Pics;
import com.luchuang.fileImport.pojo.Rule;
import com.luchuang.fileImport.service.PicsService;
import com.luchuang.fileImport.util.FileWord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName PicsController
 * @Author PPPL
 * @Date 2019/8/31 15:13
 **/
@Controller
@RequestMapping(value = "/pics")
public class PicsController {

    @Autowired
    private PicsService picsService;

    private Logger log = Logger.getLogger(FileWordImportController.class);

    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    public String home(HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "wid")String wid){
        req.setAttribute("wid", wid);
        req.setAttribute("pics", JSON.toJSONString(picsService.getPics(wid)));
        return "pics";
    }

    @ResponseBody
    @RequestMapping(value = "/{wid}" , method = RequestMethod.POST)
    public ResultBean post(@RequestBody List<Pics> pics, @RequestParam(value = "pwd")String pwd){
        ResultBean resultBean = new ResultBean(Rule.getName(picsService.addPics(pics,pwd)));
        return resultBean;
    }

    @ResponseBody
    @RequestMapping(value = "/{wid}/{pid}",method = RequestMethod.DELETE)
    public ResultBean del(@PathVariable(value = "wid")String wid,@PathVariable(value = "pid")String pid,@RequestParam(value = "pwd")String pwd){
        ResultBean resultBean = new ResultBean();
        int flag = picsService.delPics(wid,pid,pwd);
        resultBean = new ResultBean(Rule.getName(flag));
        return resultBean;
    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity<byte[]> image(@PathVariable String id) throws IOException {
//        byte[] zp = FileWord.toByteArray("C:\\Users\\Administrator\\Desktop\\常用的png\\test\\" + id + ".png");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        return new ResponseEntity<byte[]>(zp, headers, HttpStatus.OK);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String id) {
        try {
            InputStream inputStream = new FileInputStream(new File(FileWord.file_path.replaceAll("classes", "pics") + id + ".png"));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(inputStreamResource,headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
