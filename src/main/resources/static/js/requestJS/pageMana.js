var count=1;
var flag=0;
var object;
$(function(){
    getCataLog("","#tree li");

    $("select[name='SubmitType']").change(function(){
        if($(this).val()==0){
            $("div[name='contentSub']").css("display","none");
            $("div[name='docSub']").css("display","block");
        }else if($(this).val()==1){
            $("div[name='contentSub']").css("display","block");
            $("div[name='docSub']").css("display","none");
        }else{
            $("div[name='contentSub']").css("display","none");
            $("div[name='docSub']").css("display","none");
        }
    })

    $("input[name='doc']").change(function(){
        var file=document.getElementById("doc");
        if (file.files && file.files[0]) {
            $(this).parent("span").children("span").text("文件准备就绪...");
        }else{
            $(this).parent("span").children("span").text("Add files...");
        }
    })

    $("input[name='pressDoc']").change(function(){
        var file=document.getElementById("pressDoc");
        if (file.files && file.files[0]) {
            $(this).parent("span").children("span").text("文件准备就绪...");
        }else{
            $(this).parent("span").children("span").text("Add files...");
        }
    })

    $("select[name='exitDocs']").change(function(){
        $("span[name='pressDocPath']").text($("select[name='exitDocs']").val());
        showZipDetail();
    })
})

/**
 * 请求文件目录信息
 * @param path
 */
var getCataLog=function(path,objStr){
    $.ajax({
        type:'GET',
        url:'/static/staticPaths.do',
        data:{
            localPath:path
        },
        dataType:'json',
        success:function(result){
            if(result.flag==1){
                var sign=0;
                if(result.data.length>0){
                    sign=1
                }
                var showlist = $("<ul></ul>");
                showall(result.data, showlist,sign,objStr);
                var branches = $(showlist).appendTo(objStr);
                $(objStr).treeview({
                    add: branches
                });
            }else{
                $("#pdCataLog").html("获取目录失败!!!");
            }
        }
    })
}

/**
 * 生成文件目录树的ul列表
 * @param menu_list
 * @param parent
 */
var showall=function(menu_list,parent,sign,pObj) {
     for (var menu in menu_list) {
         //如果有子节点，则遍历该子节点
         if (menu_list[menu].childrenList.length > 0) {
             //创建一个子节点li
             var li = $("<li><span class='folder' onclick='chooseSpan($(this))'></span><span name='localPath' style='display: none'></span></span><span name='sign' style='display: none'></span></li>");
             //将li的文本设置好，并马上添加一个空白的ul子节点，并且将这个li添加到父亲节点中
             $(li).children("span").eq(0).append(menu_list[menu].name);
             $(li).children("span").eq(1).append(menu_list[menu].localPath);
             $(li).children("span").eq(2).append(menu_list[menu].sign);
             $(li).append("<ul></ul>").appendTo(parent);
             //将空白的ul作为下一个递归遍历的父亲节点传入
             showall(menu_list[menu].childrenList, $(li).children("ul"));
         }
         //如果该节点没有子节点，则直接将该节点li以及文本创建好直接添加到父亲节点中
         else {
             if(menu_list[menu].sign==0){
                 var li = $("<li onclick='addBranch(this)'><span class='folder' onclick='chooseSpan($(this))'></span><span name='localPath' style='display: none'></span><span name='sign' style='display: none'></span></li>");
                 $(li).children("span").eq(0).append(menu_list[menu].name);
                 $(li).children("span").eq(1).append(menu_list[menu].localPath);
                 $(li).children("span").eq(2).append(menu_list[menu].sign);
                 $(li).append("<ul></ul>").appendTo(parent);
             }else{
                 var li = $("<li style='cursor: pointer'><span class='file' onclick='chooseSpan($(this))'></span><span name='localPath' style='display: none'></span><span name='sign' style='display: none'></span></li>");
                 $(li).children("span").eq(0).append(menu_list[menu].name);
                 $(li).children("span").eq(1).append(menu_list[menu].localPath);
                 $(li).children("span").eq(2).append(menu_list[menu].sign);
                 $(li).appendTo(parent);
             }
         }
     }
}

/**
 * 生成预览文件目录树的ul列表
 * @param menu_list
 * @param parent
 */
