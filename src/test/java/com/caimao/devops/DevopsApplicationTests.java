package com.caimao.devops;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caimao.devops.entity.CsAppPlistInfoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevopsApplicationTests {


	RedisTemplate redisTemplate;

	public static int BUFFER_SIZE=1024;

	public void unZip(String filePath,String targetPath){
		File srcFile=new File(filePath);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
		}
		// 开始解压
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(srcFile);
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				// 如果是文件夹，就创建个文件夹
				if (entry.isDirectory()) {
					String dirPath = targetPath + "/" + entry.getName();
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
					byte[] buf = new byte[BUFFER_SIZE];
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					// 关流顺序，先打开的后关闭
					fos.close();
					is.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("unzip error from ZipUtils", e);
		} finally {
			if(zipFile != null){
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void contextLoads() throws Exception{
		String uuid = UUID.randomUUID().toString();
		//去掉“-”符号
		System.out.println(uuid.replaceAll("-", ""));
	}

	@Test
	public void redisTest()throws Exception{
		HashOperations h=redisTemplate.opsForHash();
		CsAppPlistInfoEntity entity=new CsAppPlistInfoEntity();
		entity.setProductId(111);
		String entityStr= JSONObject.toJSONString(entity);
		h.put("IOS_CLIENT_PLIST","111"+"-"+1111,entityStr);

		String str= (String) h.get("IOS_CLIENT_PLIST","111"+"-"+1111);
		JSONObject json=JSONObject.parseObject(str);
		CsAppPlistInfoEntity entityNew=JSON.toJavaObject(json,CsAppPlistInfoEntity.class);

		System.out.println(entityNew.getProductId());
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
