package com.caimao.devops.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 客户端历史版本信息
 * 模块名称：CaimaoDevOps com.caimao.devops.entity
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:41
 * 系统版本：1.0.0
 **/

@Entity
@Table(name = "CS_APP_HISVERSION")
public class CsAppHisVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "CLIENT_TYPE")
    //客户端类型:
    protected Integer clientType;

    @Column(name = "PRODUCT_ID")
    //产品ID
    protected Integer productId;

    @Column(name = "BRANCH")
    //分支
    protected String branch;

    @Column(name = "CHANNEL")
    //渠道
    protected String channel;

    @Column(name = "VERSION1")
    //版本号1
    protected Integer version1;

    @Column(name = "VERSION2")
    //版本号2
    protected Integer version2;

    @Column(name = "VERSION3")
    //版本号3
    protected Integer version3;

    @Column(name = "MD5")
    //MD5
    protected String md5;

    @Column(name = "SHA1")
    //SHA1
    protected String sha1;

    @Column(name = "PUBLISHER")
    //上传人
    protected Integer publisher;

    @Column(name = "APP_DESCRIBE")
    //历史版本描述
    protected String appDescribe;

    @Column(name = "PUBLISH_TIME")
    //上传时间
    protected Date publishTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getVersion1() {
        return version1;
    }

    public void setVersion1(Integer version1) {
        this.version1 = version1;
    }

    public Integer getVersion2() {
        return version2;
    }

    public void setVersion2(Integer version2) {
        this.version2 = version2;
    }

    public Integer getVersion3() {
        return version3;
    }

    public void setVersion3(Integer version3) {
        this.version3 = version3;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public Integer getPublisher() {
        return publisher;
    }

    public void setPublisher(Integer publisher) {
        this.publisher = publisher;
    }

    public String getAppDescribe() {
        return appDescribe;
    }

    public void setAppDescribe(String appDescribe) {
        this.appDescribe = appDescribe;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
}