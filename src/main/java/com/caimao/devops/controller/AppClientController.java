package com.caimao.devops.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caimao.devops.contants.CodeContants;
import com.caimao.devops.controller.base.BaseController;
import com.caimao.devops.entity.CsAppHisVersion;
import com.caimao.devops.entity.CsAppPlistInfoEntity;
import com.caimao.devops.entity.CsAppVersion;
import com.caimao.devops.service.CsAppHisVersionService;
import com.caimao.devops.service.CsAppPlistInfoService;
import com.caimao.devops.service.CsAppVersionService;
import com.caimao.devops.utils.DocManager;
import com.caimao.devops.utils.MD5Utils;
import com.caimao.devops.vo.CsAppHisVersionVO;
import com.caimao.devops.vo.CsAppPlistInfoVO;
import com.caimao.devops.vo.CsAppVersionVO;
import com.caimao.devops.vo.DocFileInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * 客户端版本控制层
 * 模块名称：CaimaoDevOps com.caimao.devops.controller
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-16 16:09
 * 系统版本：1.0.0
 **/

@Controller
@RequestMapping(value = "/appClient")
public class AppClientController extends BaseController{

    /**
     * 文件真实物理位置
     */
    @Value("${doc.phyPosition}")
    private String phyPosition;

    /**
     * 文件地址前缀
     */
    @Value("${doc.docUrl}")
    private String docUrl;

    /**
     * 文件地址前缀
     */
    @Value("${doc.iosDocUrl}")
    private String iosDocUrl;

    @Autowired
    private CsAppPlistInfoService csAppPlistInfoService;

    @Autowired
    private CsAppVersionService csAppVersionService;

    @Autowired
    private CsAppHisVersionService csAppHisVersionService;

    /**
     * 获取客户端列表
     * @param pageNumber   页码
     * @param pageSize  页数
     * @param id
     * @param clientType 客户端类型
     * @param productId 项目id
     * @param branch 分支
     * @param channel 渠道
     * @param version 版本
     * @param publisher 发布人
     * @param status 状态
     * @param guideStatus 引导状态
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": {
     *         "content":  [    //数据内容
     *                          {
     *                              "id":    //id
     *                              "clientType":    //客户端类型:2：android；7：ios
     *                              "productId":     //产品ID
     *                              "branch":    //分支
     *                              "channel":   //渠道
     *                              "version1":  //版本号1
     *                              "version2":  //版本号2
     *                              "version3":  //版本号3
     *                              "docPosition":   //文件存储地址
     *                              "docUrl":    //文件访问地址
     *                              "md5":   //文件加密的MD5
     *                              "sha1":
     *                              "publisher":     //上传人
     *                              "status":    //状态   0:测试、1:预发布、2:正式、3:下架
     *                              "apkStatus":     //客户端的状态
     *                              "guideStatus":   //引导状态     0:允许使用、1:提示更新、2:强制更新
     *                              "guideTarget":   //引导目标
     *                              "appMemo":   //客户端简介
     *                              "publishTime":   //上传时间
     *                          }
     *          ],
     *          "empty": false,
     *          "first": true,
     *          "last": true,
     *          "number": 0,        //页码
     *          "numberOfElements": 2,
     *          "pageable": {
     *                          "offset": 0,
     *                          "pageNumber": 0,
     *                          "pageSize": 10,
     *                          "paged": true,
     *                          "sort": {
     *                                      "empty": false,
     *                                      "sorted": true,
     *                                      "unsorted": false
     *                                  },
     *                          "unpaged": false
     *                      },
     *          "size": 10,         //一页条数
     *          "sort": {
     *                      "$ref": "$.data.pageable.sort"
     *          },
     *          "totalElements": 2,
     *          "totalPages": 1
     *     }
     * }
     * @throws Exception
     */
    @RequestMapping(value = "/appClients",method = RequestMethod.GET)
    @ResponseBody
    public String appClients(Integer pageNumber,Integer pageSize,Integer id,Integer clientType,Integer productId,String branch,String channel,String version,Integer publisher,Integer status,Integer guideStatus)throws Exception{
        CsAppVersionVO model=new CsAppVersionVO(id,clientType,productId,branch,channel,version,publisher,status,guideStatus);
        if(pageNumber==null||pageNumber<=0){
            pageNumber=1;
        }
        if(pageSize==null||pageSize<1){
            pageSize=10;
        }
        model=getVersionsModel(model);
        Page<CsAppVersion> page=csAppVersionService.getAppHisClientByCondition(pageNumber-1,pageSize,model);
        return responseData(CodeContants.SUCCESS,"",page);
    }


