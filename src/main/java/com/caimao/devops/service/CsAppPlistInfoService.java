package com.caimao.devops.service;

import com.caimao.devops.entity.CsAppPlistInfoEntity;
import org.springframework.data.domain.Page;

/**
 * ios的plist的接口层
 * 模块名称：CaimaoDevOps com.caimao.devops.service
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-09 10:14
 * 系统版本：1.0.0
 **/
public interface CsAppPlistInfoService {

    /**
     * 根据分支和产品，获取对应的plist的内容(缓存走redis)
     * @param branch
     * @param productId
     * @param channel
     * @return
     */
    public CsAppPlistInfoEntity getInfoByCondition(String branch,String channel,Integer productId);

    /**
     * 根据分支和产品，获取对应的plist的内容(缓存走本地)
     * @param branch
     * @param productId
     * @param channel
     * @return
     */
    public CsAppPlistInfoEntity getInfoByConditionLocal(String branch,String channel,Integer productId);

    public void saveOrUpdate(CsAppPlistInfoEntity info);

    public CsAppPlistInfoEntity getInfoById(Integer id);

    /**
     * 根据条件获取ios的plist信息列表
     * @param pageNum
     * @param pageSize
     * @param info
     * @return
     */
    public Page<CsAppPlistInfoEntity> getInfoListByCondition(Integer pageNum,Integer pageSize,CsAppPlistInfoEntity info);
}
