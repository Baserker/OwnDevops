package com.caimao.devops.entity;

import com.alibaba.fastjson.JSONArray;

import javax.persistence.*;

/**
 * 模块名称：CaimaoDevOps com.caimao.devops.entity
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-09 10:00
 * 系统版本：1.0.0
 **/

@Entity
@Table(name = "CS_APP_PLIST_INFO", schema = "dbo")
public class CsAppPlistInfoEntity {
    private Integer id;
    /**
     * 所属分支
     */
    private String branch;
    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 版本的json字段信息
     */
    private String clientField;

    /**
     * plist模版信息
     */
    private String plistModule;

    public CsAppPlistInfoEntity(String branch, Integer productId, String channel, String clientField, String plistModule) {
        this.branch = branch;
        this.productId = productId;
        this.channel = channel;
        this.clientField = clientField;
        this.plistModule = plistModule;
    }

    public CsAppPlistInfoEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "BRANCH", nullable = false, length = 100)
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Basic
    @Column(name = "PRODUCT_ID",nullable = false)
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "CHANNEL",nullable = false)
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Basic
    @Column(name = "CLIENT_FIELD",nullable = false)
    public String getClientField() {
        return clientField;
    }

    public void setClientField(String clientField) {
        this.clientField = clientField;
    }

    @Basic
    @Column(name = "PLIST_MODULE",nullable = false)
    public String getPlistModule() {
        return plistModule;
    }

    public void setPlistModule(String plistModule) {
        this.plistModule = plistModule;
    }
}