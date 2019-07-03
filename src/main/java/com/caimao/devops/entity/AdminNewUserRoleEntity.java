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
@Table(name = "ADMIN_NEW_USER_ROLE", schema = "dbo")
public class AdminNewUserRoleEntity {
    private int id;
    private int userId;
    private int roleId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "USER_ID", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "ROLE_ID", nullable = false)
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}