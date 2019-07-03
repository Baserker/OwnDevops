package com.caimao.devops.controller;

import com.alibaba.fastjson.JSONObject;
import com.caimao.devops.contants.CodeContants;
import com.caimao.devops.controller.base.BaseController;
import com.caimao.devops.entity.CsStaticdocRecord;
import com.caimao.devops.service.CsStaticdocRecordService;
import com.caimao.devops.utils.DocManager;
import com.caimao.devops.utils.MD5Utils;
import com.caimao.devops.vo.NodeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * 静态内容控制层
 * 模块名称：CaimaoDevOps com.caimao.devops.controller
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-22 15:51
 * 系统版本：1.0.0
 **/

@RestController
@RequestMapping(value = "/static")
public class StaticContentController extends BaseController{

    private static Map<String,JSONObject> nodeCache=new HashMap<>();

    @Autowired
    private CsStaticdocRecordService csStaticdocRecordService;

    /**
     * TODO 暂定为4，有风险，后期修改
     */
    private final static int urlCount=4;

    /**
     * 静态文件的物理路径前缀
     */
    @Value("${doc.static.doc}")
    private String docPath;

    /**
     * 静态文件的访问地址前缀
     */
    @Value("${doc.static.commonUrl}")
    private String commonUrl;

    /**
     * 静态文件的临时存储地址
     */
    @Value("${doc.static.compressDoc}")
    private String compressDoc;

