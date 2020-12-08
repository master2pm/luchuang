package com.luchuang.fileImport.util;

import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {

    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb,String date){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFFont f = wb.createFont();
        f.setFontName("宋体");
        f.setFontHeightInPoints((short)11);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(f);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom((short) 2);
        style.setBorderLeft((short) 2);
        style.setBorderRight((short) 2);
        style.setBorderTop((short) 2);

        //声明列对象
        HSSFCell cell = null;
        Map<String,short[]> titleWidthMap = new HashMap();
        titleWidthMap.put("井架",new short[]{4,9,15,15,15,15,17,9,9,11,6,13,13,12,11,9,9,9,12,12,12,13,14,14,16,8,8,18,18});
        titleWidthMap.put("塔吊",new short[]{4,9,15,15,15,15,17,9,9,11,6,13,13,12,11,9,9,9,12,12,12,13,14,14,16,8,8,18,18});
        titleWidthMap.put("吊篮",new short[]{4,9,15,15,15,15,17,9,9,11,6,13,13,12,11,9,9,9,12,12,12,13,14,14,16,8,8,18,18});
        titleWidthMap.put("升降机",new short[]{4,9,15,15,15,15,17,9,9,11,6,13,13,12,11,9,9,9,12,12,12,13,14,14,16,8,8,18,18});
        titleWidthMap.put("防坠器",new short[]{4,9,15,15,15,15,25,7,12,14,10,10,10,28,6,8,9,16,17,19,18});
        titleWidthMap.put("三宝钢管扣件",new short[]{10,15,15,15,15,17,24,7,14,26,26,9,9,9,12,14,16,21,10,17,19,18});
        short[] title_width;

        if("井架，塔吊，吊篮，升降机，防坠器，三宝钢管扣件".indexOf(sheetName)!=-1){
            title_width = titleWidthMap.get(sheetName);
        }else{
            title_width = new short[]{4,8,13,23,8,9,9,32,8,15,17,21,22,21,32,9,14,8,13,10,10,10,10,8,9,9,9,9,9,9,9,18};
        }

        //创建标题
        {
            row.setHeight((short)(32*20));
            HSSFCell cell1 = row.createCell(0);
            Region region = new Region((short)0,(short)0,(short)0,(short)(title.length-1));
            sheet.addMergedRegion(region);
            if(!sheetName.equals("小组汇总表")){
                String[] dateArr = date.split("-");
                cell1.setCellValue(dateArr[0]+"年"+dateArr[1]+"月"+sheetName+"汇总表");
            }else{
                cell1.setCellValue("小组汇总表");
            }

            cell1.setCellStyle(style);
        }

        row = sheet.createRow(1);
        //创建标题
        for(int i=0;i<title.length;i++){

            sheet.setColumnWidth(i,title_width[i]*282);
            row.setHeight((short)(29*20));

            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }



        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont f1 = wb.createFont();
        f1.setFontName("宋体");
        f1.setFontHeightInPoints((short)(10));
        style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        style1.setFont(f1);
        style1.setBorderBottom((short) 1);
        style1.setBorderLeft((short) 1);
        style1.setBorderRight((short) 1);
        style1.setBorderTop((short) 1);
        style1.setWrapText(true);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.createDataFormat().getFormat("¥#,##0"));
        HSSFFont f2 = wb.createFont();
        f2.setFontName("宋体");
        f2.setFontHeightInPoints((short)(10));
        f2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(f2);
        cellStyle.setBorderBottom((short) 2);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        HSSFCellStyle bottomStyle = wb.createCellStyle();
        bottomStyle.setBorderBottom((short) 2);
        bottomStyle.setBorderLeft((short) 1);
        bottomStyle.setBorderRight((short) 1);
        bottomStyle.setBorderTop((short) 1);

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 2);
            row.setHeight((short)(23*20));
            boolean hasValue = false;
            if(!sheetName.equals("小组汇总表")){
                hasValue = "合计".equals(values[i][0]);
            }else{
                hasValue = values[i][0]==null;
            }
            for(int j=0;j<values[i].length;j++){
                HSSFCell cell1 = row.createCell(j);

                if(hasValue){
                    if(!sheetName.equals("小组汇总表")){
                        if(j == 0){
                            Region region = new Region((short)i+2,(short)0,(short)i+2,(short)1);
                            sheet.addMergedRegion(region);
                            cell1.setCellValue(values[i][0]);
                        }
                        if(j == 2){
                            if(sheetName.equals("防坠器")){
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula("SUM(L3:L"+ (i + 2) +")");
                                cell1.setCellStyle(cellStyle);
                            }else{
                                cell1.setCellValue(i + "台");
                            }
                        }
                    }else{
                        switch(j){
                            case 0 : cell1.setCellValue("总计");break;
                            case 1 :
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula("SUM(B3:B"+ (i + 2) +")");
                            break;
                            case 2 :
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula("SUM(C3:C"+ (i + 2) +")");
                                break;
                            case 3 :
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula("SUM(D3:D"+ (i + 2) +")");
                                break;
                            case 4 :
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula("SUM(E3:E"+ (i + 2) +")");
                                break;
                            case 5 :
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula("SUM(F3:F"+ (i + 2) +")");
                                break;
                            case 6 :
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula("SUM(G3:G"+ (i + 2) +")");
                                break;
                        }
                    }

                    cell1.setCellStyle(bottomStyle);
                }else{
                    if(sheetName.equals("小组汇总表")){
                        try {
                            cell1.setCellValue(Integer.parseInt(values[i][j]));
                        }catch (Exception e){
                            cell1.setCellValue(values[i][j]);
                        }
                    }
//                    cell1.setCellValue(values[i][j]);
                }
                //将内容按顺序赋给对应的列对象

                switch(sheetName){
                    case "井架":
                    case "塔吊":
                    case "吊篮":
                    case "升降机":
                        if(j == 21 || j == 22 || j == 23 || j == 24){
                            if(values[i][j]!=null&&values[i][j].indexOf("(")==-1&&!"".equals(values[i][j])){
                                try{
                                    cell1.setCellValue(Double.parseDouble(values[i][j]));

                                }catch(Exception e){
                                    cell1.setCellValue(values[i][j]);
                                }
                                cell1.setCellStyle(style1);
                            }
                            if(values[i][j]!=null&&values[i][j].indexOf("(")!=-1){
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula(values[i][j]);
                                cell1.setCellStyle(cellStyle);

                            }
                        }else{

                            if(!hasValue){
                                if(j==0){
                                    cell1.setCellValue(i+1);
                                }else{
                                    cell1.setCellValue(values[i][j]);
                                }
                            }

                        }
                        break;
                    case "三宝钢管扣件":
                        if(j == 13 || j == 14 || j == 15 || j == 16){
                            if(values[i][j]!=null&&values[i][j].indexOf("(")==-1&&!"".equals(values[i][j])){
                                cell1.setCellValue(Double.parseDouble(values[i][j]));
                                cell1.setCellStyle(style1);
                            }
//                            cell1.setCellStyle(cellStyle);
                            if(values[i][j]!=null&&values[i][j].indexOf("(")!=-1){
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula(values[i][j]);
                                cell1.setCellStyle(cellStyle);
                            }
                        }else{
                            if(!hasValue){
                                if(j==0){
                                    cell1.setCellValue(i+1);
                                }else{
                                    cell1.setCellValue(values[i][j]);
                                }
                                cell1.setCellStyle(style1);
                            }
                        }
                        break;
                    case "防坠器":
                        if(j == 13 || j == 14 || j == 15 || j == 16){
                            if(values[i][j]!=null&&values[i][j].indexOf("(")==-1&&!"".equals(values[i][j])){
                                cell1.setCellValue(Double.parseDouble(values[i][j]));
                                cell1.setCellStyle(style1);
                            }
                            if(values[i][j]!=null&&values[i][j].indexOf("(")!=-1){
                                cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell1.setCellFormula(values[i][j]);
                                cell1.setCellStyle(cellStyle);
                            }
                        }else{
                            if(!hasValue){
                                if(j==11){
                                    System.out.println(values[i][j]);
                                    cell1.setCellValue(Integer.parseInt(values[i][j].trim()));
                                }else if(j==0){
                                    cell1.setCellValue(i+1);
                                }else{
                                    cell1.setCellValue(values[i][j]);
                                }
                                cell1.setCellStyle(style1);
                            }
                        }
                        break;
                }
            }
            if(hasValue)break;
        }
        return wb;
    }

    private static void setStyleBorder(HSSFCellStyle style){
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderTop((short) 1);
    }

    private static void setStyleFont(HSSFCellStyle style, HSSFWorkbook wb,String fontName,short size,boolean bold,short direction){
        style.setAlignment(direction); // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont f = wb.createFont();
        f.setFontName(fontName);
        f.setFontHeightInPoints(size);
        if(bold){
            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        style.setFont(f);
    }

    public static HSSFWorkbook getAcceptReport(String sheetName,String []title,String [][]values, HSSFWorkbook wb,String date){
// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();

        setStyleFont(style,wb,"宋体",(short) 12,true,HSSFCellStyle.ALIGN_CENTER);
        setStyleBorder(style);

        //声明列对象
        HSSFCell cell = null;
        Map<String,short[]> titleWidthMap = new HashMap();
        titleWidthMap.put("塔吊",new short[]{4,16,16,30,45,16,16,16,16,16,16});
        titleWidthMap.put("施工梯",new short[]{4,16,16,30,45,16,16,16,16,16,16});
        titleWidthMap.put("井架",new short[]{4,16,16,30,45,16,16,16,16,16,16});
        titleWidthMap.put("吊篮",new short[]{4,16,16,30,45,16,16,16,16,16});
        titleWidthMap.put("防坠器",new short[]{4,16,16,30,45,16,16,16,16});
        titleWidthMap.put("三宝钢管扣件",new short[]{4,16,16,16,16,16,16,16});
        short[] title_width = titleWidthMap.get(sheetName);



        //创建标题
        {
            row.setHeight((short)(42*20));
            HSSFCell cell1 = row.createCell(0);
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, title.length-1);
            sheet.addMergedRegion(region);
            setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
            String[] dateArr = date.split("-");
            cell1.setCellValue("广东麓创检测有限公司\n"+dateArr[0]+"年"+dateArr[1]+"月"+sheetName+"检验受理情况报表");
            HSSFCellStyle style1 = wb.createCellStyle();
            setStyleBorder(style1);
            setStyleFont(style1,wb,"黑体",(short)14,false,HSSFCellStyle.ALIGN_CENTER);
            cell1.setCellStyle(style1);
        }

        {
            row = sheet.createRow(1);
            row.setHeight((short)(23*20));
            HSSFCell cell1 = row.createCell(0);
            CellRangeAddress region = new CellRangeAddress(1, 1, 0, title.length-1);
            sheet.addMergedRegion(region);
            setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cell1.setCellValue("填报日期："+sdf.format(new Date()));
            HSSFCellStyle style1 = wb.createCellStyle();
            setStyleBorder(style1);
            setStyleFont(style1,wb,"Times New Roman",(short)12,false,HSSFCellStyle.ALIGN_RIGHT);
            cell1.setCellStyle(style1);
        }

        row = sheet.createRow(2);
        //创建标题
        for(int i=0;i<title.length;i++){

            sheet.setColumnWidth(i,title_width[i]*282);
            row.setHeight((short)(29*20));

            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }



        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont f1 = wb.createFont();
        f1.setFontName("宋体");
        f1.setFontHeightInPoints((short)(10));
        style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        style1.setFont(f1);
        style1.setBorderBottom((short) 1);
        style1.setBorderLeft((short) 1);
        style1.setBorderRight((short) 1);
        style1.setBorderTop((short) 1);
        style1.setWrapText(true);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.createDataFormat().getFormat("¥#,##0"));
        HSSFFont f2 = wb.createFont();
        f2.setFontName("宋体");
        f2.setFontHeightInPoints((short)(10));
        f2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(f2);
        cellStyle.setBorderBottom((short) 2);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        //创建内容
        if(values!=null)for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 3);
            row.setHeight((short)(33*20));
            boolean hasValue = false;
            hasValue = values[i][0]==null;
            if(hasValue){

                if("塔吊，施工梯，井架".indexOf(sheetName)!=-1){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 3);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1 = row.createCell(4);
                    cell1.setCellValue("检验费合计（元）");
                    HSSFCellStyle style12 = wb.createCellStyle();
                    setStyleBorder(style12);
                    setStyleFont(style12,wb,"宋体",(short)11,true,HSSFCellStyle.ALIGN_CENTER);
                    cell1.setCellStyle(style12);

                    cell1 = row.createCell(5);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(G4:G"+ (i + 3) +")");
                    cell1.setCellStyle(style);

                    row = sheet.createRow(i + 4);
                    cell1 = row.createCell(0);
                    region = new CellRangeAddress(i+4, i+4, 0, 1);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1.setCellValue("合计（台）");
                    cell1.setCellStyle(style12);

                    cell1 = row.createCell(2);
                    region = new CellRangeAddress(i+4, i+4, 2, 4);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    if("塔吊".equals(sheetName)){
                        cell1.setCellValue("委托检验：   初检：    复检：    年检：  最大高度： 第一道附墙：");
                    }else if("施工梯".equals(sheetName)){
                        cell1.setCellValue("初检：0     年检：0    复检：0                     最大高度：0   委托检验：0");
                    }else if("井架".equals(sheetName)){
                        cell1.setCellValue("初检：     复检：     委托：     年检： ");
                    }

                    cell1.setCellStyle(style12);

                    cell1 = row.createCell(5);
                    cell1.setCellValue("总台数");
                    cell1.setCellStyle(style12);

                    cell1 = row.createCell(6);
                    cell1.setCellValue(i+"(台)");
                    cell1.setCellStyle(style12);
                }else if("吊篮".equals(sheetName)){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 2);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1 = row.createCell(0);
                    region = new CellRangeAddress(i+3, i+3, 3, 4);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1.setCellValue("检验费合计（元）");

                    HSSFCellStyle style12 = wb.createCellStyle();
                    setStyleBorder(style12);
                    setStyleFont(style12,wb,"宋体",(short)11,true,HSSFCellStyle.ALIGN_CENTER);
                    cell1.setCellStyle(style12);

                    cell1 = row.createCell(5);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(F4:F"+ (i + 3) +")");
                    cell1.setCellStyle(style);

                }else if("防坠器".equals(sheetName)){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 3);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1 = row.createCell(0);
                    region = new CellRangeAddress(i+3, i+3, 4, 5);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1.setCellValue("检验费合计（元）");
                    HSSFCellStyle style12 = wb.createCellStyle();
                    setStyleBorder(style12);
                    setStyleFont(style12,wb,"宋体",(short)11,true,HSSFCellStyle.ALIGN_CENTER);
                    cell1.setCellStyle(style12);

                    cell1 = row.createCell(6);
                    cell1.setCellValue(i);
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(7);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(H4:H"+ (i + 3) +")");
                    cell1.setCellStyle(style);

                }else if("三宝钢管扣件".equals(sheetName)){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 2);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1 = row.createCell(3);
                    cell1.setCellValue("检验费合计（元）");
                    HSSFCellStyle style12 = wb.createCellStyle();
                    setStyleBorder(style12);
                    setStyleFont(style12,wb,"宋体",(short)11,true,HSSFCellStyle.ALIGN_CENTER);
                    cell1.setCellStyle(style12);

                    cell1 = row.createCell(5);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(F4:F"+ (i + 3) +")");
                    cell1.setCellStyle(style);

                }




                {
                    if("三宝钢管扣件".equals(sheetName)){
                        i-=2;
                    }else if("防坠器".equals(sheetName)){
                        i-=1;
                    }
                    row = sheet.createRow(i + 6);
                    row.setHeight((short)(22*20));
                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+6, i+6, 0, 6);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1.setCellValue("说明：1、本表一式三份，分别由受理员、综合室负责人、财务各一份；");
                    HSSFCellStyle style12 = wb.createCellStyle();
                    setStyleBorder(style12);
                    setStyleFont(style12,wb,"宋体",(short)11,false,HSSFCellStyle.ALIGN_LEFT);
                    cell1.setCellStyle(style12);

                    row = sheet.createRow(i + 7);
                    row.setHeight((short)(22*20));
                    cell1 = row.createCell(1);
                    region = new CellRangeAddress(i+7, i+7, 1, 6);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                     cell1.setCellValue("2、本表反映当月塔式起重机的受理情况，于次月五日前填报。");
                    cell1.setCellStyle(style12);

                    row = sheet.createRow(i + 8);
                    row.setHeight((short)(22*20));

                    row = sheet.createRow(i + 9);
                    row.setHeight((short)(22*20));
                    cell1 = row.createCell(1);
                    cell1.setCellValue("编报人：");
                    cell1.setCellStyle(style12);
                    cell1 = row.createCell(3);
                    cell1.setCellValue("核对人：");
                    cell1.setCellStyle(style12);
                    break;
                }
            }else{
                HSSFCellStyle style12 = wb.createCellStyle();
                for(int j=0;j<values[i].length;j++){
                    HSSFCell cell1 = row.createCell(j);

                    if((j==6&&"塔吊,施工梯,井架".indexOf(sheetName)!=-1)||(j==5&&"吊篮".indexOf(sheetName)!=-1)||("防坠器".indexOf(sheetName)!=1)&&(j==7||j==6)||("三宝钢管扣件".equals(sheetName)&&j==5)){
                        try {
                            cell1.setCellValue(Double.parseDouble(values[i][j]));
                        }catch (Exception e){
                            cell1.setCellValue(values[i][j]);
//                            e.printStackTrace();
                        }
                    }else{
                        cell1.setCellValue(values[i][j]);
                    }

                    setStyleBorder(style12);
                    setStyleFont(style12,wb,"宋体",(short)10,false,HSSFCellStyle.ALIGN_CENTER);
                    cell1.setCellStyle(style12);
                }
            }
        }


        return wb;
    }






    public static HSSFWorkbook getEarningsReport(String sheetName,String []title,String [][]values, HSSFWorkbook wb,String date){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFFont f = wb.createFont();
        f.setFontName("宋体");
        f.setFontHeightInPoints((short)12);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(f);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderTop((short) 1);

        //声明列对象
        HSSFCell cell = null;
        Map<String,short[]> titleWidthMap = new HashMap();
        titleWidthMap.put("塔吊",new short[]{4,16,16,30,45,16,16,16,16,16,16});
        titleWidthMap.put("施工梯",new short[]{4,16,16,30,45,16,16,16,16,16,16});
        titleWidthMap.put("井架",new short[]{4,16,16,30,45,16,16,16,16,16,16});
        titleWidthMap.put("吊篮",new short[]{4,16,16,30,45,16,16,16,16,16});
        titleWidthMap.put("防坠器",new short[]{4,16,16,30,45,16,16,16,16});
        titleWidthMap.put("三宝钢管扣件",new short[]{4,16,16,16,16,16,16,16});
        short[] title_width = titleWidthMap.get(sheetName);



        //创建标题
        {
            row.setHeight((short)(42*20));
            HSSFCell cell1 = row.createCell(0);
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, title.length-1);
            sheet.addMergedRegion(region);
            setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
            String[] dateArr = date.split("-");
            cell1.setCellValue("广东麓创检测有限公司\n"+dateArr[0]+"年"+dateArr[1]+"月"+sheetName+"检验受理收入报表");
            cell1.setCellStyle(style);

        }

        {
            row = sheet.createRow(1);
            row.setHeight((short)(23*20));
            HSSFCell cell1 = row.createCell(0);
            CellRangeAddress region = new CellRangeAddress(1, 1, 0, title.length-1);
            sheet.addMergedRegion(region);
            setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cell1.setCellValue("填报日期："+sdf.format(new Date()));
            HSSFCellStyle style1 = wb.createCellStyle();
            setStyleBorder(style1);
            setStyleFont(style1,wb,"宋体",(short)12,false,HSSFCellStyle.ALIGN_RIGHT);
            cell1.setCellStyle(style1);
        }

        row = sheet.createRow(2);
        //创建标题
        for(int i=0;i<title.length;i++){

            sheet.setColumnWidth(i,title_width[i]*282);
            row.setHeight((short)(29*20));

            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }



        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont f1 = wb.createFont();
        f1.setFontName("宋体");
        f1.setFontHeightInPoints((short)(10));
        style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        style1.setFont(f1);
        style1.setBorderBottom((short) 1);
        style1.setBorderLeft((short) 1);
        style1.setBorderRight((short) 1);
        style1.setBorderTop((short) 1);
        style1.setWrapText(true);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.createDataFormat().getFormat("¥#,##0"));
        HSSFFont f2 = wb.createFont();
        f2.setFontName("宋体");
        f2.setFontHeightInPoints((short)(10));
        f2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(f2);
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        //创建内容
        if(values!=null)
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 3);
            row.setHeight((short)(33*20));
            boolean hasValue = false;
            hasValue = values[i][0]==null;
            if(hasValue){
                if("施工梯,塔吊".indexOf(sheetName)!=-1){
                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 5);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    String[] dateArr = date.split("-");
                    cell1.setCellValue("收入合计");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(6);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(G4:G"+ (i + 3) +")");

                    row = sheet.createRow(i + 4);
                    cell1 = row.createCell(0);
                    region = new CellRangeAddress(i+4, i+4, 0, 1);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    row.setHeight((short)(29*20));
                    cell1.setCellValue("合计（台数）");
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(2);
                    region = new CellRangeAddress(i+4, i+4, 2, 4);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    if("塔吊".equals(sheetName)){
                        cell1.setCellValue("委托检验：    复检：   年检：    最大高度：     第一道附墙：    初检： ");
                    }else{
                        cell1.setCellValue("初检：   复检:     年检:    最大高度：   委托检验： ");
                    }
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(5);
                    cell1.setCellValue("总台数");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(6);
                    cell1.setCellValue(i+"台");
                    cell1.setCellStyle(style);
                    row = sheet.createRow(i + 5);
                    row.setHeight((short)(29*20));

                }else if("井架".indexOf(sheetName)!=-1){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 5);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    String[] dateArr = date.split("-");
                    cell1.setCellValue("收入合计");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(6);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(G4:G"+ (i + 3) +")");

                    row = sheet.createRow(i + 4);
                    cell1 = row.createCell(0);
                    region = new CellRangeAddress(i+4, i+4, 0, 1);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    row.setHeight((short)(29*20));
                    cell1.setCellValue("合计（台数）");
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(2);
                    region = new CellRangeAddress(i+4, i+4, 2, 4);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);

                    cell1.setCellValue("初检：   复检：     年检：    委托检验： ");
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(5);
                    cell1.setCellValue("总台数");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(6);
                    cell1.setCellValue(i+"台");
                    cell1.setCellStyle(style);
                    row = sheet.createRow(i + 5);
                    row.setHeight((short)(29*20));


                }else if("吊篮".indexOf(sheetName)!=-1){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 4);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    String[] dateArr = date.split("-");
                    cell1.setCellValue("收入合计");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(5);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(F4:F"+ (i + 3) +")");

                    row = sheet.createRow(i + 4);
                    cell1 = row.createCell(0);
                    region = new CellRangeAddress(i+4, i+4, 0, 1);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    row.setHeight((short)(29*20));
                    cell1.setCellValue("合计（台数）");
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(2);
                    region = new CellRangeAddress(i+4, i+4, 2, 3);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);

                    cell1.setCellValue("委托台数：      复检台数：          年检台数： ");
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(4);
                    cell1.setCellValue("总台数");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(5);
                    cell1.setCellValue(i+"台");
                    cell1.setCellStyle(style);
                    row = sheet.createRow(i + 5);
                    row.setHeight((short)(29*20));

                }else if("防坠器".indexOf(sheetName)!=-1){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 2);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    String[] dateArr = date.split("-");
                    cell1.setCellValue("收入合计");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(3);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(D4:D"+ (i + 3) +")");
                    cell1 = row.createCell(4);
                    row.setHeight((short)(29*20));
                    cell1.setCellValue("收入总计");
                    cell1.setCellStyle(style);
                    cell1 = row.createCell(5);
                    region = new CellRangeAddress(i+3, i+3, 5, 6);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    row.setHeight((short)(29*20));
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(E4:E"+ (i + 3) +")");
                    cell1.setCellStyle(style);

                }else if("三宝钢管扣件".equals(sheetName)){

                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+3, i+3, 0, 2);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    sheet.addMergedRegion(region);
                    String[] dateArr = date.split("-");
                    cell1.setCellValue("");
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(3);
                    cell1.setCellValue("总收入（元）");
                    cell1.setCellStyle(style);

                    cell1 = row.createCell(6);
                    cell1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("SUM(G4:G"+ (i + 3) +")");
                    cell1.setCellStyle(style);
                }


                {

                    HSSFCellStyle style11 = wb.createCellStyle();
                    setStyleBorder(style1);
                    setStyleFont(style1,wb,"宋体",(short)11,false,HSSFCellStyle.ALIGN_LEFT);

                    if("三宝钢管扣件".equals(sheetName)){
                        i-=2;
                    }else if("防坠器".equals(sheetName)){
                        i-=1;
                    }
                    row = sheet.createRow(i + 6);
                    row.setHeight((short)(22*20));
                    HSSFCell cell1 = row.createCell(0);
                    CellRangeAddress region = new CellRangeAddress(i+6, i+6, 0, 6);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1.setCellValue("说明：1、本表金额及总收入均以当月开出发票金额为准；");
                    cell1.setCellStyle(style11);

                    row = sheet.createRow(i + 7);
                    row.setHeight((short)(22*20));
                    cell1 = row.createCell(1);
                    region = new CellRangeAddress(i+7, i+7, 1, 6);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1.setCellValue("2、本表一式三份，分别由受理员、综合室负责人、财务各一份；");
                    cell1.setCellStyle(style11);

                    row = sheet.createRow(i + 8);
                    row.setHeight((short)(22*20));
                    cell1 = row.createCell(1);
                    region = new CellRangeAddress(i+8, i+8, 1, 6);
                    sheet.addMergedRegion(region);
                    setBorderStyle(HSSFCellStyle.BORDER_THIN,region,sheet,wb);
                    cell1.setCellValue("3、本表于次月五日前填报。");
                    cell1.setCellStyle(style11);

                    row = sheet.createRow(i + 9);
                    row.setHeight((short)(22*20));
                    cell1 = row.createCell(1);
                    cell1.setCellValue("编报人：");
                    cell1.setCellStyle(style11);
                    cell1 = row.createCell(3);
                    cell1.setCellValue("核对人：");
                    cell1.setCellStyle(style11);
                    break;
                }

            }else{
                for(int j=0;j<values[i].length;j++){
                    HSSFCell cell1 = row.createCell(j);

                    if((j==6&&"塔吊,施工梯,井架,三宝钢管扣件".indexOf(sheetName)!=-1)||(j==5&&"吊篮".indexOf(sheetName)!=-1)||("防坠器".indexOf(sheetName)!=1)&&(j==4||j==3)||("三宝钢管扣件".equals(sheetName)&&j==6)){
                        try {
                            cell1.setCellValue(Double.parseDouble(values[i][j]));
                        }catch (Exception e){
                            cell1.setCellValue(values[i][j]);
//                            e.printStackTrace();
                        }
                    }else{
                        cell1.setCellValue(values[i][j]);
                    }
                    cell1.setCellStyle(style);
                }
            }
        }

        return wb;
    }

    private static void setBorderStyle(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook wb){
        RegionUtil.setBorderBottom(border, region, sheet, wb);      //下边框
        RegionUtil.setBorderLeft(border, region, sheet, wb);     //左边框
        RegionUtil.setBorderRight(border, region, sheet, wb);    //右边框
        RegionUtil.setBorderTop(border, region, sheet, wb);      //上边框
    }

}
