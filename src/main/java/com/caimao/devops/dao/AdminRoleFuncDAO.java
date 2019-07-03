package com.caimao.devops.dao;

import com.caimao.devops.entity.AdminNewRoleFunctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 角色功能持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-08 15:29
 * 系统版本：1.0.0
 **/
public interface AdminRoleFuncDAO extends JpaRepository<AdminNewRoleFunctionEntity,Integer>,JpaSpecificationExecutor<AdminNewRoleFunctionEntity> {

    @Query(value = "SELECT * FROM ADMIN_NEW_ROLE_FUNCTION WHERE ROLE_ID=?",nativeQuery = true)
    AdminNewRoleFunctionEntity getRFuncByRoleId(Integer roleId);
}
