package com.caimao.devops.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.caimao.devops.contants.FileContants;
import com.caimao.devops.vo.DocFileInfo;
import com.caimao.devops.vo.NodeVO;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 文件上传管理类
 * 模块名称：CaimaoDevOps com.caimao.devops.utils
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-17 11:09
 * 系统版本：1.0.0
 **/

public class DocManager {

    public static int plies=2;

    /**
     * 文件存储的物理地址前缀
     */
    public static String DOCSERVER_PHYSICAL = null;

    /**
     * 文件存储的网络地址前缀
     */
    public static String DOCSERVER_URL=null;

    /**
     * 文件路径
     */
    public static StringBuilder buf=new StringBuilder();

    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>(20);

    public static DocManager getIntance(){
        return new DocManager();
    }

    static {
        CONTENT_TYPE_MAP.put("apk","apk");
        CONTENT_TYPE_MAP.put("ipa","ipa");
    }

    /**
     *
     * @param secondPath
     * @param docName
     * @param suffix
     */
    public static DocFileInfo createImageFileInfo(String physicalPath,String secondPath,String docName, String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            throw new IllegalArgumentException("文件类型为空");
        }
        String fileTypeName = CONTENT_TYPE_MAP.get(suffix);
        if (StringUtils.isEmpty(fileTypeName)) {
            throw new IllegalArgumentException("不支持的文件格式：" + suffix);
        }

        StringBuilder virDir = new StringBuilder(100);
        if (StringUtils.isNotEmpty(secondPath)) {
            virDir.append(physicalPath).append("/").append(secondPath);
        }
        if (!virDir.toString().endsWith("/")) {
            virDir.append("/");
        }

