package com.caimao.devops.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 客户端版本信息
 * 模块名称：CaimaoDevOps com.caimao.devops.entity
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:29
 * 系统版本：1.0.0
 **/

@Entity
@Table(name = "CS_APP_VERSION")
public class CsAppVersion {

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

    @Column(name = "DOC_POSITION")
    //文件地址
    protected String docPosition;

    @Column(name = "DOC_URL")
    //文件访问地址
    protected String docUrl;

    @Column(name = "MD5")
    //MD5
    protected String md5;

    @Column(name = "SHA1")
    //SHA1
    protected String sha1;

    @Column(name = "PUBLISHER")
    //上传人
    protected Integer publisher;

    @Column(name = "STATUS")
    //状态:测试、预发布、正式、下架
    protected Integer status;

    @Column(name = "GUIDE_STATUS")
    //引导状态:允许使用、提示更新、强制更新
    protected Integer guideStatus;

    @Column(name = "GUIDE_TARGET")
    //引导目标
    protected String guideTarget;

    @Column(name = "APP_MEMO")
    //客户端简介
    protected String appMemo;

    @Column(name = "PUBLISH_TIME")
    //上传时间
    protected Date publishTime;

    //ios的plist的Field信息（json格式）
    @Column(name = "PLIST_FIELD")
    protected String plistField;

    //apk的状态
    @Column(name = "APK_STATUS")
    protected Integer apkStatus;

    public CsAppVersion(){}

    public CsAppVersion(Integer clientType, Integer productId, String branch, String channel,Integer publisher) {
        this.clientType = clientType;
        this.productId = productId;
        this.branch = branch;
        this.channel = channel;
        this.publisher=publisher;
        this.publishTime=new Date();
    }

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

    public String getDocPosition() {
        return docPosition;
    }

    public void setDocPosition(String docPosition) {
        this.docPosition = docPosition;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        if(status!=null){
            this.status = status;
        }
    }

    public Integer getGuideStatus() {
        return guideStatus;
    }

    public void setGuideStatus(Integer guideStatus) {
        if(guideStatus!=null){
            this.guideStatus = guideStatus;
        }
    }

    public String getGuideTarget() {
        return guideTarget;
    }

    public void setGuideTarget(String guideTarget) {
        this.guideTarget = guideTarget;
    }

    public String getAppMemo() {
        return appMemo;
    }

    public void setAppMemo(String appMemo) {
        this.appMemo = appMemo;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getPlistField() {
        return plistField;
    }

    public void setPlistField(String plistField) {
        this.plistField = plistField;
    }

    public Integer getApkStatus() {
        return apkStatus;
    }

    public void setApkStatus(Integer apkStatus) {
        this.apkStatus = apkStatus;
    }
}