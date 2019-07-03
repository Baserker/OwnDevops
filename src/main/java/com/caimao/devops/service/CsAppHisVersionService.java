package com.caimao.devops.service;

import com.caimao.devops.entity.CsAppHisVersion;
import com.caimao.devops.vo.CsAppHisVersionVO;
import org.springframework.data.domain.Page;

/**
 * 客户端历史版本接口层
 * 模块名称：CaimaoDevOps com.caimao.devops.service
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:55
 * 系统版本：1.0.0
 **/
public interface CsAppHisVersionService {

    public void saveOrUpdate(CsAppHisVersion model);

    public CsAppHisVersion getModelById(int id);

    public Page<CsAppHisVersion> getAppHisClientByCondition(int pageNum, int pageSize, CsAppHisVersionVO model);
}
