package com.dorm.controller.admin.sys;

import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.BuildingMapper;
import com.dorm.dao.mapper.DormitoryMapper;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.Building;
import com.dorm.dao.model.Dormitory;
import com.dorm.dao.model.Student;
import com.dorm.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 后台接口：元数据管理，楼宇、宿舍
 */
@RequestMapping("/admin")
@RestController
public class MetaDataManagerApi {

    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private DormitoryMapper dormitoryMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询楼宇列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("building/list")
    public JsonResult buildingList(PageView<Building> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Building.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("buildingName", "%"+kw+"%");
        }
        List<Building> list = buildingMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(list);
        page.setTotalrecord(buildingMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    @RequestMapping("building/list/all")
    public JsonResult allBuildingList() {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Building.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        List<Building> list = buildingMapper.selectByExample(example);
        result.setData(list);
        return result;
    }

    @RequestMapping("building/save")
    public JsonResult saveOrUpdateBuilding(Building building) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(building.getBuildingId()==null){
                buildingMapper.insertSelective(building);
            }else{
                buildingMapper.updateByPrimaryKeySelective(building);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 注销楼宇
     * @param buildingId
     * @return
     */
    @RequestMapping("building/delete")
    public JsonResult deleteBuilding(Integer buildingId) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            Building building =  buildingMapper.selectByPrimaryKey(buildingId);
            building.setStatus(-1);//注销状态
            buildingMapper.updateByPrimaryKeySelective(building);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查询宿舍列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("dorm/list")
    public JsonResult dormList(PageView<Dormitory> page, String roomNo, Integer buildingId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Dormitory.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(roomNo)){
            criteria.andLike("roomNo", "%"+roomNo+"%");
        }
        if(buildingId != null){
            criteria.andEqualTo("buildingId", buildingId);
        }
        List<Dormitory> list = dormitoryMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for(Dormitory dorm:list){
            dorm.setBuilding(buildingMapper.selectByPrimaryKey(dorm.getBuildingId()));
        }
        page.setRecords(list);
        page.setTotalrecord(dormitoryMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 所有宿舍列表，用于填充下拉框
     * @return
     */
    @RequestMapping("dorm/list/all")
    public JsonResult allDormList() {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Dormitory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        List<Dormitory> list = dormitoryMapper.selectByExample(example);
        result.setData(list);
        return result;
    }

    /**
     * 根据楼宇ID查询宿舍
     * @param buildingId
     * @return
     */
    @RequestMapping("building/dorm")
    public JsonResult buildingDormList(Integer buildingId) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Dormitory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        criteria.andEqualTo("buildingId", buildingId);
        List<Dormitory> list = dormitoryMapper.selectByExample(example);
        result.setData(list);
        return result;
    }

    /**
     * 保存或更新宿舍
     * @param dorm
     * @return
     */
    @RequestMapping("dorm/save")
    public JsonResult saveOrUpdateDorm(Dormitory dorm) {
        JsonResult result = new JsonResult(true, "0", "更新成功");
        try {
            if(dorm.getDormitoryId()==null){
                dormitoryMapper.insertSelective(dorm);
            }else{
                dormitoryMapper.updateByPrimaryKeySelective(dorm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
    /**
     * 修改宿舍状态
     * @param dormId
     * @param status
     * @return
     */
    @RequestMapping("dorm/update/status")
    public JsonResult updateDormStatus(Integer dormId, Integer status) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Dormitory dorm = dormitoryMapper.selectByPrimaryKey(dormId);
        dorm.setStatus(status);//注销状态
        dormitoryMapper.updateByPrimaryKeySelective(dorm);
        result.setData(dorm);
        return result;
    }

}
