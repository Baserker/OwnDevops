package com.caimao.devops.vo;

import com.caimao.devops.entity.CsAppVersion;

import java.util.Date;

/**
 * 客户端版本拓展类
 * 模块名称：CaimaoDevOps com.caimao.devops.vo
 * 功能说明：<br>
 * 开发人员：Wangyq
 * 创建时间： 2019-04-16 15:52
 * 系统版本：1.0.0
 **/

public class CsAppVersionVO extends CsAppVersion{

    /**
     * 上传开始时间str
     */
    private String publishTimeStart;

    /**
     * 上传结束时间str
     */
    private String publishTimeEnd;

    /**
     * 上传开始时间Date
     */
    private Date start;

    /**
     * 上传结束时间Date
     */
    private Date end;

    /**
     * 版本号（拼接而成）
     */
    private String version;

    /**
     * 引导目标中的分支
     */
    private String targetBranch;

    /**
     * 引导目标中的渠道
     */
    private String targetChannel;

    /**
     * 引导目标中的版本
     */
    private String targetVersion;

    /**
     * ios的plist访问地址
     */
    private String plistUrl;

    public CsAppVersionVO() {
    }

    public CsAppVersionVO(CsAppVersion version) {
        this.clientType = version.getClientType();
        this.productId = version.getProductId();
        this.branch = version.getBranch();
        this.channel = version.getChannel();
        this.version1 = version.getVersion1();
        this.version2 = version.getVersion2();
        this.version3 = version.getVersion3();
        this.docPosition = version.getDocPosition();
        this.md5 = version.getMd5();
        this.sha1 = version.getSha1();
        this.publisher = version.getPublisher();
        this.status = version.getStatus();
        this.guideStatus = version.getGuideStatus();
        this.guideTarget = version.getGuideTarget();
        this.appMemo = version.getAppMemo();
        this.publishTime = version.getPublishTime();
    }

    public CsAppVersionVO (Integer id,Integer clientType,Integer productId,String branch,String channel,String version,Integer publisher,Integer status,Integer guideStatus){
        this.id=id;
        this.clientType = clientType;
        this.productId = productId;
        this.branch = branch;
        this.channel = channel;
        this.version = version;
        this.publisher = publisher;
        this.status = status;
        this.guideStatus = guideStatus;
    }

    public String getPublishTimeStart() {
        return publishTimeStart;
    }

    public void setPublishTimeStart(String publishTimeStart) {
        this.publishTimeStart = publishTimeStart;
    }

    public String getPublishTimeEnd() {
        return publishTimeEnd;
    }

    public void setPublishTimeEnd(String publishTimeEnd) {
        this.publishTimeEnd = publishTimeEnd;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public String getTargetChannel() {
        return targetChannel;
    }

    public void setTargetChannel(String targetChannel) {
        this.targetChannel = targetChannel;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public String getPlistUrl() {
        return plistUrl;
    }

    public void setPlistUrl(String plistUrl) {
        this.plistUrl = plistUrl;
    }
}