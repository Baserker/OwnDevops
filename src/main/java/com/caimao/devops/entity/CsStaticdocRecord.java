package com.caimao.devops.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 模块名称：CaimaoDevOps com.caimao.devops.entity
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-29 11:31
 * 系统版本：1.0.0
 **/

@Entity
@Table(name = "CS_STATICDOC_RECORD", schema = "dbo")
public class CsStaticdocRecord {
    protected Integer id;
    /**
     *  文件名称
     */
    protected String docName;
    /**
     * 文件类型
     */
    protected String docType;
    /**
     * 文件存储路径
     */
    protected String docPath;
    /**
     * 文件访问路径
     */
    protected String docUrl;
    /**
     * 文件上传人
     */
    protected Integer publisher;
    /**
     * 文件上传时间
     */
    protected Date publishTime;
    /**
     * 文件MD5
     */
    protected String md5;

    public CsStaticdocRecord() {
    }

    public CsStaticdocRecord(String docName, String docType, String docPath, String docUrl, Integer publisher, Date publishTime, String md5) {
        this.docName = docName;
        this.docType = docType;
        this.docPath = docPath;
        this.docUrl = docUrl;
        this.publisher = publisher;
        this.publishTime = publishTime;
        this.md5 = md5;
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
    @Column(name = "DOC_NAME", nullable = true, length = 127)
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Basic
    @Column(name = "DOC_TYPE", nullable = false, length = 50)
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Basic
    @Column(name = "DOC_PATH", nullable = true, length = 255)
    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    @Basic
    @Column(name = "DOC_URL", nullable = true, length = 255)
    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    @Basic
    @Column(name = "PUBLISHER", nullable = true)
    public Integer getPublisher() {
        return publisher;
    }

    public void setPublisher(Integer publisher) {
        this.publisher = publisher;
    }

    @Basic
    @Column(name = "PUBLISH_TIME", nullable = true)
    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    @Basic
    @Column(name = "MD5", nullable = true, length = 255)
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}