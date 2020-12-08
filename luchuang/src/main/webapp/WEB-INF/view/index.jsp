<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>麓创 文档自动生成系统</title>
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
        .input-group{
            margin-bottom:2px;
        }
        .panel{
            margin-bottom:0px;
        }
        .form-group{
            position:relative;
            display: inline-block;
            width: 230px;
            margin-right: 55px;
        }
        .his{
            display:none;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/test.css"/> "/>
</head>

<body style="margin:0;">
<div class="" style="width:100%;">
    <div class="top" style="height:200px;width:100%;position:relative;">
        <img src="<c:url value="/image/1-02.jpg"/>" style="width:1920px;height:200px;">
        <h1 style="position:absolute;top:70px;left:20px;">中山市麓创建设机械安全检测有限公司 <small>文档自动生成系统</small></h1>
    </div>
    <div class="" style="">
        <div class="" style="margin:10px auto 5px;width:80%;">
            <a href="ku" target="_blank" style="margin-left:50px;">服务器里面的文档</a>
        </div>
        <div class="" style="width:1800px;margin:0 auto;">
            <div class="panel panel-info" style="width:100%;">
                <div style="margin:0 auto;width:1700px;">
                    <form action="add_file" method="post" enctype="multipart/form-data" style="margin:42px auto 0;width:1442px;">
                        <div class="form-group">
                            <label>种类</label>
                            <select name="mtype" class="form-control">
                                <option value="-1" selected>请选择种类</option>
                                <option value="物料提升机">物料提升机</option>
                                <option value="塔机">塔机</option>
                                <option value="施工升降机">施工升降机</option>
                                <option value="防坠器">防坠器</option>
                                <option value="钢管、脚手架扣件">钢管、脚手架扣件</option>
                                <option value="安全建材">安全建材</option>
                            </select>
                        </div>
                        <div class="form-group" style="position:relative;">
                            <label>文件名</label>
                            <input type="type" class="form-control" name="filename">
                        </div>
                        <div class="form-group">
                            <label>编号</label>
                            <input type="type" class="form-control" name="bh">
                        </div>

                        <div class="form-group">
                            <label>委托单位</label>
                            <input type="type" class="form-control" name="wtdw">
                        </div>
                        <div class="form-group">
                            <label>联系人</label>
                            <input type="type" class="form-control" name="name">
                        </div>
                        <div class="form-group">
                            <label>电话</label>
                            <input type="type" class="form-control" name="phone">
                        </div>
                        <div class="form-group">
                            <label>安装单位</label>
                            <input type="type" class="form-control" name="azdw">
                        </div>
                        <div class="form-group">
                            <label>使用单位</label>
                            <input type="type" class="form-control" name="sydw">
                        </div>
                        <div class="form-group">
                            <label>工程名称</label>
                            <input type="type" class="form-control" name="gcmc">
                        </div>
                        <div class="form-group">
                            <label>工程地址</label>
                            <input type="type" class="form-control" name="gcdz">
                        </div>
                        <div class="form-group">
                            <label>设备型号</label>
                            <input type="type" class="form-control" name="sbxh">
                        </div>
                        <div class="form-group">
                            <label>备案编号</label>
                            <input type="type" class="form-control" name="babh">
                        </div>
                        <div class="form-group">
                            <label>生产厂家</label>
                            <input type="type" class="form-control" name="sccj">
                        </div>
                        <div class="form-group">
                            <label>出厂编号</label>
                            <input type="type" class="form-control" name="ccbh">
                        </div>
                        <div class="form-group">
                            <label>生厂日期</label>
                            <input type="type" class="form-control" placeholder="生产日期(格式 20171228)" name="ccri">
                        </div>
                        <button type="button" class="btn btn-default test" style="margin: 50px 150px 0px 580px;font-size: 23px;padding: 7px 20px;">提交</button>
                        <button type="button" class="btn btn-default clean" style="margin: 50px 0px 0px;font-size: 23px;padding: 7px 20px;">清空</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>





<script src="<c:url value="/js/jquery.js"/>"></script>
<script src="<c:url value="/js/common.js"/>"></script>
<script src="<c:url value="/js/bootstrap.js"/>"></script>

<script>
    Location.prototype.get_attrs = function(){
        let strs = location.href.substring(location.href.indexOf("?")+1);
        strs = strs.split("&");
        let obj = [];
        for(var i = 0 ; i <strs.length ;i++){
            let tempstrs = strs[i].split("=");
            let tempobj = {};
            tempobj.name = tempstrs[0];
            tempobj.val = tempstrs[1];
            obj.push(tempobj);
        }
        console.log(obj)
        return obj;
    }
    var url_attr = location.get_attrs();
    let post_data = {};
    for(let item of url_attr){
        post_data[item.name] = item.val;
    }
    if(url_attr.filename!=""&&url_attr.filename!=undefined){
        $.ajax({
            url:"getfilemsg",
            type:"GET",
            data:post_data,
            success:function(data){
                console.log(data);
                if(data.status==1){
                    let inputs = $("input");
                    $.each(inputs,function(){
                        let ee = $(this);
                        ee.val(data.data[ee.attr("name")]);
                    })
                    $("select").val(data.data.mtype);
                }
            }
        })
    }

    //清空输入框里面的内容
    $(".clean").click(function(){
        $("input").val("");
    })



    $(".test").click(function(){
        if($($("select")[0]).val()=="-1"){
            alert("请选择种类");
            return;
        }
        let post_json = JSON.parse($("form").serializeJson());
        if(location.get_attr("date")!=""){
            post_json.date = location.get_attr("date");
            post_json.edit_file_name = location.get_attr("filename");
        }
        // for(let key in post_json){
        //     let item = post_json[key];
        //     post_json[key] = encodeURI(encodeURI(item));
        // }
        $.ajax({
            url:"add_file",
            data: JSON.stringify(post_json),
            type:"POST",
            contentType : 'application/json;charset=utf-8', //设置请求头信息
            dataType : "json",
            success:function(data){
                console.log(data);
                if(data.status==1){
                    location.href = "ku";
                }
            }
        })
        // $("form").submit();
    })

</script>
</body>
</html>