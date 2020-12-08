<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>麓创 文档自动生成 文档库</title>
    <meta http-equiv="Pragma" content="no-cache"> 
    <meta http-equiv="Cache-Control" content="no-cache"> 
    <meta http-equiv="Expires" content="0"> 
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <script src="<c:url value="/js/jquery.js"/>"></script>
    <script src="<c:url value="/js/common.js"/>"></script>
    <script src="<c:url value="/js/bootstrap.js"/>"></script>
    <script>
        $(function() {
            $(".meun-item").click(function() {
                $(".meun-item").removeClass("meun-item-active");
                $(this).addClass("meun-item-active");
                var itmeObj = $(".meun-item").find("img");
                itmeObj.each(function() {
                    var items = $(this).attr("src");
                    items = items.replace("_grey.png", ".png");
                    items = items.replace(".png", "_grey.png")
                    $(this).attr("src", items);
                });
                var attrObj = $(this).find("img").attr("src");
                ;
                attrObj = attrObj.replace("_grey.png", ".png");
                $(this).find("img").attr("src", attrObj);
            });
            $("#topAD").click(function() {
                $("#topA").toggleClass(" glyphicon-triangle-right");
                $("#topA").toggleClass(" glyphicon-triangle-bottom");
            });
            $("#topBD").click(function() {
                $("#topB").toggleClass(" glyphicon-triangle-right");
                $("#topB").toggleClass(" glyphicon-triangle-bottom");
            });
            $("#topCD").click(function() {
                $("#topC").toggleClass(" glyphicon-triangle-right");
                $("#topC").toggleClass(" glyphicon-triangle-bottom");
            });
            $(".toggle-btn").click(function() {
                $("#leftMeun").toggleClass("show");
                $("#rightContent").toggleClass("pd0px");
            })
        })
    </script>
    <style>
        .w2{
            overflow: hidden;
        }
        .w2>.form-group{
            float: left;
            width:50%;
        }
        .ccenter>th{
            text-align: center;    
        }
    </style>
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.css"/>"/>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/slide.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/flat-ui.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.nouislider.css"/>">
    <style type="text/css">
        .row{
            font-size: 20px !important;
            overflow: hidden !important;
        }
        a{
            color: black;
        }
        a:visited{
            color: darkred;
        }
    </style>
</head>

