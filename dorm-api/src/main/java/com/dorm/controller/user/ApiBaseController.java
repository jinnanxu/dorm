package com.dorm.controller.user;

import com.dorm.dao.model.Student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ApiBaseController {
	
	public Student getCurrUser(HttpServletRequest request) {
		Student user = (Student) request.getAttribute("loginUser");//从Request中根据Token获取登录用户
		return user;
	}
	
	public void updateCurrUser(HttpSession session, Student user) {
		session.setAttribute("CURR_USER", user);
	}

}
