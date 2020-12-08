<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title></title>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="<c:url value="/js/jquery.js"/>"></script>
</head>

<body>
<div id="test"></div>
<script>
    $(function(){
        let pwd = prompt("请输入密码:123");
        if(pwd=="123"){
            $(`<div style="font-size:18px;">
                            <p>众程机械   7030（002）标准节 采购日期：2019年8月18日 </p>
                            <p>使用记录：东莞地铁三标、佛山大桥、深圳迷香湖高速、深中通道三标段。</p>
                    </div>`).appendTo("#test");
        }else{
            alert("密码错误");
        }
    });

</script>
</body>
</html>
