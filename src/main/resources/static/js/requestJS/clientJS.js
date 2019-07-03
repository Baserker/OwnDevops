var hrefFix="http://60.190.13.162:6120/req/iosDown?id=";
$(function(){
	loadTable();

	$("input[name='doc']").change(function(){
        var file=document.getElementById("doc");
        if (file.files && file.files[0]) {
            $(this).parent("span").children("span").text("文件准备就绪...");
        }else{
            $(this).parent("span").children("span").text("Add files...");
        }
    })
})

/**
 * 加载表格
 */
var loadTable=function(){
    var t = $("#client-table").bootstrapTable({
        url:'/appClient/appClients.do',
        method: 'get',
        dataType: "json",
        striped: true,//设置为 true 会有隔行变色效果
        undefinedText: "空",//当数据为 undefined 时显示的字符
        pagination: true, //分页
        // paginationLoop:true,//设置为 true 启用分页条无限循环的功能。
        showToggle: false,//是否显示 切换试图（table/card）按钮
        showColumns: false,//是否显示 内容列下拉框
        pageNumber: 1,//如果设置了分页，首页页码
        //showPaginationSwitch:false,//是否显示 数据条数选择框
        pageSize: 10,//如果设置了分页，页面数据条数
        pageList: [5],  //如果设置了分页，设置可供选择的页面数据条数。设置为All 则显示所有记录。
        paginationPreText: '‹',//指定分页条中上一页按钮的图标或文字,这里是<
        paginationNextText: '›',//指定分页条中下一页按钮的图标或文字,这里是>
        singleSelect: false,//设置True 将禁止多选
        search: false, //显示搜索框
        data_local: "zh-US",//表格汉化
        sidePagination: "server", //服务端处理分页
        queryParams:function queryParams(params){
            var param = {
                pageNumber: params.pageNumber,
                pageSize: params.pageSize,
                id:$("input[name='sId']").val(),
                clientType:$("select[name='sClientType']").val(),
                productId:$("select[name='sProductId']").val(),
                branch:$("input[name='sBranch']").val(),
                channel:$("input[name='sChannel']").val(),
                version:$("input[name='sVersion']").val(),
                publisher:$("input[name='sPublisher']").val(),
                status:$("select[name='sStatus']").val(),
                guideStatus:$("select[name='sGuideStatus']").val()
            };
            return param;
        },
        responseHandler:function(res){
            if(res.flag==1){
                return {
                    'total':res.data.totalElements,
                    'rows':res.data.content
                }
            }
        },
        queryParamsType: "undefined",
        idField: "id",//指定主键列
        columns: [
            {
                title: '',//表的列名
                field: 'num',
                align: 'center',//水平居中
                width: 50,
                formatter:function(value, row, index){
                    return index+1;
                }
            },
            {
                title: '客户端类型',
                field: 'clientType',
                align: 'center',
                width: 50,
				formatter:function(value, row, index){
                	if(value==2){
                		return "Android";
					}else if(value==7){
                		return "ios";
					}else{
						return "未知"
					}
				}
            },
            {
                title: '项目',
                field: 'productId',
                align: 'center',
                width: 40,
                formatter:function(value,row,index){
                    if(row.productId==0){
                        return "unknown";
                    }
                    if(row.productId==1){
                        return "lottery";
                    }
                    if(row.productId==2){
                        return "sports";
                    }
                    if(row.productId==3){
                        return "niuniu";
                    }
                    if(row.productId==4){
                        return "coin";
                    }
                    if(row.productId==5){
                        return "stock";
                    }
                }
            },
            {
                title: '分支',
                field: 'branch',
                align: 'center',
                width: 80
            },
            {
                title: '渠道',
                field: 'channel',
                align: 'center',
                width: 80
            },
            {
                title: '版本号',
                field: 'version',
                align: 'center',
                width: 80,
                formatter:function(value,row,index){
                    var version="";
                    if(row.version1!=null){
                        version=version+row.version1;
                    }
                    if(row.version2!=null){
                        version=version+"."+row.version2;
                    }
                    if(row.version3!=null){
                        version=version+"."+row.version3;
                    }
                    return version;
                }
            },
            {
                title: '状态',
                field: 'status',
                align: 'center',
                width: 40,
                formatter:function(value, row, index){
                	if(value==0){
						return "测试";
					}else if(value==1){
						return "预发布";
					}else if(value==2){
						return "正式";
                    }else if(value==3){
						return "下架";
                    }
				}
            },
            {
                title: '客户端状态',
                field: 'apkStatus',
                align: 'center',
                width: 50,
                formatter:function(value, row, index){
                    if(value==0){
                        return "未审核";
                    }else if(value==1){
                        return "审核中";
                    }else if(value==2){
                        return "审核已通过";
                    }else if(value==3){
                        return "审核未通过";
                    }
                }
            },
            {
                title: '上传人',
                field: 'publisher',
                align: 'center',
                width: 30
            },
            {
                title: '引导状态',
                field: 'guideStatus',
                align: 'center',
                width: 60,
                formatter:function(value, row, index){
                    if(value==0){
                        return "允许使用";
                    }else if(value==1){
                        return "提示更新";
                    }else if(value==2){
                        return "强制更新";
                    }
                }
            },
            {
                title: '上传时间',
                field: 'publishTime',
                align: 'center',
                width: 100,
                formatter:function(value,row,index){
                    return new Date(parseInt(value)).toLocaleString().replace(/:\d{1,2}$/,' ');
                }
            },
            {
                title: '操作',
                field: 'id',
                align: 'center',
                width: 220,
                formatter: function (value, row, index) {//自定义显示可以写标签哦~
                    if(row.clientType==2){
                        return '<button type="button" class="btn btn-default" onclick="editClient('+value+')">编辑</button><button type="button" class="btn btn-default" onclick="searchHisClient('+value+')">历史版本</button><button type="button" class="btn btn-default" onclick="updateClient($(this))">更新架包</button><button type="button" class="btn btn-default" onclick="downloadClient($(this))">下载</button><span name="rowId" style="display: none">'+row.id+'</span><span name="rowUrl" style="display: none">'+row.docUrl+'</span><span name="rowProduct" style="display: none">'+row.productId+'</span>'
                    }else if(row.clientType==7){
                        return '<button type="button" class="btn btn-default" onclick="editClient('+value+')">编辑</button><button type="button" class="btn btn-default" onclick="searchHisClient('+value+')">历史版本</button><button type="button" class="btn btn-default" onclick="updateClient($(this))">更新架包</button><button type="button" class="btn btn-default" onclick="downloadClient($(this))">下载</button><button type="button" class="btn btn-default" onclick="editPlistInfo('+value+')">plist配置</button><span name="rowId" style="display: none">'+row.id+'</span><span name="rowUrl" style="display: none">'+row.docUrl+'</span><span name="rowProduct" style="display: none">'+row.productId+'</span>'
                    }
                }
            }
		]
	})
}

