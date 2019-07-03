$(function(){
    loadTable();
})

var loadTable=function(){
    var t = $("#plist-table").bootstrapTable({
        url:'/appClient/plistInfos.do',
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
                title: '分支',
                field: 'branch',
                align: 'center',
                width: 50
            },
            {
                title: '渠道',
                field: 'channel',
                align: 'center',
                width: 50
            },
            {
                title: '项目ID',
                field: 'productId',
                align: 'center',
                width: 50,
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
                title: '操作',
                field: 'id',
                align: 'center',
                width: 50,
                formatter: function (value, row, index) {//自定义显示可以写标签哦~
                    return '<button type="button" class="btn btn-default" onclick="editPlist('+value+')">编辑</button>'
                }
            }
        ]
    })
}

var editPlist=function(id){
    window.open("/req/plistDetail.do?id="+id);
}

var showPupNew=function(){
    window.open("/req/plistDetail.do?id=0");
}