var showPreviewAll=function(menu_list,parent,sign,pObj) {
    for (var menu in menu_list) {
        //如果有子节点，则遍历该子节点
        if (menu_list[menu].childrenList.length > 0) {
            //创建一个子节点li
            var li = $("<li><span class='folder'></span><span name='localPath' style='display: none'></span></span><span name='sign' style='display: none'></span></li>");
            //将li的文本设置好，并马上添加一个空白的ul子节点，并且将这个li添加到父亲节点中
            $(li).children("span").eq(0).append(menu_list[menu].name);
            $(li).children("span").eq(1).append(menu_list[menu].localPath);
            $(li).children("span").eq(2).append(menu_list[menu].sign);
            $(li).append("<ul></ul>").appendTo(parent);
            //将空白的ul作为下一个递归遍历的父亲节点传入
            showPreviewAll(menu_list[menu].childrenList, $(li).children("ul"));
        }
        //如果该节点没有子节点，则直接将该节点li以及文本创建好直接添加到父亲节点中
        else {
            if(menu_list[menu].sign==0){
                var li = $("<li><span class='folder'></span><span name='localPath' style='display: none'></span><span name='sign' style='display: none'></span></li>");
                $(li).children("span").eq(0).append(menu_list[menu].name);
                $(li).children("span").eq(1).append(menu_list[menu].localPath);
                $(li).children("span").eq(2).append(menu_list[menu].sign);
                $(li).append("<ul></ul>").appendTo(parent);
            }else{
                var li = $("<li style='cursor: pointer'><span class='file'></span><span name='localPath' style='display: none'></span><span name='sign' style='display: none'></span></li>");
                $(li).children("span").eq(0).append(menu_list[menu].name);
                $(li).children("span").eq(1).append(menu_list[menu].localPath);
                $(li).children("span").eq(2).append(menu_list[menu].sign);
                $(li).appendTo(parent);
            }
        }
    }
}


/**
 * 请求新的文件目录
 * @param obj
 */
var addBranch=function(obj){
    $(obj).removeAttr("onclick");
    var localPath=$(obj).children("span[name='localPath']").text();
    getCataLog(localPath,obj);
}

/**
 * 选中的li获取对应文件信息
 * @param obj
 */
var chooseSpan=function(obj){
    $("#tree span").css("color","");
    obj.css("color","red");

    object=obj;
    var sign=obj.parent("li").children("span").eq(2).text()
    if(sign=="0"){
        flag=1;
        $("textarea[name='fileDetail']").val("");
    }else if(typeof(sign)=='undefined'){
        flag=1;
        $("textarea[name='fileDetail']").val("");
    }else{
        flag=0;
        getDocDetail();
    }
}

/**
 * 展示添加文件
 */
var addCataLog=function(){
    if(typeof(object)=='undefined'){
        alert("请选择父文件目录!!!");
        return;
    }
    if(flag==0){
        alert("文件下无法创建目录!!!");
        return;
    }

    $("input[name='parentCataLog']").val(object.parent("li").children("span").eq(1).text());
    showCataLog();
}

/**
 * 添加文件目录
 */
var submitCataLog=function(){
    var parentCataLog=$("input[name='parentCataLog']").val();
    var cataLogName=$("input[name='cataLogName']").val();
    $.ajax({
        type:'POST',
        url:'/static/cataLog.do',
        data:{
            parentPath:parentCataLog,
            path:cataLogName
        },
        dataType:"json",
        success:function(result){
            alert(result.errorMessage);
            if(result.flag==1){
                closeCataLog();
                window.location.reload();
            }
        }
    })
}

/**
 * 获取文件内容
 */
var getDocDetail=function(){
    var path=object.parent("li").children("span").eq(1).text();
    $.ajax({
        type:'GET',
        url:'/static/cataFile.do',
        data:{
            filePath:path
        },
        dataType:'json',
        success:function(result){
            if(result.flag==1){
                $("textarea[name='fileDetail']").val(result.data);
            }else{
                alert(result.errorMessage);
            }
        }
    })
}

/**
 * 更新文本内容
 */
var updateDoc=function(){
    var path=object.parent("li").children("span").eq(1).text();
    var content=$("textarea[name='fileDetail']").val();
    $.ajax({
        type:'PUT',
        url:'/static/cataFile.do',
        data:{
            filePath:path,
            content:content
        },
        dataType:'json',
        success:function(result){
            alert(result.errorMessage);
            if(result.flag==1){
                window.location.reload();
            }
        }
    })
}

/**
 * 展示创建文件弹框
 */
var showCreateDoc=function(){
    if(typeof(object)=='undefined'){
        alert("请选择父文件目录!!!");
        return;
    }
    if(flag==0){
        alert("文件下无法创建文件!!!");
        return;
    }
    $("input[name='parentDoc']").val(object.parent("li").children("span").eq(1).text());
    showDocMent();
}

/**
 * 上传普通文档
 */
var submitCreateDoc=function(){
    var data=new FormData();
    var type=$("select[name='SubmitType']").val();
    data.append("docPath",$("input[name='parentDoc']").val());

    if(type==0){
        var file=document.getElementById("doc");
        data.append("file",file.files[0]);
    }else if(type==1){
        data.append("name",$("input[name='DocName']").val());
        data.append("suffix",$("select[name='DocType']").val());
        data.append("docCode",$("textarea[name='docContent']").val());
    }else{
        alert("未选择");
    }

    $.ajax({
        url:'/static/docOrCode.do',
        type:"POST",
        data:data,
        cache: false,
        contentType: false,
        processData: false,
        dataType:"json",
        success:function(result){
            closeDocment();
            alert(result.errorMessage);
            if(result.flag==1){
                window.location.reload();
            }
        }
    })
}

