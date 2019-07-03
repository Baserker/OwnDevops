package com.caimao.devops.contants;

/**
 * 项目ID的枚举类
 * 模块名称：CaimaoDevOps com.caimao.devops.contants
 * 功能说明：<br>
 * 开发人员：Wangyq
 * 创建时间： 2019-07-02 15:08
 * 系统版本：1.0.0
 **/

public enum ProductContants {

    UNKNOWN("unknown",0),
    LOTTERY("lottery",1),
    SPORTS("sports",2),
    NIUNIU("niuniu",3),
    COIN("coin",4),
    STOCk("stock",5);

    ProductContants(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private String name;

    private int code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static int getCodeByName(String name){
        for(ProductContants p:ProductContants.values()){
            if(p.name.equals(name)){
                return p.code;
            }
        }
        return 0;
    }
}