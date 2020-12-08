package com.luchuang.fileImport.service;

import com.luchuang.fileImport.pojo.Wltsj;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface IFileWordService {

    void addFile(Wltsj wl,boolean one_key);

    /**
     * 创建整改意见 和回执
     * */
    public boolean createCommonWord(Wltsj wl);

    /**
     *  物料提升机
     * */
    public boolean createWltsj(Wltsj wl);

    public File createWltsj(Wltsj wl,String mtype);

    /**
     *  塔机
     * */
    public boolean createTj(Wltsj wl);

    public File createTj(Wltsj wl,String mtype);
    /**
     *  施工升降机
     * */
    public boolean createSgt(Wltsj wl);

    public File createSgt(Wltsj wl,String mtype);

    /**
     *  防坠器
     * */
    public boolean createfzq(Wltsj wl);

    public File createfzq(Wltsj wl,String mtype);

    /**
     *  钢管 脚手架
     * */
    public boolean creategj(Wltsj wl);

    public File creategj(Wltsj wl,String mtype);
    /**
     *  安全建材
     * */
    public boolean createaqqc(Wltsj wl);

    public File createaqqc(Wltsj wl,String mtype);

    public File addFile(Wltsj wl, String mtype);

    public List wordList(String searchkey);

    public Map readTxt(String file_name,String date);

    public HSSFWorkbook buildReportForm(String date);

    int editFileAttr(String pwd,Long id,String attr,String text) throws Exception;

    int editFileAttr(String pwd,Long id,String attr,String text,String mid) throws Exception;
}