var showZipDoc=function(){
    if(typeof(object)=='undefined'){
        alert("请选择目录!!!");
        return;
    }
    if(flag==0){
        alert("文件下无法上传压缩文件!!!");
        return;
    }
    $("input[name='pressParent']").val(object.parent("li").children("span").eq(1).text());
    showZipMent();
}

/**
 * 上传压缩文件，并进行预览
 */
var submitZipDoc=function(){
    var data=new FormData();
    data.append("docPath",$("input[name='pressParent']").val());
    var file=document.getElementById("pressDoc");
    data.append("file",file.files[0]);
    $.ajax({
        type:'POST',
        url:'/static/pressDoc.do',
        data:data,
        cache: false,
        contentType: false,
        processData: false,
        dataType:"json",
        success:function(result){
            if(result.flag==1){
                var sign=0;
                var data=result.data.nodeList;
                $("span[name='pressDocPath']").text(result.data.temporary);
                if(data.length>0){
                    sign=1
                }
                var objStr="#previewTree";
                var showlist = $("<ul></ul>");
                showPreviewAll(data, showlist,sign,objStr);
                var branches = $(showlist).appendTo(objStr);
                $(objStr).treeview({
                    add: branches,
                    collapsed:true
                });
            }else{
                alert(result.errorMessage);
            }
        }
    })
}

/**
 * 根据本地保存的压缩文件进行选择
 */
var submitZipPath=function () {
    var docPath=object.parent("li").children("span").eq(1).text();
    if(docPath!=''&&docPath!=null){
        $.ajax({
            type:'GET',
            url:'/static/pressDoc.do',
            data:{
                docPath:docPath
            },
            dataType:'json',
            success:function(result){
                if(result.flag==1){
                    var data=result.data;
                    var html="";
                    $.each(data,function(name,value) {
                        html=html+"<option value='"+name+"'>"+name+"</option>"
                    })
                    $("div[name='exitDoc']").css("display","block");
                    $("select[name='exitDocs']").html(html);
                    showZipDetail();
                }
            }
        })
    }
}

var showZipDetail=function(){
    var temPath=$("select[name='exitDocs']").val();
    var docPath=object.parent("li").children("span").eq(1).text();
    $.ajax({
        type:'GET',
        url:'/static/pressDetail.do',
        data:{
            docPath:docPath,
            temPath:temPath
        },
        dataType:'json',
        success:function(result){
            if(result.flag==1){
                var sign=0;
                var data=result.data;
                if(data.length>0){
                    sign=1
                }
                var objStr="#previewTree";
                $(objStr).html("");
                var showlist = $("<ul></ul>");
                showPreviewAll(data, showlist,sign,objStr);
                var branches = $(showlist).appendTo(objStr);
                $(objStr).treeview({
                    add: branches,
                    collapsed:true
                });
            }else{
                alert(result.errorMessage);
            }
        }
    })
}

/**
 * 确认压缩文件上传，并将压缩文件解压到指定目录下
 */
var confirmZipDoc=function(){
    var targetPath=$("input[name='pressParent']").val();
    var temPath=$("span[name='pressDocPath']").text();
    if($("select[name='exitDocs']").val()!=null){
        temPath=$("select[name='exitDocs']").val();
    }
    $.ajax({
        type:'POST',
        url:'/static/extractedDoc.do',
        data:{
            temPath:temPath,
            targetPath:targetPath
        },
        dataType:'json',
        success:function(result){
            alert(result.errorMessage);
            closeZipMent();
            if(result.flag==1){
                $("span[name='pressDocPath']").text("");
                window.location.reload();
            }
        }
    })
}

/**
 * 打开目录添加弹框
 */
var showCataLog=function(){
    $("div[name='bgD']").css("display","block");
    $("div[name='docCataLog']").css("display","block");
}

/**
 * 关闭目录添加弹框
 */
var closeCataLog=function(){
    $("div[name='bgD']").css("display","none");
    $("div[name='docCataLog']").css("display","none");
}

/**
 * 打开文件生成弹框
 */
var showDocMent=function(){
    $("div[name='bgD']").css("display","block");
    $("div[name='docMent']").css("display","block");
}

/**
 * 关闭目录添加弹框
 */
var closeDocment=function(){
    $("div[name='bgD']").css("display","none");
    $("div[name='docMent']").css("display","none");
    $("div[name='contentSub']").css("display","none");
    $("div[name='docSub']").css("display","none");
}

/**
 * 打开压缩文件上传弹框
 */
var showZipMent=function(){
    submitZipPath();
    $("div[name='bgD']").css("display","block");
    $("div[name='zipMent']").css("display","block");
}

/**
 * 关闭压缩文件上传弹框
 */
var closeZipMent=function(){
    $("div[name='bgD']").css("display","none");
    $("div[name='zipMent']").css("display","none");
    $("div[name='exitDoc']").css("display","none");
}
