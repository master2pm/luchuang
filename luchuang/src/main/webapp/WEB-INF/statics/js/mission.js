$(function(){
    var mission_list_data = [],
    option_date = JSON.parse($("#optionBtn-date").text());
    var el = `
        
        <div class="check-div form-inline">
            <input type="text" class="form-control input-sm" placeholder="这里填小组" id="group-input">
            <button class="btn btn-yellow btn-xs grouping-btn" data-toggle="modal">开始分组</button>
            <button class="btn btn-green btn-xs fast-btn" data-toggle="modal" style="margin-left: 105px;">${option_date.A}</button>
            <button class="btn btn-green btn-xs fast-btn" data-toggle="modal">${option_date.B}</button>
            <button class="btn btn-green btn-xs fast-btn" data-toggle="modal">${option_date.C}</button>
            <button class="btn btn-green btn-xs fast-btn" data-toggle="modal">${option_date.D}</button>
            <button class="btn btn-danger btn-xs edit-fast-btn" data-toggle="modal" style="background:cornflowerblue !important">修改快捷名称</button>
        </div>
        <div class="data-div">
            <div class="mission-list tablebody" style="margin:0;">
                
            </div>
        </div>
    `;    
    $("#optionBtn-date").remove();
    $(el).appendTo("#sour2");
    $(".fast-btn").click(function(e){
        let text = $(e.currentTarget).text() + ",";
        $("#group-input").val($("#group-input").val()+text);
    })
    $(".edit-fast-btn").click(function(e){
        let text = prompt("请输入 例 张三，李四，老五，六六"),
        postData = {};
        
        if(text!=null&&text!=""){
            postData.text = text;
            $.ajax({
                url:"mission/fastOption",
                type:"POST",
                data:postData,
                success:function(data){
                    console.log(data);
                    alert(data.msg);
                    if("操作成功~".indexOf(data.msg)!=-1){
                        location.reload();
                    }
                }
            })            
        }        
    })
    $(".grouping-btn").click(function(e){
        let group = $("#group-input").val();
        let post_data = {};
        post_data.group = group;
        post_data.pwd = $(".word-input").val();
        let el_arr = $(".group-mission-input:not(:disabled)");       
        let mission_list = {};   
        for(let ee of el_arr){
            let num = $(ee).val();
            if(num=="")continue;
            console.log(num);
            let data = mission_list_data[$(ee).attr("index")];
            if(mission_list[num]==undefined){
                mission_list[num] = data.id+"";
            }else{
                mission_list[num] += ","+data.id;
            }                        
        }                
        post_data.ids = JSON.stringify(mission_list);        
        $.ajax({
            url:"random_group",
            type:"POST",
            contentType:"application/json;charset=utf-8",
            data:JSON.stringify(post_data),
            success:function(data){
                alert(data.msg);
                if(data.msg=="成功"){
                    location.reload();
                }
                
                console.log(data);
            }
        })
    })
    var getMissionList = function(){
        let post_data = {};
        let ddd = new Date();
        post_data.date = ddd.getFullYear()+"-"+(ddd.getMonth()+1);
        post_data.isGroup = true;
        $.ajax({
            url:"mission",
            type:"GET",
            data: post_data,
            success:function(data){
                setMissionList(data.data);
                mission_list_data = data.data;
                window.mission_list_data = data.data;
            }
        })
    }

    // getMissionList();

    var setMissionList = function(data){
        $(".mission-list").empty();
        let table_el = `<table class="table table-striped">
                            <tr class="ccenter">
                                <th>任务种类</th>
                                <th>任务生成时间</th>
                                <th>任务下单人</th>    
                                <th>联系人</th>
                                <th>电话</th>
                                <th>检测种类</th>
                                <th>数量</th>  
                                <th>地址</th> 
                                <th>单价价格</th>
                                <th>检测组</th>
                                <th>操作</th>                                 
                            </tr>`;
        for(let index in data){
            let item = data[index];
            table_el +=`
                <tr style="text-align:center;">
                    <td><input index="${index}" class="group-mission-input" mid="${item.id}" type="text" style="width: 45px;text-align: center;" ${item.category!=null?"disabled='disabled' value='"+item.category+"'":""}/></td>
                    <td>${item.create_time}</td>
                    <td>${item.creator}</td>
                    <td>${item.detect_contact}</td>
                    <td>${item.detect_contact_number}</td>
                    <td>${item.mtype}</td>
                    <td>${item.num}</td>
                    <td>${item.project_name}</td>
                    <td>${item.price}</td>
                    <td>${item.executive==null?"还未分配":item.executive}</td>
                    <td>`;
                    if(item.executive!=null){
                        table_el +=  `<button class="btn btn-xs btn-green edit-mission-btn" mid="${item.id}">添加小组</button>
                                      <button style="background: cornflowerblue !important;" class="btn btn-danger btn-xs del-btn edit-executive-btn" ename="${item.executive}" mid="${item.id}">修改</button>
                                      <button class="btn btn-danger btn-xs del-btn del-mission-btn" mid="${item.id}">删除</button>`
                    }                    
                        table_el +=  `
                </td>
                </tr>
            `;
        }
        table_el += `</table>`;
        $(table_el).appendTo(".mission-list");
        $(".del-mission-btn").click(function(e){
            if(!confirm("确定要删除吗"))return;
            let pwd = $(".word-input").val(),
                ee = $(e.currentTarget);
            if(pwd==""){alert("密码不能为空，请输入密码");return;}
            $.ajax({
                url:`mission/${ee.attr("mid")}/${$(".word-input").val()}`,
                type:"DELETE",
                // contentType:"application/json;charset=utf-8",
                // data:JSON.stringify(post_data),
                success:function(data){
                    alert(data.msg);
                    console.log(data);
                    if(data.msg=="删除成功"){
                        location.reload();
                    }
                }
            })
        })
        $(".edit-mission-btn").click(function(e){
            let ee = $(e.currentTarget),
            str = "",
            post_data = {};
            post_data.pwd = $(".word-input").val();
            if(post_data.pwd==""){
                alert("密码不能为空");
                return;
            }
            post_data.id = ee.attr("mid");            
            post_data.num = parseInt(prompt("要添加几个小组？"));
            if(post_data.num==null){
                return;
            }
            post_data.group = prompt("要添加的小组名字是什么？");        
            if(post_data.group==null){
                return;
            }                
            // encodeURI(encodeURI())
            // str = `?id=${post_data.id}&num=${post_data.num}&group=${post_data.group}&pwd=${post_data.pwd}`;
            str = `?id=${post_data.id}&num=${post_data.num}&group=${post_data.group}&pwd=${post_data.pwd}`;
            if(!confirm(`${post_data.group}小组 随机分${post_data.num}个小组出来?`)){return;}
            // post_data.group = encodeURI(encodeURI(post_data.group));
            $.ajax({
                url:`appendGroup`,//${str}
                type:"POST",
                contentType:"application/json;charset=utf-8",
                data:JSON.stringify(post_data),
                success:function(data){
                    alert(data.msg);
                    console.log(data);
                    if(data.msg=="添加成功"){
                        location.reload();
                    }
                }
            })
        })
        $(".edit-executive-btn").click(function(e){
            let ee = $(e.currentTarget),
            postData = {};
            postData.id = ee.attr("mid"),
            name = prompt("输入要修改成的名字");
            if(!confirm(`确定把${ee.attr("ename")}修改成${name}吗?`)){return;}
            
            postData.executive = name;
            $.ajax({
                url:`mission/executive?pwd=${$(".word-input").val()}`,
                type:"POST",
                contentType:"application/json;charset=utf-8",
                data:JSON.stringify(postData),
                success:function(data){
                    console.log(data);
                    alert(data.msg);
                    if(data.msg=="修改成功"){
                        location.reload();
                    }
                }
            })        
            // $.ajax({
            //     url:`mission/${ee.attr("mid")}/${$(".word-input").val()}`,
            //     type:"DELETE",
            //     // contentType:"application/json;charset=utf-8",
            //     // data:JSON.stringify(post_data),
            //     success:function(data){
            //         alert(data.msg);
            //         console.log(data);
            //         if(data.msg=="删除成功"){
            //             location.reload();
            //         }
            //     }
            // })
        })
    }
});

