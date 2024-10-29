package com.dorm.controller.admin.sys;

import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.AdminMapper;
import com.dorm.dao.mapper.AdminModifyMapper;
import com.dorm.dao.mapper.BuildingMapper;
import com.dorm.dao.mapper.StudentMapper;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.AdminModify;
import com.dorm.dao.model.Student;
import com.dorm.utils.EasyExcelUtil;
import com.dorm.utils.StudentDataClassListener;
import com.dorm.utils.page.PageView;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 后台接口：用户管理
 */
@RequestMapping("/admin/user")
@RestController
public class AdminUserManagerApi {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminModifyMapper modifyMapper;
    @Autowired
    private BuildingMapper buildingMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询学生用户列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    public JsonResult userList(PageView<Student> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<Student> list = studentMapper.selectStudentList(kw, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(studentMapper.selectStudentCount(kw));
        result.setData(page);
        return result;
    }

    @RequestMapping("list/all")
    public JsonResult allStudentList() {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Student.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andGreaterThan("status", -1);
        List<Student> list = studentMapper.selectByExample(example);
        result.setData(list);
        return result;
    }

    /**
     * 注销学生用户
     * @param userId
     * @return
     */
    @RequestMapping("delete")
    public JsonResult delete(Integer userId) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        Student user = studentMapper.selectByPrimaryKey(userId);
        user.setStatus(-1);
        studentMapper.updateByPrimaryKeySelective(user);
        result.setData(user);
        return result;
    }

    @RequestMapping("save")
    public JsonResult saveOrUpdateApartment(Student user) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(user.getStudentId()==null){
                studentMapper.insertSelective(user);
            }else{
                studentMapper.updateByPrimaryKeySelective(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 批量导入
     * @param file
     * @return
     */
    @PostMapping("/uploadExcel")
    public JsonResult uploadExcel(@RequestPart MultipartFile file) {
        JsonResult result = new JsonResult(true, "0", "导入成功");
        try {
            // 1、获取输入流，这里需要自行实现获取输入流的逻辑
            InputStream inputStream = file.getInputStream();
            //2、解析输入流数据，并添加到数据库
            StudentDataClassListener studentDataClassListener = new StudentDataClassListener();
            EasyExcelUtil.readExcel(inputStream, Student.class, studentDataClassListener);
            List<Student> list = studentDataClassListener.getDatas();
            for (Student student:list){
                System.out.println("读取到数据：" + student);
                Student record = new Student();
                record.setStudentNo(student.getStudentNo());
                if(studentMapper.selectCount(record)>0){
                    System.out.println("学号重复：" + student.getStudentNo());
                    continue;
                }
                studentMapper.insertSelective(student);
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return JsonResult.error("500", "导入失败");
        }
        return result;
    }

    @RequestMapping("update2")
    public JsonResult updateUserStatus(Integer studentId, Integer status) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            Student student = studentMapper.selectByPrimaryKey(studentId);
            student.setStatus(status);
            studentMapper.updateByPrimaryKeySelective(student);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查询宿舍管理员列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("manager/list")
    public JsonResult apartmentManagerList(PageView<Admin> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("mobile", "%"+kw+"%");
        }
        criteria.andEqualTo("role", 2);
        List<Admin> list = adminMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for(Admin admin:list){
            admin.setBuilding(buildingMapper.selectByPrimaryKey(admin.getBuildingId()));
        }
        page.setRecords(list);
        page.setTotalrecord(adminMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 注销宿舍管理员信息
     * @param expertId
     * @return
     */
    @RequestMapping("manager/delete")
    public JsonResult expertDelete(Integer expertId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Admin expert = adminMapper.selectByPrimaryKey(expertId);
        expert.setStatus(-1);//注销状态
        adminMapper.updateByPrimaryKeySelective(expert);
        result.setData(expert);
        return result;
    }

    /**
     * 注销宿舍管理员
     * @param adminId
     * @param status
     * @return
     */
    @RequestMapping("manager/update")
    public JsonResult updateAdminStatus(Integer adminId, Integer status) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            Admin admin = adminMapper.selectByPrimaryKey(adminId);
            admin.setStatus(status);
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 宿舍管理员注册
     * @param user
     * @param user
     * @return
     */
    @RequestMapping("manager/save")
    public JsonResult saveApartmentManager(Admin user) {
        JsonResult result = new JsonResult(true, "0", "注册成功");
        try {
            if(user.getAdminId()==null){
                Admin exist = new Admin();
                exist.setMobile(user.getMobile());
                int count = adminMapper.selectCount(exist);
                if(count > 0){
                    result.setMsg("该手机号已注册");
                    result.setSuccess(false);
                }else{
                    adminMapper.insertSelective(user);
                }
            }else{
                adminMapper.updateByPrimaryKeySelective(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("注册失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("manager/modify/list")
    public JsonResult apartmentManagerModifyList(PageView<AdminModify> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(AdminModify.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("mobile", "%"+kw+"%");
        }
        criteria.andEqualTo("role", 2);
        List<AdminModify> list = modifyMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for(AdminModify modify:list){
            modify.setOrgAdmin(adminMapper.selectByPrimaryKey(modify.getAdminId()));
        }
        page.setRecords(list);
        page.setTotalrecord(modifyMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    @RequestMapping("manager/modify/update")
    public JsonResult updateManagerModifyStatus(Integer adminId, Integer status) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            AdminModify admin = modifyMapper.selectByPrimaryKey(adminId);
            admin.setAppStatus(status);
            modifyMapper.updateByPrimaryKeySelective(admin);
            if(status==1){
                //通过审核，把修改的内容写到admin表
                Admin orgAdmin = adminMapper.selectByPrimaryKey(admin.getAdminId());
                orgAdmin.setStatus(admin.getStatus());
                orgAdmin.setGender(admin.getGender());
                orgAdmin.setPwd(admin.getPwd());
//                orgAdmin.setBuilding(admin.getBuildingId());
                orgAdmin.setEmployeeNo(admin.getEmployeeNo());
                orgAdmin.setRealName(admin.getRealName());
                orgAdmin.setMobile(admin.getMobile());
                adminMapper.updateByPrimaryKeySelective(orgAdmin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 维修人员列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("worker/list")
    public JsonResult apartmentWorkerList(PageView<Admin> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("mobile", "%"+kw+"%");
        }
        criteria.andEqualTo("role", 3);
        List<Admin> list = adminMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for(Admin admin:list){
            admin.setBuilding(buildingMapper.selectByPrimaryKey(admin.getBuildingId()));
        }
        page.setRecords(list);
        page.setTotalrecord(adminMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }
}