var searchClients=function(){
    $("#client-table").bootstrapTable('refresh');
}

/**
 * 更新架包
 */
var updateClient=function(obj){
    var objTd=obj.parent("td").parent("tr").children("td")
    var type=objTd.eq(1).text();
    if(type=='Android'){
        $("select[name='clientType']").val(2);
    }else if(type=='ios'){
        $("select[name='clientType']").val(7);
    }else{
        $("select[name='clientType']").val(0);
    }

    $("select[name='productId']").val(obj.parent("td").children("span[name='rowProduct']").text());
    $("input[name='branch']").val(objTd.eq(3).text());
    $("input[name='channel']").val(objTd.eq(4).text());
    $("input[name='version']").val(objTd.eq(5).text());

    $("select[name='clientType']").removeAttr("disabled");
    $("select[name='productId']").removeAttr("disabled");
    $("input[name='branch']").removeAttr("disabled");
    $("input[name='channel']").removeAttr("disabled");
    $("input[name='version']").removeAttr("disabled");

    showPupNew();
}

/**
 * 获取需要修改版本架包信息
 * @param id
 */
var editClient=function(id){
    $.ajax({
        type:'get',
        url:'/appClient/appClientOne.do',
        data:{
            id:id
        },
        dataType:'json',
        success:function(result){
            if(result.flag==1){
                var data=result.data;
                $("input[name='clientId']").val(data.id);
                $("select[name='status']").children("option").each(function(){
                    if($(this).val()==data.status){
                        $(this).prop("selected",true);
                    }
                })

                $("select[name='apkStatus']").children("option").each(function(){
                    if($(this).val()==data.apkStatus){
                        $(this).prop("selected",true);
                    }
                })

                $("select[name='guideStatus']").children("option").each(function(){
                    if($(this).val()==data.guideStatus){
                        $(this).prop("selected",true);
                    }
                })

                $("input[name='guideTarget']").val(data.guideTarget);
                $("textarea[name='appMemo']").html(data.appMemo);
                showPup();
            }else if(result.data=='0'){
                window.open("/req/plistInfos");
            }else{
                alert(result.errorMessage);
            }
        }
    })
}

