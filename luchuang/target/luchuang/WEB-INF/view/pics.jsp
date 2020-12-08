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
    <script src="<c:url value="/js/jquery.js"/>"></script>
    <script src="<c:url value="/js/common.js"/>"></script>
    <script src="<c:url value="/js/bootstrap.js"/>"></script>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/slide.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/flat-ui.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.nouislider.css"/>">
    
<style type="text/css">   
    .del-btn{
        position: absolute;
        right:0;
        bottom:0;
        z-index:999;
    }
    .float, .lastPic{    
        float:left;    
        width : 500px;    
        height: 500px;    
        overflow: hidden;    
        border: 1px solid #CCCCCC;    
        border-radius: 10px;    
        padding: 5px;    
        margin: 5px;    
    }    
    img{    
        position: relative;    
    }    
    .result{    
        width: 500px;    
        height: 500px;    
        text-align: center;    
        box-sizing: border-box;    
        position: relative;
    }       
    #file_input{  
        display: none;  
    }      
    .delete{  
        width: 200px;  
        height:200px;  
        position: absolute;  
        text-align: center;  
        line-height: 200px;  
        z-index: 10;  
        font-size: 30px;  
        background-color: rgba(255,255,255,0.8);  
        color: #777;  
        opacity: 0;  
        transition-duration: 0.7s;  
        -webkit-transition-duration: 0.7s;   
    }  
    .delete:hover{  
        cursor: pointer;  
        opacity: 1;  
    }  
  
</style>   
</head>

<body>
<div id="wid" style="display: none;">${wid}</div>
<div id="pics" style="display: none;">${pics}</div>
<div>
    <div>
        <div>
            <div>密码: <input type="password" id="word-input" value=""/></div>
            <div class="container">    
                <label>请选择一个图像文件：</label>  
                <button id="select">(重新)选择图片</button>  
                <button id="add">(追加)图片</button>  
                <input type="file" id="file_input" multiple/> <!--用input标签并选择type=file，记得带上multiple，不然就只能单选图片了-->    
                <button id="submit">提交</button>
            </div>                         
        </div>
    </div>
    <div id="allImg"></div>
</div>
<script src="<c:url value="/js/pics.js"/>"></script>
</body>
</html>
