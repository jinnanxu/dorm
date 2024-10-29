package com.dorm.controller.admin.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.*;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.Occupation;
import com.dorm.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/admin")
@RestController
public class StaticsApi {

    @Autowired
    private FeeMapper feeMapper;

    @Autowired
    private OccupationMapper occMapper;
    @Autowired
    private FixMapper fixMapper;
    @Autowired
    private DormitoryMapper dormMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private LeaveInfoMapper leaveInfoMapper;

    /**
     * 统计报表数据
     * @return
     */
    @RequestMapping("statics/data")
    public JsonResult statics(Integer buildingId, String dateRangeStr) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        String d1 = dateRangeStr.split(",")[0];
        String d2 = dateRangeStr.split(",")[1];
        String[] dateRange = new String[]{d1, d2};
        //饼图：[{ value: 1048, name: 'Search Engine' },{},...]
        //柱状图：{[150, 230, 224, 218, 135, 147, 260],[150, 230, 224, 218, 135, 147, 260]}, ['01-01', '01-02', '01-03', '01-04', '01-05', '01-06', '01-07']

        //先查迁入迁出的饼图
        JSONObject jsonObject = new JSONObject();
        //处理饼图数据 [{ value: 1048, name: 'Search Engine' },{},...]
        JSONArray pieData = new JSONArray();
        String date1Str = d1+"-01";
        String month2Str = "";
        int pieMont2 = Integer.valueOf(dateRange[1].split("-")[1]);
        pieMont2+=1;
        if(pieMont2<10){
            month2Str = "0"+pieMont2;
        }
        String date2Str = dateRange[1].split("-")[0]+"-"+month2Str+"-01";
        Date date1 = DateUtils.parseStandardDate(date1Str);
        Date date2 = DateUtils.parseStandardDate(date2Str);
        int checkInCount = occMapper.selectCheckInCount(buildingId, date1, date2);
        int checkOutCount = occMapper.selectCheckOutCount(buildingId, date1, date2);
        //饼图不需要排序
        JSONObject pieItem = new JSONObject();
        pieItem.put("name", "迁入");
        pieItem.put("value", checkInCount);
        JSONObject pieItem2 = new JSONObject();
        pieItem2.put("name", "迁出");
        pieItem2.put("value", checkOutCount);
        pieData.add(pieItem);
        pieData.add(pieItem2);

        jsonObject.put("occ", pieData);

        Integer year0=null, year1=null;
        Integer month0=null, month1=null;
        if(dateRange != null && dateRange.length > 0){
            year0 = Integer.valueOf(dateRange[0].split("-")[0]);
            year1 = Integer.valueOf(dateRange[1].split("-")[0]);
            month0 = Integer.valueOf(dateRange[0].split("-")[1]);
            month1 = Integer.valueOf(dateRange[1].split("-")[1]);
        }
        //水电费柱状图
        //水电分别的数值：{[150, 230, 224, 218, 135, 147, 260],[150, 230, 224, 218, 135, 147, 260]}
        //对应的X轴日期：['2024-01', '2024-02', '2024-03']
        List<Map<String, Object>> elcFeeDataList = feeMapper.staticsFeeByBuilding(1, buildingId, year0, month0, year1, month1);
        List<Map<String, Object>> waterFeeDataList = feeMapper.staticsFeeByBuilding(2, buildingId, year0, month0, year1, month1);
        //从数据查询时是以月份进行分组，并按升序做好排序
        JSONObject dayChartData = new JSONObject();
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
        dayChartData.put("dayXAxis", dayXAxis);
        dayChartData.put("dayYAxis", usages);

        jsonObject.put("fee", dayChartData);
        result.setData(jsonObject);
        return result;
    }

    @RequestMapping("index/data")
    @AuthRequired
    public JsonResult indexData(HttpServletRequest request) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        Admin manager = (Admin) request.getAttribute("loginAdmin");
        if(manager.getBuildingId()==null){
            return result;
        }
        //待处理维修
        int waitFix = fixMapper.selectWaitFixCount(manager.getBuildingId());
        //待收水电费
        int waitFee = feeMapper.selectAllWaitPayFeeCount(manager.getBuildingId());
        //总宿舍/空宿舍
        int totalDorm = dormMapper.selectTotalDormitory(manager.getBuildingId());
        int avDorm = dormMapper.selectAvDormitory(manager.getBuildingId());

        //各个宿舍楼入住人数、空床位数
        List<Map<String, Object>> buildingStudent = occMapper.selectBuildingOccCount();
        JSONArray buildingOccData = new JSONArray();
        for(Map<String, Object> building:buildingStudent){
            JSONObject newStudent = new JSONObject();
            newStudent.put("building", building.get("buildingName"));
            newStudent.put("total", building.get("total"));
            buildingOccData.add(newStudent);
        }
        System.out.println(buildingOccData.toJSONString());

        //各学院新生数量，按年级查询
        List<Map<String, Object>> newStudents = studentMapper.selectNewStudentCount();
        JSONArray newStudentKey = new JSONArray();
        JSONArray newStudentData = new JSONArray();
        for(Map<String, Object> grade:newStudents){
            newStudentKey.add(grade.get("college"));
            newStudentData.add(grade.get("total"));
        }

        //处理饼图数据 [{ value: 1048, name: 'Search Engine' },{},...]
        JSONArray pieData = new JSONArray();
        //饼图不需要排序
        List<Map<String, Object>> leaveData = leaveInfoMapper.selectLeaveStatistic();
        for(Map<String, Object> leave:leaveData){
            JSONObject pieItem = new JSONObject();
            pieItem.put("name", leave.get("reason"));
            pieItem.put("value", leave.get("total"));
            pieData.add(pieItem);
        }

        JSONObject data = new JSONObject();
        data.put("waitFix", waitFix);
        data.put("waitFee", waitFee);
        data.put("totalDorm", totalDorm);
        data.put("avDorm", avDorm);
        data.put("leaveData", pieData);
        data.put("newStudentKey", newStudentKey);
        data.put("newStudentData", newStudentData);
        data.put("buildingOccData", buildingOccData);

        result.setData(data);
        return result;
    }
}
