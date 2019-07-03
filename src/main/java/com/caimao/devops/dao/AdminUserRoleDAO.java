package com.caimao.devops.dao;

import com.caimao.devops.entity.AdminNewUserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户角色持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-08 15:28
 * 系统版本：1.0.0
 **/
public interface AdminUserRoleDAO extends JpaRepository<AdminNewUserRoleEntity,Integer>,JpaSpecificationExecutor<AdminNewUserRoleEntity> {

    @Query(value = "SELECT * FROM ADMIN_NEW_USER_ROLE WHERE USER_ID=?",nativeQuery = true)
    List<AdminNewUserRoleEntity> getURListByUserId(Integer userId);
}
