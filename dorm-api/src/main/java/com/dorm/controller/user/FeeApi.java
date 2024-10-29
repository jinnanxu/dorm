package com.dorm.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.AdminMapper;
import com.dorm.dao.mapper.FeeMapper;
import com.dorm.dao.mapper.MsgMapper;
import com.dorm.dao.mapper.OccupationMapper;
import com.dorm.dao.model.*;
import com.dorm.utils.DateUtils;
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
import java.util.Map;

/**
 * 教师：问题管理
 */
@RequestMapping("/front/fee")
@RestController
public class FeeApi {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private FeeMapper feeMapper;
    @Autowired
    private OccupationMapper occupationMapper;
    @Autowired
    private MsgMapper msgMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询我的水电费
     * @param request
     * @param page
     * @param feeType
     * @param buildingId
     * @param dormId
     * @param status
     * @param dateRange
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult feeList(HttpServletRequest request, PageView<Fee> page, Integer feeType, Integer buildingId, Integer dormId, Integer status, String[] dateRange) {
        Student student = (Student) request.getAttribute("loginUser");
        JsonResult result = new JsonResult(true, "0", "查询成功");
        List<Occupation> studentOcc = occupationMapper.selectExistOccupation(student.getStudentId());
        Occupation occ = null;
        if(studentOcc!=null&&studentOcc.size()>0){
            occ = studentOcc.get(0);
        }else{
            result.setData(page);
            return result;
        }
        Integer year0=null, year1=null;
        Integer month0=null, month1=null;
        if(dateRange != null && dateRange.length > 0){
            year0 = Integer.valueOf(dateRange[0].split("-")[0]);
            year1 = Integer.valueOf(dateRange[1].split("-")[0]);
            month0 = Integer.valueOf(dateRange[0].split("-")[1]);
            month1 = Integer.valueOf(dateRange[1].split("-")[1]);
        }
        //根据条件查询水电费
        List<Fee> list = feeMapper.selectMyFeeList(feeType, occ.getDormId(),
                year0, month0, year1, month1, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(feeMapper.selectMyFeeCount(feeType, occ.getDormId(), year0, month0, year1, month1));
        result.setData(page);
        return result;
    }

    /**
     * 支付水电费
     * @param request
     * @param feeId
     * @return
     */
    @RequestMapping("pay")
    @AuthRequired
    public JsonResult payFee(HttpServletRequest request, Integer feeId) {
        JsonResult result = new JsonResult(true, "0", "支付成功");
        Student student = (Student) request.getAttribute("loginUser");
        try {
            Fee fee = feeMapper.selectByPrimaryKey(feeId);
            fee.setPayTime(new Date());
            fee.setStatus(1);
            feeMapper.updateByPrimaryKeySelective(fee);
            //将催缴记录设置为过期，不再显示
            Msg msg = new Msg();
            msg.setFeeId(fee.getFeeId());
            List<Msg> list = msgMapper.select(msg);
            for(Msg m:list){
                m.setIsRead(1);
                msgMapper.updateByPrimaryKeySelective(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("statics/data")
    @AuthRequired
    public JsonResult statics(HttpServletRequest request) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Student student = (Student) request.getAttribute("loginUser");
        List<Occupation> studentOcc = occupationMapper.selectExistOccupation(student.getStudentId());
        Occupation occ = null;
        if(studentOcc!=null&&studentOcc.size()>0){
            occ = studentOcc.get(0);
        }else{
            return result;
        }
        //饼图：[{ value: 1048, name: 'Search Engine' },{},...]
        //柱状图：{[150, 230, 224, 218, 135, 147, 260],[150, 230, 224, 218, 135, 147, 260]}, ['01-01', '01-02', '01-03', '01-04', '01-05', '01-06', '01-07']
        //水电费柱状图
        //水电分别的数值：{[150, 230, 224, 218, 135, 147, 260],[150, 230, 224, 218, 135, 147, 260]}
        //对应的X轴日期：['2024-01', '2024-02', '2024-03']
        //水电用量
        List<Map<String, Object>> elcFeeDataList = feeMapper.staticsUsageByDorm(1, occ.getDormId());
        List<Map<String, Object>> waterFeeDataList = feeMapper.staticsUsageByDorm(2, occ.getDormId());
        //从数据查询时是以月份进行分组，并按升序做好排序
        JSONObject usageData = new JSONObject();
        JSONArray dayXAxis = new JSONArray();//曲线图X轴数据（日期）
        JSONArray dayYAxis = new JSONArray();//曲线图Y辆数据（用电度数）
        JSONArray dayYAxis2 = new JSONArray();//曲线图Y辆数据（用水量吨数）
        //排完序后的数据，分别放到X轴与Y轴的数组中
        for(int idx=0; idx<elcFeeDataList.size(); idx++){
            Map<String, Object> item = elcFeeDataList.get(idx);
            dayXAxis.add(item.get("billYear")+"-"+item.get("billMonth"));
            dayYAxis.add(item.get("amount"));
        }
        for(int idx=0; idx<waterFeeDataList.size(); idx++){
            Map<String, Object> item = waterFeeDataList.get(idx);
            dayYAxis2.add(item.get("amount"));
        }
        JSONArray usages = new JSONArray();
        usages.add(dayYAxis);
        usages.add(dayYAxis2);
        usageData.put("dayXAxis", dayXAxis);
        usageData.put("dayYAxis", usages);

        //水电费用柱状图
        List<Map<String, Object>> elcAmountDataList = feeMapper.staticsFeeByDorm(1, occ.getDormId());
        List<Map<String, Object>> waterAmountDataList = feeMapper.staticsFeeByDorm(2, occ.getDormId());
        //从数据查询时是以月份进行分组，并按升序做好排序
        JSONArray feeDayXAxis = new JSONArray();//曲线图X轴数据（日期）
        JSONArray elcFeeYAxis = new JSONArray();//曲线图Y辆数据（电费金额）
        JSONArray waterFeeYAxis2 = new JSONArray();//曲线图Y辆数据（水消金额）
        //排完序后的数据，分别放到X轴与Y轴的数组中
        for(int idx=0; idx<elcAmountDataList.size(); idx++){
            Map<String, Object> item = elcAmountDataList.get(idx);
            feeDayXAxis.add(item.get("billYear")+"-"+item.get("billMonth"));
            elcFeeYAxis.add(item.get("amount"));
        }
        for(int idx=0; idx<waterAmountDataList.size(); idx++){
            Map<String, Object> item = waterAmountDataList.get(idx);
            waterFeeYAxis2.add(item.get("amount"));
        }
        JSONArray amountArray = new JSONArray();
        amountArray.add(elcFeeYAxis);
        amountArray.add(waterFeeYAxis2);
        JSONObject amountJsonData = new JSONObject();
        amountJsonData.put("dayXAxis", feeDayXAxis);
        amountJsonData.put("dayYAxis", amountArray);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usage", usageData);
        jsonObject.put("fee", amountJsonData);
        result.setData(jsonObject);
        return result;
    }
}
