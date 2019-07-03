package com.caimao.devops.service.impl;

import com.caimao.devops.dao.CsAppVersionDAO;
import com.caimao.devops.entity.CsAppVersion;
import com.caimao.devops.service.CsAppVersionService;
import com.caimao.devops.vo.CsAppVersionVO;
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
 * 客户端版本实现方法
 * 模块名称：CaimaoDevOps com.caimao.devops.service.impl
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 14:55
 * 系统版本：1.0.0
 **/

@Service
public class CsAppVersionImpl implements CsAppVersionService{

    @Autowired
    private CsAppVersionDAO csAppVersionDAO;


    @Override
    public void saveOrUpdate(CsAppVersion model) {
        csAppVersionDAO.saveAndFlush(model);
    }

    @Override
    public CsAppVersion getModelById(int id) {
        return csAppVersionDAO.getById(id);
    }

    @Override
    public Page<CsAppVersion> getAppHisClientByCondition(int pageNum, int pageSize, CsAppVersionVO model) {
        String[] properties=new String[]{"publishTime","version1","version2","version3"};
        Pageable pageable=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,properties);
        Page<CsAppVersion> page=csAppVersionDAO.findAll(new Specification<CsAppVersion>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<CsAppVersion> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
                if(StringUtils.isNotEmpty(model.getMd5())){
                    predicates.add(cb.equal(root.get("md5").as(String.class),model.getMd5()));
                }
                if(StringUtils.isNotEmpty(model.getSha1())){
                    predicates.add(cb.equal(root.get("sha1").as(String.class),model.getSha1()));
                }
                if(model.getPublisher()!=null&&model.getPublisher()>0){
                    predicates.add(cb.equal(root.get("publisher").as(Integer.class),model.getPublisher()));
                }
                if(model.getGuideStatus()!=null&&model.getGuideStatus()>-1){
                    predicates.add(cb.equal(root.get("guideStatus").as(Integer.class),model.getGuideStatus()));
                }
                if(StringUtils.isNotEmpty(model.getGuideTarget())){
                    predicates.add(cb.equal(root.get("guideTarget").as(String.class),model.getGuideTarget()));
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
            }
        },pageable);
        return page;
    }

    @Override
    public CsAppVersion getLatestByCondition(String branch, String channel,int productId,int clientType) {
        return csAppVersionDAO.getLatestByCondition(branch,channel,productId,clientType);
    }

    @Override
    public CsAppVersion getVersionByCondition(CsAppVersion model) {
        CsAppVersion db=new CsAppVersion();
        if(StringUtils.isEmpty(model.getChannel())){
            db=csAppVersionDAO.getLVersionByCondition(model.getClientType(),model.getProductId(),model.getBranch(),model.getVersion1(),model.getVersion2(),model.getVersion3());
        }else{
            db=csAppVersionDAO.getVersionByCondition(model.getClientType(),model.getProductId(),model.getBranch(),model.getChannel(),model.getVersion1(),model.getVersion2(),model.getVersion3());
        }

        if(db==null){
            return model;
        }
        return db;
    }

    @Override
    public CsAppVersion getLaVersionByCondition(CsAppVersion model) {
        CsAppVersion db=csAppVersionDAO.getLaVersionByCondition(model.getClientType(),model.getProductId(),model.getBranch());
        if(db==null){
            return model;
        }
        return db;
    }
}