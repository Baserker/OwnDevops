package com.caimao.devops.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caimao.devops.dao.CsAppPlistInfoDAO;
import com.caimao.devops.entity.CsAppPlistInfoEntity;
import com.caimao.devops.service.CsAppPlistInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ios的plist的实现类
 * 模块名称：CaimaoDevOps com.caimao.devops.service.impl
 * 功能说明：<br>
 * 开发人员：Wangyq
 * 创建时间： 2019-05-09 10:16
 * 系统版本：1.0.0
 **/

@Service
public class CsAppPlistInfoImpl implements CsAppPlistInfoService{

    @Autowired
    private CsAppPlistInfoDAO csAppPlistInfoDAO;

    RedisTemplate redisTemplate;

    private final static String key="IOS_CLIENT_PLIST";

    private static Map<String,CsAppPlistInfoEntity> localPlistInfo=new HashMap<>();

    @Override
    public CsAppPlistInfoEntity getInfoByCondition(String branch,String channel,Integer productId) {
        HashOperations hashOper=redisTemplate.opsForHash();
        StringBuffer keyStr=new StringBuffer(productId+"-"+branch);
        if(StringUtils.isNotEmpty(channel)){
            keyStr.append("-"+channel);
        }
        String str= (String) hashOper.get(key,keyStr.toString());
        if(StringUtils.isNotEmpty(str)){
            try{
                JSONObject json=JSONObject.parseObject(str);
                return JSON.toJavaObject(json,CsAppPlistInfoEntity.class);
            }catch (Exception e){
                e.printStackTrace();
                return csAppPlistInfoDAO.getInfoByCondition(branch,productId,channel);
            }
        }else{
            CsAppPlistInfoEntity entity=csAppPlistInfoDAO.getInfoByCondition(branch,productId,channel);

            if(entity!=null){
                String entityStr=JSONObject.toJSONString(entity);
                hashOper.put(key,keyStr.toString(),entityStr);
            }

            return entity;
        }
    }

    @Override
    public CsAppPlistInfoEntity getInfoByConditionLocal(String branch, String channel, Integer productId) {
        StringBuffer keyStr=new StringBuffer(productId+"-"+branch);
        if(StringUtils.isNotEmpty(channel)){
            keyStr.append("-"+channel);
        }
        if(localPlistInfo.containsKey(keyStr.toString())){
            return localPlistInfo.get(keyStr.toString());
        }else{
            CsAppPlistInfoEntity entity=csAppPlistInfoDAO.getInfoByCondition(branch,productId,channel);
            localPlistInfo.put(keyStr.toString(),entity);
            return entity;
        }
    }

    @Override
    public void saveOrUpdate(CsAppPlistInfoEntity info) {
        csAppPlistInfoDAO.saveAndFlush(info);
        flushLocalData(info);
    }

    /**
     * 刷新本地存储数据
     * @param info
     */
    private void flushLocalData(CsAppPlistInfoEntity info){
        StringBuffer keyStr=new StringBuffer(info.getProductId()+"-"+info.getBranch());
        if(StringUtils.isNotEmpty(info.getChannel())){
            keyStr.append("-"+info.getChannel());
        }
        localPlistInfo.put(keyStr.toString(),info);
    }

    /**
     * 刷新redis中存储的数据
     * @param info
     */
    private void flushRedisData(CsAppPlistInfoEntity info){
        HashOperations hashOper=redisTemplate.opsForHash();
        StringBuffer keyStr=new StringBuffer(info.getProductId()+"-"+info.getBranch());
        if(StringUtils.isNotEmpty(info.getChannel())){
            keyStr.append("-"+info.getChannel());
        }
        hashOper.put(key,keyStr.toString(),JSONObject.toJSONString(info));
    }

    @Override
    public CsAppPlistInfoEntity getInfoById(Integer id) {
        return csAppPlistInfoDAO.getInfoById(id);
    }

    @Override
    public Page<CsAppPlistInfoEntity> getInfoListByCondition(Integer pageNum, Integer pageSize, CsAppPlistInfoEntity info) {
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<CsAppPlistInfoEntity> page=csAppPlistInfoDAO.findAll(new Specification<CsAppPlistInfoEntity>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<CsAppPlistInfoEntity> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(info.getId()!=null&&info.getId()>0){
                    predicates.add(cb.equal(root.get("id").as(Integer.class),info.getId()));
                }
                if(StringUtils.isNotEmpty(info.getBranch())){
                    predicates.add(cb.equal(root.get("branch").as(String.class),info.getBranch()));
                }
                if(StringUtils.isNotEmpty(info.getChannel())){
                    predicates.add(cb.equal(root.get("channel").as(String.class),info.getChannel()));
                }
                if(info.getProductId()!=null&&info.getProductId()>0){
                    predicates.add(cb.equal(root.get("productId").as(Integer.class),info.getProductId()));
                }
                if(StringUtils.isNotEmpty(info.getClientField())){
                    predicates.add(cb.equal(root.get("clientField").as(String.class),info.getClientField()));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                cq.where(predicates.toArray(pre));
                return cb.and(predicates.toArray(pre));
            }
        },pageable);
        return page;
    }

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }
}