<body>
<div id="wrap">
    <!-- 左侧菜单栏目块 -->
    <div class="leftMeun" id="leftMeun">
        <div id="logoDiv">
            <p id="logoP"><span>麓创</span></p>
        </div>
        <div style="display: none" id="optionBtn-date">${optionBtn}</div>
        <div style="display: none" id="report-date">${report_list}</div>
        <div style="display: none" id="type-data-list">${type_list}</div>
        <div style="display: none" id="download-show">${down_show}</div>
        <div class="meun-title">文档</div>
        <div class="meun-item meun-item-active" id="meun-item1" href="#sour" aria-controls="sour" role="tab" data-toggle="tab"><img src="<c:url value="image/icon_source.png"/>">资源管理</div>
        <div class="meun-item" id="meun-item2" href="#sour1" aria-controls="sour1" role="tab" data-toggle="tab"><img src="<c:url value="image/icon_source.png"/>">文档导出</div>
        <div class="meun-item" id="meun-item3" href="#sour2" aria-controls="sour2" role="tab" data-toggle="tab"><img src="<c:url value="image/icon_source.png"/>">任务分配</div>
    </div>
    <!-- 右侧具体内容栏目 -->
    <div id="rightContent">
        <a class="toggle-btn" id="nimei">
            <i class="glyphicon glyphicon-align-justify"></i>
        </a>
        <!-- Tab panes -->
        <div class="tab-content">
            <!-- 任务分配 -->
            <div role="tabpanel" class="tab-pane" id="sour2">
            </div>
            <!-- 资源管理模块 -->
            <div role="tabpanel" class="tab-pane" id="sour1">
                <div class="check-div form-inline">
                    <input type="text" class="form-control input-sm">
                    <button class="btn btn-yellow btn-xs search-xls-btn" data-toggle="modal">搜索</button>
                    <input type="month" id="report-sel" name="report-sel" min="2017-03" style="height: 25px;" value="">                    
                    <button class="btn btn-xs report-all-btn">导出小组汇总表</button>
                    <button class="btn btn-xs new-report-all-btn">导出机器汇总表</button>
                    <button class="btn btn-xs report-earning-btn">导出收入汇总表</button>
                    <button class="btn btn-xs report-accept-btn">导出受理情况汇总表</button>
                    <button class="btn btn-xs report-btn" disabled style="opacity: 0;">导出选中的文档</button>
                </div>
                <div class="data-div">
                    <div class="wendandaochu tablebody" style="margin:0;">

                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane active" id="sour">
                <div class="check-div form-inline">
                    <button class="btn btn-yellow btn-xs" data-toggle="modal" data-target="#addSource">添加资源</button>
                    <input type="text" class="form-control input-sm search-input" placeholder="搜索内容">
                    <button class="btn btn-yellow btn-xs search-btn" data-toggle="modal">搜索</button>
                    <input type="password" class="form-control input-sm word-input" placeholder="输入密码并敲回车">
                    <input type="month" id="report-sel1" name="report-sel1" min="2017-03" style="height: 25px;" value="">
                </div>
                <div class="data-div">
                    <div class="row tableHeader">
                        <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 ">
                            编码
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
                            名称
                        </div>
                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">

                        </div>
                        <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                            操作
                        </div>
                    </div>
                    <div class="tablebody ziyuanguanli">


                    </div>
                </div>            

                <!--弹出窗口 添加任务-->
                <div class="modal fade" id="addSource" role="dialog" aria-labelledby="gridSystemModalLabel">
                    <div class="modal-dialog" role="document" style="width:900px;">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="gridSystemModalLabel">添加任务</h4>
                            </div>
                            <div class="modal-body">
                                <div class="container-fluid">
                                    <form class="form-horizontal mission-msg-form">
                                        <div class="form-group">
                                            <label for="exampleInput1" class="col-xs-2 control-label">*检测种类：</label>
                                            <div class="col-xs-7">
                                                <select name="mtype" class="form-control input-sm" id="add_type">
                                                    <option value="-1" selected>请选择种类</option>
                                                    <option value="塔吊">塔吊</option>
                                                    <option value="升降机">升降机</option>
                                                    <option value="井架">井架</option>
                                                    <option value="吊篮">吊篮</option>
                                                    <option value="防坠器">防坠器</option>
                                                    <option value="三宝钢管扣件">三宝钢管扣件</option>
                                                </select>

                                            </div>
                                        </div>
                                        <div class="w2">
                                            <div class="form-group one-scene two-scene tree-scene">
                                                <label for="sName" class="col-xs-5 control-label">检测费负责单位：</label>
                                                <div class="col-xs-4">
                                                    <input type="text" class="form-control input-sm add_detect_company" placeholder="" name="detect_company">
                                                </div>
                                            </div>
                                            <div class="form-group one-scene two-scene tree-scene">
                                                <label for="sLink" class="col-xs-5 control-label">检测费负责人：</label>
                                                <div class="col-xs-4 ">
                                                    <input type="text" class="form-control input-sm add_detect_contact" placeholder="" name="detect_contact">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="w2">
                                            <div class="form-group one-scene two-scene tree-scene">
                                                <label for="sName" class="col-xs-5 control-label">负责人电话：</label>
                                                <div class="col-xs-4 ">
                                                    <input type="text" class="form-control input-sm add_detect_contact_number" placeholder="" name="detect_contact_number">
                                                </div>
                                            </div>
                                            <div class="form-group one-scene">
                                                <label for="sLink" class="col-xs-5 control-label">现场联系人：</label>
                                                <div class="col-xs-4 ">
                                                    <input type="text" class="form-control input-sm add_scene_contact" placeholder="" name="scene_contact">
                                                </div>
                                            </div>
                                            <div class="form-group two-scene">
                                                <label for="sLink" class="col-xs-5 control-label">委托单位：</label>
                                                <div class="col-xs-4 ">
                                                    <input type="text" class="form-control input-sm add_wtdw" placeholder="" name="wtdw">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="w2">
                                            <div class="form-group one-scene">
                                                <label for="sName" class="col-xs-5 control-label">现场联系人电话：</label>
                                                <div class="col-xs-4">
                                                    <input type="text" class="form-control input-sm add_scene_contact_number" placeholder="" name="scene_contact_number">
                                                </div>
                                            </div>
                                            <div class="form-group one-scene two-scene">
                                                <label for="sLink" class="col-xs-5 control-label">数量：</label>
                                                <div class="col-xs-4">
                                                    <input type="text" class="form-control input-sm add_num" placeholder="" name="num">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="w2">
                                            <div class="form-group one-scene tree-scene">
                                                <label for="sName" class="col-xs-5 control-label">工程地址/项目名称：</label>
                                                <div class="col-xs-4">
                                                    <input type="email" class="form-control input-sm add_project_name" placeholder="" name="project_name">
                                                </div>
                                            </div>
                                            <div class="form-group one-scene two-scene">
                                                <label for="sLink" class="col-xs-5 control-label">单价：</label>
                                                <div class="col-xs-4">
                                                    <input type="text" class="form-control input-sm add_price" placeholder="" name="price">
                                                </div>
                                            </div>
                                            <div class="form-group tree-scene">
                                                <label for="sLink" class="col-xs-5 control-label">总价：</label>
                                                <div class="col-xs-4">
                                                    <input type="text" class="form-control input-sm add_count" placeholder="" name="count">
                                                </div>
                                            </div>
                                        </div> 
                                        <div class="w2 tree-scene">
                                            <div class="form-group">
                                                <label for="sLink" class="col-xs-5 control-label">三宝委托单：</label>
                                                <div class="col-xs-6">
                                                    <div>安全帽：<input type="checkbox" class="add_checkbox" name="safe_h"/></div>
                                                    <div>安全网：<input type="checkbox" class="add_checkbox" name="safe_w"/></div>
                                                    <div>安全带：<input type="checkbox" class="add_checkbox" name="safe_d"/></div>                                                                                                                                                                                                                
                                                </div>
                                            </div> 
                                            <div class="form-group">
                                                <label for="sLink" class="col-xs-5 control-label">钢管扣件委托单：</label>
                                                <div class="col-xs-6">
                                                    <div>直角扣件：<input type="checkbox" class="add_checkbox" name="kou0"/></div>
                                                    <div>旋转扣件：<input type="checkbox" class="add_checkbox" name="kou1"/></div>
                                                    <div>对接扣件：<input type="checkbox" class="add_checkbox" name="kou2"/></div>
                                                    <div>钢管：<input type="checkbox" class="add_checkbox" name="kou3"/></div>                                                                                                                                                                                                                                                                 
                                                </div>                                            
                                            </div> 
                                        </div>                                      
                                    </form>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-xs btn-xs btn-white close-mission-btn" data-dismiss="modal">取 消</button>
                                <button type="button" class="btn btn-xs btn-xs btn-green add-mission-btn">保 存</button>
                            </div>
                        </div>
                        <!-- /.modal-content -->
                    </div>
                    <!-- /.modal-dialog -->
                </div>
                <!-- /.modal -->






                <!--修改资源弹出窗口-->
                <div class="modal fade" id="changeSource" role="dialog" aria-labelledby="gridSystemModalLabel">
                    <div class="modal-dialog" role="document" style="width:900px;">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="gridSystemModalLabel">任务单</h4>
                                <div class="edit-one">
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">任务生成时间：</label>
                                            <label class="edit1-create_time"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">任务下单人：</label>
                                            <label class="edit1-creator"></label>
                                        </div>
                                    </div> 
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">检测组：</label>
                                            <label class="edit1-executive"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">检测费负责单位：</label>
                                            <label class="edit1-detect_company"></label>
                                        </div>
                                    </div>
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">检测费负责人：</label>
                                            <label class="edit1-detect_contact"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">负责人电话：</label>
                                            <label class="edit1-detect_contact_number"></label>                                            
                                        </div>
                                    </div>   
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">现场联系人：</label>
                                            <label class="edit1-scene_contact"></label>   
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">现场联系人电话：</label>                                            
                                            <label class="edit1-scene_contact_number"></label>
                                        </div>
                                    </div>   
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">数量：</label>
                                            <label class="edit1-num"></label>   
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">工程地址/项目名称：</label>                                            
                                            <label class="edit1-project_name"></label>
                                        </div>
                                    </div>                                   
                                </div>
                                <div class="edit-two">
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">任务生成时间：</label>
                                            <label class="edit1-create_time"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">任务下单人：</label>
                                            <label class="edit1-creator"></label>
                                        </div>
                                    </div> 
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">检测组：</label>
                                            <label class="edit1-executive"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">检测费负责单位：</label>
                                            <label class="edit1-detect_company"></label>
                                        </div>
                                    </div>
                                    <div class="w2">          
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">检测费负责人：</label>
                                            <label class="edit1-detect_contact"></label>
                                        </div>                              
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">负责人电话：</label>
                                            <label class="edit1-detect_contact_number"></label>                                            
                                        </div>
                                    </div>   
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">委托单位：</label>
                                            <label class="edit1-wtdw"></label>
                                        </div>
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">数量：</label>
                                            <label class="edit1-num"></label>   
                                        </div>
                                    </div>                                       
                                </div>
                                <div class="edit-tree">
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">任务生成时间：</label>
                                            <label class="edit1-create_time"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">任务下单人：</label>
                                            <label class="edit1-creator"></label>
                                        </div>
                                    </div> 
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">检测组：</label>
                                            <label class="edit1-executive"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">检测费负责单位：</label>
                                            <label class="edit1-detect_company"></label>
                                        </div>
                                    </div>
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">检测费负责人：</label>
                                            <label class="edit1-detect_contact"></label>
                                        </div>
                                        <div class="form-group">
                                            <label for="sLink" class="col-xs-5 control-label">负责人电话：</label>
                                            <label class="edit1-detect_contact_number"></label>                                            
                                        </div>
                                    </div>   
                                    <div class="w2">
                                        <div class="form-group ">
                                            <label for="sName" class="col-xs-5 control-label">工程地址/项目名称：</label>
                                            <label class="edit1-project_name"></label>   
                                        </div>
                                    </div>                                      
                                </div>
                                <h4 class="modal-title" id="gridSystemModalLabel">报告信息</h4>
                            </div>
                            <div class="modal-body">
                                <div class="container-fluid">
                                    <form class="form-horizontal">
                                        <div class="form-group">
                                            <label for="exampleInput1" class="col-xs-1 control-label">种类：</label>
                                            <div class="col-xs-7">
                                                <select name="mtype" class="form-control input-sm edit-mtype" disabled>
                                                    <option value="-1" selected>请选择种类</option>
                                                    <option value="塔吊">塔吊</option>
                                                    <option value="升降机">升降机</option>
                                                    <option value="井架">井架</option>
                                                    <option value="吊篮">吊篮</option>
                                                    <option value="防坠器">防坠器</option>
                                                    <option value="三宝钢管扣件">三宝钢管扣件</option>
                                                </select>

                                            </div>
                                        </div>
                                        <div class="w2">
                                            <div class="form-group" style="display:none;">
                                                <label for="sName" class="col-xs-3 control-label">文件名：</label>
                                                <div class="col-xs-4 ">
                                                    <input type="text" class="form-control input-sm duiqi edit-filename" placeholder="" name="filename">                                                    
                                                    <input type="text" class="form-control input-sm duiqi edit-id" name="id" style="display: none;">
                                                    <input type="text" class="form-control input-sm duiqi edit-mission_id" name="mission_id" style="display: none;">
                                                    <input type="text" class="form-control input-sm duiqi edit-price" name="price" style="display: none;">
                                                </div>
                                            </div>                                            
                                            <div class="form-group">
                                                <label for="sLink" class="col-xs-3 control-label">委托编号：</label>
                                                <div class="col-xs-4 ">
                                                    <input type="text" class="form-control input-sm duiqi edit-bh" placeholder="" name="bh">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="sOrd" class="col-xs-3 control-label">委托单位：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-wtdw" placeholder="" name="wtdw">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="sKnot" class="col-xs-3 control-label">联系人：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-name" placeholder="" name="name">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="sKnot" class="col-xs-3 control-label">电话：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-phone" placeholder="" name="phone">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">安装单位：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-azdw" placeholder="" name="azdw">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">使用单位：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-sydw" placeholder="" name="sydw">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one edit-tree">
                                                <label for="sKnot" class="col-xs-3 control-label">工程名称：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-gcmc" placeholder="" name="gcmc">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one edit-tree">
                                                <label for="sKnot" class="col-xs-3 control-label">工程地址：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-gcdz" placeholder="" name="gcdz">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">设备型号：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-sbxh" placeholder="" name="sbxh">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">产权单位：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-property_unit" placeholder="选填" name="property_unit">
                                                </div>
                                            </div>  
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">备案编号：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-babh" placeholder="" name="babh">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">生产厂家：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-sccj" placeholder="" name="sccj">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">生产编号：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-ccbh" placeholder="" name="ccbh">
                                                </div>
                                            </div>
                                            <div class="form-group edit-one">
                                                <label for="sKnot" class="col-xs-3 control-label">生厂日期：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-ccri" placeholder="生产日期(格式 20171228)" name="ccri">
                                                </div>
                                            </div>                                            
                                            <div class="form-group sanbao edit-tree aqm">
                                                <label for="sKnot" class="col-xs-4 control-label">安全帽：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-aqmbh" placeholder="" name="aqmbh">
                                                </div>
                                            </div> 
                                            <div class="form-group sanbao edit-tree aqw">
                                                <label for="sKnot" class="col-xs-4 control-label">安全网：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-maqmbh" placeholder="" name="maqmbh">
                                                </div>
                                            </div> 
                                            <div class="form-group sanbao edit-tree aqd">
                                                <label for="sKnot" class="col-xs-4 control-label">安全带：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-aqdbh" placeholder="" name="aqdbh">
                                                </div>
                                            </div> 
                                            <div class="form-group gangguang edit-tree zjkj">
                                                <label for="sKnot" class="col-xs-4 control-label">直角扣件：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-zjbh" placeholder="" name="zjbh">
                                                </div>
                                            </div> 
                                            <div class="form-group gangguang edit-tree xzkj">
                                                <label for="sKnot" class="col-xs-4 control-label">旋转扣件：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-xzbh" placeholder="" name="xzbh">
                                                </div>
                                            </div> 
                                            <div class="form-group gangguang edit-tree djkj">
                                                <label for="sKnot" class="col-xs-4 control-label">对接扣件：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-djbh" placeholder="" name="djbh">
                                                </div>
                                            </div> 
                                            <div class="form-group gangguang edit-tree gg">
                                                <label for="sKnot" class="col-xs-4 control-label">钢管：</label>
                                                <div class="col-xs-8">
                                                    <input type="text" class="form-control input-sm duiqi edit-ggbh" placeholder="" name="ggbh">
                                                </div>
                                            </div>                                             
                                        </div>                                
                                    </form>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-xs btn-white" data-dismiss="modal">取 消</button>
                                <button type="button" class="btn btn-xs btn-green edit-save-btn">保 存</button>
                                <button type="button" class="btn btn-xs btn-green edit-save-all-btn" style="float:left;">一键填写</button>
                            </div>
                        </div>
                        <!-- /.modal-content -->
                    </div>
                    <!-- /.modal-dialog -->
                </div>
                <input type="text" class="edit-date" name="date" style="display: none;"/>
                <input type="text" class="edit-file-name" name="edit_file_name" style="display: none;"/>
                <!-- /.modal -->
                <!--弹出删除资源警告窗口-->
                <div class="modal fade" id="deleteSource" role="dialog" aria-labelledby="gridSystemModalLabel">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="gridSystemModalLabel">提示</h4>
                            </div>
                            <div class="modal-body">
                                <div class="container-fluid">
                                    确定要删除该资源？删除后不可恢复！
                                </div>
                            </div>
                            <input type="text" name="" class="del-msg" style="display: none;"/>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-xs btn-white del-cls-btn" data-dismiss="modal">取 消</button>
                                <button type="button" class="btn btn-xs btn-danger del-sub-btn">保 存</button>
                            </div>
                        </div>
                        <!-- /.modal-content -->
                    </div>
                    <!-- /.modal-dialog -->
                </div>
                <!-- /.modal -->
            </div>
        </div>
    </div>
