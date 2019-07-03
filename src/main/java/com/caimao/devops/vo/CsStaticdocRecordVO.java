package com.caimao.devops.vo;

import com.caimao.devops.entity.CsStaticdocRecord;

import java.util.Date;

/**
 * 静态文件的记录的拓展类
 * 模块名称：CaimaoDevOps com.caimao.devops.vo
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-05 15:55
 * 系统版本：1.0.0
 **/

public class CsStaticdocRecordVO extends CsStaticdocRecord {

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
}