var editPlistInfo=function(id){
    $.ajax({
        type:'GET',
        url:'/appClient/appPlistField.do',
        data:{
            id:id
        },
        dataType:'json',
        success:function(result){
            if(result.flag==1){
                var data=result.data;
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        var html="<li>" +
                            "<p class='pTitle'>名称:</p>" +
                            "<input type='text' class='form-control input-small' value='"+data[i].key+"'/>" +
                            "<p class='pTitle'>值:</p>" +
                            "<input type='text' class='form-control input-small' value='"+data[i].value+"'/>" +
                            "<p><i class='fa fa-plus-circle' onclick='addPlistField($(this))'></i></p>" +
                            "<p><i class='fa fa-minus-circle' onclick='removePlistField($(this))'></i></p>" +
                            "</li>";
                        $("div[name='fieldBody']").children("ul").append(html);
                    }
                }else{
                    var html="<li>" +
                        "<p class='pTitle'>名称:</p>" +
                        "<input type='text' class='form-control input-small' value=''/>" +
                        "<p class='pTitle'>值:</p>" +
                        "<input type='text' class='form-control input-small' value=''/>" +
                        "<p><i class='fa fa-plus-circle' onclick='addPlistField($(this))'></i></p>" +
                        "<p><i class='fa fa-minus-circle' onclick='removePlistField($(this))'></i></p>" +
                        "</li>";
                    $("div[name='fieldBody']").children("ul").append(html);
                }
                showPlistInfo();
            }else{
                alert(result.errorMessage);
            }
        }
    })

    $("input[name='pId']").val(id);
}

var searchHisClient=function(id){
    window.open("/req/hisAppClient.do?id="+id);
}

var submitNewOrUpdate=function(){
    var data=new FormData();
    var file=document.getElementById("doc");
    data.append("clientType",$("select[name='clientType']").val());
    data.append("productId",$("select[name='productId']").val());
    data.append("branch",$("input[name='branch']").val());
    data.append("channel",$("input[name='channel']").val());
    data.append("version",$("input[name='version']").val());
    data.append("doc",file.files[0]);

    console.log(data);

    $.ajax({
        url:'/appClient/appClient.do',
        type:"post",
        data:data,
        cache: false,
        contentType: false,
        processData: false,
        dataType:"json",
        beforeSend: function(){
            showwaitFrame();
        },
        complete: function(){            //ajax得到请求结果后的操作
            closewaitFrame();
        },
        success:function(result){
            closePupNew();
            alert(result.errorMessage);
            if(result.flag==1){
                window.location.reload();
            }
        },
        error:function () {
            closewaitFrame();
            closePupNew();
        }
    })
}

/**
 * 发送版本更新信息
 */
var submitEdit=function(){
    var id=$("input[name='clientId']").val();
    var status=$("select[name='status']").val();
    var apkStatus=$("select[name='apkStatus']").val();
    var guideStatus=$("select[name='guideStatus']").val();
    var guideTarget=$("input[name='guideTarget']").val();
    var appMemo=$("textarea[name='appMemo']").val();

    $.ajax({
        type:'put',
        url:'/appClient/appClient.do',
        data:{
            id:id,
            status:status,
            apkStatus:apkStatus,
            guideStatus:guideStatus,
            guideTarget:guideTarget,
            memo:appMemo
        },
        dataType:'json',
        success:function(result){
            closePup();
            alert(result.errorMessage);
            if(result.flag==1){
                window.location.reload();
            }
        }
    })
}

/**
 * 下载客户端
 * @param obj
 */
