package com.caimao.devops.vo;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传拓展类
 * 模块名称：CaimaoDevOps com.caimao.devops.vo
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-17 10:19
 * 系统版本：1.0.0
 **/
public class DocFileInfo {
    /**
     * 虚拟文件夹路径      /news/thumb/2015/08/17/
     */
    private String virDir;

    /**
     * 文件名  xxxx.apk
     */
    private String fileName;

    /**
     * 文件后缀    apk、ipa
     */
    private String fileTypeName;

    /**
     * 获取物理文件夹路径
     * @return
     */
    public String getDirPath(){
        return this.virDir;
    }

    /**
     * 初始化  物理文件夹路径 没有文件夹时需要新建
     * @return
     */
    public void initDirPath() {
        File file = new File(getDirPath());
        try{
            if(!file.exists()){
                file.mkdirs();
            } else if(!file.isDirectory()) {
                throw new IOException("this dirPath is not directory : " + file.getPath());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取真实的 文件绝得地址
     * @return
     */
    public String getFilePath(String phyPath){
        StringBuilder sb = new StringBuilder(100);
        sb.append(phyPath).append(this.virDir);
        sb.append(this.fileName);
        return sb.toString();
    }

    /**
     * 获取 网络访问地址
     * @return
     */
    public String getUrl(String url){
        StringBuilder sb = new StringBuilder(100);
        sb.append(url).append(this.virDir);
        sb.append(this.fileName);
        return sb.toString();
    }

    public String getVirDir() {
        return virDir;
    }

    public void setVirDir(String virDir) {
        this.virDir = virDir;
        initDirPath();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }
}