package com.dorm.controller.user;

import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.AdminMapper;
import com.dorm.dao.mapper.FixMapper;
import com.dorm.dao.mapper.OccupationMapper;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.Fix;
import com.dorm.dao.model.Occupation;
import com.dorm.dao.model.Student;
import com.dorm.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 教师：问题管理
 */
@RequestMapping("/front/fix")
@RestController
public class FixApi {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private FixMapper fixMapper;
    @Autowired
    private OccupationMapper occupationMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("list")
    @AuthRequired
    public JsonResult fixList(HttpServletRequest request, PageView<Fix> page, @RequestParam(required = false) Integer buildingId, @RequestParam(required = false) Integer status) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Student student = (Student) request.getAttribute("loginUser");
        List<Fix> list = fixMapper.selectMyFixList(student.getStudentId(), page.getFirstResult(), page.getMaxresult());
        for(Fix task:list){
            if(StringUtils.isNotBlank(task.getPics())){
                List<String> picList = new ArrayList<>();
                String[] pics = task.getPics().split(",");
                for(String pic:pics){
                    picList.add(pic);
                }
                task.setPicList(picList);
            }
        }
        page.setRecords(list);
        page.setTotalrecord(fixMapper.selectMyFixCount(student.getStudentId()));
        result.setData(page);
        return result;
    }

    @RequestMapping("save")
    @AuthRequired
    public JsonResult updateFix(HttpServletRequest request, Fix fix) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        Student student = (Student) request.getAttribute("loginUser");
        try {
            List<Occupation> studentOcc = occupationMapper.selectExistOccupation(student.getStudentId());
            if(studentOcc!=null&&studentOcc.size()>0){
                fix.setDormId(studentOcc.get(0).getDormId());
                fix.setBuildingId(studentOcc.get(0).getBuildingId());
            }else{
                return JsonResult.error("500", "未找到您入住的宿舍，无法报修！");
            }
            fix.setCreateTime(new Date());
            fix.setStudentId(student.getStudentId());
            //分配维修人员
            Admin t = new Admin();
            t.setRole(3);
            t.setStatus(1);
            List<Admin> workers = adminMapper.select(t);
            Random rdn = new Random();
            int idx = rdn.nextInt(workers.size());
            fix.setWorkerId(workers.get(idx).getAdminId());
            fixMapper.insertSelective(fix);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("manager/save")
    @AuthRequired
    public JsonResult managerSaveFix(HttpServletRequest request, Fix fix) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        Student student = (Student) request.getAttribute("loginUser");
        try {
            fix.setCreateTime(new Date());
            fix.setStudentId(0);//宿舍管理员上报的
            //分配维修人员
            Admin t = new Admin();
            t.setRole(3);
            t.setStatus(1);
            List<Admin> workers = adminMapper.select(t);
            Random rdn = new Random();
            int idx = rdn.nextInt(workers.size());
            fix.setWorkerId(workers.get(idx).getAdminId());
            fixMapper.insertSelective(fix);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 取消报修
     * @param fixId
     * @return
     */
    @RequestMapping("cancel")
    @AuthRequired
    public JsonResult cancelFix(Integer fixId) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            Fix fix = fixMapper.selectByPrimaryKey(fixId);
            fix.setStatus("2");
            fixMapper.updateByPrimaryKeySelective(fix);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
}
