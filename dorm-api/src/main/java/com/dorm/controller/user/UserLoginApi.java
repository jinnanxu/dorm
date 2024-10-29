package com.dorm.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.dorm.dao.mapper.OccupationMapper;
import com.dorm.dao.mapper.StudentMapper;
import com.dorm.dao.model.Occupation;
import com.dorm.dao.model.Student;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dorm.author.JwtConfig;
import com.dorm.controller.vo.JsonResult;

import java.util.List;

/**
 * 前台学生相关接口
 */
@RestController
@RequestMapping("/user")
public class UserLoginApi {

    Logger logger = LoggerFactory.getLogger(UserLoginApi.class);

    @Resource
    private JwtConfig jwtConfig ;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private OccupationMapper occupationMapper;

    /**
     * 前台用户登录
     * @param pwd
     * @param mobile
     * @return
     */
    @PostMapping("/login")
    public JsonResult login (@RequestParam("pwd") String pwd, @RequestParam("mobile") String mobile){
        JSONObject json = new JSONObject();
        Student record = new Student();
        //以手机号与密码为条件查询数据库，看是否存在用户
        record.setPwd(pwd);
        record.setMobile(mobile);
        Student user = studentMapper.selectOne(record);
        if(user==null) {
            return JsonResult.error("403", "用户不存在或未激活");
        }
        //将手机号存入token中，之后就可以在TokenInterceptor中根据token取到用户
        String token = jwtConfig.createToken(user.getStudentId()+"");
        if (!StringUtils.isEmpty(token)) {
            json.put("token",token) ;
        }
        List<Occupation> studentOcc = occupationMapper.selectExistOccupation(user.getStudentId());
        if(studentOcc!=null&&studentOcc.size()>0){
            user.setOcc(studentOcc.get(0));
        }
        json.put("user", user);
        return JsonResult.success(json) ;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public JsonResult register (Student user){
        Student record = new Student();
        //以学号、姓名为条件查询数据库，看是否存在用户
        record.setRealName(user.getRealName());
        record.setStudentNo(user.getStudentNo());
        int existCount = studentMapper.selectCount(record);
        if(existCount==0) {
            return JsonResult.error("500", "学生信息错误");
        }
        Student dbStudent = studentMapper.select(record).get(0);
        //将新用户更新到数据库
        dbStudent.setPwd(user.getPwd());
        dbStudent.setMobile(user.getMobile());
        dbStudent.setStatus(1);//激活成功状态
        studentMapper.updateByPrimaryKeySelective(dbStudent);
        return JsonResult.success(user) ;
    }

    @RequestMapping("updateStudent")
    public JsonResult updateStudent(Student user, HttpServletRequest request) {
        studentMapper.updateByPrimaryKeySelective(user);
        Student newStudent = studentMapper.selectByPrimaryKey(user.getStudentId());
        JsonResult ret = new JsonResult();
        ret.setData(newStudent);
        return ret;
    }

}