package com.dorm.utils;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

public class FileTypeUtils {

	public static boolean checkImageFile(File file) {
		MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
		mtftp.addMimeTypes("image png tif jpg jpeg bmp");
		String mimetype= mtftp.getContentType(file);
        String type = mimetype.split("/")[0];
        System.out.println("上传文件类型判断："+type);
        return type.equals("image");
	}
}
