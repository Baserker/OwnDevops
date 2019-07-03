package com.caimao.devops.vo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 模块名称：CaimaoDevOps com.caimao.devops.vo
 * 功能说明：测试节点<br>
 * 开发人员：Wangyq
 * 创建时间： 2019-04-24 9:55
 * 系统版本：1.0.0
 **/

public class NodeVO {

    /**
     * 文件代号
     */
    private String name;

    /**
     * 文件物理地址
     */
    private String localPath;

    /**
     * 文件访问地址
     */
    private String urlPath;

    /**
     * 文件上级目录
     */
    private String parent;

    /**
     * 压缩文件临时目录
     */
    private String temporaryPath;

    /**
     * 文件夹与文件的标志：0：文件夹，1：文件
     */
    private Integer sign;

    /**
     * 文件下级目录
     */
    private List<NodeVO> childrenList=new ArrayList<>();

    public NodeVO(String name, String localPath, String urlPath, String parent) {
        this.name = name;
        this.localPath = localPath;
        this.urlPath = urlPath;
        this.parent = parent;
        this.sign = judgeFile(localPath);
    }

    public NodeVO(String localPath,String urlPath){
        NodeVO root=getRoot(localPath,urlPath);
        this.name=root.getName();
        this.localPath=root.getLocalPath();
        this.urlPath=root.getUrlPath();
        this.parent=root.getParent();
    }

    /**
     * 判断文件夹与否
     * @param path
     * @return
     */
    private Integer judgeFile(String path){
        Integer sign=null;
        File file = new File(path);
        if(file.isDirectory()){
            sign=0;
        }
        if(file.isFile()){
            sign=1;
        }
        return sign;
    }

    /**
     * 轮训添加目录
     * @param node
     */
    public void add(NodeVO node){
        if(node.getParent().equals(this.localPath)){
            this.childrenList.add(node);
        }else{
            if(childrenList.size()>0){
                for(NodeVO vo:childrenList){
                    vo.add(node);
                }
            }else{
                return;
            }
        }
    }

    /**
     * 根据物理地址，获取文件代号、上级目录
     * @return
     */
    public NodeVO getRoot(String localPath,String urlPath){
        if(localPath.contains("/")){
            String[] localArr=localPath.split("/");
            String name=localArr[localArr.length-1];
            StringBuffer parent=new StringBuffer();
            for(int i=0;i<localArr.length-1;i++){
                if(i==0){
                    parent.append(localArr[i]);
                }else{
                    parent.append("/").append(localArr[i]);
                }
            }
            return new NodeVO(name,localPath,urlPath,parent.toString());
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getTemporaryPath() {
        return temporaryPath;
    }

    public void setTemporaryPath(String temporaryPath) {
        this.temporaryPath = temporaryPath;
    }

    public List<NodeVO> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<NodeVO> childrenList) {
        this.childrenList = childrenList;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }
}