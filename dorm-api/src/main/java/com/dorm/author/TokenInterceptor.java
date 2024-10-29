package com.dorm.author;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dorm.dao.mapper.AdminMapper;
import com.dorm.dao.mapper.StudentMapper;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

	@Resource
    private JwtConfig jwtConfig ;
	@Autowired
	private StudentMapper studentMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws SignatureException {
    	System.out.println("进入拦截器……");
    	
        /** 地址过滤：登录、登出、注册 */
        String uri = request.getRequestURI() ;
        if (uri.contains("login")||uri.contains("logout")||uri.contains("/api/user")){
            return true ;
        }
        
        boolean authRequired = true;
        if (handler instanceof HandlerMethod) {
        	//注！
			HandlerMethod method = (HandlerMethod) handler;
			//获取方法上的注解
			AuthRequired authAnnontation = AnnotationUtils.findAnnotation(method.getMethod(), AuthRequired.class);
			System.out.println(authAnnontation);
			if(authAnnontation==null){
				System.out.println("免登录请求！");
	            //return true;
				authRequired = false;
	        }
        }
        
        /** Token 验证 */
        String token = request.getHeader(jwtConfig.getHeader());
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        if(StringUtils.isEmpty(token)&&authRequired){
            throw new SignatureException(jwtConfig.getHeader()+ "不能为空");
        }
 
        Claims claims = null;
        try{
            claims = jwtConfig.getTokenClaim(token);//取到openid
            if(claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
            	if(authRequired) {            		
            		throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
            	}else {
            		return true;
            	}
            }
        }catch (Exception e){
        	if(authRequired) {        		
        		throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
        	}
        }
        String tokenStr = claims.getSubject().toString();
        if(tokenStr.indexOf("ADMIN")>-1){
            // 后台管理员用户
            Integer adminId = Integer.parseInt(tokenStr.substring(6));
            Admin admin = adminMapper.selectByPrimaryKey(adminId);
            //将当前登录用户放到request里
            request.setAttribute("loginAdmin", admin);
        }else{
            // 前台用户，Token是ID
            Integer uid = Integer.valueOf(tokenStr);
            Student user = studentMapper.selectByPrimaryKey(uid);
            //将当前登录用户放到request里
            request.setAttribute("loginUser", user);
        }
        return true;
    }
}
