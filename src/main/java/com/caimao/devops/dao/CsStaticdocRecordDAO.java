package com.caimao.devops.dao;

import com.caimao.devops.entity.CsStaticdocRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 静态文件的记录持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-05 15:52
 * 系统版本：1.0.0
 **/
public interface CsStaticdocRecordDAO extends JpaRepository<CsStaticdocRecord,Integer>,JpaSpecificationExecutor<CsStaticdocRecord> {

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    @Query(value = "select * from CS_STATICDOC_RECORD where ID=?",nativeQuery = true)
    CsStaticdocRecord getModelById(int id);
}
