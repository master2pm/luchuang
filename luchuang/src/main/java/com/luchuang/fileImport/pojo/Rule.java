package com.luchuang.fileImport.pojo;

/**
 * @version 1.0
 * @ClassName rule
 * @Author PPPL
 * @Date 2019/9/4 17:48
 **/
public enum Rule {
    PWDERROR("密码错误",-1),NORULE("没有权限",0),SUCCESS("操作成功",1),ERROR("未知错误，请稍后重试",2),NOTCOMPLETE("信息未填写完整",3);
    private String name;
    private int num;

    private Rule(String name,int num){
        this.name = name;
        this.num = num;
    }

    public static String getName(int num){
        for(Rule r : Rule.values()){
            if(r.getNum()==num){
                return r.getName();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }}
