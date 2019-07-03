$(function(){
    loadTable($("span[name='id']").text());
})

var loadTable=function(id){
    var t = $("#hisClient-table").bootstrapTable({
        url:'/appClient/hisAppClients.do',
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
                id:id,
                pageNum: params.pageNumber,
                pageSize: params.pageSize
            };
            return param;
        },
        responseHandler:function(res){
            if(res.flag==1){
                return {
                    'total':res.data.numberOfElements,
                    'rows':res.data.content
                }
            }
        },
        queryParamsType: "undefined",
        idField: "id",//指定主键列
        columns: [
            {
                title: '',//表的列名
                field: 'id',
                align: 'center',//水平居中
                width: 50,
                formatter:function(value, row, index){
                    return index+1;
                }
            },
            {
                title: 'ID',
                field: 'id',
                align: 'center',
                width: 80
            },
            {
                title: '客户端类型',
                field: 'clientType',
                align: 'center',
                width: 100,
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
                title: '产品ID',
                field: 'productId',
                align: 'center',
                width: 50
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
                title: '上传人',
                field: 'publisher',
                align: 'center',
                width: 50
            },
            {
                title: '客户端简介',
                field: 'appDescribe',
                align: 'center',
                width: 200
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
                width: 50,
                formatter: function (value, row, index) {//自定义显示可以写标签哦~
                    return '<button type="button" class="btn btn-default" onclick="editClient($(this))">编辑</button>'
                }
            }
        ]
    })
}

var editClient=function(obj){
    var id=obj.parent("td").parent("tr").children("td").eq(1).text();
    $("input[name='clientId']").val(id);
    if(obj.parent("td").parent("tr").children("td").eq(8).text().trim()!='空'){
        $("textarea[name='appMemo']").val(obj.parent("td").parent("tr").children("td").eq(8).text().trim());
    }
    showPup();
}

var submitEdit=function(){
    var id=$("input[name='clientId']").val();
    var describe=$("textarea[name='appMemo']").val();
    $.ajax({
        type:'put',
        url:'/appClient/hisAppClient.do',
        data:{
            id:id,
            describe:describe
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
}