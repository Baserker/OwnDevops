package com.caimao.devops.controller;

import com.alibaba.fastjson.JSONObject;
import com.caimao.devops.contants.CodeContants;
import com.caimao.devops.contants.ProductContants;
import com.caimao.devops.controller.base.BaseController;
import com.caimao.devops.entity.CsAppPlistInfoEntity;
import com.caimao.devops.entity.CsAppVersion;
import com.caimao.devops.service.CsAppPlistInfoService;
import com.caimao.devops.service.CsAppVersionService;
import com.caimao.devops.utils.DocManager;
import com.caimao.devops.vo.CsAppVersionVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 对外接口
 * 模块名称：CaimaoDevOps com.caimao.devops.controller
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-06-11 16:31
 * 系统版本：1.0.0
 **/

@Controller
@RequestMapping(value = "/exter")
public class ExterTailController extends BaseController {

    @Autowired
    private CsAppVersionService csAppVersionService;

    @Autowired
    private CsAppPlistInfoService csAppPlistInfoService;

    /**
     * ios的plist的请求地址前缀
     */
    @Value("${doc.iosPlistUrl}")
    private String plistUrlFixed;

    /**
     * android客户端对外访问地址
     * @param productId
     * @param branch
     * @param channel
     * @param version
     * @return
     * {
     *      errorMessage: "",
     *      flag: 1,
     *      retTime: 1560737259903,
     *      data: {
     *          branch: "cpcp",     //分支
     *          channel: "cm",      //渠道
     *          clientType: 2,      //客户端类型
     *          docPosition: "/usr/local/clients/cpcp/cm/2.0.0/cpcp-cm-2.0.0.apk",      //客户端存储地址
     *          docUrl: "https://download.fcaimao.com/clients/cpcp/cm/2.0.0/cpcp-cm-2.0.0.apk",     //客户端访问地址
     *          id: 1,      //ID
     *          md5: "efb5c05b2bea419fca5158c8e465958b",       //md5加密
     *          productId: 1,       //产品ID
     *          publishTime: 1560502534727,     //发布时间
     *          publisher: 0,       //发布人
     *          version1: 2,        //版本1
     *          version2: 0,        //版本2
     *          version3: 0         //版本3
     *}
     *}
     */
    @RequestMapping(value = {"/apk/{productId}/{branch}/{channel}/{version}","/apk/{productId}/{branch}/{channel}"},method = RequestMethod.GET)
    @ResponseBody
    public String apk(@PathVariable("productId") String productId,@PathVariable("branch") String branch,@PathVariable("channel") String channel,@PathVariable(required = false) String version){
        if(StringUtils.isEmpty(productId)){
            return responseData(CodeContants.SUCCESS,"项目ID不能为空!!!","");
        }
        if(StringUtils.isEmpty(branch)){
            return responseData(CodeContants.SUCCESS,"分支不能为空!!!","");
        }

        if(StringUtils.isEmpty(channel)){
            return responseData(CodeContants.SUCCESS,"渠道不能为空!!!","");
        }
        int pId= ProductContants.getCodeByName(productId);
        CsAppVersion cv=getCsAppVersion(branch,channel,pId,version,2);
        return responseData(CodeContants.SUCCESS,"",cv);
    }

    /**
     * ios信息访问接口
     * @param productId
     * @param branch
     * @param channel
     * @param version
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": {
     *          branch: "CaiMao",   //分支
     *          channel: "store",   //渠道
     *          clientType: 7,      //客户端类型
     *          docPosition: "/usr/local/clients/CaiMao/store/1.0.0/CaiMao-store-1.0.0.ipa",    //文件存储地址
     *          md5: "4ce2b77d61c4f935c4ecfb94e7cccf7d",    //md5加密
     *          plistUrl: "https://download.fcaimao.com/exter/ios/5/CaiMao/store/1.0.0.plist",  //plist访问地址
     *          productId: 5,   //产品ID
     *          publishTime: 1560734224327, //发布时间
     *          publisher: 0,   //发布人
     *          version1: 1,    //版本1
     *          version2: 0,    //版本2
     *          version3: 0     //版本3
     *     }
     * }
     */
    @RequestMapping(value = {"/iosClient/{productId}/{branch}/{channel}/{version}","/iosClient/{productId}/{branch}/{channel}","/iosClient/{productId}/{branch}//{version}"},method = RequestMethod.GET)
    @ResponseBody
    public String iosClient(@PathVariable("productId") String productId,@PathVariable("branch") String branch,@PathVariable(required = false) String channel,@PathVariable(required = false) String version){
        if(StringUtils.isEmpty(productId)){
            return responseData(CodeContants.EXCEPTION_ERROR,"项目ID不能为空!!!","");
        }
        if(StringUtils.isEmpty(branch)){
            return responseData(CodeContants.EXCEPTION_ERROR,"分支不能为空!!!","");
        }

        if(StringUtils.isEmpty(channel)&&StringUtils.isEmpty(version)){
            return responseData(CodeContants.EXCEPTION_ERROR,"渠道和版本不能都为空!!!","");
        }
        int pId= ProductContants.getCodeByName(productId);
        CsAppVersion cv=getCsAppVersion(branch,channel,pId,version,7);

        if(cv==null){
            return responseData(CodeContants.EXCEPTION_ERROR,"客户端不存在!!!","");
        }

        CsAppVersionVO vo=new CsAppVersionVO(cv);

        if(StringUtils.isNotEmpty(version)){
            vo.setPlistUrl(plistUrlFixed+"/"+cv.getProductId()+"/"+cv.getBranch()+"/"+cv.getChannel()+"/"+version+".plist");
        }else{
            vo.setPlistUrl(plistUrlFixed+"/"+cv.getProductId()+"/"+cv.getBranch()+"/"+cv.getChannel()+".plist");
        }
        return responseData(CodeContants.SUCCESS,"",vo);
    }

