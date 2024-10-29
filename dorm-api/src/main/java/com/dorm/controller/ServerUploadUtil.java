package com.dorm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerUploadUtil {

    @Value("${upload.server.uuid}")
    private String uuid;

	public String uploadToServer(String fileName,File file) {
		String url = "http://118.25.89.125:16602/upload-server/upload";
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            //HttpMultipartMode.RFC6532参数的设定是为避免文件名为中文时乱码
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            httpPost.addHeader("header1", "111");//头部放文件上传的head可自定义
            builder.addBinaryBody("myFile", file, ContentType.MULTIPART_FORM_DATA, fileName);
            builder.addTextBody("fileName", file.getName());//其余参数，可自定义
            builder.addTextBody("u", uuid);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            System.out.println("开始向图片服务器上传图片……");
            HttpResponse response = httpClient.execute(httpPost);// 执行提交
            HttpEntity responseEntity = response.getEntity();//接收调用外部接口返回的内容
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            // 返回的内容都在content中
            InputStream content = responseEntity.getContent();
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            if(!StringUtils.isBlank(result)) {
                System.out.println("上传文件"+fileName+"返回参数==>"+result);
            }else {
            	System.out.println("上传文件失败：返回result为null");
            }
        }
        }catch(Exception e) {
        	System.out.println("上传文件失败：");
        	e.printStackTrace();
        }finally {//处理结束后关闭httpclient的链接
            try {

                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONObject json = JSON.parseObject(result);
        return json.getString("url");
    }
	
}
