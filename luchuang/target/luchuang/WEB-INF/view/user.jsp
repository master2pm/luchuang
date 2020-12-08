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
    <div style="display: none" id="users">${users}</div>
    <div style="display: none" id="pwd">${rootPwd}</div>
    <div id="fixed" style="position:fixed;top:0;left:0;width:100%;height:100%;background:white;z-index: 1;">
        <div style="width: 500px;margin: 100px auto 0;">
            <input type="password" placeholder="超级密码" id="rootPwd" class="form-control input-sm">
        </div>        
    </div>
    <div id="wrap">
        <div>
            <button class="btn btn-yellow btn-xs" data-toggle="modal" data-target="#addSource">添加用户</button>
        </div>
        <div>
            <table class="table table-striped">
                <tr>
                    <th>下标</th>
                    <th>姓名</th>
                    <th>密码</th>
                    <th>权限</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>                        
            </table>
        </div>      
        <div class="modal fade" id="addSource" role="dialog" aria-labelledby="gridSystemModalLabel">
            <div class="modal-dialog" role="document" style="width:900px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="gridSystemModalLabel">添加任务</h4>
                    </div>
                    <div class="modal-body">
                        <div class="container-fluid">
                            <form class="form-horizontal user-msg-form">                                       
                                <div class="w2">
                                    <div class="form-group ">
                                        <label for="sName" class="col-xs-3 control-label">姓名：</label>
                                        <div class="col-xs-4 ">
                                            <input type="text" class="form-control input-sm duiqi" placeholder="" name="name">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="sLink" class="col-xs-3 control-label">密码：</label>
                                        <div class="col-xs-4 ">
                                            <input type="password" class="form-control input-sm duiqi" placeholder="" name="pwd">
                                        </div>
                                    </div>
                                </div>  
                                <div class="form-group">
                                    <label class="col-xs-3 control-label">权限：</label>
                                    <div class="col-xs-4">
                                        <div>添加任务:<input type="checkbox" class="change-status" name="add"></div>
                                        <div>分配任务+修改小组名称+添加小组:<input type="checkbox" class="change-status" name="random"></div>
                                        <div>删除任务:<input type="checkbox" class="change-status" name="delete"></div>
                                        <div>盖章:<input type="checkbox" class="change-status" name="seal"></div>
                                        <div>收款:<input type="checkbox" class="change-status" name="settle"></div>
                                        <div>检验备注:<input type="checkbox" class="change-status" name="photoRemark"></div>
                                        <div>检验合格:<input type="checkbox" class="change-status" name="regular"></div>
                                        <div>审核:<input type="checkbox" class="change-status" name="verify"></div>
                                        <div>赊账:<input type="checkbox" class="change-status" name="credit"></div>
                                        <div>填写发票单号:<input type="checkbox" class="change-status" name="invoiceNum"></div>
                                        <div>填写收款备注:<input type="checkbox" class="change-status" name="settle_remark"></div>
                                        <div>报告发放:<input type="checkbox" class="change-status" name="reportIss"></div>
                                        <div>总结表导出:<input type="checkbox" class="change-status" name="summary"></div>
                                        <div style="display: none;">列表密码的显示:<input type="checkbox" class="change-status" name="price"></div>
                                        <div>下载报告:<input type="checkbox" class="change-status" name="download"></div>
                                        <div>列表价格是否显示:<input type="checkbox" class="change-status" name="showpwd"></div>
                                        <div>上传图片:<input type="checkbox" class="change-status" name="postphoto"></div>
                                        <div>修改检验单价:<input type="checkbox" class="change-status" name="editprice"></div>
                                        <div>电话回访:<input type="checkbox" class="change-status" name="phonevisit"></div>
                                        <div>照片审核:<input type="checkbox" class="change-status" name="photoverify"></div>
                                        <div>取消钩子:<input type="checkbox" class="change-status" name="cancel"></div>
                                    </div>                                            
                                </div>                                      
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-xs btn-xs btn-white close-mission-btn" data-dismiss="modal">取 消</button>
                        <button type="button" class="btn btn-xs btn-xs btn-green add-user-btn">保 存</button>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>  
    </div>
<script src="<c:url value="/js/user.js"/>"></script>
<script>

</script>
</body>
</html>