    /**
     * 获取plist信息
     * @param productId
     * @param branch
     * @param channel
     * @param version
     * @return
     * xml的信息
     */
    @RequestMapping(value = {"/ios/{productId}/{branch}/{channel}/{version}","/ios/{productId}/{branch}/{channel}"},method = RequestMethod.GET,produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String ios(@PathVariable("productId") String productId,@PathVariable("branch") String branch,@PathVariable("channel") String channel,@PathVariable(required = false) String version){
        if(StringUtils.isEmpty(productId)){
            return DocManager.getIntance().createPlistContent("项目ID不能为空!!!");
        }
        if(StringUtils.isEmpty(branch)){
            return DocManager.getIntance().createPlistContent("分支不能为空!!!");
        }

        if(StringUtils.isEmpty(channel)){
            return DocManager.getIntance().createPlistContent("渠道不能为空!!!");
        }

        int pId= ProductContants.getCodeByName(productId);

        CsAppVersion cv=getCsAppVersion(branch,channel,pId,version,7);

        if(cv==null){
            return DocManager.getIntance().createPlistContent("客户端不存在!!!");
        }

        CsAppPlistInfoEntity plistInfo=csAppPlistInfoService.getInfoByConditionLocal(branch,channel,pId);
        if(plistInfo==null){
            plistInfo=csAppPlistInfoService.getInfoByConditionLocal(branch,"",pId);
        }
        if(plistInfo==null){
            return DocManager.getIntance().createPlistContent("ios对应的plist信息不存在!!!");
        }

        if(StringUtils.isEmpty(plistInfo.getPlistModule())){
            return DocManager.getIntance().createPlistContent("ios对应的plist的模版信息不存在，请添加!!!");
        }

        JSONObject infoJSON=new JSONObject();
        if(StringUtils.isNotEmpty(plistInfo.getClientField())){
            infoJSON=JSONObject.parseObject(plistInfo.getClientField());
        }

        JSONObject versionJSON=new JSONObject();
        if(StringUtils.isNotEmpty(cv.getPlistField())){
            versionJSON=JSONObject.parseObject(cv.getPlistField());
        }

        if(versionJSON.size()>0){
            for (Map.Entry<String, Object> entry : versionJSON.entrySet()) {
                Object value;
                if(entry.getValue()==null){
                    value="";
                }else{
                    value=entry.getValue();
                }
                infoJSON.put(entry.getKey(), value);
            }
        }

        if(infoJSON.size()>0){
            for (Map.Entry<String, Object> entry : infoJSON.entrySet()) {
                if(entry.getValue()==null){
                    infoJSON.put(entry.getKey(), "");
                }
            }
        }

        if(StringUtils.isNotEmpty(cv.getDocUrl())){
            infoJSON.put("url",cv.getDocUrl());
        }else{
            infoJSON.put("url","");
        }

        try{
            String result=DocManager.getIntance().createXmlModule(infoJSON,plistInfo.getPlistModule());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return DocManager.getIntance().createPlistContent("错误信息:"+e.getMessage());
        }
    }

    /**
     * 对传递上来的version进行切割
     */
    private CsAppVersion getVersionModel(CsAppVersion model,String version){
        if(StringUtils.isNotEmpty(version)){
            if(version.contains(".")){
                String[] versions=version.split("\\.");
                int length=versions.length;
                switch (length){
                    case 1:
                        model.setVersion1(NumberUtils.toInt(versions[0]));
                        break;
                    case 2:
                        model.setVersion1(NumberUtils.toInt(versions[0]));
                        model.setVersion2(NumberUtils.toInt(versions[1]));
                        break;
                    case 3:
                        model.setVersion1(NumberUtils.toInt(versions[0]));
                        model.setVersion2(NumberUtils.toInt(versions[1]));
                        model.setVersion3(NumberUtils.toInt(versions[2]));
                        break;
                    default:
                        break;
                }
            }else{
                model.setVersion1(NumberUtils.toInt(version));
            }
        }
        return model;
    }

    /**
     * 根据信息获取客户端版本信息
     * @param branch
     * @param channel
     * @param productId
     * @param version
     * @return
     */
    private CsAppVersion getCsAppVersion(String branch,String channel,int productId,String version,int clientType){
        CsAppVersion cv=null;
        if(StringUtils.isEmpty(version)){
            if(StringUtils.isEmpty(channel)){
                CsAppVersion model=new CsAppVersion();
                model.setClientType(clientType);
                model.setProductId(productId);
                model.setBranch(branch);
                cv=csAppVersionService.getLaVersionByCondition(model);
            }else{
                cv=csAppVersionService.getLatestByCondition(branch,channel, productId,clientType);
            }
        }else{
            CsAppVersion model=new CsAppVersion();
            model.setClientType(clientType);
            model.setProductId(productId);
            model.setBranch(branch);
            model.setChannel(channel);
            model=getVersionModel(model,version);
            cv=csAppVersionService.getVersionByCondition(model);
        }
        return cv;
    }
}