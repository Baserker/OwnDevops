package com.caimao.devops.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.caimao.devops.dao.AdminRoleDAO;
import com.caimao.loginRefact.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 角色实现类
 * 模块名称：CaimaoDevOps com.caimao.devops.service.impl
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-08 15:52
 * 系统版本：1.0.0
 **/

@Service(version = "1.0.0",timeout = 6000)
public class AdminRoleImpl implements AdminRoleService {

    @Autowired
    private AdminRoleDAO adminRoleDAO;
}