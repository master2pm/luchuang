<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>麓创 文档自动生成 文档库</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <style media="screen">
        label{
            font-size: 23px;
            display:inline-block;
            margin-bottom: 2px;
            height:40px;
        }
        input{
            font-size: 23px;
            height:40px;
            margin-bottom: 2px;
        }
        .right > div > div > a{
            margin-right: 37px;
        }
        p{
            text-align: center;
        }
        a{
            cursor:pointer;
            position: relative;
            right:0;
        }
        .xz{
            background-color: blanchedalmond;
        }
        #jc{
            position:absolute;
            width:20px;
            right:5px;
            top:2px;
            display: none;
        }
        .mengban{
            width:100%;
            height:100%;
            position:fixed;
            top:0;
            left:0;
            overflow:hidden;
            display: none;
        }
    </style>
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.css"/>"/>
</head>

<body style="margin:0;">
<div class="panel panel-info" style="width:1616px;margin:30px auto 0;">
    <a href="index" style="margin-left:20px;">生成文档到服务器</a>
</div>
<div class="panel panel-info" style="width:1616px;position:relative;margin:0 auto;">
    <p style="margin:0;padding:20px;font-size: 50px;text-align: left;">文档库</p>
    <div style="width:1616px;">
        <div class="left panel panel-info" style="width:200px;font-size:17px;float:left;">
            <p style='margin:0;'>日期</p>
            <img src="<c:url value="/image/1-04.png"/>" id="jc">
        </div>
        <div class="right panel panel-info" style="width:1413px;font-size:17px;float:left;margin-left:2px;">

        </div>
        <input type='text' id="riqi" style='display:none;'/>
    </div>
    <div class="mengban">
        <div class="cancel" style="width:100%;height:100%;background-color:black;opacity: 0.5;z-index:-1;"></div>
        <div class="panel panel-info" style="width:300px;height:100px;background-color:white;z-index:2;position:absolute;top:35%;left:40%;">
            <p style="font-size:25px;margin-top:15px;">确定要删除该文件？</p>
            <a style="margin-left:100px;margin-right:20px;" href="javascript:void(0)" class="sure">确定</a>
            <a href="javascript:void(0)" class="cancel">取消</a>
        </div>
    </div>
</div>

<script src="<c:url value="/js/jquery.js"/>"></script>
<script src="<c:url value="/js/bootstrap.js"/>"></script>
<script src="<c:url value="/js/get_word_file.js"/>"></script>
</body>
</html>
