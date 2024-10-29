package com.dorm.controller.admin.manager;

import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.AdminMapper;
import com.dorm.dao.mapper.LeaveInfoMapper;
import com.dorm.dao.mapper.OccupationMapper;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.LeaveInfo;
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

/**
 * 教师：问题管理
 */
@RequestMapping("/manager/leave")
@RestController
public class LeaveManagerApi {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private LeaveInfoMapper leaveInfoMapper;
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
    public JsonResult leaveList(HttpServletRequest request, PageView<LeaveInfo> page, @RequestParam(required = false) Integer buildingId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Admin manager = (Admin) request.getAttribute("loginAdmin");
        if(buildingId==null){
            if(manager.getBuildingId()==null){
                return result;
            }
            buildingId = manager.getBuildingId();
        }
        List<LeaveInfo> list = leaveInfoMapper.selectLeaveList(buildingId, page.getFirstResult(), page.getMaxresult());
        for(LeaveInfo task:list){
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
        page.setTotalrecord(leaveInfoMapper.selectMyLeaveCount(buildingId));
        result.setData(page);
        return result;
    }

}