    /**
     * 获取文件的路径树
     * @param localPath 文件绝对路径
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data":  {
     *         "name":  //文件名称
     *         "localPath":    //绝对路径
     *         "parent":    //父目录
     *         "urlPath":   //访问链接
     *         "childrenList":[     //子目录
     *
     *         ]
     *     }
     * }
     */
    @RequestMapping(value = "/staticPaths",produces = "application/json;charset=UTF-8",method = RequestMethod.GET)
    public String staticPaths(String localPath){
        List<NodeVO> list=new ArrayList<>();
        try{
            if(StringUtils.isEmpty(localPath)){
                localPath=docPath;
                String urlPath=commonUrl+getUrlSuffix(localPath);
                list.add(DocManager.getIntance().scanDoc(localPath,urlPath));
            }else{
                String urlPath=commonUrl+getUrlSuffix(localPath);
                list.addAll(DocManager.getIntance().scanDoc(localPath,urlPath).getChildrenList());
            }
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
        return responseData(CodeContants.SUCCESS,"",list);
    }

    /**
     * 创建项目目录
     * @param parentPath    文件上级目录
     * @param path (由前端的上级目录和新建的文件夹名称组成)
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     */
    @RequestMapping(value = "/cataLog",method = RequestMethod.POST)
    public String cataLog(String parentPath,String path){
        if(StringUtils.isEmpty(path)){
            return responseData(CodeContants.PARAMS_ERROR,"文件名不能为空","");
        }
        if(StringUtils.isEmpty(parentPath)){
            return responseData(CodeContants.PARAMS_ERROR,"父路径不能为空","");
        }
        try{
            File file=new File(parentPath+"/"+path);
            if(!file.exists()){
                file.mkdirs();
            }
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
        return responseData(CodeContants.SUCCESS,"创建目录成功","");
    }

    /**
     * 获取需要编辑的文件的详情
     * @param filePath
     * @return
     */
    @RequestMapping(value = "/cataFile",method = RequestMethod.GET)
    public String cataFile(String filePath){
        if(StringUtils.isEmpty(filePath)){
            return responseData(CodeContants.PARAMS_ERROR,"文件路径不能为空!!!","");
        }
        try{
            boolean flag=DocManager.getIntance().judgeFileType(filePath);
            if(flag){
                return responseData(CodeContants.SUCCESS,"",DocManager.getIntance().getfileinfo(filePath));
            }else{
                return responseData(CodeContants.PARAMS_ERROR,"暂不支持此类文件格式!!!","");
            }
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
    }

    /**
     * 编辑文件详情
     * @param filePath
     * @param content
     * @return
     */
    @RequestMapping(value = "/cataFile",method = RequestMethod.PUT)
    public String cataFile(String filePath,String content){
        if(StringUtils.isEmpty(filePath)){
            return responseData(CodeContants.PARAMS_ERROR,"文件路径不能为空!!!","");
        }
        try{
            DocManager.getIntance().editFile(filePath,content);
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
        return responseData(CodeContants.SUCCESS,"操作成功！！！","");
    }

    /**
     * 上传公共静态文件或者文件信息(两者二选其一),若上传静态文件，文件名称和后缀可以不上传；上传文件内容，则必须上传这两项;
     * @param docPath  公共静态文件路径
     * @param file  公共静态文件流
     * @param name  公共静态文件名称
     * @param docCode  文件内容
     * @param suffix  文件后缀
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": ""
     * }
     */
    @RequestMapping(value = "/docOrCode",method = RequestMethod.POST)
    public String docOrCode(String docPath,String name,MultipartFile file,String docCode,String suffix) throws Exception {
        if(StringUtils.isEmpty(docCode)&&file==null){
            return responseData(CodeContants.PARAMS_ERROR,"上传数据不得为空","");
        }
        if(StringUtils.isEmpty(docPath)){
            return responseData(CodeContants.PARAMS_ERROR,"文件保存路径不能为空","");
        }

        try{
            if(file==null){
                //上传文件内容的模式
                if(StringUtils.isEmpty(name)){
                    return responseData(CodeContants.PARAMS_ERROR,"上传内容时文件名称不能为空","");
                }
                if(StringUtils.isEmpty(suffix)){
                    return responseData(CodeContants.PARAMS_ERROR,"上传内容时文件类型不能为空","");
                }
                String filePath=docPath+"/"+name+"."+suffix;
                DocManager.getIntance().createFile(filePath,suffix,docPath,name,docCode);
                //TODO 后期添加登录信息
                CsStaticdocRecord record=new CsStaticdocRecord(name,suffix,docPath,filePath,1,new Date(), MD5Utils.md5Encryte(new File(filePath)));
                csStaticdocRecordService.saveOrUpdate(record);
            }else{
                //上传文件的模式
                String[] nameArr=file.getOriginalFilename().split("\\.");
                suffix=nameArr[nameArr.length-1];
                name=System.currentTimeMillis()+suffix;
                String filePath=docPath+"/"+name+"."+suffix;
                String md5=MD5Utils.md5Encryte(file);
                file.transferTo(new File(filePath));
                //TODO 后期添加登录信息
                CsStaticdocRecord record=new CsStaticdocRecord(name,suffix,docPath,filePath,1,new Date(),md5);
                csStaticdocRecordService.saveOrUpdate(record);
            }
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,"操作失败","");
        }
        return responseData(CodeContants.SUCCESS,"上传文件成功","");
    }


    /**
     * 上传压缩文件（默认到临时目录）
     * @param docPath   文件存储静态目录
     * @param file
     * @return
     * {
     *     "errorMessage":  //信息
     *     "flag":  //标签，0：失败；1：成功。
     *     "retTime":   //时间戳
     *     "data": {
     *         "name":  //文件名称
     *         "localPath":    //绝对路径
     *         "parent":    //父目录
     *         "urlPath":   //访问链接
     *         "childrenList":[     //子目录
     *
     *         ]
     *     }
     * }
     */
    @RequestMapping(value = "/pressDoc",method = RequestMethod.POST)
    public String pressDoc(String docPath,MultipartFile file)throws Exception{
        JSONObject data=new JSONObject();
        String fileName=file.getOriginalFilename();
        String[] fileArr=fileName.split("\\.");
        String suffix=fileArr[fileArr.length-1];
        NodeVO comNode=null;
        try{
            String name= UUID.randomUUID().toString().replaceAll("-", "");
            if(suffix.equals("zip")){
                String path=compressDoc+"/"+name+"."+suffix;
                file.transferTo(new File(path));
                data.put("temporary",path);
                comNode=DocManager.getIntance().analyZip(path,docPath,commonUrl+"/"+getUrlSuffix(path));
            }else{
                return responseData(CodeContants.PARAMS_ERROR,"不符合","");
            }
        }catch (Exception e){
            e.printStackTrace();
            return responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
        List<NodeVO> list=new ArrayList<>();
        list.add(comNode);
        data.put("nodeList",list);
        JSONObject cacheJSON=new JSONObject();
        if(nodeCache.containsKey(docPath)){
            cacheJSON=nodeCache.get(docPath);
            cacheJSON.put(data.getString("temporary"),list);
            nodeCache.put(docPath,cacheJSON);
        }else{
            cacheJSON.put(data.getString("temporary"),list);
            nodeCache.put(docPath,cacheJSON);
        }
        return responseData(CodeContants.SUCCESS,"",data);
    }

    /**
     * 根据压缩文件的地址，获取缓存中的压缩文件信息(应用于前端关闭页面后，无需重新上传文件)
     * @param docPath
     * @return
     */
    @RequestMapping(value = "/pressDoc",method = RequestMethod.GET)
    public String pressDoc(String docPath){
        if(StringUtils.isEmpty(docPath)){
            return responseData(CodeContants.PARAMS_ERROR,"父目录不能为空!!!","");
        }
        JSONObject obj=nodeCache.get(docPath);
        if(obj==null){
            return responseData(CodeContants.PARAMS_ERROR,"还没上传压缩文件!!!","");
        }
        return responseData(CodeContants.SUCCESS,"",obj);
    }

    /**
     * 获取压缩文件的详细信息（从缓存中）
     * @param docPath  父文件目录
     * @param temPath  压缩文件目录
     * @return
     */
    @RequestMapping(value = "/pressDetail",method = RequestMethod.GET)
    public String pressDetail(String docPath,String temPath){
        if(StringUtils.isEmpty(docPath)){
            return responseData(CodeContants.PARAMS_ERROR,"父目录不能为空!!!","");
        }
        if(StringUtils.isEmpty(temPath)){
            return responseData(CodeContants.PARAMS_ERROR,"压缩文件目录不能为空!!!","");
        }

        JSONObject obj=nodeCache.get(docPath);
        if(obj!=null){
            List<NodeVO> list= (List<NodeVO>) obj.get(temPath);
            return responseData(CodeContants.SUCCESS,"",list);
        }else{
            return responseData(CodeContants.PARAMS_ERROR,"数据不存在，请重新上传","");
        }
    }

    /**
     * 确认临时文件上传，并进行解压
     * @param temPath   压缩文件临时存储地址
     * @param targetPath 压缩文件解压的目标目录
     * @return
     */
    @RequestMapping(value = "/extractedDoc",method = RequestMethod.POST)
    public String extractedDoc(String temPath,String targetPath){
        try{
            String dirPath=DocManager.getIntance().unZip(temPath,targetPath);
            if(StringUtils.isEmpty(dirPath)){
                responseData(CodeContants.EXCEPTION_ERROR,"文件解压失败","");
            }
            JSONObject cacheJSON=new JSONObject();
            if(nodeCache.containsKey(docPath)){
                cacheJSON=nodeCache.get(docPath);
                if(cacheJSON.containsKey(temPath)){
                    cacheJSON.remove(temPath);
                    nodeCache.put(docPath,cacheJSON);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            responseData(CodeContants.EXCEPTION_ERROR,e.getMessage(),"");
        }
        return responseData(CodeContants.SUCCESS,"文件解压成功","");
    }

    /**
     * 获取文件访问路径的中缀
     * @param path
     */
    private String getUrlSuffix(String path){
        StringBuffer urlSuffix=new StringBuffer();
        String[] pathArr=path.split("/");
        if(pathArr.length>urlCount){
            for(int i=urlCount;i<pathArr.length;i++){
                urlSuffix.append("/").append(pathArr[i]);
            }
            return urlSuffix.toString();
        }
        return null;
    }


}