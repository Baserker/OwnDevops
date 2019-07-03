package com.caimao.devops.service;

import com.caimao.devops.entity.CsStaticdocRecord;
import com.caimao.devops.vo.CsStaticdocRecordVO;
import org.springframework.data.domain.Page;

/**
 * 静态文件的记录接口层
 * 模块名称：CaimaoDevOps com.caimao.devops.service
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-05 15:51
 * 系统版本：1.0.0
 **/
public interface CsStaticdocRecordService {

    /**
     * 根据条件分页获取静态文件记录
     * @param page  页码
     * @param pageSize  页数
     * @param model 拓展类
     * @return
     */
    Page<CsStaticdocRecord> getStaticDocByCondition(Integer page, Integer pageSize, CsStaticdocRecordVO model);

    /**
     * 保存或者更新上传记录
     * @param model
     */
    void saveOrUpdate(CsStaticdocRecord model);

    /**
     * 根据Id获取对应的上传记录
     * @param id
     * @return
     */
    CsStaticdocRecord getModelById(int id);
}
