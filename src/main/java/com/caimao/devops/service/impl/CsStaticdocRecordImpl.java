package com.caimao.devops.service.impl;

import com.caimao.devops.dao.CsStaticdocRecordDAO;
import com.caimao.devops.entity.CsStaticdocRecord;
import com.caimao.devops.service.CsStaticdocRecordService;
import com.caimao.devops.vo.CsStaticdocRecordVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 静态文件的记录的接口实现
 * 模块名称：CaimaoDevOps com.caimao.devops.service.impl
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-05 15:57
 * 系统版本：1.0.0
 **/

@Service
public class CsStaticdocRecordImpl implements CsStaticdocRecordService{

    @Autowired
    private CsStaticdocRecordDAO csStaticdocRecordDAO;



    @Override
    public Page<CsStaticdocRecord> getStaticDocByCondition(Integer page, Integer pageSize, CsStaticdocRecordVO model) {
        String[] properties=new String[]{"publishTime"};
        Pageable pageable=new PageRequest(page,pageSize, Sort.Direction.DESC,properties);
        Page<CsStaticdocRecord> result=csStaticdocRecordDAO.findAll(new Specification<CsStaticdocRecord>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<CsStaticdocRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(model.getId()!=null&&model.getId()>0){
                    predicates.add(cb.equal(root.get("id").as(Integer.class),model.getId()));
                }
                if(StringUtils.isNotEmpty(model.getDocName())){
                    predicates.add(cb.equal(root.get("docName").as(String.class),model.getDocName()));
                }
                if(StringUtils.isNotEmpty(model.getDocType())){
                    predicates.add(cb.equal(root.get("docType").as(String.class),model.getDocType()));
                }
                if(StringUtils.isNotEmpty(model.getDocPath())){
                    predicates.add(cb.equal(root.get("docPath").as(String.class),model.getDocPath()));
                }
                if(StringUtils.isNotEmpty(model.getDocUrl())){
                    predicates.add(cb.equal(root.get("docUrl").as(String.class),model.getDocUrl()));
                }
                if(model.getPublisher()!=null&&model.getPublisher()>0){
                    predicates.add(cb.equal(root.get("publisher").as(Integer.class),model.getPublisher()));
                }
                if(model.getStart()!=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("publishTime").as(Date.class),model.getStart()));
                }
                if(model.getEnd()!=null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("publishTime").as(Date.class),model.getEnd()));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                cq.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            };
        },pageable);
        return result;
    }

    @Override
    public void saveOrUpdate(CsStaticdocRecord model) {
        csStaticdocRecordDAO.saveAndFlush(model);
    }

    @Override
    public CsStaticdocRecord getModelById(int id) {
        return csStaticdocRecordDAO.getModelById(id);
    }
}