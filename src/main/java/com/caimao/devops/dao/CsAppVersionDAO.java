package com.caimao.devops.dao;

import com.caimao.devops.entity.CsAppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 客户端版本持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:52
 * 系统版本：1.0.0
 **/
public interface CsAppVersionDAO extends JpaRepository<CsAppVersion,Integer>,JpaSpecificationExecutor<CsAppVersion> {

    @Query(value = "select * from CS_APP_VERSION where ID=?",nativeQuery = true)
    CsAppVersion getById(int id);

    @Query(value = "select top(1)* from CS_APP_VERSION where BRANCH=? and CHANNEL=? and PRODUCT_ID=? and CLIENT_TYPE=? ORDER BY VERSION1 DESC,VERSION2 DESC,VERSION3 DESC",nativeQuery = true)
    CsAppVersion getLatestByCondition(String branch,String channel,int productId,int clientType);

    @Query(value = "select * from CS_APP_VERSION where CLIENT_TYPE=? and PRODUCT_ID=? and BRANCH=? and CHANNEL=? and VERSION1=? and VERSION2=? and VERSION3=?",nativeQuery = true)
    CsAppVersion getVersionByCondition(Integer clientType,Integer productId,String branch,String channel,Integer version1,Integer version2,Integer version3);

    @Query(value = "select * from CS_APP_VERSION where CLIENT_TYPE=? and PRODUCT_ID=? and BRANCH=? and VERSION1=? and VERSION2=? and VERSION3=?",nativeQuery = true)
    CsAppVersion getLVersionByCondition(Integer clientType,Integer productId,String branch,Integer version1,Integer version2,Integer version3);

    @Query(value = "select * from CS_APP_VERSION where CLIENT_TYPE=? and PRODUCT_ID=? and BRANCH=? ORDER BY VERSION1 DESC,VERSION2 DESC,VERSION3 DESC",nativeQuery = true)
    CsAppVersion getLaVersionByCondition(Integer clientType,Integer productId,String branch);
}
