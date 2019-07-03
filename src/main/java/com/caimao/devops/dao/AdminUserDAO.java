package com.caimao.devops.dao;

import com.caimao.devops.entity.AdminNewUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户持久层
 * 模块名称：CaimaoDevOps com.caimao.devops.dao
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-08 15:25
 * 系统版本：1.0.0
 **/
public interface AdminUserDAO extends JpaRepository<AdminNewUserEntity,Integer>,JpaSpecificationExecutor<AdminNewUserEntity> {

    @Query(value = "SELECT * FROM ADMIN_NEW_USER WHERE NAME=? and PASSWORD=?",nativeQuery = true)
    AdminNewUserEntity getUserByNameAndPsd(String name,String password);
}
