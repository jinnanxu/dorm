package com.dorm.controller.admin.sys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.dorm.author.JwtConfig;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.AdminMapper;
import com.dorm.dao.mapper.AdminModifyMapper;
import com.dorm.dao.mapper.BuildingMapper;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.AdminModify;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/admin")
public class AdminLoginController {
	
	@Resource
	private JwtConfig jwtConfig ;
	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private BuildingMapper buildingMapper;
	@Autowired
	private AdminModifyMapper adminModifyMapper;

	@RequestMapping("logout")
	public String logOut(HttpSession session) {
		session.removeAttribute("currAdmin");
		return "admin/login";
	}
	
	/**
	 * 管理后台登录表单
	 * @return
	 */
	@RequestMapping("login")
	public JsonResult loginForm(String mobile, String pwd, Integer role) {
		JsonResult jsonResult = new JsonResult();
		Admin admin = new Admin();
		admin.setRole(role);
		admin.setMobile(mobile);
		admin.setPwd(pwd);
		List<Admin> list = adminMapper.select(admin);
		if(list!=null&&list.size()>0) {
			Admin admin3 = list.get(0);
			if(admin3.getRole()==2){
				admin3.setBuilding(buildingMapper.selectByPrimaryKey(admin3.getBuildingId()));
			}
			//使用ID来签发token
			String token = jwtConfig.createToken("ADMIN:"+admin3.getAdminId());
			JSONObject json = new JSONObject();
			json.put("admin", admin3);
			json.put("status", "OK");
			if (!StringUtils.isEmpty(token)) {
				json.put("token",token);
			}
			jsonResult.setData(json);
			return  jsonResult;
		}else {
			return JsonResult.error("500", "手机号或密码错误");
		}
	}

	/**
	 * 修改个人信息
	 * @param session
	 * @param admin
	 * @return
	 */
	@RequestMapping("updateProfile")
	public JsonResult updateProfile(HttpSession session, Admin admin) {
		JsonResult result = new JsonResult();
		try {
			if(admin.getRole()>1){
				admin = adminMapper.selectByPrimaryKey(admin.getAdminId());
				AdminModify modify = new AdminModify(admin);
				adminModifyMapper.insertSelective(modify);
			}else{
				adminMapper.updateByPrimaryKeySelective(admin);
				admin = adminMapper.selectByPrimaryKey(admin.getAdminId());
			}
			result.setData(admin);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.error("-1", "更新失败");
		}
		return result;
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
