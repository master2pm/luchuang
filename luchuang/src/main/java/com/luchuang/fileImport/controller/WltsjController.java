package com.luchuang.fileImport.controller;

import com.luchuang.common.ResultBean;
import com.luchuang.fileImport.service.WltsjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @ClassName WltsjController
 * @Author PPPL
 * @Date 2019/8/7 15:46
 **/
@RestController
public class WltsjController {

    @Autowired
    private WltsjService wltsjService;

    @RequestMapping(value = "/wltsj/{id}",method = RequestMethod.DELETE)
    public ResultBean delFile(@PathVariable(value = "id")String id, @RequestParam(value = "pwd")String pwd){

        ResultBean resultBean = null;
        boolean flag = wltsjService.delWltsj(id,pwd)!=0;
        resultBean = new ResultBean(flag);
        return resultBean;

    }


}
