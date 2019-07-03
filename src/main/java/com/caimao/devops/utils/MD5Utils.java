package com.caimao.devops.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * MD5的操作
 * 模块名称：CaimaoDevOps com.caimao.devops.utils
 * 功能说明：<br>
 * 开发人员：Luzx
 * 创建时间： 2019-04-17 9:50
 * 系统版本：1.0.0
 **/

public class MD5Utils {

    private static char[] hexdigits={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    /**
     * MD5文件加密
     * @param doc
     * @return
     */
    public static String md5Encryte(MultipartFile doc)throws Exception{
        MessageDigest me=MessageDigest.getInstance("MD5");
        InputStream input=doc.getInputStream();
        byte[] buffer=new byte[1024];
        int num=0;
        while((num=input.read(buffer))>0){
            me.update(buffer,0,num);
        }
        input.close();

        return byteToHexString(me.digest());
    }

    /**
     * MD5对文件的加密
     * @param file
     * @return
     * @throws Exception
     */
    public static String md5Encryte(File file)throws Exception{
        MessageDigest me=MessageDigest.getInstance("MD5");
        InputStream input=new FileInputStream(file);
        byte[] buffer=new byte[1024];
        int num=0;
        while((num=input.read(buffer))>0){
            me.update(buffer,0,num);
        }
        input.close();

        return byteToHexString(me.digest());
    }

    private static String byteToHexString(byte[] tmp) {
        String s;
        char str[] = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexdigits[byte0 >>> 4 & 0xf];
            str[k++] = hexdigits[byte0 & 0xf];
        }
        s = new String(str);
        return s;
    }
}