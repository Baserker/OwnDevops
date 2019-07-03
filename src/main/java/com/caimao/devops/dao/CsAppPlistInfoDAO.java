package com.caimao.devops.dao;

import com.caimao.devops.entity.CsAppPlistInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * ios的Plist持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-09 10:03
 * 系统版本：1.0.0
 **/
public interface CsAppPlistInfoDAO extends JpaRepository<CsAppPlistInfoEntity,Integer>,JpaSpecificationExecutor<CsAppPlistInfoEntity> {

    @Query(value = "SELECT * FROM CS_APP_PLIST_INFO WHERE ID=?",nativeQuery = true)
    CsAppPlistInfoEntity getInfoById(Integer id);

    /**
     * 根据分支、产品、分支，获取对应的plist的内容
     * @param branch
     * @param productId
     * @return
     */
    @Query(value = "SELECT * FROM CS_APP_PLIST_INFO WHERE BRANCH=? and PRODUCT_ID=? and CHANNEL=?",nativeQuery = true)
    CsAppPlistInfoEntity getInfoByCondition(String branch,Integer productId,String channel);
}
