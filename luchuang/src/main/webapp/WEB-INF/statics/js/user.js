$(function(){
    let users = JSON.parse($("#users").html());     
    let rootPwd = $("#pwd").html();
    let urll = location.origin+"/luchuang/user";
    // let urll = location.origin+"/user";
    $("#pwd").remove();   
    $("#users").remove();        
    var init = ()=>{                
        $(".add-user-btn").click(e=>{            
            let jsonstr = $(".user-msg-form").serializeJson(),
            status = "",
            post_data = {};            
            jsonstr = JSON.parse(jsonstr);                        
            post_data.name = jsonstr.name;
            post_data.pwd = jsonstr.pwd;  
            if(post_data.name==""||post_data.pwd==""){
                alert("姓名和密码不能为空！");
                return;
            }
            for(let key in jsonstr){
                if("name,pwd".indexOf(key)!=-1){
                    continue;
                }
                if(jsonstr[key]=="on"){
                    switch(key){
                        case "add":      status += "1-";break;
                        case "random":   status += "2-";break;
                        case "delete":   status += "3-";break;
                        case "seal":     status += "4-";break;
                        case "settle":   status += "5-";break;
                        case "regular":  status += "6-";break;
                        case "verify":   status += "7-";break;
                        case "credit":  status += "8-";break;
                        case "reportInf":   status += "9-";break;
                        case "reportIss":  status += "10-";break;
                        case "summary":   status += "11-";break;
                        case "price":  status += "12-";break;
                        case "download":  status += "13-";break;
                        case "settle_remark":  status += "14-";break;
                        case "invoiceNum":  status += "15-";break;
                        case "showpwd": status += "16-";break;
                        case "postphoto": status += "17-";break;
                        case "photoRemark": status += "18-";break;
                        case "editprice": status += "19-";break;
                        case "phonevisit": status += "20-";break;
                        case "photoverify": status += "21-";break;
                        case "cancel": status += "22-";break;
                    }
                }                
            }
            status = status.substring(0,status.length-1);                   
            if(status==""){
                alert("请选择一个权限");
                return;
            }
            post_data.status = status;
            console.log(post_data);    
            $.ajax({
                url:urll+"?rootPwd="+rootPwd,
                type:"POST",
                contentType:"application/json;charset=utf-8",
                data:JSON.stringify(post_data),
                success:function(req){
                    console.log(req);
                    alert(req.msg);
                    if(req.msg=="添加成功"){
                        location.reload();
                    }
                }
            })   
        })

        if(sessionStorage.getItem("pwd")==rootPwd){
            $("#fixed").remove();       
            getUserList();   
        }else{
            $("#rootPwd").keyup(e=>{
                let ee = $(e.currentTarget);           
                if(e.keyCode==13){
                    if(ee.val()==rootPwd){
                        sessionStorage.setItem("pwd",rootPwd);
                        $("#fixed").remove();       
                        getUserList();             
                    }else{
                        alert("密码错误");
                    }
                }
            });
        }                
    }

    Date.prototype.format = function(fmt) { // author: meizz
        var o = {
          "M+" : this.getMonth()+1,                 // 月份
          "d+" : this.getDate(),                    // 日
          "h+" : this.getHours(),                   // 小时
          "m+" : this.getMinutes(),                 // 分
          "s+" : this.getSeconds(),                 // 秒
          "q+" : Math.floor((this.getMonth()+3)/3), // 季度
          "S"  : this.getMilliseconds()             // 毫秒
        };
        if(/(y+)/.test(fmt))
          fmt=fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for(var k in o)
          if(new RegExp("(" + k + ")").test(fmt))
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
      }

    var getUserList = ()=>{
        let el = ``,
        rule = {"1":"添加任务","2":"分配任务","3":"删除任务","4":"盖章",
        "5":"收款","6":"检验合格","7":"审核","8":"赊账","9":"报告信息",
        "10":"报告发放","11":"总结表导出","12":"列表价格是否显示","13":"下载报告",
        "14":"填写收款备注","15":"填写发票单号","16":"列表密码的显示","17":"上传图片",
        "18":"检验备注","19":"修改检验单价","20":"电话回访","21":"照片审核","22":"取消钩子"};
        console.log(users);
        for(let index in users){
            let user = users[index],
            user_rules = user.status.split("-");
            status = "";            
            for(let key of user_rules){
                console.log(key,rule[key])
                status += rule[key]+"-";
                // if(user.status.indexOf(key)!=-1){
                //     status += rule[key]+"-";
                // }
            }            
            status = status.substring(0,status.length-1);
            el += `
                <tr>
                    <td>${index}</td>
                    <td>${user.name}</td>
                    <td>${user.pwd}</td>
                    <td>${status}</td>
                    <td>${new Date(user.create_time).format("yyyy-MM-dd hh:mm:ss")}</td>
                    <td><button class="btn btn-danger btn-xs del-btn del-user-btn" mid="${user.id}">删除</button></td>
                </tr>
            `;
        }
        $(el).appendTo(".table");
        $(".del-user-btn").click(e=>{            
            let flag = confirm("是否删除");
            if(flag){
                let id = $(e.currentTarget).attr("mid");
                $.ajax({
                    url:urll+`/${id}?rootPwd=${rootPwd}`,
                    type:"DELETE",
                    success:function(req){
                        console.log(req);
                        if(req.data==1){
                            alert("删除成功！");
                            location.reload();
                        }else{
                            alert("删除失败！");
                        }
                    }
                })
            }
        })
    }
    init();
    
});