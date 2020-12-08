package com.luchuang.fileImport.util;


import com.luchuang.fileImport.controller.ReportFormController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class FileAop {

    private Logger log = Logger.getLogger(FileAop.class.toString());

    @Pointcut("execution(public * com.luchuang.fileImport.controller.FileWordImportController.*(..))")
    public void excudeService(){}

    @Before("excudeService()")
    public void test(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //打印请求内容
        log.info("===============请求内容===============");
        log.info("请求地址:"+request.getRequestURL().toString());
        log.info("请求方式:"+request.getMethod());
        log.info("请求类方法:"+joinPoint.getSignature());
        log.info("请求类方法参数:"+ Arrays.toString(joinPoint.getArgs()));
        log.info("===============请求内容===============");

        log.info("切面执行了!!");
    }

    @AfterReturning(returning = "o",pointcut = "excudeService()")
    public void methodAfterReturing(Object o ) {
        log.info("--------------返回内容----------------");
        log.info("Response内容:啦啦啦");
        log.info("--------------返回内容----------------");
    }

    }
