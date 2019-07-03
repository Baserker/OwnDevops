package com.caimao.devops.dao;

import com.caimao.devops.entity.AdminNewRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 角色持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-08 15:26
 * 系统版本：1.0.0
 **/
public interface AdminRoleDAO extends JpaRepository<AdminNewRoleEntity,Integer>,JpaSpecificationExecutor<AdminNewRoleEntity> {


}
