package com.caimao.devops.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.caimao.devops.dao.AdminRoleFuncDAO;
import com.caimao.devops.dao.AdminUserDAO;
import com.caimao.devops.dao.AdminUserRoleDAO;
import com.caimao.devops.entity.AdminNewRoleFunctionEntity;
import com.caimao.devops.entity.AdminNewUserEntity;
import com.caimao.devops.entity.AdminNewUserRoleEntity;
import com.caimao.loginRefact.entity.LoginVO;
import com.caimao.loginRefact.service.AdminUserService;
import com.caimao.loginRefact.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户实现类
 * 模块名称：CaimaoDevOps com.caimao.devops.service.impl
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-05-08 15:41
 * 系统版本：1.0.0
 **/

@Service(version = "1.0.0",timeout = 6000)
public class AdminUserImpl implements AdminUserService {

    @Autowired
    private AdminUserDAO adminUserDAO;

    @Autowired
    private AdminUserRoleDAO adminUserRoleDAO;

    @Autowired
    private AdminRoleFuncDAO adminRoleFuncDAO;

    @Override
    public List<LoginVO> testGetAll() {
        List<LoginVO> result=new ArrayList<>();
        List<AdminNewUserEntity> list=adminUserDAO.findAll();
        if(list.size()>0){
            for(AdminNewUserEntity e:list){
                LoginVO vo=new LoginVO();
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public LoginVO getUserToken(String userName, String password) {
        LoginVO vo=new LoginVO();
        AdminNewUserEntity user=adminUserDAO.getUserByNameAndPsd(userName,password);
        if(user==null){
            return vo;
        }
        String token= TokenUtil.getIntance().createToken(user.getId());
        List<String> funcList=new ArrayList<>();
        List<AdminNewUserRoleEntity> list=adminUserRoleDAO.getURListByUserId(user.getId());
        if(list.size()>0){
            for(AdminNewUserRoleEntity ur:list){
                AdminNewRoleFunctionEntity rf=adminRoleFuncDAO.getRFuncByRoleId(ur.getRoleId());
                if(rf!=null){
                    String funcStr=rf.getFunctionList();
                    if(StringUtils.isNotEmpty(funcStr)){
                        JSONArray fArr=JSONArray.parseArray(funcStr);
                        if(fArr.size()>0){
                            for(Object func:fArr){
                                funcList.add((String) func);
                            }
                        }
                    }
                }
            }
        }
        vo.setUserToken(token);
        vo.setFuncList(funcList);
        return vo;
    }
}