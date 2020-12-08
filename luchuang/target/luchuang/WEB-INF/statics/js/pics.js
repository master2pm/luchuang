$(function(){
    var input = document.getElementById("file_input");    
    var result;    
    var dataArr = []; // 储存所选图片的结果(文件名和base64数据)  
    var fd;  //FormData方式发送请求    
    var oSelect = document.getElementById("select");  
    var oAdd = document.getElementById("add");  
    var oSubmit = document.getElementById("submit");  
    var oInput = document.getElementById("file_input");

    let pwd = sessionStorage.getItem("ppwwdd");
    if(pwd!=undefined&&pwd!=null){
        $("#word-input").val(pwd);
    }
    
    if(typeof FileReader==='undefined'){    
        alert("抱歉，你的浏览器不支持 FileReader");    
        input.setAttribute('disabled','disabled');    
    }else{    
        $("#file_input").change(function(e){
            console.log(e);
            readFile(e.target);
        })
        input.addEventListener('change',readFile,false);    
    }　　　　　//handler      

    var readFile = (e)=>{
        let target = e;
        dataArr = { data : [] };
        fd = new FormData();
        var iLen = target.files.length;
        for(var i=0;i<iLen;i++){	
            if (!input['value'].match(/.jpeg|.jpg|.gif|.png|.bmp/i)){　　//判断所选文件格式
                return alert("上传的图片格式不正确，请重新选择");
            }
            var reader = new FileReader();
            fd.append(i,target.files[i]);
            reader.readAsDataURL(target.files[i]);  //转成base64
            var fileName = target.files[i].name;
            reader.onload = function(e){
                // console.log(e)
                var imgMsg = {
                    name : fileName,//获取文件名
                    base64 : this.result   //reader.readAsDataURL方法执行完后，base64数据储存在reader.result里
                }
                dataArr.data.push(imgMsg);
                result = '<div class="delete not-post">delete</div><div style="display:none" class="result" ><img class="subPic" src="'+this.result+'" alt="'+fileName+'"/></div>';
                div = document.createElement('div');
                div.innerHTML = result;
                div['className'] = 'float';
                document.getElementById('allImg').appendChild(div);  　　//插入页面
                var img = div.getElementsByTagName('img')[0];
                img.onload = function(){
                    var nowHeight = ReSizePic(this); //设置图片大小
                    this.parentNode.style.display = 'block';
                    var oParent = this.parentNode;
                    if(nowHeight){
                        oParent.style.paddingTop = (oParent.offsetHeight - nowHeight)/2 + 'px';
                    }
                    $(".not-post").click(function(e){
                        if(confirm("是否删除图片")){
                            let ee = $(e.currentTarget);
                            $(ee).parent().remove();                     
                        }
                    })   
                }
            }
        }
    }

    var ReSizePic = (ThisPic) =>{
        var RePicWidth = 500; //这里修改为您想显示的宽度值
     
        var TrueWidth = ThisPic.width; //图片实际宽度
        var TrueHeight = ThisPic.height; //图片实际高度
        
        if(TrueWidth>TrueHeight){
            //宽大于高
            var reWidth = RePicWidth;
            ThisPic.width = reWidth;
            //垂直居中
            var nowHeight = TrueHeight * (reWidth/TrueWidth);
            return nowHeight;  //将图片修改后的高度返回，供垂直居中用
        }else{
            //宽小于高
            var reHeight = RePicWidth;
            ThisPic.height = reHeight;
        }
    }

    $("#select").click(function(e){
        oInput.value = "";   // 先将oInput值清空，否则选择图片与上次相同时change事件不会触发  
        //清空已选图片  
        $('.float').remove();        
        oInput.click();   
    })
              
    $("#add").click(function(e){
        console.log("执行");
        oInput.value = "";   // 先将oInput值清空，否则选择图片与上次相同时change事件不会触发  
        oInput.click();  
    })  

    $("#submit").click(function(e){
        console.log(dataArr)
        if(!dataArr.data.length){    
            return alert('请先选择文件');    
		}    
        send();  
    })  

    function send1(post_data){

        $.ajax({    
            url : `?pwd=${$("#word-input").val()}`,    
            type : 'POST', 
            contentType:"application/json;charset=utf-8",
            data : JSON.stringify(post_data),                
            success : function(data){    
                alert(data.msg);
                if(data.msg=="操作成功"){
                    // location.reload();
                }
          　}
        })  
    }


    function send(){   
        var submitArr = [];  
        var wid = $("#wid").html();
        $('.subPic').each(function () {
                submitArr.push({
                    name: $(this).attr('alt'),
                    remark: $(this).attr('src'),
                    wid: wid 
                });  
            }
        );
        if(submitArr.length>10){
            while(submitArr.length>10){
                let arr = submitArr.splice(10);
                console.log("1",submitArr);
                send1(submitArr);
                submitArr = arr;
            }
            send1(submitArr);
            console.log("2",submitArr);
        }else{
            send1(submitArr);
        }
        
        
        // $.ajax({    
        //     url : `?pwd=${$("#word-input").val()}`,    
        //     type : 'POST', 
        //     contentType:"application/json;charset=utf-8",
        //     data : JSON.stringify(submitArr),                
        //     success : function(data){    
        //         alert(data.msg);
        //         if(data.msg=="操作成功"){
        //             // location.reload();
        //         }
        //   　}
        // })    
    }    
    
    var pics = JSON.parse($("#pics").html());
    $("#pics").html("");
    var init = function(){
        console.log(pics);
        for(let item of pics){
            let el = `<div class="delete" pid=${item.id}>delete</div><div style="display:none" class="result" ><img src="./${item.name}" alt="${item.id}"/></div>`;
            div = document.createElement('div');
            div.innerHTML = el;
            div['className'] = 'lastPic';
            document.getElementById('allImg').appendChild(div);  　　//插入页面
            var img = div.getElementsByTagName('img')[0];
            img.onload = function(){
                var nowHeight = ReSizePic(this); //设置图片大小
                this.parentNode.style.display = 'block';
                var oParent = this.parentNode;
                if(nowHeight){
                    oParent.style.paddingTop = (oParent.offsetHeight - nowHeight)/2 + 'px';
                }
            }
        }
        $(".delete").click(function(e){
            if(confirm("是否删除图片")){
                let ee = $(e.currentTarget);
                let post_data = {};
                post_data.pwd = $("#word-input").val();
                console.log(ee.attr("pid"))
                $.ajax({
                    url:`./${$("#wid").html()}/${ee.attr("pid")}?pwd=${$("#word-input").val()}`,
                    type:"DELETE",
                    // data:post_data,
                    success:function(data){
                        console.log(data);
                        alert(data.msg);
                        if(data.msg=="操作成功"){
                            $(ee).parent().remove();
                        }
                    }
                })
            }
        })     
    }
    init();

});

