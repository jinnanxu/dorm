package com.dorm.utils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP工具类
 * @author xujinnan
 *
 */
public class HttpUtils {

	public static void get(String url, Map<String, String> params) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		//拼接参数
		url = url+"?";
		Set<Entry<String, String>> entrySet = params.entrySet();
		for (Entry<String, String> entry : entrySet) {
			url = url+entry.getKey()+"="+entry.getValue()+"&";
		}
		if(url.endsWith("&")) {
			url = url.substring(0, url.length()-2);
		}
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		System.out.println(response);
		System.out.println(response.getBody());

		return;
	}

	/**
	 * 发送post请求
	 * @param uri
	 * @return
	 */
	public static String post(String uri, Map<String, String> params) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		Set<Entry<String, String>> entrySet = params.entrySet();
		for (Entry<String, String> entry : entrySet) {			
			paramsMap.add(entry.getKey(), entry.getValue());
		}
		HttpHeaders headers = new HttpHeaders();
		// 以表单的方式提交
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// 将请求头部和参数合成一个请求
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramsMap, headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		return response.getBody();
	}
}