</div>
<script src="<c:url value="/js/ku.js"/>"></script>
<script src="<c:url value="/js/mission.js"/>"></script>
<script>    
    $(".add-file-btn").click(function (e) {
        let jsonstr = $(".file-msg-form").serializeJson();
        jsonstr = JSON.parse(jsonstr);
        console.log(jsonstr);
        $(".close-file-btn").click();
        // return;
        $.ajax({
            url:"add_file?oneKey=true",
            type:"POST",
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify(jsonstr),
            success:function(data){
                console.log(data);
                if(data.status==1){
                    alert("添加成功~");
                    location.reload();
                }
            }
        })
    })
    var edit_bool = false;
    $(".edit-save-all-btn").click(function(e){
        editFilePost(true);
    })
    var editFilePost = function(oneKey){
        if(edit_bool){alert("文件正在生成中，请勿重复点击保存，请稍等");return;}
        let attr_strs = `price,id,mission_id,azdw,babh,bh,ccbh,ccri,filename,gcdz,gcmc,mtype,name,phone,sbxh,sccj,sydw,wtdw,file-name,date,property_unit,photo,examine,seal,submit,num,ypbh,aqmbh,maqmbh,aqdbh,zjbh,xzbh,djbh,ggbh`;
        attr_strs = attr_strs.split(",");
        let lala = `photo,examine,seal,submit`;
        let post_data = {};
        for(let item of attr_strs){
            if("file-name"==item){
                post_data["edit_file_name"] = $(".edit-"+item).val();
            }else{
                if(lala.indexOf(item)!=-1){
                    if($(".edit-"+item).prop("checked")){
                        post_data[item] = new Date().getTime();
                    }
                    continue;
                }
                post_data[item] = $(".edit-"+item).val();
            }
        }
        if(post_data.filename==""){
            alert("文件名不能为空");
            return;
        }
        edit_bool = true;
        $.ajax({
            url:"add_file?oneKey="+oneKey,
            type:"POST",
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify(post_data),
            success:function(data){
                edit_bool = false;
                console.log(data);
                if(data.status==1){
                    alert("修改成功");
                    location.reload();
                }
            }
        })
    }
    $(".edit-save-btn").click(function(e){
        editFilePost(false);
    })
    $(".del-sub-btn").click(function(e){
        let ee = $(e.currentTarget);
        $(".del-cls-btn").click();
        let del_msg = $(".del-msg");
        let del_date = del_msg.attr("date");
        let del_file_name = encodeURI(del_msg.attr("filename"));
        let post_data = {};
        let url = "delfile/"+del_date;
        if(del_file_name!=undefined){
            url += "/"+del_file_name;
        }
        $.ajax({
            url:url,
            type:"DELETE",
            data:post_data,
            success:function(data){
                console.log(data);
                if(data.status==1){
                    alert("操作完毕");
                    location.reload();
                }
            }
        })

    })
</script>
</body>
</html>
