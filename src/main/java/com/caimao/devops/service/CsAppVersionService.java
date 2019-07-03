package com.caimao.devops.service;

import com.caimao.devops.entity.CsAppVersion;
import com.caimao.devops.vo.CsAppVersionVO;
import org.springframework.data.domain.Page;

/**
 * 客户端版本接口层
 * 模块名称：CaimaoDevOps com.caimao.devops.service
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:54
 * 系统版本：1.0.0
 **/
public interface CsAppVersionService {

    public void saveOrUpdate(CsAppVersion model);

    public CsAppVersion getModelById(int id);

    /**
     * 按照上传时间倒序、版本号1、版本号2、版本3进行排序查询
     * @param pageNum   页数
     * @param pageSize  每页条数
     * @param model 对象
     * @return
     */
    public Page<CsAppVersion> getAppHisClientByCondition(int pageNum, int pageSize, CsAppVersionVO model);

    /**
     * 获取最新版本的客户端版本
     * @param branch   分支
     * @param channel   渠道
     * @return
     */
    public CsAppVersion getLatestByCondition(String branch,String channel,int productId,int clientType);

    /**
     * 获取对应条件的客户端
     * @param version
     * @return
     */
    public CsAppVersion getVersionByCondition(CsAppVersion version);

    /**
     * 获取对应条件的客户端
     * @param version
     * @return
     */
    public CsAppVersion getLaVersionByCondition(CsAppVersion version);
}
