var urlFix='itms-services://?action=download-manifest&url=https://download.fcaimao.com/exter/ios/';
$(function(){

    $.ajax({
        type:'GET',
        url:'/appClient/plistOne.do',
        data:{
            id:$("span[name='iosId']").text()
        },
        dataType:'json',
        success:function(result){
            if(result.flag==1){
                var data=result.data;
                var url=urlFix+data.productId+"/"+data.branch+"/"+data.channel;
                if(data.version1==null||data.version1==""||typeof(data.version1)=='undefined'){
                    url=url+'.plist';
                }else{
                    url=url+"/"+data.version1+"."+data.version2+"."+data.version3+".plist";
                }
                $("span[name='iosHref']").text(url);
            }else{
                alert(result.errorMessage);
            }
        }
    })

    $("button[name='downBtn']").click(function(){
        downloadClient();
    })
})

/**
 * 下载ios客户端
 */
var downloadClient=function(){
    /*var id=$("span[name='iosId']").text();
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'get'); //请求方式
    form.attr('action', '/appClient/plistOne.do');//请求地址

    var input = $('<input>');
    //将你请求的数据模仿成一个input表单
    input.attr('type', 'hidden');
    input.attr('name', "id");//该输入框的name
    input.attr('value', id);//该输入框的值
    form.append(input);

    $('body').append(form);

    form.submit();
    form.remove();*/
    var url=$("span[name='iosHref']").text();
    window.location.href=url;
}