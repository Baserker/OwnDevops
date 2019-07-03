package com.caimao.devops.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.caimao.devops.contants.ParamsContants;

/**
 * 拓展控制层
 * 模块名称：CaimaoDevOps com.caimao.devops.controller
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 16:27
 * 系统版本：1.0.0
 **/

public class BaseController {
    /**
     * 返回数据
     *
     * @param data
     */
    protected String responseData(int flag,String msg,Object data) {
        JSONObject result = new JSONObject(6);
        result.put(ParamsContants.FLAG, flag);
        result.put(ParamsContants.ERROR_MESSAGE, msg);
        result.put(ParamsContants.RET_TIME, System.currentTimeMillis());
        result.put("data", data);
        return result.toJSONString();
    }

    /**
     * 返回表格数据
     * @param total
     * @param data
     * @return
     */
    protected String responseTableData(int flag,String msg,int total,Object list,Object data){
        JSONObject result = new JSONObject(6);
        result.put("total",total);
        result.put("rows",list);
        result.put(ParamsContants.FLAG, flag);
        result.put(ParamsContants.ERROR_MESSAGE, msg);
        result.put(ParamsContants.RET_TIME, System.currentTimeMillis());
        result.put("data", data);
        return result.toJSONString();
    }
}