package com.luchuang.common;

/**
 * @version 1.0
 * @ClassName UUID
 * @Author PPPL
 * @Date 2019/8/1 15:26
 **/
public class UUID {

    public static String random(){
        return java.util.UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
    }
}
