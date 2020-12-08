<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>任务列表</title>
    <%@include file="../../basic.jsp" %>
    <script src="<c:url value="/js/common.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="../css/bootstrap.css"/>"/>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="../css/common.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="../css/slide.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="../css/bootstrap.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="../css/flat-ui.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="../css/jquery.nouislider.css"/>">
    <base href="<%=basePath%>">
</head>
<body style="overflow:scroll;overflow-x:hidden;">
<div id="app">
    <div class="tablebody ziyuanguanli">
        <div v-for="(item,index) in missions" class="collapse in" aria-expanded="false">
            <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 levl12 coll11" :id="'coll'+index" role="button" data-toggle="collapse" data-parent="#accordion" :href="'#collapseAccount'+index" aria-expanded="false" aria-controls="collapseOne">
                    <div :style="item.priceCount||item.redWarn||item.purpleWarn?'line-height:35px;':''">
                        <span style="margin-right:5x;color:#2196f3;">{{index}}</span>
                        <span style="margin-right:5px;">塔吊:{{item.tdCount}};</span>
                        <span style="margin-right:5px;">升降机:{{item.sjjCount}};</span>
                        <span style="margin-right:5px;">井架:{{item.jjCount}};</span>
                        <span style="margin-right:5px;">吊篮:{{item.dlCount}};</span>
                        <span style="margin-right:5px;">防坠器:{{item.fzqCount}};</span>
                        <span style="margin-right:5px;">三宝钢管扣件:{{item.sbCount}};</span>
                        <span style="margin-right:5px;">数量:{{item.tdCount+item.sjjCount+item.jjCount+item.dlCount+item.fzqCount+item.sbCount}}</span>                                
                    </div>
                    <div :style="item.priceCount||item.redWarn||item.purpleWarn?'line-height:35px;':''">
                        <span v-if="item.priceCount" style="margin-right:5px;">总产值:{{item.priceCount}};</span>
                        <span v-if="item.priceCount" style="margin-right:5px;">未收款:{{item.settleCount}};</span>
                        <img src="image/warn.png" :style="'width:20px;'+!item.redWarn?'display:none;':''">     
                        <img src="image/warn2.png" :style="'width:20px;'+!item.purpleWarn?'display:none;':''">                                        
                    </div>
                </div>
                <div :id="'topB'+index" class="col-lg-4 col-md-4 col-sm-4 col-xs-4 levl2" role="button" data-toggle="collapse" data-parent="#accordion" :href="'#collapseAccount'+index" aria-expanded="false" aria-controls="collapseOne">
                    <span :id="'topB'+index"></span>
                </div>
                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
                    <span></span>
                </div>
                <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                    <button class="btn btn-success btn-xs daochu-btn" data-toggle="modal" :date="item.date" disabled="disabled" style="opacity: 0;">导出</button>
                    <button class="btn btn-danger btn-xs del-all-btn" data-toggle="modal" data-target="#deleteSource" disabled="disabled" style="opacity: 0;" :date="item.date">删除</button>
                </div>
            </div>                      
            <div :id="'collapseAccount'+index" class="collapse" aria-expanded="false" style="overflow: hidden;">
                <div v-for="(item1,index1) in item.list" class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 coll22" :id="'coll'+index+index1" role="button" data-toggle="collapse" data-parent="#accordion" :href="'#collapseAccount'+index+index1" aria-expanded="false" aria-controls="collapseOne">
                        <div :style="'item1.red_warn||item1.purple_warn?"line-height:35px;":""'">
                            <span style="margin-right:5px;">下单人：{{item1.creator}};</span> 
                            <span v-if="'防坠器,三宝钢管扣件'.indexOf(item1.mtype)==-1" style="margin-right:15px;">检测组：{{item1.executive==null?"未分配":item1.executive}};</span>                           
                            <span v-if='"防坠器,三宝钢管扣件".indexOf(item1.mtype)==-1' style="margin-right:15px;">种类：{{item1.mtype}};</span>
                            <span v-if='"防坠器,三宝钢管扣件".indexOf(item1.mtype)==-1' style="margin-right:15px;">数量：{{item1.num}};</span>
                            <span v-if='"防坠器,三宝钢管扣件".indexOf(item1.mtype)==-1'>{{item1.priceCount?("单价："+item1.price+";"):""}}</span>
                            <span v-if='"防坠器,三宝钢管扣件".indexOf(item1.mtype)==-1'>工程地址&项目名称：{{item1.project_name}};</span>

                            <span v-if='item1.mtype=="防坠器"' style="margin-right:15px;">种类：{{item1.mtype}};</span>
                            <span v-if='item1.mtype=="防坠器"' style="margin-right:15px;">数量：{{item1.num}};</span>
                            <span v-if='item1.mtype=="防坠器"'>{{item.priceCount?("单价："+item1.price+";"):""}}</span>
                            <span v-if='item1.mtype=="防坠器"'>委托单位&送样人：{{item1.wtdw}};</span>

                            <span v-if='item1.mtype=="三宝钢管扣件"' style="margin-right:15px;">种类：{{item1.mtype}};</span>
                            <span v-if='item1.mtype=="三宝钢管扣件"' style="margin-right:15px;">{{item.priceCount?("总价："+item1.count+";"):""}}</span>
                            <span v-if='item1.mtype=="三宝钢管扣件"'>工程地址&项目名称：{{item1.project_name}};</span>
                        </div>                    
                        <div :style="'line-height:35px;'+(!(item.redWarn!=undefined&&item.redWarn.toString().indexOf(mis.id)!=-1)&&!(item.purpleWarn!=undefined&&item.purpleWarn.toString().indexOf(mis.id)!=-1)?'display:none;':'')">                                            
                            <img src="image/warn.png" :style="'width:20px;'+(!(item.redWarn!=undefined&&item.redWarn.toString().indexOf(mis.id)!=-1)?'display:none;':'')">     
                            <img src="image/warn2.png" :style="'width:20px;'+(!(item.purpleWarn!=undefined&&item.purpleWarn.toString().indexOf(mis.id)!=-1)?'display:none;':'')">                                        
                        </div>
                        <button :mid="item1.id" class="btn btn-danger btn-xs btn-change-price" style="position:absolute;right:40px;top:25px;background:#ff9800;">修改单价/总价</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
   
    var vm = new Vue({
        el: '#app',
        data: {
            missions:[],
        },
        created() {
            console.log(2323)
            this.initData();
        },
        methods: {
            initData(searchkey){
                let self = this;
                let post_data = {};
                post_data.sort = "123";
                post_data.pwd = $(".word-input").val();
                let dd = new Date();
                post_data.date = dd.getFullYear()+"-"+(dd.getMonth()+1);
                $.ajax({
                    url:"mission",
                    type:"GET",
                    data:post_data,
                    success:function(data){
                        console.log(data);
                        window.mission_data = data.data;  
                        console.log("ddd",data);              
                        self.missions = data.data;
                    }
                })
            }
        }
    })
</script>
</body>
</html>
