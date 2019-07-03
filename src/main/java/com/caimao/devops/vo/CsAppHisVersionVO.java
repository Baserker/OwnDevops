package com.caimao.devops.vo;

import com.caimao.devops.entity.CsAppHisVersion;
import com.caimao.devops.entity.CsAppVersion;

import java.util.Date;

/**
 * 模块名称：CaimaoDevOps com.caimao.devops.vo
 * 功能说明：客户端历史版本扩展类
 * 开发人员：Wangyq
 * 创建时间： 2019-04-16 15:53
 * 系统版本：1.0.0
 **/

public class CsAppHisVersionVO extends CsAppHisVersion{

    private String publishTimeStart;

    private String publishTimeEnd;

    private Date start;

    private Date end;

    private String version;

    public CsAppHisVersionVO() {

    }

    public CsAppHisVersionVO(CsAppVersion version){
        this.clientType = version.getClientType();
        this.productId = version.getProductId();
        this.branch = version.getBranch();
        this.channel = version.getChannel();
        this.version1 = version.getVersion1();
        this.version2 = version.getVersion2();
        this.version3 = version.getVersion3();
        this.md5 = version.getMd5();
        this.sha1 = version.getSha1();
        this.publisher = version.getPublisher();
        this.publishTime = version.getPublishTime();
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
}