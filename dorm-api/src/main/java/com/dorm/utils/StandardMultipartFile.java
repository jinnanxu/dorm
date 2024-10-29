package com.dorm.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.servlet.http.Part;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class StandardMultipartFile implements MultipartFile, Serializable {

	private static final long serialVersionUID = 8361707154721659708L;

	private final Part part;  
	  
    private final String filename;  
	
	public StandardMultipartFile(Part part, String filename) {  
        this.part = part;  
        this.filename = filename;  
    }
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOriginalFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		this.part.write(dest.getPath());  
        if (dest.isAbsolute() && !dest.exists()) {  
            // Servlet 3.0 Part.write is not guaranteed to support absolute file paths:  
            // may translate the given path to a relative location within a temp dir  
            // (e.g. on Jetty whereas Tomcat and Undertow detect absolute paths).  
            // At least we offloaded the file from memory storage; it'll get deleted  
            // from the temp dir eventually in any case. And for our user's purposes,  
            // we can manually copy it to the requested location as a fallback.  
            FileCopyUtils.copy(this.part.getInputStream(), new FileOutputStream(dest));  
        }  
	}

}