    /**
     * 上传客户端版本文件
     * @param clientType  客户端类型：2：android；7：ios
     * @param productId 项目ID
     * @param branch    分支
     * @param channel   渠道
     * @param version   版本：类似1.0.0
     * @param doc   文件流
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     */
    @RequestMapping(value = "/appClient",method = RequestMethod.POST)
    @ResponseBody
    public String appClient(Integer clientType,Integer productId,String branch,String channel,String version,MultipartFile doc){
        //TODO 后期添加了权限之后，在添加publisher
        CsAppVersion model=new CsAppVersion(clientType,productId,branch,channel,0);
        model=getVersionModel(model,version);
        model=csAppVersionService.getVersionByCondition(model);

        try{
            if(doc!=null){
                String fileName=doc.getOriginalFilename();
                String[] nameArr=fileName.split("\\.");
                String newFileName=null;
                String secondPath=null;
                if(StringUtils.isNotEmpty(model.getChannel())){
                    newFileName=model.getBranch()+"-"+model.getChannel()+"-"+version+"."+nameArr[nameArr.length-1];
                    secondPath=model.getBranch()+"/"+model.getChannel()+"/"+version;
                }else{
                    newFileName=model.getBranch()+"-"+version+"."+nameArr[nameArr.length-1];
                    secondPath=model.getBranch()+"/"+version;
                }
                DocFileInfo info= DocManager.createImageFileInfo(phyPosition,secondPath,newFileName,nameArr[nameArr.length-1]);

                String md5= MD5Utils.md5Encryte(doc);

                //判断是否需要添加历史客户端信息
                if(model.getId()!=null&&model.getId()>0){
                    boolean flag=addHisVersion(model,md5);
                    if(!flag){
                        return responseData(CodeContants.EXCEPTION_ERROR,"客户端重复!!!","");
                    }
                }
                model.setMd5(md5);
                model.setDocPosition(info.getVirDir()+info.getFileName());
                if(model.getClientType()==2){
                    model.setDocUrl(getDocUrl(model,0));
                }else if(model.getClientType()==7){
                    model.setDocUrl(getDocUrl(model,1));
                }

                //判断是否是ios文件，如果是，则需要生成一份plist的xml文档（同一目录）
                if(nameArr[nameArr.length-1].equals("ipa")){
                    if(nameArr[nameArr.length-1].equals("ipa")){
                        CsAppPlistInfoEntity plistInfo=csAppPlistInfoService.getInfoByConditionLocal(model.getBranch(),model.getChannel(),model.getProductId());
                        if(plistInfo==null){
                            return responseData(CodeContants.EXCEPTION_ERROR,"plist信息不存在，请添加对应分支的plist信息","0");
                        }
                    }
                }

                doc.transferTo(new File(model.getDocPosition()));
            }

            csAppVersionService.saveOrUpdate(model);
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
        return responseData(CodeContants.SUCCESS,"上传客户端版本文件成功","");
    }

    /**
     * 修改时获取版本信息(本地页面专属)
     * @param id    //获取本地信息
     * @return
     */
    @RequestMapping(value = "/appClientOne",method = RequestMethod.GET)
    @ResponseBody
    public String appClientOne(Integer id){
        if(id==null||id==0){
            return responseData(CodeContants.PARAMS_ERROR,"id不能为空或者为0","");
        }
        try{
            CsAppVersion model=csAppVersionService.getModelById(id);
            if(model==null){
                return responseData(CodeContants.PARAMS_ERROR,"id不存在，请确认","");
            }
            return responseData(CodeContants.SUCCESS,"",model);
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
    }

    /**
     * 获取客户端中的plist补充信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/appPlistField",method = RequestMethod.GET)
    @ResponseBody
    public String appPlistField(Integer id){
        if(id==null||id==0){
            return responseData(CodeContants.PARAMS_ERROR,"id不能为空或者为0","");
        }
        try{
            CsAppVersion cv=csAppVersionService.getModelById(id);
            JSONObject infoJson=JSONObject.parseObject(cv.getPlistField());
            JSONArray result=new JSONArray();
            if(infoJson!=null&&infoJson.size()>0){
                for (Map.Entry<String, Object> entry : infoJson.entrySet()) {
                    JSONObject object=new JSONObject();
                    object.put("key",entry.getKey());
                    object.put("value",entry.getValue());
                    result.add(object);
                }
                return responseData(CodeContants.SUCCESS,"",result);
            }else{
                return responseData(CodeContants.SUCCESS,"","");
            }
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
    }

    /**
     * 修改客户端文件的记录信息
     * @param id    客户端版本id
     * @param status    客户端版本状态:0:测试、1:预发布、2:正式、3:下架
     * @param guideStatus   引导状态:0:允许使用、1:提示更新、2:强制更新
     * @param guideTarget   引导目标
     * @param apkStatus 客户端的状态
     * @param memo  客户端介绍
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     */
    @RequestMapping(value = "/appClient",method = RequestMethod.PUT)
    @ResponseBody
    public String appClient(Integer id,Integer status,Integer guideStatus,String guideTarget,String memo,Integer apkStatus){
        if(id==null||id==0){
            return responseData(CodeContants.PARAMS_ERROR,"id不能为空或者为0","");
        }
        try{
            CsAppVersion model=csAppVersionService.getModelById(id);
            if(model==null){
                return responseData(CodeContants.PARAMS_ERROR,"id不存在，请确认","");
            }
            if(status!=null&&status>-1){
                model.setStatus(status);
            }
            if(guideStatus!=null&&guideStatus>-1){
                model.setGuideStatus(guideStatus);
            }
            if(apkStatus!=null&&apkStatus>-1){
                model.setApkStatus(apkStatus);
            }

            //当引导状态为提示更新或者强制更新的时候，如果没有填写引导目标，则默认更新到当前分支、渠道下的最新版本
            if(guideStatus!=null){
                if(guideStatus==1||guideStatus==2){
                    if(StringUtils.isEmpty(guideTarget)){
                        model.setGuideTarget(fillTargetStr(csAppVersionService.getLatestByCondition(model.getBranch(),model.getChannel(),model.getProductId(),model.getClientType())));
                    }else{
                        model.setGuideTarget(guideTarget);
                    }
                }else{
                    model.setGuideTarget(guideTarget);
                }
            }

            model.setAppMemo(memo);
            csAppVersionService.saveOrUpdate(model);
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
        return responseData(CodeContants.SUCCESS,"客户端版本修改成功","");
    }

    /**
     * 修改plist的补充信息(只针对ios的)
     * @param map
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     */
    @RequestMapping(value = "/appPlistField",method = RequestMethod.POST)
    @ResponseBody
    public String appPlistField(@RequestBody Map<String, Object> map){
        if(!map.containsKey("id")){
            return responseData(CodeContants.PARAMS_ERROR,"id不能为空或者为0","");
        }
        String idStr= (String) map.get("id");
        int id=NumberUtils.toInt(idStr);
        if(id==0){
            return responseData(CodeContants.PARAMS_ERROR,"id不能为空或者为0","");
        }
        try{
            CsAppVersion cv=csAppVersionService.getModelById(id);
            map.remove("id");
            JSONObject json=new JSONObject(map);
            cv.setPlistField(json.toJSONString());
            csAppVersionService.saveOrUpdate(cv);
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,"客户端版本修改失败","");
        }
        return responseData(CodeContants.SUCCESS,"客户端版本修改成功","");
    }

    /**
     * 客户端历史版本列表
     * @param id    客户端版本id
     * @param pageNum   页码
     * @param pageSize  页数
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": {
     *          "content": [      //内容
     *              {
     *                  branch:     //分支
     *                  channel:    //渠道
     *                  clientType: //客户端类型 2：android；7：ios
     *                  id:     //id
     *                  md5:    //MD5加密
     *                  productId:  //产品id
     *                  publishTime:    //发布时间
     *                  publisher:  //发布人
     *                  version1:   //版本号1
     *                  version2:   //版本号2
     *                  version3:   //版本号3
     *              }
     *          ],
     *          empty: true,
     *          first: false,
     *          last: true,
     *          number: 0,      //页码
     *          numberOfElements: 0,
     *          pageable: {
     *                      offset: 10,
     *                      pageNumber: 1,
     *                      pageSize: 10,
     *                      paged: true,
     *                      sort: {
     *                              empty: false,
     *                              sorted: true,
     *                              unsorted: false
     *                      },
     *                      unpaged: false
     *          },
     *          size: 10,       //一页条数
     *          sort: {
     *                  $ref: "$.data.pageable.sort"
     *          },
     *          totalElements: 0,
     *          totalPages: 0
     *     }
     * }
     */
    @RequestMapping(value = "/hisAppClients",method = RequestMethod.GET)
    @ResponseBody
    public String hisAppClients(Integer id,Integer pageNum,Integer pageSize){
        if(id!=null&&id!=0){
            CsAppVersion version=csAppVersionService.getModelById(id);
            if(version!=null){
                CsAppHisVersionVO model=new CsAppHisVersionVO(version);
                if(pageNum==null||pageNum<=0){
                    pageNum=1;
                }
                if(pageSize==null||pageSize<1){
                    pageSize=10;
                }
                Page<CsAppHisVersion> page=csAppHisVersionService.getAppHisClientByCondition(pageNum-1,pageSize,model);
                return responseData(CodeContants.SUCCESS,"",page);
            }else{
                return responseData(CodeContants.PARAMS_ERROR,"id不符合要求","");
            }
        }else{
            return responseData(CodeContants.PARAMS_ERROR,"id不符合要求","");
        }
    }

    /**
     * 修改客户端历史版本
     * @param id     客户端历史版本id
     * @param describe   客户端历史版本简介
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     */
    @RequestMapping(value = "/hisAppClient",method = RequestMethod.PUT)
    @ResponseBody
    public String hisAppClient(Integer id,String describe){
        try{
            if(id==null||id==0){
                return responseData(CodeContants.PARAMS_ERROR,"id不能为空或为0","");
            }
            CsAppHisVersion local=csAppHisVersionService.getModelById(id);
            if(!describe.equals(local.getAppDescribe())){
                local.setAppDescribe(describe);
                csAppHisVersionService.saveOrUpdate(local);
            }
            return responseData(CodeContants.SUCCESS,"操作成功","");
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,"操作失败","");
        }
    }

    /**
     * 获取ios的plist信息列表
     * @param pageNum   页码
     * @param pageSize  页面数量
     * @param info  plist的对象
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": {
     *         "content":  [    //数据内容
     *                          {
     *                              "id":    //id
     *                              "fullImage":    //图片1
     *                              "displayImage":     //图片2
     *                              "bundleIdentifier":    //项目包路径
     *                              "bundleVersion":   //版本号
     *                              "title":  //标题
     *                              "branch":  //分支
     *                              "kind":  //类型
     *                              "productId":   //项目ID
     *                          }
     *          ],
     *          "empty": false,
     *          "first": true,
     *          "last": true,
     *          "number": 0,        //页码
     *          "numberOfElements": 2,
     *          "pageable": {
     *                          "offset": 0,
     *                          "pageNumber": 0,
     *                          "pageSize": 10,
     *                          "paged": true,
     *                          "sort": {
     *                                      "empty": false,
     *                                      "sorted": true,
     *                                      "unsorted": false
     *                                  },
     *                          "unpaged": false
     *                      },
     *          "size": 10,         //一页条数
     *          "sort": {
     *                      "$ref": "$.data.pageable.sort"
     *          },
     *          "totalElements": 2,
     *          "totalPages": 1
     *     }
     * }
     */
    @RequestMapping(value = "/plistInfos",method = RequestMethod.GET)
    @ResponseBody
    public String plistInfos(Integer pageNum, Integer pageSize, CsAppPlistInfoEntity info){
        if(pageNum==null||pageNum==0){
            pageNum=1;
        }
        if(pageSize==null||pageSize==0){
            pageSize=10;
        }
        Page page=csAppPlistInfoService.getInfoListByCondition(pageNum-1,pageSize,info);
        return responseData(CodeContants.SUCCESS,"",page);
    }

    /**
     * 创建plist的信息
     * @param productId 项目id
     * @param branch    分支信息
     * @param channel 渠道
     * @param clientField 自定义属性
     * @param plistModule 模版信息
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     */
    @RequestMapping(value = "/plistInfo",method = RequestMethod.POST)
    @ResponseBody
    public String plistInfo(Integer productId,String branch,String channel,String clientField,String plistModule){
        if(productId==null){
            return responseData(CodeContants.PARAMS_ERROR,"项目ID不能为空","");
        }
        if(StringUtils.isEmpty(branch)){
            return responseData(CodeContants.PARAMS_ERROR,"分支不能为空","");
        }
        try{
            CsAppPlistInfoEntity plistInfo=new CsAppPlistInfoEntity(branch,productId,channel,clientField,plistModule);
            csAppPlistInfoService.saveOrUpdate(plistInfo);
            return responseData(CodeContants.SUCCESS,"操作成功","");
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
    }

    /**
     * 获取需要编辑的plist详情
     * @param id
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     * @throws Exception
     */
    @RequestMapping(value = "/plistInfo",method=RequestMethod.GET)
    @ResponseBody
    public String plistInfo(Integer id)throws Exception{
        if(id==null){
            return responseData(CodeContants.PARAMS_ERROR,"ID不能为空","");
        }
        CsAppPlistInfoEntity info=csAppPlistInfoService.getInfoById(id);
        if(info==null){
            return responseData(CodeContants.PARAMS_ERROR,"ID不能为空","");
        }
        CsAppPlistInfoVO vo=new CsAppPlistInfoVO(info);
        if(StringUtils.isNotEmpty(vo.getClientField())){
            JSONObject json=JSONObject.parseObject(vo.getClientField());
            JSONArray array=new JSONArray();
            if(json.size()>0){
                for (Map.Entry<String, Object> entry : json.entrySet()) {
                    JSONObject obj=new JSONObject();
                    obj.put("key",entry.getKey());
                    obj.put("value",entry.getValue());
                    array.add(obj);
                }
                vo.setField(array);
            }
        }
        return responseData(CodeContants.SUCCESS,"",vo);
    }

    /**
     * 生成模版信息
     * @return
     */
    @RequestMapping(value = "/plistModuleInfo",method = RequestMethod.GET)
    @ResponseBody
    public String plistModuleInfo(){
        return responseData(CodeContants.SUCCESS,"",DocManager.getIntance().createPlistContent(""));
    }

    /**
     * 修改ios的plist信息
     * @param id
     * @param clientField 自定义参数
     * @param plistModule xml模版信息
     * @return
     */
    @RequestMapping(value = "/plistInfo",method = RequestMethod.PUT)
    @ResponseBody
    public String plistInfo(Integer id,String clientField,String plistModule){
        if(id==null){
            return responseData(CodeContants.PARAMS_ERROR,"ID不能为空","");
        }
        CsAppPlistInfoEntity info=csAppPlistInfoService.getInfoById(id);
        if(info==null){
            return responseData(CodeContants.PARAMS_ERROR,"ID不能为空","");
        }
        if(plistModule!=null){
            info.setPlistModule(plistModule);
        }

        info.setClientField(clientField);
        csAppPlistInfoService.saveOrUpdate(info);
        return responseData(CodeContants.SUCCESS,"操作成功","");
    }

    /**
     * 获取plist的xml
     * @param id
     * @return
     */
    @RequestMapping(value = "/plistOne",method = RequestMethod.GET)
    @ResponseBody
    public String plistOne(Integer id){

        if(id==null){
            return responseData(CodeContants.PARAMS_ERROR,"id不能为空!!!","");
        }
        CsAppVersion cv=csAppVersionService.getModelById(id);
        if(cv==null){
            return responseData(CodeContants.PARAMS_ERROR,"客户端不存在!!!","");
        }

        return responseData(CodeContants.SUCCESS,"",cv);
    }

    /**
     * 对传递上来的version进行切割
     */
    private CsAppVersionVO getVersionsModel(CsAppVersionVO model){
        if(StringUtils.isNotEmpty(model.getVersion())){
            String version=model.getVersion();
            if(version.contains(".")){
                String[] versions=version.split("\\.");
                int length=versions.length;
                switch (length){
                    case 0:
                        model.setVersion1(NumberUtils.toInt(version));
                        break;
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
                model.setVersion1(NumberUtils.toInt(model.getVersion()));
            }
        }
        return model;
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
     * 对版本进行修改的时候，判断文件是否有了修改（MD5判断），如果出现了修改，则在历史版本中添加一条
     */
    private boolean addHisVersion(CsAppVersion version,String md5){
        boolean flag=false;
        if(StringUtils.isNotEmpty(md5)){
            if(!version.getMd5().equals(md5)){
                flag=true;
                CsAppHisVersion hisVersion=getHisVersion(version);
                csAppHisVersionService.saveOrUpdate(hisVersion);
            }
        }
        return flag;
    }

    /**
     * 获取文件访问路径
     * TODO 有待商榷。。。暂时写成这样
     * @param cv
     * @return
     */
    private String getDocUrl(CsAppVersion cv,int flag){
        StringBuffer url=new StringBuffer();
        if(flag==0){
            url.append(docUrl);
        }else{
            url.append(iosDocUrl);
        }
        String[] positionArr=cv.getDocPosition().split("/");
        if(positionArr.length>4){
            for(int i=4;i<positionArr.length;i++){
                url.append("/").append(positionArr[i]);
            }
        }
        return url.toString();
    }

    /**
     * 映射客户端历史版本
     * @param version
     */
    private CsAppHisVersion getHisVersion(CsAppVersion version){
        CsAppHisVersion hisVersion=new CsAppHisVersion();
        hisVersion.setClientType(version.getClientType());
        hisVersion.setBranch(version.getBranch());
        hisVersion.setChannel(version.getChannel());
        hisVersion.setProductId(version.getProductId());
        hisVersion.setVersion1(version.getVersion1());
        hisVersion.setVersion2(version.getVersion2());
        hisVersion.setVersion3(version.getVersion3());
        hisVersion.setPublisher(version.getPublisher());
        hisVersion.setPublishTime(new Date());
        hisVersion.setMd5(version.getMd5());
        hisVersion.setSha1(version.getSha1());
        return hisVersion;
    }

    /**
     * 根据客户端版本对象，组装出引导目标的String
     */
    private String fillTargetStr(CsAppVersion version){
        StringBuffer buffer=new StringBuffer();
        if(version!=null){
            buffer.append(version.getBranch());
            buffer.append(":");
            buffer.append(version.getChannel());
            buffer.append(":");
            StringBuffer versionBuf=new StringBuffer();
            if(version.getVersion1()!=null){
                versionBuf.append(version.getVersion1());
            }
            if(version.getVersion2()!=null){
                versionBuf.append(".").append(version.getVersion2());
            }
            if(version.getVersion3()!=null){
                versionBuf.append(".").append(version.getVersion3());
            }
            buffer.append(versionBuf.toString());
        }
        return buffer.toString();
    }


}