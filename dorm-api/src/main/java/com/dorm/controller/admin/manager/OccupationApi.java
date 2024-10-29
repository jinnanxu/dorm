package com.dorm.controller.admin.manager;

import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.*;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.Dormitory;
import com.dorm.dao.model.Occupation;
import com.dorm.dao.model.Student;
import com.dorm.utils.page.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/admin/occ")
@RestController
public class OccupationApi {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private DormitoryMapper dormMapper;
    @Autowired
    private OccupationMapper occupationMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询入住记录列表
     * @param page
     * @param buildingId
     * @param dormId
     * @return
     */
    @RequestMapping("list")
    public JsonResult occList(PageView<Occupation> page, Integer buildingId, Integer dormId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<Occupation> list = occupationMapper.selectOccupation(buildingId, dormId, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(occupationMapper.selectOccupationCount(buildingId, dormId));
        result.setData(page);
        return result;
    }

    /**
     * 保存入住信息
     * @param occ
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveOrUpdateOccupation(Occupation occ, HttpServletRequest request) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        Admin manager = (Admin) request.getAttribute("loginAdmin");
        try {
            Dormitory dorm = dormMapper.selectByPrimaryKey(occ.getDormId());
            if(occ.getOccupationId()==null){
                //先判断目标宿舍是否有空位
                if(dorm.getMaxCapacity() - dorm.getOccupied() <= 0){
                    return JsonResult.error("500", "该宿舍已住满，无法入住新学生");
                }
                //先看一下这个学生有没有正在住宿的宿舍，若有就先迁出
                List<Occupation> existList = occupationMapper.selectExistOccupation(occ.getStudentId());
                if(existList!=null&&existList.size()>0){
                    Occupation occExist = existList.get(0);
                    occExist.setCheckOutTime(new Date());
                    occExist.setRemark(occExist.getRemark()+"#入住新宿舍自动迁出。");
                    occExist.setCurrStatus(-1);
                    occupationMapper.updateByPrimaryKeySelective(occExist);
                }
                occ.setCheckInTime(new Date());
                occ.setOperatorId(manager.getAdminId());
                occupationMapper.insertSelective(occ);
                //将入住的宿舍对应的已入住人数加1
                dorm.setOccupied(dorm.getOccupied()+1);
                dormMapper.updateByPrimaryKeySelective(dorm);
            }else{
                if(occ.getCurrStatus()==-1){
                    occ.setCheckOutTime(new Date());
                    //将入住的宿舍对应的已入住人数减1
                    dorm.setOccupied(dorm.getOccupied()-1);
                    dormMapper.updateByPrimaryKeySelective(dorm);
                }
                occupationMapper.updateByPrimaryKeySelective(occ);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

}
