package com.dorm.controller.admin.manager;

import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.*;
import com.dorm.dao.model.*;
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
@RequestMapping("/manager/fix")
@RestController
public class FixManagerApi {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private FixMapper fixMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("list")
    @AuthRequired
    public JsonResult treeList(HttpServletRequest request, PageView<Fix> page, @RequestParam(required = false) Integer buildingId, @RequestParam(required = false) Integer status) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<Fix> list = fixMapper.selectFixList(buildingId, status, page.getFirstResult(), page.getMaxresult());
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
        page.setTotalrecord(fixMapper.selectFixCount(buildingId, status));
        result.setData(page);
        return result;
    }

    @RequestMapping("update")
    @AuthRequired
    public JsonResult updateFix(HttpServletRequest request, Fix fix) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        Admin expert = (Admin) request.getAttribute("loginAdmin");
        try {
            fix.setFixedTime(new Date());
            fixMapper.updateByPrimaryKeySelective(fix);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
}