        DocFileInfo docFileInfo = new DocFileInfo();
        docFileInfo.setVirDir(virDir.toString());
        docFileInfo.setFileName(docName);
        docFileInfo.setFileTypeName(fileTypeName);
        return docFileInfo;
    }

    /**
     * 获取文件存储路径(方法不再使用)
     * @param path
     */
    @Deprecated
    public String scan(String path) {
        if(StringUtils.isEmpty(path)){
            return null;
        }
        File f = new File(path);
        if (f.isDirectory())
        {
            scan(new File(path));
            buf.delete(buf.length() - 2, buf.length());
        }
        if(isJSONValid(buf.toString())){
            return buf.toString();
        }else{
            return buf.append("}").toString();
        }
    }

    @Deprecated
    private void scan(File f) {
        if (f.isDirectory())
        {
            buf.append("{").append("\"name\" : \"").append(f.getName()).append("\",").append("\"filePath\":").append(JSON.toJSONString(f.getAbsolutePath())).append(",\"isDir\":\"0\",").append("\"children\":[");
            Arrays.asList(f.listFiles()).forEach(this::scan);
            if(buf.toString().endsWith("\"children\":[")) {
                buf.append("{\"name\":\"暂无文件\",\"path\":\"\",\"isDir\":\"0\"},");
            }
            buf.delete(buf.length() - 2, buf.length());
            buf.append("}").append("]").append("},");
        }
        else {
            buf.append("{").append("\"name\" : \"").append(f.getName()).append("\"").append(",\"filePath\":").append(JSON.toJSONString(f.getAbsolutePath()))
                    .append(",\"isDir\":\"1\"").append("},");
        }
    }

    /**
     * 判断是否是json类型
     * @param str
     */
    @Deprecated
    public final static boolean isJSONValid(String str) {
        try {
            JSONObject.parseObject(str);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(str);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 遍历在线文件夹
     * @param docPath
     * @param urlPath
     * @throws IOException
     */
    public NodeVO scanDoc(String docPath,String urlPath) throws IOException {
        Path path = Paths.get(docPath);
        Stream<Path> walk = Files.walk(path,plies);
        NodeVO root=new NodeVO(docPath,urlPath);
        walk.forEach(path1 -> {
            String fileName=path1.getFileName().toString().replaceAll("\\\\","/");
            if(fileName.equals(root.getName())){
                return;
            }
            String parentName=path1.getParent().toString().replaceAll("\\\\","/");
            parentName=parentName.replaceAll("//","/");
            NodeVO node=new NodeVO(fileName,parentName+"/"+fileName,"",parentName);
            root.add(node);
        });
        return root;
    }

    /**
     * 解析zip压缩文件的文件目录
     * @param path  文件临时存储路径
     * @param targetPath    文件目标存储路径
     * @param sourceUrlPath  文件源访问路径
     * @throws Exception
     */
    public NodeVO analyZip(String path,String targetPath,String sourceUrlPath)throws Exception{
        ZipFile zf=new ZipFile(path);
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        Charset gbk = Charset.forName("gbk");
        ZipInputStream zin = new ZipInputStream(in,gbk);
        ZipEntry ze;
        int count=0;
        NodeVO root=null;
        while((ze = zin.getNextEntry()) != null){
            String pathSuffix=ze.getName();
            if(ze.isDirectory()){
                pathSuffix=pathSuffix.substring(0,pathSuffix.length()-1);
            }
            String allPath=targetPath+"/"+pathSuffix;
            String urlPath=sourceUrlPath+"/"+pathSuffix;
            if(count==0){
                root=new NodeVO(allPath,urlPath);
            }else{
                NodeVO node=new NodeVO(allPath,urlPath);
                root.add(node);
            }
            count=count+1;
        }
        zin.closeEntry();
        return root;
    }

    /**
     *
     * @param filePath  压缩文件现在地址
     * @param targetPath    文件解压目标地址
     * @return
     */
    public String unZip(String filePath,String targetPath)throws Exception{
        String dirPath=null;
        File srcFile=new File(filePath);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        zipFile = new ZipFile(srcFile);
        Enumeration<?> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 如果是文件夹，就创建个文件夹
            if (entry.isDirectory()) {
                dirPath = targetPath + "/" + entry.getName();
                File dir = new File(dirPath);
                dir.mkdirs();
            } else {
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(targetPath + "/" + entry.getName());
                // 保证这个文件的父文件夹必须要存在
                if(!targetFile.getParentFile().exists()){
                    targetFile.getParentFile().mkdirs();
                }
                targetFile.createNewFile();
                // 将压缩文件内容写入到这个文件中
                InputStream is = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(targetFile);
                int len;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭
                fos.close();
                is.close();
            }
        }
        if(zipFile != null){
            try {
                zipFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dirPath;
    }

    /**
     * 创建文件到指定文件夹
     * @param staticUrl 静态文件访问路径(需要包含文件所在的上级目录)
     * @param suffix 文件后缀
     * @param path 文件存储的物理路径
     * @param name 文件名称
     * @param content 文件内容
     * @throws Exception
     */
    public String createFile(String staticUrl,String suffix,String path,String name,String content)throws Exception{
        File checkFile = new File(staticUrl);
        FileWriter writer = null;
        // 一、检查目标文件夹是否存在，不存在则创建
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        // 二、检查目标文件是否存在，不存在则创建
        if (!checkFile.exists()) {
            checkFile.createNewFile();// 创建目标文件
        }
        // 三、向目标文件中写入内容
        // FileWriter(File file, boolean append)，append为true时为追加模式，false或缺省则为覆盖模式
        writer = new FileWriter(checkFile, false);
        writer.append(content);
        writer.flush();

        writer.close();
        return staticUrl;
    }

    /**
     * 修改已有文件的内容
     * @param filePath
     * @param content
     * @throws Exception
     */
    public void editFile(String filePath,String content)throws Exception{
        File checkFile = new File(filePath);
        FileWriter writer = null;

        // 三、向目标文件中写入内容
        // FileWriter(File file, boolean append)，append为true时为追加模式，false或缺省则为覆盖模式
        writer = new FileWriter(checkFile, false);
        writer.append(content);
        writer.flush();

        writer.close();
    }

    /**
     * 出现的几个参数：${fullImage}、${displayImage}、${identifier}、${version}、${kind}、${title}、${url}
     * @param errorMsg  错误信息
     * @return
     */
    public String createPlistContent(String errorMsg){
        XMLOutputter outputer=null;
        try {
        if(StringUtils.isNotEmpty(errorMsg)){

            // 1、生成一个根节点
            Element rss = new Element("errorMsg");
            // 2、为节点添加属性
            rss.setAttribute("version", "1.0");
            // 3、生成一个document对象
            Document document = new Document(rss);

            Element dict = new Element("error");
            rss.addContent(dict);
            Element key = new Element("msg");
            key.setText(errorMsg);
            dict.addContent(key);

            Format format = Format.getCompactFormat();
            // 设置换行Tab或空格
            format.setIndent("	");
            format.setEncoding("UTF-8");

            // 4、创建XMLOutputter的对象
            outputer = new XMLOutputter(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            outputer.output(document, out);
            return out.toString("UTF-8");
        }
            // 1、生成一个根节点
            Element rss = new Element("plist");
            // 2、为节点添加属性
            rss.setAttribute("version", "1.0");
            // 3、生成一个document对象
            Document document = new Document(rss);

            Element dict = new Element("dict");
            rss.addContent(dict);
            Element key = new Element("key");
            key.setText("items");
            dict.addContent(key);

            Element array1=new Element("array");
            dict.addContent(array1);
            Element dict1_1=new Element("dict");
            array1.addContent(dict1_1);
            Element key1=new Element("key");
            key1.setText("assets");
            dict1_1.addContent(key1);

            Element array2=new Element("array");
            dict1_1.addContent(array2);

            for(int i=0;i<3;i++){
                Element dict1=new Element("dict");
                array2.addContent(dict1);
                if(i==0){
                    Element key1_1=new Element("key");
                    key1_1.addContent("kind");
                    dict1.addContent(key1_1);
                    Element string1=new Element("string");
                    string1.addContent("software-package");
                    dict1.addContent(string1);
                    Element key1_2=new Element("key");
                    key1_2.addContent("url");
                    dict1.addContent(key1_2);
                    Element string2=new Element("string");
                    string2.addContent("${url!''}");
                    dict1.addContent(string2);
                }else if(i==1){
                    Element key1_1=new Element("key");
                    key1_1.addContent("kind");
                    dict1.addContent(key1_1);
                    Element string1=new Element("string");
                    string1.addContent("full-size-image");
                    dict1.addContent(string1);
                    Element key1_2=new Element("key");
                    key1_2.addContent("needs-shine");
                    dict1.addContent(key1_2);
                    Element fa=new Element("false");
                    dict1.addContent(fa);
                    Element key1_3=new Element("key");
                    key1_3.addContent("url");
                    dict1.addContent(key1_3);
                    Element string2=new Element("string");
                    string2.addContent("${fullImage!''}");
                    dict1.addContent(string2);
                }else if(i==2){
                    Element key1_1=new Element("key");
                    key1_1.addContent("kind");
                    dict1.addContent(key1_1);
                    Element string1=new Element("string");
                    string1.addContent("display-image");
                    dict1.addContent(string1);
                    Element key1_2=new Element("key");
                    key1_2.addContent("needs-shine");
                    dict1.addContent(key1_2);
                    Element fa=new Element("false");
                    dict1.addContent(fa);
                    Element key1_3=new Element("key");
                    key1_3.addContent("url");
                    dict1.addContent(key1_3);
                    Element string2=new Element("string");
                    string2.addContent("${displayImage!''}");
                    dict1.addContent(string2);
                }
            }

            Element key2=new Element("key");
            key2.setText("metadata");
            dict1_1.addContent(key2);

            Element dict2=new Element("dict");
            dict1_1.addContent(dict2);
            for(int j=0;j<4;j++){
                if(j==0){
                    Element key2_1=new Element("key");
                    key2_1.addContent("bundle-identifier");
                    dict2.addContent(key2_1);
                    Element string1_1=new Element("string");
                    string1_1.addContent("${identifier!''}");
                    dict2.addContent(string1_1);
                }else if(j==1){
                    Element key2_1=new Element("key");
                    key2_1.addContent("bundle-version");
                    dict2.addContent(key2_1);
                    Element string1_1=new Element("string");
                    string1_1.addContent("${version!''}");
                    dict2.addContent(string1_1);
                }else if(j==2){
                    Element key2_1=new Element("key");
                    key2_1.addContent("kind");
                    dict2.addContent(key2_1);
                    Element string1_1=new Element("string");
                    string1_1.addContent("${kind!''}");
                    dict2.addContent(string1_1);
                }else if(j==3){
                    Element key2_1=new Element("key");
                    key2_1.addContent("title");
                    dict2.addContent(key2_1);
                    Element string1_1=new Element("string");
                    string1_1.addContent("${title!''}");
                    dict2.addContent(string1_1);
                }
            }


            Format format = Format.getCompactFormat();
            // 设置换行Tab或空格
            format.setIndent("	");
            format.setEncoding("UTF-8");

            // 4、创建XMLOutputter的对象
            outputer = new XMLOutputter(format);
            // 5、利用outputer将document转换成xml文档
            /*plistPath=info.getVirDir()+info.getFileName().split("\\.")[0]+".xml";
            File file = new File(plistPath);*/
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            outputer.output(document, out);
            return out.toString("UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建xml的模版信息
     * @param map
     * @param xmlStr
     * @return
     * @throws Exception
     */
    public String createXmlModule(Map<String,Object> map,String xmlStr)throws Exception{
        String result = "";
        Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("plistModule",xmlStr);

        cfg.setTemplateLoader(stringLoader);

        Template template = cfg.getTemplate("plistModule","utf-8");

        StringWriter writer = new StringWriter();
        template.process(map, writer);
        return writer.toString();
    }

    public boolean judgeFileType(String filePath){
        boolean flag=false;
        String[] pathArr=filePath.split("/");
        String pathName=pathArr[pathArr.length-1];
        String[] fileArr=pathName.split("\\.");
        String suffix=fileArr[fileArr.length-1];
        try{
            if(FileContants.valueOf(suffix)!=null){
                flag=true;
            }
            return flag;
        }catch (Exception e){
            return flag;
        }
    }

    /**
     * 获取本地的文件（xml、txt等文件）内容
     * @param filePath
     * @return
     */
    public String getfileinfo(String filePath)throws Exception{
        String rstr = "";
        FileSystemResource resource = new FileSystemResource(filePath);
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
        String str = null;
        while ((str = br.readLine()) != null) {
            rstr += str;
        }
        br.close();
        return rstr;
    }
}