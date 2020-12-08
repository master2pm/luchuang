$(function(){
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
    var type_list_data = JSON.parse($("#type-data-list").html());
    console.log("type_list_data",type_list_data);
    let pwd = sessionStorage.getItem("ppwwdd");
    if(pwd!=null){
        $(".word-input").val(pwd);
    }
    let searchKey1 = sessionStorage.getItem("searchKey");
    if(searchKey1!=null){
        $(".search-input").val(searchKey1);
    }
    $(".word-input").keyup(function(e){
        if(e.keyCode==13){
            let ee = $(e.currentTarget);
            sessionStorage.setItem("ppwwdd",ee.val());
            getWordData();
        }
    })
    // 获取word文档列表
    var getWordData = function(searchkey){
        $(".ziyuanguanli").empty();
        let post_data = {};
        post_data.sort = "123";
        post_data.pwd = $(".word-input").val();
        if(searchkey==undefined){
            searchkey = $(".search-input").val();
        }

        if(searchkey!=undefined&&searchkey!=""&&searchkey!=null){
            post_data.search = searchkey;
            sessionStorage.setItem("searchKey",searchkey);
        }else{
            post_data.date = $("#report-sel1").val();
            if(post_data.date == null||post_data.date == ""){
                let dd = new Date();
                post_data.date = dd.getFullYear()+"-"+(dd.getMonth()+1);
            }
        }
        $.ajax({
            url:"mission",
            type:"GET",
            data:post_data,
            success:function(data){
                console.log(data);
                window.mission_data = data.data;                
                for(let index in data.data){
                    let item = data.data[index].list,
                    count = data.data[index];
                    let el = `
                <div id="collapseSystem" class="collapse in" aria-expanded="false">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 levl12 coll11" id="coll${index}" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseAccount${index}" aria-expanded="false" aria-controls="collapseOne">
                            <div style="${count.priceCount||count.redWarn||count.purpleWarn?'line-height:35px;':''}">
                                <span style="margin-right:5x;color:#2196f3;">${index}</span>
                                <span style="margin-right:5px;">塔吊:${count.tdCount};</span>
                                <span style="margin-right:5px;">升降机:${count.sjjCount};</span>
                                <span style="margin-right:5px;">井架:${count.jjCount};</span>
                                <span style="margin-right:5px;">吊篮:${count.dlCount};</span>
                                <span style="margin-right:5px;">防坠器:${count.fzqCount};</span>
                                <span style="margin-right:5px;">三宝钢管扣件:${count.sbCount};</span>
                                <span style="margin-right:5px;">数量:${count.tdCount+count.sjjCount+count.jjCount+count.dlCount+count.fzqCount+count.sbCount}</span>                                
                            </div>
                            <div style="${count.priceCount||count.redWarn||count.purpleWarn?'line-height:35px;':''}">`;                                            
                            if(count.priceCount){                                
                                el += `<span style="margin-right:5px;">总产值:${count.priceCount};</span>
                                <span style="margin-right:5px;">未收款:${count.settleCount};</span>`
                            }
                                el += `<img src="image/warn.png" style="width:20px;${!count.redWarn?"display:none;":""}">     
                                <img src="image/warn2.png" style="width:20px;${!count.purpleWarn?"display:none;":""}">                                        
                            </div>
                        </div>
                        <div id="topB${index}" class="col-lg-4 col-md-4 col-sm-4 col-xs-4 levl2" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseAccount${index}" aria-expanded="false" aria-controls="collapseOne">
                            <span id="topB${index}"></span>
                        </div>
                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
                            <span></span>
                        </div>
                        <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                            <button class="btn btn-success btn-xs daochu-btn" data-toggle="modal" date="${item.date}" disabled="disabled" style="opacity: 0;">导出</button>
                            <button class="btn btn-danger btn-xs del-all-btn" data-toggle="modal" data-target="#deleteSource" disabled="disabled" style="opacity: 0;" date="${item.date}">删除</button>
                        </div>
                    </div>                      
                    <div id="collapseAccount${index}" class="collapse" aria-expanded="false" style="overflow: hidden;">              
               `;
                    for(let index1 in item){
                        let mis = item[index1];
                        el += `              
                                <div class="row">
                                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 coll22" id="coll${index}${index1}" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseAccount${index}${index1}" aria-expanded="false" aria-controls="collapseOne">
                                        <div style="${mis.red_warn||mis.purple_warn?"line-height:35px;":""}">
                                            <span style="margin-right:5px;">下单人：${mis.creator};</span>`;
                        if("防坠器,三宝钢管扣件".indexOf(mis.mtype)==-1){
                            el += ` <span style="margin-right:15px;">检测组：${mis.executive==null?"未分配":mis.executive};</span>
                                    <span style="margin-right:15px;">种类：${mis.mtype};</span>
                                    <span style="margin-right:15px;">数量：${mis.num};</span>
                                    <span>${count.priceCount?("单价："+mis.price+";"):""}</span>
                                    <span>工程地址&项目名称：${mis.project_name};</span>
                            `;
                        }
                        if(mis.mtype=="防坠器"){
                            el += `
                                <span style="margin-right:15px;">种类：${mis.mtype};</span>
                                <span style="margin-right:15px;">数量：${mis.num};</span>
                                <span>${count.priceCount?("单价："+mis.price+";"):""}</span>
                                <span>委托单位&送样人：${mis.wtdw};</span>
                            `;
                        }
                        if(mis.mtype=="三宝钢管扣件"){
                            el += `
                                <span style="margin-right:15px;">种类：${mis.mtype};</span>
                                <span style="margin-right:15px;">${count.priceCount?("总价："+mis.count+";"):""}</span>
                                <span>工程地址&项目名称：${mis.project_name};</span>
                            `;
                        }
                        let redW = count.redWarn!=undefined&&count.redWarn.toString().indexOf(mis.id)!=-1,
                        purW = count.purpleWarn!=undefined&&count.purpleWarn.toString().indexOf(mis.id)!=-1;
                        el +=          `
                                        </div>
                                        <div style="line-height:35px;${!redW&&!purW?"display:none;":""}">                                            
                                            <img src="image/warn.png" style="width:20px;${!redW?"display:none;":""}">     
                                            <img src="image/warn2.png" style="width:20px;${!purW?"display:none;":""}">                                        
                                        </div>
                                        <button mid="${mis.id}" class="btn btn-danger btn-xs btn-change-price" style="position:absolute;right:40px;top:25px;background:#ff9800;">修改单价/总价</button>
                                    </div>                                    
                                </div>
                                <div id="collapseAccount${index}${index1}" class="collapse" aria-expanded="false" style="overflow: hidden;">`;
                            //     <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2" style="float:right;">
                            //     <!--<button class="btn btn-danger btn-xs del-btn" data-toggle="modal" data-target="#deleteSource2323">删除</button>-->
                            // </div>
                        if(1==1){
                            for(let index2 in mis.wltsjs){
                                let wltsj = mis.wltsjs[index2];
                                let fileName = wltsj.filename;
                                el += `
                                        <div class="row" style="margin-left: auto;margin-right: auto;width: 97%;">
                                            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6" style="min-width: 600px;">
                                                <div style="float:left;margin-right:10px">${wltsj.bh!=""?wltsj.bh:(fileName.length>32&&fileName.indexOf("-")==32?(fileName.substring(33)):fileName)}</div>
                                                `;
                                                if(wltsj.bh!=""){
                                                    el += `<div style="line-height:35px;font-size:15px;">`;
                                                    if("防坠器,三宝钢管扣件".indexOf(wltsj.mtype)==-1){
                                                        el+= `
                                                        <span class="check-photoRemark" wid="${wltsj.id}" remark='${wltsj.photoRemark!=null?wltsj.photoRemark:""}' name="photoRemark" style="${wltsj.photoRemark==null?"color:red;":""}">检验备注</span>
                                                        <span class="check-photo" wid="${wltsj.id}">照片</span>
                                                        <span style="margin-right:10px">检验员合格：<input type="checkbox" class="change-status" name="examine" wid="${wltsj.id}" ${wltsj.examine!=null?"checked":""}></span>`;
                                                    }                               
                                                    if(wltsj.mtype=="防坠器"){
                                                        el+=`<span class="check-photo" wid="${wltsj.id}">照片</span>`;
                                                    }                     
                                                    el+= `
                                                        <span style="margin-right:10px">审核：<input type="checkbox" class="change-status" name="review" wid="${wltsj.id}" ${wltsj.review!=null?"checked":""}></span>`
                                                    if("三宝钢管扣件".indexOf(wltsj.mtype)==-1){
                                                        el += `<span style="margin-right:10px">照片审核：<input type="checkbox" class="change-status" name="photo_verify" wid="${wltsj.id}" ${wltsj.photo_verify!=null?"checked":""}></span>`;
                                                    }                                                        
                                                    el+=`<span style="margin-right:10px">盖章：<input type="checkbox" class="change-status" name="seal" wid="${wltsj.id}" ${wltsj.seal!=null?"checked":""}></span>
                                                    </div>
                                                    <div style="line-height:35px;font-size:15px;">                                                
                                                        <span style="margin-right:10px">收款:<input type="checkbox" class="change-status" mid="${wltsj.mission_id}" name="settle" wid="${wltsj.id}" ${wltsj.settle!=null?"checked":""}></span>
                                                        <span style="margin-right:10px">赊账:<input type="checkbox" class="change-status" mid="${wltsj.mission_id}" name="credit" wid="${wltsj.id}" ${wltsj.credit!=null?"checked":""}></span>
                                                        <span class="settle_remark" name="settle_remark" remark='${wltsj.settle_remark!=null?wltsj.settle_remark:""}' mid="${wltsj.mission_id}" wid="${wltsj.id}" style="margin-right:10px;${wltsj.settle_remark==null?"color:red;":""}">收款备注</span>
                                                        <span style="margin-right:10px">报告发放:<input type="checkbox" class="change-status" name="report_send" wid="${wltsj.id}" mid="${wltsj.mission_id}" ${wltsj.report_send!=null?"checked":""}></span>
                                                        <span class="invoiceNum" name="invoiceNum" num='${wltsj.invoiceNum!=null?wltsj.invoiceNum:""}' mid="${wltsj.mission_id}" wid="${wltsj.id}" style="margin-right:10px;${wltsj.invoiceNum==null?"color:red;":""}">发票号码</span>
                                                        <span class="phone_visit" name="phone_visit" remark='${wltsj.phone_visit!=null?wltsj.phone_visit:""}' mid="${wltsj.mission_id}" wid="${wltsj.id}" style="margin-right:10px;${wltsj.phone_visit==null?"color:red;":""}">电话回访</span>
                                                        <!--<span style="margin-right:10px">报告信息:<input type="checkbox" class="change-status" name="report_msg" wid="${wltsj.id}" ${wltsj.report_msg!=null?"checked":""}></span>    -->
                                                    </div>`;
                                                }
                                                
                                                el += `                                                                                        
                                            </div>
                                            `;
                                if(wltsj.bh!=""){
                                    let str = type_list_data[wltsj.mtype].split(",");
                                    el += `<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4" style="text-align: left;position:relative;"><div style="line-height:35px;font-size:19px;">`;                                
                                    for(let i = 0 ; i < str.length ; i++){
                                        if(i == 4){
                                            break;
                                        }
                                        el += `<a href="javascript:void(0)" style="margin-right:5px;" class="down-load-btn" uu='mtype=${encodeURI(encodeURI(str[i].trim()))}&date=${wltsj.date}&filepath=${encodeURI(encodeURI(wltsj.filename))}'>${str[i]} &nbsp;</a>`;                                
                                    }
                                    el += `</div>`;
                                    if(str.length > 4){
                                        el += `<div style="line-height:35px;font-size:19px;">`;
                                        for(let i = 4 ; i < str.length ; i++){
                                            el += `<a href="javascript:void(0)"  style="margin-right:5px;" class="down-load-btn" uu='mtype=${encodeURI(encodeURI(str[i].trim()))}&date=${wltsj.date}&filepath=${encodeURI(encodeURI(wltsj.filename))}'>${str[i]} &nbsp;</a>`;                                
                                        }
                                        el += `</div>`;
                                    }                                
                                    el += ` 
                                        <div style="position:absolute;top:0;right:0;">
                                            <button class="btn btn-danger btn-xs del-btn del-wl-btn" data-toggle="modal" wid="${wltsj.id}">删除</button>
                                            <button class="btn btn-success btn-xs edit-btn" data-toggle="modal" data-target="#changeSource" date="${index}" mis-index="${index1}" file-index="${index2}" >报告信息</button>
                                        </div>
                                    </div>`;
                                }else{
                                    el += `<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4" style="text-align: left;position:relative;"><div style="line-height:35px;font-size:19px;">
                                                <div style="position:absolute;top:0;right:0;">
                                                <button class="btn btn-danger btn-xs del-btn del-wl-btn" data-toggle="modal" wid="${wltsj.id}">删除</button>
                                                <button class="btn btn-success btn-xs edit-btn" data-toggle="modal" data-target="#changeSource" date="${index}" mis-index="${index1}" file-index="${index2}" >报告信息</button>
                                            </div></div></div>`;
                                } 
                                el +=    
                                //  <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1" style="float:right;text-align: right;min-width: 125px;">
                                //                 <button class="btn btn-danger btn-xs del-btn del-wl-btn" data-toggle="modal" wid="${wltsj.id}">删除</button>
                                //                 <button class="btn btn-success btn-xs edit-btn" data-toggle="modal" data-target="#changeSource" date="${index}" mis-index="${index1}" file-index="${index2}" >报告信息</button>
                                //             </div>                                        
                                        `</div>                                
                                `;
                            }
                        }
                        el += "</div>";                        
                    }
// ${item.files[index1].attr==undefined?'disabled':''}
                    // $(".tablebody").append(el);
                    $(el+"</div></div>").appendTo(".ziyuanguanli");
                }
                $(".btn-change-price").click(function(e){
                    let price = prompt("请输入单价/总价"),
                    postData = {};                                        
                    if(price!=null&&price!=""){
                        postData.pwd = $(".word-input").val();
                        postData.id = $(e.currentTarget).attr("mid");
                        postData.price = price;
                        $.ajax({
                            url:"mission/price",
                            type:"POST",
                            data:postData,
                            success:function(data){
                                console.log(data);
                                let msg = data.msg;
                                alert(data.msg);
                                if("操作成功".indexOf(msg)!=-1){
                                    location.reload();
                                }
                            }
                        })
                    }
                    
                })

                //记录有哪些是点开的
                $(".coll11").click(function(e){
                    let ee = $(e.currentTarget),
                    flag = !(ee.attr("aria-expanded")=="true");
                    var coll11 = sessionStorage.getItem("coll1");
                    if(coll11==null){
                        coll11 = "";
                    }
                    let id = ee.attr("id");
                    if(flag){                        
                        if(coll11.indexOf(id)==-1){
                            coll11 += ee.attr("id")+",";
                        }
                    }else{
                        if(coll11.indexOf(id)!=-1){
                            coll11 = coll11.replace(id+",","")
                        }
                    }
                    sessionStorage.setItem("coll1",coll11);
                    console.log(flag)                    
                })

                $(".coll22").click(function(e){
                    let ee = $(e.currentTarget),
                    flag = !(ee.attr("aria-expanded")=="true");
                    var coll11 = sessionStorage.getItem("coll2");
                    if(coll11==null){
                        coll11 = "";
                    }
                    let id = ee.attr("id");
                    if(flag){                        
                        if(coll11.indexOf(id)==-1){
                            coll11 += ee.attr("id")+",";
                        }
                    }else{
                        if(coll11.indexOf(id)!=-1){
                            coll11 = coll11.replace(id+",","")
                        }
                    }
                    sessionStorage.setItem("coll2",coll11);
                })

                {
                    try {
                        var coll11 = sessionStorage.getItem("coll1");
                        coll11 = coll11.substring(0,coll11.length-1)
                        if(coll11!=null&&coll11!=""){
                            coll11 = coll11.split(",");
                            // console.log(coll11);
                            for(let item of coll11){
                                console.log(item);
                                let e = $("#"+item);
                                e.attr("aria-expanded","true");
                                let ee = $(e.attr("href"));
                                ee.addClass("in");
                                ee.attr("aria-expanded","true");
                            }                       
                        }
                    } catch (error) {
                        
                    }
                    
                    try {
                        var coll22 = sessionStorage.getItem("coll2");  
                        coll22 = coll22.substring(0,coll22.length-1);   
                        if(coll22!=null&&coll22!=""){
                            coll22 = coll22.split(",");
                            for(let item of coll22){
                                console.log(item);
                                let e = $("#"+item);
                                e.attr("aria-expanded","true");
                                let ee = $(e.attr("href"));
                                ee.addClass("in");
                                ee.attr("aria-expanded","true");
                            } 
                        } 
                    } catch (error) {
                        
                    }
                                  
                }



                $(".down-load-btn").click(function(e){
                    let ee = $(e.currentTarget);
                    let url = ee.attr("uu");
                    location.href = `downFile?${url}&pwd=${$(".word-input").val()}`;
                })
                $(".check-photo").click(function(e){
                    let ee = $(e.currentTarget);
                    window.open(`pics/list?wid=${ee.attr("wid")}`);
                })

                var remarkMethod = function(e,attrName){
                    let ee = $(e.currentTarget),
                    remark = ee.attr("remark"),
                    attr = ee.attr("name");                    
                    if(remark!=""){
                        let jsonStr = "";
                        try {
                            jsonStr = JSON.parse(remark);
                        } catch (error) {
                            console.log(error)                            
                        }
                        let flag = confirm(`
                            填写时间：${new Date(jsonStr.execution_time).format("yyyy-MM-dd hh:mm:ss")};
                            操作者: ${jsonStr.executor};
                            ${attr=="phone_visit"?"电话回访":"备注信息为"}：${jsonStr.text};
                            如需修改请点击确定;
                        `);
                        if(flag){
                            remark = prompt(attr=="phone_visit"?"请填写回访情况":"请填写备注信息");
                            if(remark!=null&&remark!=""&&confirm(`你输入的内容是：${remark}`)){
                                let post_data = {};
                                post_data.id = ee.attr("wid");
                                post_data.attr = ee.attr("name");
                                post_data.pwd = $(".word-input").val();
                                post_data.text = remark;
                                if(confirm("是否修改全部？")){
                                    post_data.mid = ee.attr("mid");
                                }
                                editFileAttr(post_data,ee);
                            }
                        }
                    }else{
                        if(confirm(`无${attr=="phone_visit"?"回访信息":"备注信息"}，是否添加`)){
                            remark = prompt("请填写备注信息");
                            if(remark!=null&&remark!=""&&confirm(`你输入的内容是：${remark}`)){
                                let post_data = {};
                                post_data.id = ee.attr("wid");
                                post_data.attr = ee.attr("name");
                                post_data.pwd = $(".word-input").val(); 
                                post_data.text = remark;
                                if(confirm("是否修改全部？")){
                                    post_data.mid = ee.attr("mid");
                                }
                                editFileAttr(post_data,ee);
                            }                            
                        }
                    }
                }
                
                $(".check-photoRemark").click(function(e){
                    console.log(123123)
                    remarkMethod(e);
                })
                $(".settle_remark").click(function(e){
                    remarkMethod(e);                    
                })
                $(".phone_visit").click(function(e){
                    remarkMethod(e);                    
                })
                $(".invoiceNum").click(function(e){
                    let ee = $(e.currentTarget),
                    num = ee.attr("num");
                    if(num==""){
                        if(confirm("发票号码还未录入，是否录入")){
                            num = prompt("请输入发票号码");
                            if(num!=null&&num!=""&&confirm(`你输入的发票号码是：${num}`)){
                                let post_data = {};
                                post_data.id = ee.attr("wid");
                                post_data.attr = ee.attr("name");
                                post_data.pwd = $(".word-input").val(); 
                                post_data.text = num;
                                if(confirm("是否修改全部？")){
                                    post_data.mid = ee.attr("mid");
                                }
                                editFileAttr(post_data,ee);
                            }
                        }
                        return;
                    }else{
                        console.log("num",ee.attr("num"));
                        let jsonStr = JSON.parse(ee.attr("num")),
                        flag = confirm(`
                            填写时间：${new Date(jsonStr.execution_time).format("yyyy-MM-dd hh:mm:ss")};
                            操作者: ${jsonStr.executor};
                            发票：${jsonStr.text};
                            如需修改请点击确定;
                        `);
                        if(flag){
                            let num = prompt("请输入发票号码");
                            if(num!=null&&num!=""&&confirm(`你输入的发票号码是：${num}`)){
                                let post_data = {};
                                post_data.id = ee.attr("wid");
                                post_data.attr = ee.attr("name");
                                post_data.pwd = $(".word-input").val();
                                post_data.text = num;
                                if(confirm("是否修改全部？")){
                                    post_data.mid = ee.attr("mid");
                                }
                                editFileAttr(post_data,ee);
                            }
                        }
                    }
                })
                var editFileAttr = function(postData,ee){                    
                    $.ajax({
                        url:"file_attr",
                        type:"POST",
                        data:postData,
                        success:function(data){
                            console.log(data);
                            let msg = data.msg;
                            alert(data.msg);
                            if("操作成功".indexOf(msg)==-1){        
                                if(postData.text==undefined){
                                    ee.prop("checked",!ee.prop("checked"));
                                }else{
                                    location.reload();
                                }                                
                                // else{
                                //     if(postData.name == "settle_remark"){
                                //         ee.attr("remark",postData.text);
                                //     }else{
                                //         ee.attr("num",postData.text);
                                //     }                                    
                                // }
                                
                            }else{
                                location.reload();
                                // if(postData.text!=undefined){
                                //     location.reload();
                                // }
                            }                            
                        }
                    })
                }
                $(".change-status").change(function(e){                             
                    let flag = confirm("你确定？"),                    
                    post_data = {},
                    ee = $(e.currentTarget);                                            
                    if(!flag){
                        ee.prop("checked",!ee.prop("checked"));
                        return;
                    }        
                    if(ee.attr("mid")!=undefined){
                        if(confirm("确定勾选全部吗？")){
                            post_data.mid = ee.attr("mid");
                        }
                    }
                    post_data.id = ee.attr("wid");
                    post_data.attr = ee.attr("name");
                    post_data.pwd = $(".word-input").val(); 
                    editFileAttr(post_data,ee);
                    console.log(ee.attr("name"),ee.prop("checked"));
                })
                $(".down-load-btn").click(function(e){
                    let ee = $(e.currentTarget);
                    ee.css("color","darkred");
                })

                $(".edit-btn").click(function(e){
                    let ee = $(e.currentTarget);
                        date = ee.attr("date"),
                        mis_index = ee.attr("mis-index"),
                        file_index = ee.attr("file-index"),
                        mis = data.data[date].list[mis_index],
                        file_attr = mis.wltsjs[file_index],
                        lalala = `photo,examine,seal,submit`;
                        if(file_attr.report_msg!=null&&$(".word-input").val()!="SystemRoot"){
                            $(".edit-save-btn,.edit-save-all-btn").hide();
                        }else{
                            $(".edit-save-btn,.edit-save-all-btn").show();
                        }
                    $(".edit-one,.edit-two,.edit-tree").hide();
                    switch(mis.mtype){
                        case "塔吊": 
                        case "升降机": 
                        case "井架": 
                        case "吊篮": $(".edit-one").show(); break;
                        case "防坠器": $(".edit-two").show();break;
                        case "三宝钢管扣件": 
                            $(".edit-tree").show();
                            $(".gangguang,.sanbao,.aqm,.aqw,.aqd,.zjkj,.xzkj,.djkj,.gg").hide();
                            console.log("ff",file_attr);
                            if(file_attr.remark.indexOf("三宝")!=-1){
                                console.log(file_attr.remark.indexOf("安全带"));
                                if(file_attr.remark.indexOf("安全帽")!=-1){
                                    $(".aqm").show();
                                }
                                if(file_attr.remark.indexOf("安全网")!=-1){
                                    $(".aqw").show();
                                }
                                if(file_attr.remark.indexOf("安全带")!=-1){
                                    $(".aqd").show();
                                }
                                console.log("123123123123");

                            }else{
                                if(file_attr.remark.indexOf("直角扣件")!=-1){
                                    $(".zjkj").show();
                                }
                                if(file_attr.remark.indexOf("旋转扣件")!=-1){
                                    $(".xzkj").show();
                                }
                                if(file_attr.remark.indexOf("对接扣件")!=-1){
                                    $(".djkj").show();
                                }
                                if(file_attr.remark.indexOf("钢管")!=-1){
                                    $(".gg").show();
                                }
                            }
                        break;
                    }

                    for(let key in mis){                        
                        $(`.edit1-${key}`).text(mis[key]);
                    }
                    for(let key in file_attr){
                        if(lalala.indexOf(key)==-1){
                            $(`.edit-${key}`).val(file_attr[key]);
                        }
                    }
                    for(let key of lalala.split(",")){
                        console.log("keyL",key);
                        if(file_attr[key]!=undefined){
                            $(`.edit-${key}`).prop("checked",true);
                            continue;
                        }
                        $(`.edit-${key}`).prop("checked",false);
                    }
                    $(`.edit-file-name`).val(file_attr.filename);
                    $(`.edit-date`).val(date);
                    console.log(file_attr);
                })

                $(".del-wl-btn").click(function(e){
                    if(!confirm("确定要删除吗？")){return;}
                    let pwd = $(".word-input").val(),
                    wid = $(e.currentTarget).attr("wid"),
                    post_data1 = {};
                    post_data1.pwd = pwd;
                    
                    console.log(pwd,wid);     
                    $.ajax({
                        url:`wltsj/${wid}?pwd=${post_data1.pwd}`,
                        type:"DELETE",
                        success:function(data){
                            console.log(data);
                            location.reload();
                            // if(data.data){
                            //     alert("删除成功！~");
                            //     location.reload();
                            // }else{
                            //     alert("删除失败！请检查密码是否正确");
                            // }
                        }
                    })
                    // let ee = $(e.currentTarget);
                    // let date_index = ee.prev().attr("date-index");
                    // let filename_index = ee.prev().attr("file-index");
                    // let date = data.results.data[date_index].date;
                    // console.log(data.results.data[date_index]);
                    // let filename = data.results.data[date_index].files[filename_index].file_name;
                    // $(".del-msg").attr({"date":date,"filename":filename});
                })
                $(".del-all-btn").click(function(e){
                    let ee = $(e.currentTarget);
                    $(".del-msg").attr({"date":ee.attr("date")});
                })
                // $(".daochu-btn").click(function(e){
                //     let ee = $(e.currentTarget);
                //     console.log(ee.attr("date"));
                //     window.open("bulid_report_form?date="+ee.attr("date"));
                // })
                setListTable();
                alert("获取完毕");
            }
        })
    }
    var init_click = function(){
        $(".add-mission-btn").click(function (e) {
            let jsonstr = $(".mission-msg-form").serializeJson();
            jsonstr = JSON.parse(jsonstr);
            console.log("--",jsonstr);
            if(jsonstr.mtype==-1){
                alert("请选择种类");
                return;
            }

            let one_scene = "detect_company,detect_contact,detect_contact_number,scene_contact,scene_contact_number,num,project_name,price",
            two_scene = "detect_company,detect_contact,detect_contact_number,wtdw,num,price",
            tree_scene = "detect_company,detect_contact,detect_contact_number,project_name,count",
            post_data = {},
            type_val = $("#add_type").val();
            var flag = false;
            function make_post_data(scene){
                let scene_arr = scene.split(",");
                for(let key of scene_arr){
                    console.log(key);
                    let el_val = $(`.add_${key}`).val();
                    if(el_val==""){
                        alert("信息填写不全！");
                        return;
                    }
                    post_data[key] = el_val;
                }
                post_data.mtype = type_val;
                post_data.pwd = $(".word-input").val();
                console.log("post_data",post_data);
                flag = true;
            }
            switch(type_val){
                case "塔吊": ;
                case "升降机": ;
                case "井架": ;
                case "吊篮": make_post_data(one_scene); break;
                case "防坠器": 
                    make_post_data(two_scene);
                    if(post_data.num>7){
                        alert("数量最多为7");
                        return;
                    }
                break;
                case "三宝钢管扣件": 
                    make_post_data(tree_scene);
                    let add_el = $(".add_checkbox"),
                    sanbao = "";       
                    add_el.each((index,e)=>{
                        let ee = $(e),
                        type = {"safe_h":"安全帽","safe_w":"安全网","safe_d":"安全带","kou0":"直角扣件","kou1":"旋转扣件","kou2":"对接扣件","kou3":"钢管"},
                        e_name = ee.attr("name"),
                        e_prop = ee.prop("checked");
                        if(e_prop){
                            sanbao += type[e_name] + ",";
                        }
                    })
                    sanbao = sanbao.substring(0,sanbao.length);
                    post_data.type_list = sanbao;                               
                break;
            }

            if(!flag)return;
            $(".close-mission-btn").click();
            $.ajax({
                url:"mission",
                type:"POST",
                contentType:"application/json;charset=utf-8",
                data:JSON.stringify(post_data),
                success:function(data){
                    console.log(data);
                    if(data.data){
                        alert("添加成功~");
                        location.reload();
                    }else{
                        alert("添加失败！请查看填写的内容和密码是否正确！");
                    }
                }
            })
        })
        $(".one-scene,.two-scene,.tree-scene").hide()
        $("#add_type").change(function(e){
            let ee = $(e.currentTarget),
            val = ee.val();
            $(".one-scene,.two-scene,.tree-scene").hide()
            switch(val){
                case "塔吊": ;
                case "升降机": ;
                case "井架": ;
                case "吊篮": $(".one-scene").show(); break;
                case "防坠器": $(".two-scene").show();break;
                case "三宝钢管扣件": $(".tree-scene").show();break;
            }
        })
    }

    init_click();


    getWordData();


    $(".search-btn").click(function(e){
        let ee = $(e.currentTarget);
        getWordData(ee.prev().val())
    })
    $(".search-xls-btn").click(function(e){
        let ee = $(e.currentTarget);
        let search = ee.prev().val();
        search = encodeURI(search);
        console.log("search",search);
        $.ajax({
            url:"get_report_list",
            type:"GET",
            //window GET乱码
            data:{"search":search},
            success:function(data){
                console.log(data);
                setListTable(data);
            }
        })
    })
    $(".search-btn").prev().keyup(function(e){
        if(e.keyCode==13){
            getWordData($(e.currentTarget).val());
        }
    })

    function setListTable(){
        $(".wendandaochu").empty();
        let table_el = `<table class="table table-striped">
                            <tr>
                                <th>选择</th>
                                <th>日期</th>
                                <th>文件名</th>    
                                <th>编号</th>
                                <th>安装单位</th>
                                <th>工程名称</th>
                                <th>设备型号</th>  
                                <th>备案编号</th> 
                                <th>委托单位</th> 
                                <th>盖章</th>
                                <th>收款</th>                   
                            </tr>`;
        for(let key_date in mission_data){
            for(let mis_date of mission_data[key_date].list){
                for(let wltsj of mis_date.wltsjs){
                    if(wltsj.bh!=""){
                        table_el += `
                            <tr>
                                <td><input type="checkbox" class="export-input" ${wltsj.bh==""?disabled="disabled":""} date="${key_date}" file_name="${wltsj.filename}"/></td>
                                <td>${wltsj.date}</td>
                                <td>${wltsj.filename}</td>
                                <td>${wltsj.bh}</td>
                                <td>${wltsj.azdw}</td>
                                <td>${wltsj.gcmc}</td>
                                <td>${wltsj.sbxh}</td>
                                <td>${wltsj.babh}</td>
                                <td>${wltsj.wtdw}</td>
                                <td><input type="checkbox" class="photo-box" disabled ${wltsj.seal!=null?"checked":""}></td>
                                <td><input type="checkbox" class="shenh-box" disabled ${wltsj.settle!=null?"checked":""}></td>
                            </tr>
                        `;
                    }
                }
            }
        }
        table_el += `</table>`;
        $(table_el).appendTo(".wendandaochu");
    }    
    {
        let date = new Date();
        $("#report-sel1").attr("value",date.getFullYear() + "-" + (date.getMonth()+1));
    }


    $(".report-all-btn").click(function(e){        
        location.href = `download_report_list?date=${$("#report-sel").val()}&pwd=${$(".word-input").val()}`;
        // window.open("download_report_list?date="+$("#report-sel").val());
    })
    $(".new-report-all-btn").click(function(e){
        location.href = `download_new_report_list?date=${$("#report-sel").val()}&pwd=${$(".word-input").val()}`;
    })
    $(".report-earning-btn").click(function(e){
        location.href = `download_earnings_list?date=${$("#report-sel").val()}&pwd=${$(".word-input").val()}`;
    })
    $(".report-accept-btn").click(function(e){
        location.href = `download_accept_list?date=${$("#report-sel").val()}&pwd=${$(".word-input").val()}`;
    })
    $(".report-btn").click(function(e){
        let els = $(".export-input");
        let list = [];
        for(let index in els){
            let item = els[index];
            let ee = $(item);
            if(ee.prop("checked")){
                let obj = {};
                obj.date = ee.attr("date");
                obj.filename = ee.attr("file_name");
                // obj += `date=${ee.attr("date")}&filename=${ee.attr("file_name")}`;
                list.push(obj);
            }
        }
        $.ajax({
            url:"download_report_list_p",
            type:"POST",
            data:JSON.stringify(list),
            contentType:"application/json;charset=utf-8",
            success:function(data){
                console.log(data);
                location.href = "download_report_list/"+data.url;
                window.open("download_report_list/"+data.url);
            }
        })
        // window.open(`download_report_list_p?date=lalala&filename=gagaga`);
        // window.open(`download_report_list?date=${ee.attr("date")}&filename=${ee.attr("file_name")}`);
    })



    //记录每次操作的位置
    $("#meun-item1,#meun-item2,#meun-item3").click(function(e){
        sessionStorage.setItem("menu-item",$(e.currentTarget).attr("id"));        
    })
    try {
        var menuItem = sessionStorage.getItem("menu-item");
        if(menuItem!=null){
            $(".meun-item-active").removeClass("meun-item-active");
            $(".active").removeClass("active");
            $("#"+menuItem).addClass("meun-item-active");
            $($("#"+menuItem).attr("href")).addClass("active");
            
        }
    } catch (error) {
        console.log(error)
    }        
});