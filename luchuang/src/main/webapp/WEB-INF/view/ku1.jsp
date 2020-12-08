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

<script src="<c:url value="/js/jquery-1.11.3.js"/>"></script>
<script src="<c:url value="/js/bootstrap.js"/>"></script>
<script>
    var filedata;
    var nowdata;
    //获取库列表
    $.get("word_file",function(data){
        console.log(data);
        filedata = data.date;
        $.each(data.date,function(index){
            var that = this;
            var riqi = "<p style='margin:0;border:1px solid #BCE8F1;position:relative;'><a style='transition:0.8s all;position:relative;'>"+that.date+"</a></p>";
            $(".left").append(riqi);
        })

        $(".cancel").click(function(e){
            $(".mengban").css("display","none");
        })

        $(".sure").click(function(e){
            $.get("del_luru",{"filepath":$("#riqi").val()},function(data){
                console.log(data);
                location.reload();
            })
        })

        $("#jc").click(function(e){
            $(".mengban").css("display","block");
        })

        // 日期选择后的事件
        $(".left > p , .left > p > a").click(function(e){
            if($(e.currentTarget).text()=="日期"){
                return;
            }
            var riqi = $(e.currentTarget).text();
            $(".xz").removeClass("xz");
            $(e.currentTarget).addClass("xz");
            $("a").css("right","0");
            $(e.currentTarget).children("a").css("right","45px");
            $("#riqi").val(riqi);
            $(e.currentTarget).append($("#jc"));
            $("#jc").css("display","block");
            $.each(data.date,function(index){
                var that = this;
                if(that.date == riqi){
                    nowdata = that;
                    $(".right").empty();
                    $.each(that.datas,function(index1){
                        var link = this.url;
                        var ys = "<div style='padding:12px;border-bottom:1px solid #BCE8F1;'>"+
                            "<span style='margin:0 5px;'>"+this.filename+"</span>"+
                            "<div style='float:right;'>"+
                            " <a href='javascript:void(0);'>任务单</a>"+
                            " <a href='javascript:void(0);'>委托单</a>"+
                            (link.bg!=undefined?(" <a href='javascript:void(0);'>报告</a>"):'')+
                            (link.yj!=undefined?(" <a href='javascript:void(0);'>意见</a>"):'')+
                            (link.ysjl!=undefined?("<a href='javascript:void(0);'>原始记录</a>"):'')+
                            (link.hz!=undefined?("<a href='javascript:void(0);'>回执</a>"):'')+
                            "</div>"+
                            "</div>";
                        $(".right").append(ys);
                    })

                    //为下载按钮添加事件
                    $(".right > div > div > a").click(function(e){
                        var text = $(e.currentTarget).text();
                        var filename = $($(e.currentTarget).parent().parent().children("span")[0]).text();
                        var link = "";
                        var date = $("#riqi").val();
                        console.log(filename);
                        console.log(nowdata);
                        $.each(nowdata.datas,function(index1){
                            if(this.filename == filename){
                                filename = encodeURI(encodeURI(filename));
                                text = encodeURI(encodeURI(text))
                                window.open("downloadFile_luru?date=" + date + "&filepath=" + filename + "&mtype=" + text);
                            }
                        })
                    });
                    return;
                }
            })

        })
    });
</script>
</body>
</html>
