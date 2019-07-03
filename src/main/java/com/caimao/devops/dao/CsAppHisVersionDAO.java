package com.caimao.devops.dao;

import com.caimao.devops.entity.CsAppHisVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 客户端历史版本持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:53
 * 系统版本：1.0.0
 **/
public interface CsAppHisVersionDAO extends JpaRepository<CsAppHisVersion,Integer>,JpaSpecificationExecutor<CsAppHisVersion> {

    @Query(value = "select * from CS_APP_HISVERSION where ID=?",nativeQuery = true)
    CsAppHisVersion getById(int id);
}
