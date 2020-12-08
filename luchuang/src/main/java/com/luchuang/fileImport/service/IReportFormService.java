package com.luchuang.fileImport.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportFormService {

    public Map getReportList(String searchkey);

    public HSSFWorkbook getReportFile(String date,String pwd);

    HSSFWorkbook getMTypeReportFile(String date,String pwd);

    public Date getSearchFile(List list);

    HSSFWorkbook getAcceptReportFile(String date,String pwd);

    HSSFWorkbook getEarningsReportFile(String date,String pwd);

}
