package com.luchuang.fileImport.controller;

import com.alibaba.fastjson.annotation.JSONType;
import com.luchuang.common.DownloadFile;
import com.luchuang.fileImport.pojo.FileAttr;
import com.luchuang.fileImport.service.IReportFormService;
import com.luchuang.fileImport.util.FileWord;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class ReportFormController {

    private Logger log = Logger.getLogger(ReportFormController.class);
    @Autowired
    private IReportFormService iReportFormService;

    @RequestMapping(value = "/get_report_list",method = RequestMethod.GET)
    public Map getReportList(@RequestParam(value = "search") String search){
        if(!"".equals(search)){
            try{
                search = URLDecoder.decode(search,"UTF-8");
                log.info(search);
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }
        Map map = iReportFormService.getReportList(search);
        return map;
    }

    /***
     *  小组汇总
     * */
    @RequestMapping(value = "/download_report_list",method = RequestMethod.GET)
    public void downReport(@RequestParam(value = "pwd")String pwd,@RequestParam(value = "date",required = false) String date,HttpServletRequest request , HttpServletResponse response){
        HSSFWorkbook wb = iReportFormService.getReportFile(date,pwd);
        //响应到客户端
        try {
            DownloadFile.setResponseHeader(response, date+"报表.xls");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     *  机器汇总表
     * **/
    @RequestMapping(value = "/download_new_report_list",method = RequestMethod.GET)
    public void downNewReport(@RequestParam(value = "pwd")String pwd , @RequestParam(value = "date",required = false) String date,HttpServletRequest request , HttpServletResponse response){
        System.out.println("12312312321312");
        HSSFWorkbook wb = iReportFormService.getMTypeReportFile(date,pwd);
        //响应到客户端
        try {
            DownloadFile.setResponseHeader(response, date+"报表.xls");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     *  收入汇总表
     * **/
    @RequestMapping(value = "/download_earnings_list",method = {RequestMethod.GET})
    public void earningsTables(@RequestParam(value = "pwd")String pwd , @RequestParam(value = "date",required = false) String date,HttpServletRequest request , HttpServletResponse response){
        HSSFWorkbook wb = iReportFormService.getEarningsReportFile(date,pwd);
        //响应到客户端
        try {
            DownloadFile.setResponseHeader(response, date+"报表.xls");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     *  受理情况汇总表
     * **/
    @RequestMapping(value = "/download_accept_list",method = {RequestMethod.GET})
    public void acceptTables(@RequestParam(value = "pwd")String pwd , @RequestParam(value = "date",required = false) String date,HttpServletRequest request , HttpServletResponse response) {
        HSSFWorkbook wb = iReportFormService.getAcceptReportFile(date,pwd);
        //响应到客户端
        try {
            DownloadFile.setResponseHeader(response, date+"报表.xls");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/download_report_list_p",method = {RequestMethod.POST})
    public Map downSearchReport(@RequestBody List<FileAttr> list){
        Map map = new HashMap();
        Date now_date = iReportFormService.getSearchFile(list);
        map.put("status","1");
        map.put("url",now_date.getTime());
        return map;
    }

    @RequestMapping(value = "/download_report_list/{xls}",method = {RequestMethod.GET})
    public ResponseEntity<byte[]> downSearchReport(@PathVariable(value = "xls")String xls,HttpServletRequest request , HttpServletResponse response){
        log.info(xls);
        File file = new File(FileWord.file_path.replaceAll("classes","xls_c")+xls+".xls");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity responseEntity = null;
        try{
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(file.getName(),"UTF-8"));
            FileUtils.readFileToByteArray(file);
            responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                    headers, HttpStatus.CREATED);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return responseEntity;
    }

}