// [{"address":"nonono","category":null,"contact":"��Ү","contact_phone":"123123123","count":1,"create_time":1564650601413,"executive":"","id":"97710A6879304056B66DE53780C4DFB3","mtype":"物料提升机","price":1000.0,"pwd":"1234","status":0},{"address":"nonono","category":null,"contact":"һ��","contact_phone":"1234567","count":123,"create_time":1564650636447,"executive":"","id":"4F17649E04024038ABF184DDF1593627","mtype":"物料提升机","price":1234123.0,"pwd":"3333","status":0},{"address":"nonono","category":null,"contact":"һ��","contact_phone":"74354","count":1,"create_time":1564650646995,"executive":"","id":"68463D29CE8346A1989FFA2F4624EC19","mtype":"物料提升机","price":22.0,"pwd":"3333","status":0},{"address":"nonono","category":null,"contact":"ˢˢˢ","contact_phone":"15019796460","count":1,"create_time":1564650661241,"executive":"","id":"3B0CFD78535E4C98B8B7D7463C08D731","mtype":"物料提升机","price":222.0,"pwd":"3334","status":0},{"address":"nonono","category":null,"contact":"ˢˢˢ","contact_phone":"15019796460","count":1,"create_time":1564650669622,"executive":"","id":"3999D25E44F740EBAA1423F3E43C0A7B","mtype":"物料提升机","price":222.0,"pwd":"3334","status":0}]
