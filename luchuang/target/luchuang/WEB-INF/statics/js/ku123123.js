$(function(){
    var getdata = function(searchkey){
        let post_data = {};
        if(searchkey!=undefined&&searchkey!=""){
            post_data.searchkey = searchkey;
        }
        $.ajax({
            url:"word_file",
            type:"GET",
            data:post_data,
            success:function(data){
                console.log(data);
                $(".ziyuanguanli").empty();
                for(let index in data.results.data){
                    let item = data.results.data[index];
                    let el = `
                <div id="collapseSystem" class="collapse in" aria-expanded="false">
                    <div class="row">
                        <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 levl2 ">
                            ${index}
                        </div>
                        <div id="topB${index}" class="col-lg-4 col-md-4 col-sm-4 col-xs-4 levl2" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseAccount${index}" aria-expanded="false" aria-controls="collapseOne">
                            <span id="topB${index}"></span><span>${item.date}</span>
                        </div>
                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">

                        </div>
                        <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                            <button class="btn btn-success btn-xs daochu-btn" data-toggle="modal" date="${item.date}" disabled="disabled" style="opacity: 0;">导出</button>
                            <button class="btn btn-danger btn-xs del-all-btn" data-toggle="modal" data-target="#deleteSource" date="${item.date}">删除</button>
                        </div>
                    </div>  
                    <div id="collapseAccount${index}" class="collapse" aria-expanded="false" style="overflow: hidden;">              
               `;
                    for(let index1 in item.files){
                        el += `              
                                <div class="row">
                                    <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 levl3 ">
                                        ${index1}
                                    </div>
                                    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4  levl3">
                                        <span class=""> &nbsp;</span><span><a href="downFile?date=${item.date}&filepath=${encodeURI(encodeURI(item.files[index1].file_name))}&mtype=">${item.files[index1].file_name}</a></span>
                                    </div>
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">`;
                        for(let item2 of item.files[index1].file_data){
                            el += `<a href="downFile?mtype=${encodeURI(encodeURI(item2.trim()))}&date=${item.date}&filepath=${encodeURI(encodeURI(item.files[index1].file_name))}" class="down-load-btn">${item2} &nbsp;</a>`;
                        }

                        el+=`</div>
                                    <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                                        <button class="btn btn-success btn-xs edit-btn" data-toggle="modal" data-target="#changeSource" date-index="${index}" file-index="${index1}" ${item.files[index1].attr==undefined?'disabled':''}>修改</button>
                                        <button class="btn btn-danger btn-xs del-btn" data-toggle="modal" data-target="#deleteSource">删除</button>
                                    </div>
                                </div>                                
                            `;
                    }

                    // $(".tablebody").append(el);
                    $(el+"</div></div>").appendTo(".ziyuanguanli");
                }
                $(".down-load-btn").click(function(e){
                    let ee = $(e.currentTarget);
                    ee.css("color","darkred");
                })

                $(".edit-btn").click(function(e){
                    let ee = $(e.currentTarget);
                    let date_index = ee.attr("date-index");
                    let file_index = ee.attr("file-index");
                    let date = data.results.data[date_index].date;
                    let file_attr = data.results.data[date_index].files[file_index].attr;
                    let lalala = `photo,examine,seal,submit`;
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
                    // if()
                    console.log(file_attr);
                })

                $(".del-btn").click(function(e){
                    let ee = $(e.currentTarget);
                    let date_index = ee.prev().attr("date-index");
                    let filename_index = ee.prev().attr("file-index");
                    let date = data.results.data[date_index].date;
                    console.log(data.results.data[date_index]);
                    let filename = data.results.data[date_index].files[filename_index].file_name;
                    $(".del-msg").attr({"date":date,"filename":filename});
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

            }
        })
    }
    getdata();
    $(".search-btn").click(function(e){
        let ee = $(e.currentTarget);
        getdata(ee.prev().val())
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
            getdata($(e.currentTarget).val());
        }
    })

    let report_date = JSON.parse($("#report-date").html());
    function setListTable(data){
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
                                <th>照片已出</th>
                                <th>审核、审批日期</th>
                                <th>盖公章日期</th>
                                <th>证书已给客户</th>                        
                            </tr>`;
        for(let files of data.data){
            for(let date_file of files.files){
                for(let file of date_file.files){
                    if(file.attr!=undefined){
                        table_el += `
                            <tr>
                                <td><input type="checkbox" class="export-input" ${file.attr==undefined?disabled="disabled":""} date="${date_file.date}" file_name="${file.file_name}"/></td>
                                <td>${date_file.date}</td>
                                <td>${file.file_name}</td>
                                <td>${file.attr.bh}</td>
                                <td>${file.attr.azdw}</td>
                                <td>${file.attr.gcmc}</td>
                                <td>${file.attr.sbxh}</td>
                                <td>${file.attr.babh}</td>
                                <td>${file.attr.wtdw}</td>
                                <td><input type="checkbox" class="photo-box"></td>
                                <td><input type="checkbox" class="shenh-box"></td>
                                <td><input type="checkbox" class="gai-box"></td>
                                <td><input type="checkbox" class="zhen-box"></td>
                            </tr>
                        `;
                    }
                }
            }
        }
        table_el += `</table>`;
        $(table_el).appendTo(".wendandaochu");
    }
    setListTable(report_date);
    console.log("lalala",report_date);
    let sel_el = "";
    for(let index in report_date.data){
        let item = report_date.data[index];
        sel_el += `<option value="${item.date}">${item.date}</option>`;
        // $(".wendandaochu").html(el)
    }
    $("#report-sel").html(sel_el);
    $("#report-date").remove();
    $(".report-all-btn").click(function(e){
        location.href = "download_report_list?date="+$("#report-sel").val();
        // window.open("download_report_list?date="+$("#report-sel").val());
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
});