var downloadClient=function(obj){
    var objTd=obj.parent("td").parent("tr").children("td");
    var id=obj.parent("td").children("span").eq(0).text();
    if(objTd.eq(1).text().trim()=='Android'){
        createQrCode(obj.parent("td").children("span").eq(1));
        showQrCode();
    }else if(objTd.eq(1).text().trim()=='ios'){
        $("#qrCode").html(" ");
        $("a[name='qrHref']").html(" ");
        var href=hrefFix+id;
        jQuery("#qrCode").qrcode({width:200,height:200,text:href});
        showQrCode();
    }else{
        alert(objTd.eq(1).text().trim());
    }
}

var submitPlist=function(){
    var data={};
    data["id"]=$("input[name='pId']").val();
    $("div[name='fieldBody']").children("ul").children("li").each(function(){
        var key=$(this).children("input").eq(0).val();
        var value=$(this).children("input").eq(1).val();
        if(key==""||key==null){
        }else{
            data[key]=value;
        }
    })
    $.ajax({
        type:'POST',
        url:'/appClient/appPlistField.do',
        contentType: "application/json; charset=utf-8",
        data:JSON.stringify(data),
        dataType:'json',
        success:function(result){
            closePlistInfo();
            alert(result.errorMessage);
            if(result.flag==1){
                window.location.reload();
            }
        }
    })
}

/**
 * 清空添加页面
 */
var clearPupNew=function(){
    $("select[name='clientType']").val("");
    $("select[name='productId']").val("");
    $("input[name='branch']").val("");
    $("input[name='channel']").val("");
    $("input[name='version']").val("");
    $("input[name='doc']").val("");

    $("select[name='clientType']").removeAttr("disabled");
    $("select[name='productId']").removeAttr("disabled");
    $("input[name='branch']").removeAttr("disabled");
    $("input[name='channel']").removeAttr("disabled");
    $("input[name='version']").removeAttr("disabled");
}

var clearPup=function(){
    $("select[name='status']").val("");
    $("select[name='guideStatus']").val("");
    $("input[name='guideTarget']").val("");
}

var showPupNew=function(){
    $("div[name='bgD']").css("display","block");
    $("div[name='pupMentN']").css("display","block");
}

var closePupNew=function(){
    $("div[name='bgD']").css("display","none");
    $("div[name='pupMentN']").css("display","none");
    clearPupNew();
}

/**
 * 打开编辑弹框
 */
var showPup=function(){
    $("div[name='bgD']").css("display","block");
    $("div[name='pupMentE']").css("display","block");
}

/**
 * 关闭编辑弹框
 */
var closePup=function(){
    $("div[name='bgD']").css("display","none");
    $("div[name='pupMentE']").css("display","none");
    clearPup();
}

/**
 * 生成二维码
 * @param obj
 */
var createQrCode=function(obj){
    $("#qrCode").html(" ");
    var href=obj.text().trim();
    jQuery("#qrCode").qrcode({width:200,height:200,text:href});
    $("a[name='qrHref']").text("点击下载apk");
    $("a[name='qrHref']").attr("href",href);
}

/**
 * 打开apk客户端二维码页面
 */
var showQrCode=function(){
    $("div[name='bgD']").css("display","block");
    $("div[name='qrCode']").css("display","block");
}

/**
 * 关闭apk客户端二维码页面
 */
var closeQrCode=function(){
    $("div[name='bgD']").css("display","none");
    $("div[name='qrCode']").css("display","none");
}

/**
 * 打开等待弹框
 */
var showwaitFrame=function(){
    $("div[name='bgDNew']").css("display","block");
    $("div[name='waitFrame']").css("display","block");
}

/**
 * 关闭等待弹框
 */
var closewaitFrame=function(){
    $("div[name='bgDNew']").css("display","none");
    $("div[name='waitFrame']").css("display","none");
}

/**
 * 打开ios的plist配置信息框
 */
var showPlistInfo=function(){
    $("div[name='bgD']").css("display","block");
    $("div[name='plistField']").css("display","block");
}

/**
 * 关闭ios的plist配置信息框
 */
var closePlistInfo=function(){
    $("div[name='bgD']").css("display","none");
    $("div[name='plistField']").css("display","none");
    clearPlistInfo();
}

var clearPlistInfo=function(){
    $("div[name='fieldBody']").children("ul").html("");
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