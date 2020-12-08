$(function(){

    $.fn.serializeJson = function(){
        var jsonData1 = {};
        var serializeArray = this.serializeArray();
        // 先转换成{"id": ["12","14"], "name": ["aaa","bbb"], "pwd":["pwd1","pwd2"]}这种形式
        $(serializeArray).each(function () {
            if (jsonData1[this.name]) {
                if ($.isArray(jsonData1[this.name])) {
                    jsonData1[this.name].push(this.value);
                } else {
                    jsonData1[this.name] = [jsonData1[this.name], this.value];
                }
            } else {
                jsonData1[this.name] = this.value;
            }
        });
        // 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name": "bb", "pwd":"pwd2"}]的形式
        var vCount = 0;
        // 计算json内部的数组最大长度
        for(var item in jsonData1){
            var tmp = $.isArray(jsonData1[item]) ? jsonData1[item].length : 1;
            vCount = (tmp > vCount) ? tmp : vCount;
        }

        if(vCount > 1) {
            var jsonData2 = new Array();
            for(var i = 0; i < vCount; i++){
                var jsonObj = {};
                for(var item in jsonData1) {
                    jsonObj[item] = jsonData1[item][i];
                }
                jsonData2.push(jsonObj);
            }
            return JSON.stringify(jsonData2);
        }else{
            return JSON.stringify(jsonData1);
        }
    };


    Location.prototype.get_attr = function(what){
        let strs = location.href.substring(location.href.indexOf("?")+1);
        strs = strs.split("?");
        for(let item of strs){
            let num = take(item);
            if(num!=null){
                return num;
            }
        }
        function take(strss){
            strss = strss.split("&");
            for(var i = 0 ; i <strss.length ;i++){
                let tempstrs = strss[i].split("=");
                if(tempstrs[0] == what){
                    return tempstrs[1];
                }
            }
            return null;
        }
        return "";
    }
})