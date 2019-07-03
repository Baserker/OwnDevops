package com.caimao.devops.service.impl;

import com.caimao.devops.dao.CsAppHisVersionDAO;
import com.caimao.devops.entity.CsAppHisVersion;
import com.caimao.devops.service.CsAppHisVersionService;
import com.caimao.devops.vo.CsAppHisVersionVO;
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
import java.util.List;

/**
 * 客户端历史版本实现方法
 * 模块名称：CaimaoDevOps com.caimao.devops.service.impl
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:56
 * 系统版本：1.0.0
 **/

@Service
public class CsAppHisVersionImpl implements CsAppHisVersionService{

    @Autowired
    private CsAppHisVersionDAO csAppHisVersionDAO;


    @Override
    public void saveOrUpdate(CsAppHisVersion model) {
        csAppHisVersionDAO.saveAndFlush(model);
    }

    @Override
    public CsAppHisVersion getModelById(int id) {
        return csAppHisVersionDAO.getById(id);
    }

    @Override
    public Page<CsAppHisVersion> getAppHisClientByCondition(int pageNum, int pageSize, CsAppHisVersionVO model) {
        String[] properties=new String[]{"publishTime","version1","version2","version3"};
        Pageable pageable=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,properties);
        Page<CsAppHisVersion> page=csAppHisVersionDAO.findAll(new Specification<CsAppHisVersion>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<CsAppHisVersion> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(model.getId()!=null&&model.getId()>0){
                    predicates.add(cb.equal(root.get("id").as(Integer.class),model.getId()));
                }if(model.getClientType()!=null&&model.getClientType()>-1){
                    predicates.add(cb.equal(root.get("clientType").as(Integer.class),model.getClientType()));
                }
                if(model.getProductId()!=null&&model.getProductId()>0){
                    predicates.add(cb.equal(root.get("productId").as(Integer.class),model.getProductId()));
                }
                if(StringUtils.isNotEmpty(model.getBranch())){
                    predicates.add(cb.equal(root.get("branch").as(String.class),model.getBranch()));
                }
                if(StringUtils.isNotEmpty(model.getChannel())){
                    predicates.add(cb.equal(root.get("channel").as(String.class),model.getChannel()));
                }
                if(model.getVersion1()!=null&&model.getVersion1()>-1){
                    predicates.add(cb.equal(root.get("version1").as(Integer.class),model.getVersion1()));
                }
                if(model.getVersion2()!=null&&model.getVersion2()>-1){
                    predicates.add(cb.equal(root.get("version2").as(Integer.class),model.getVersion2()));
                }
                if(model.getVersion3()!=null&&model.getVersion3()>-1){
                    predicates.add(cb.equal(root.get("version3").as(Integer.class),model.getVersion3()));
                }
                if(model.getPublisher()!=null&&model.getPublisher()>0){
                    predicates.add(cb.equal(root.get("publisher").as(Integer.class),model.getPublisher()));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                cq.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        },pageable);
        return page;
    }
}