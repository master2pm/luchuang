package com.luchuang.fileImport.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class PageController {

    @RequestMapping("page/mission")
    public String missionPage(){

        log.info("新增测试页面");
        return "missions";
    }

}
