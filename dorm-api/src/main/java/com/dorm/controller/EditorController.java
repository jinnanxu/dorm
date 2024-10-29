package com.dorm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dorm.controller.vo.WangEditor;

@Controller
public class EditorController {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private ServerUploadUtil serverUploadUtil;
	 
	// 图片上传
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public WangEditor uploadFile(
			@RequestParam("myFile") MultipartFile multipartFile,
			HttpServletRequest request) {
		System.out.println("收到上传文件请求");
		FileOutputStream fos = null;
		try {
			// 获取项目路径
			InputStream inputStream = multipartFile.getInputStream();
			// 获取文件名称
			String filename = getFileName();
			// 服务器根目录的路径
			String uuid = UUID.randomUUID().toString();
			// 根目录下新建文件夹upload，存放上传图片
			String uploadPath = request.getSession().getServletContext().getRealPath("/upload")+File.separator;
			System.out.println("存储的文件路径："+uploadPath);
			String newFileName = filename.substring(filename.lastIndexOf("."));
			String filePath = uploadPath+uuid+newFileName;
			System.out.println("完整文件名："+filePath);
			// 将文件上传的服务器根目录下的upload文件夹
			File file = new File(uploadPath, filename);
			FileUtils.copyInputStreamToFile(inputStream, file);
			
			String picUrl = serverUploadUtil.uploadToServer(newFileName, file);
			// 返回图片访问路径
			System.out.println("文件回显路径："+picUrl);
			String[] str = { picUrl };
			try {
				// 删除临时文件
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			WangEditor we = new WangEditor(str);
			return we;
		} catch (Exception e) {
			log.error("上传文件失败", e);
		}
		return null;

	}

	@RequestMapping(value = "/upload2", method = RequestMethod.POST)
	@ResponseBody
	public WangEditor editorUploadFile(
			@RequestParam("file") MultipartFile multipartFile,
			HttpServletRequest request) {
		System.out.println("收到编辑器上传文件请求");
		FileOutputStream fos = null;
		try {
			// 获取项目路径
			InputStream inputStream = multipartFile.getInputStream();
			// 获取文件名称
			String filename = getFileName();
			// 服务器根目录的路径
			String uuid = UUID.randomUUID().toString();
			// 根目录下新建文件夹upload，存放上传图片
			String uploadPath = request.getSession().getServletContext().getRealPath("/upload")+File.separator;
			System.out.println("存储的文件路径："+uploadPath);
			String newFileName = filename.substring(filename.lastIndexOf("."));
			String filePath = uploadPath+uuid+newFileName;
			System.out.println("完整文件名："+filePath);
			// 将文件上传的服务器根目录下的upload文件夹
			File file = new File(uploadPath, filename);
			FileUtils.copyInputStreamToFile(inputStream, file);

			String picUrl = serverUploadUtil.uploadToServer(newFileName, file);

			// 返回图片访问路径
			System.out.println("文件回显路径："+picUrl);
			String[] str = { picUrl };
			WangEditor we = new WangEditor(str);
			return we;
		} catch (Exception e) {
			log.error("上传文件失败", e);
		}
		return null;

	}
	
	public String getFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStr = sdf.format(new Date());
		String str = RandomStringUtils.random(5,
				"abcdefghijklmnopqrstuvwxyz1234567890");
		String name = timeStr + str + ".jpg";
		return name;
	}
}
