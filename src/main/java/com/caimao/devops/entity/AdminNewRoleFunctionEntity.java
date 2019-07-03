package com.caimao.devops.entity;

import javax.persistence.*;

/**
 * 模块名称：CaimaoDevOps com.caimao.devops.entity
 * 功能说明：<br>
 * 开发人员：luzx
 * 创建时间： 2019-05-08 17:23
 * 系统版本：1.0.0
 **/

@Entity
@Table(name = "ADMIN_NEW_ROLE_FUNCTION", schema = "dbo")
public class AdminNewRoleFunctionEntity {
    private int id;
    private int roleId;
    private String name;
    private String functionList;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ROLE_ID", nullable = false)
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "NAME", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "FUNCTION_LIST", nullable = true, length = 1000)
    public String getFunctionList() {
        return functionList;
    }

    public void setFunctionList(String functionList) {
        this.functionList = functionList;
    }

}