package com.caimao.devops.vo;

import com.alibaba.fastjson.JSONArray;
import com.caimao.devops.entity.CsAppPlistInfoEntity;

/**
 * plist拓展类
 * 模块名称：CaimaoDevOps com.caimao.devops.vo
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-06-11 15:41
 * 系统版本：1.0.0
 **/

public class CsAppPlistInfoVO extends CsAppPlistInfoEntity{

    private JSONArray field;

    public CsAppPlistInfoVO(CsAppPlistInfoEntity plistInfoEntity) {
        this.setId(plistInfoEntity.getId());
        this.setBranch(plistInfoEntity.getBranch());
        this.setChannel(plistInfoEntity.getChannel());
        this.setProductId(plistInfoEntity.getProductId());
        this.setClientField(plistInfoEntity.getClientField());
        this.setPlistModule(plistInfoEntity.getPlistModule());
    }

    public CsAppPlistInfoVO(){}

    public JSONArray getField() {
        return field;
    }

    public void setField(JSONArray field) {
        this.field = field;
    }
}