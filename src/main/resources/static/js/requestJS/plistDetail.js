$(function(){
    clearPlistInfo();

    var id=$("span[name='pId']").text();
    if(id==0||id==""||typeof(id)=='undefined'){
    }else{
        $.ajax({
            type:'GET',
            url:'/appClient/plistInfo.do',
            data:{
                id:id
            },
            dataType:'json',
            success:function(result){
                if(result.flag==1){
                    var data=result.data;
                    $("input[name='branch']").val(data.branch);
                    $("input[name='channel']").val(data.channel);
                    $("select[name='productId']").val(data.productId);
                    if(typeof(data.field)!='undefined'){
                        if(data.field.length>0){
                            $("ul[name='body-ul']").html("");
                            var field=data.field;
                            for(var i=0;i<field.length;i++){
                                var html="<li>" +
                                    "<p class='pTitle'>名称:</p>" +
                                    "<input type='text' class='form-control input-small' value='"+field[i].key+"'/>" +
                                    "<p class='pTitle'>值:</p>" +
                                    "<input type='text' class='form-control input-small' value='"+field[i].value+"'/>" +
                                    "<p><i class='fa fa-plus-circle' onclick='addPlistField($(this))'></i></p>" +
                                    "<p><i class='fa fa-minus-circle' onclick='removePlistField($(this))'></i></p>" +
                                    "</li>";
                                $("ul[name='body-ul']").append(html);
                            }
                        }
                    }
                    $("textarea[name='plistInfo']").val(data.plistModule);
                }else{
                    alert(result.errorMessage);
                }
            }
        })
    }
})

var createXmlModule=function(){
    $.ajax({
        type:'GET',
        url:'/appClient/plistModuleInfo.do',
        dataType:'json',
        success:function(result){
            $("textarea[name='plistInfo']").val(result.data);
        }
    })
}

var clearPlistInfo=function(){
    $("input[name='branch']").val("");
    $("input[name='channel']").val("");
    $("select[name='productId']").val("");
    $("textarea[name='plistInfo']").val("");

    var html="<li>" +
        "<p class='pTitle'>名称:</p>" +
        "<input type='text' class='form-control input-small' value=''/>" +
        "<p class='pTitle'>值:</p>" +
        "<input type='text' class='form-control input-small' value=''/>" +
        "<p><i class='fa fa-plus-circle' onclick='addPlistField($(this))'></i></p>" +
        "<p><i class='fa fa-minus-circle' onclick='removePlistField($(this))'></i></p>" +
        "</li>";

    $("ul[name='body-ul']").html(html);
}

var submitPlistInfo=function(){
    var id=$("span[name='pId']").text();
    var branch=$("input[name='branch']").val();
    var channel=$("input[name='channel']").val();
    var productId=$("select[name='productId']").val();
    var plistInfo=$("textarea[name='plistInfo']").val();
    var filed={};
    $("ul[name='body-ul']").children("li").each(function(){
        var key=$(this).children("input").eq(0).val();
        var value=$(this).children("input").eq(1).val();
        if(key==""||key==null){
        }else{
            filed[key]=value;
        }
    })
    var data;
    var type;
    if(id==0||id==""||typeof(id)=='undefined'){
        type='POST';
        data={
            branch:branch,
            productId:productId,
            channel:channel,
            clientField:JSON.stringify(filed),
            plistModule:plistInfo
        }
    }else{
        type='PUT';
        data={
            id:id,
            clientField:JSON.stringify(filed),
            plistModule:plistInfo
        }
    }

    $.ajax({
        type:type,
        url:'/appClient/plistInfo.do',
        data:data,
        dataType:'json',
        success:function(result){
            alert(result.errorMessage);
            if(result.flag==1){
                window.close();
            }
        }
    })
}

/**
 * 添加plist的field信息
 * @param obj
 */
var addPlistField=function(obj){
    var html="<li>" +
        "<p class='pTitle'>名称:</p>" +
        "<input type='text' class='form-control input-small' value=''/>" +
        "<p class='pTitle'>值:</p>" +
        "<input type='text' class='form-control input-small' value=''/>" +
        "<p><i class='fa fa-plus-circle' onclick='addPlistField($(this))'></i></p>" +
        "<p><i class='fa fa-minus-circle' onclick='removePlistField($(this))'></i></p>" +
        "</li>";
    obj.parent("p").parent("li").parent("ul").append(html);
}

/**
 * 删除plist的field信息
 * @param obj
 */
var removePlistField=function(obj){
    obj.parent("p").parent("li").remove();
}

