package com.dorm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dorm.author.TokenInterceptor;

@Configuration
@Component
public class WebConfig implements WebMvcConfigurer {

	@Autowired
    private TokenInterceptor tokenInterceptor ;
	
//	@Bean
//	TokenInterceptor tokenInterceptor() {
//		return new TokenInterceptor();
//	}
	
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("配置拦截器！");
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("**/login","**/regist");
    }
}
