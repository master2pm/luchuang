$(function(){
    var filedata;
    var nowdata;
    $(".cancel").click(function(e){
        $(".mengban").css("display","none");
    })

    $(".sure").click(function(e){
        $.ajax({
            url:"delfile/"+$("#riqi").val(),
            type:"DELETE",
            data:{"filepath":$("#riqi").val()},
            success:function(data){
                console.log(data);

                // location.reload();
            }
        })
    })
    //获取库列表
    $.get("word_file",function(data){
        console.log(data);
        filedata = data.data;
        for(let item of data.results.data){
            var riqi = "<p style='margin:0;border:1px solid #BCE8F1;position:relative;'><a style='transition:0.8s all;position:relative;'>"+item.date+"</a></p>";
            var riqi_el = $(riqi).click(function(e){
                var riqi1 = item.date;
                $(".xz").removeClass("xz");
                $(e.currentTarget).addClass("xz");
                $("a").css("right","0");
                $(e.currentTarget).children("a").css("right","45px");
                $("#riqi").val(riqi1);
                $(e.currentTarget).append($("#jc"));
                $("#jc").css("display","block");
                $("#jc").click(function(e){
                    $(".mengban").css("display","block");
                })
                $(".right").empty();
                for(let file of item.files){
                    for(let file_name in file) {
                        let file_str = JSON.stringify(file);
                        var ys = "<div style='padding:12px;border-bottom:1px solid #BCE8F1;'>"+
                            "<span style='margin:0 5px;'><a href='javascript:void(0);' class='file_name'>"+file_name+"</a></span>"+
                            "<div style='float:right;'>"+
                            " <a href='javascript:void(0);'>编辑</a>"+
                            " <a href='javascript:void(0);'>任务单</a>"+
                            " <a href='javascript:void(0);'>委托单</a>"+
                            (file_str.indexOf('报告')!=-1?(" <a href='javascript:void(0);'>报告</a>"):'')+
                            (file_str.indexOf('意见')!=-1?(" <a href='javascript:void(0);'>意见</a>"):'')+
                            (file_str.indexOf('原始记录')!=-1?("<a href='javascript:void(0);'>原始记录</a>"):'')+
                            (file_str.indexOf('回执')!=-1?("<a href='javascript:void(0);'>回执</a>"):'')+
                            "</div>"+
                            "</div>";
                        $(".right").append(ys);
                        $(".right > div > div > a").click(function(e){
                            var text = $(e.currentTarget).text();
                            var date = $("#riqi").val();
                            var filename = $($(e.currentTarget).parent().parent().children("span")[0]).text();
                            if(text!="编辑"){
                                var link = "";
                                console.log(filename);
                                filename = encodeURI(encodeURI(filename));
                                text = encodeURI(encodeURI(text))
                                window.open("downFile?date=" + date + "&filepath=" + filename + "&mtype=" + text);
                            }else{
                                location.href = "index?date=" + date + "&filename=" + filename;
                            }

                        });
                        $(".file_name").click(function(e){
                            let ee = $(e.currentTarget);
                            let text = ee.text();
                            let date = $("#riqi").val();
                            window.open("downFile?date=" + date + "&filepath=" + text + "&mtype=");
                        })
                    }
                }
            })
            $(".left").append(riqi_el);
        }
    });
});