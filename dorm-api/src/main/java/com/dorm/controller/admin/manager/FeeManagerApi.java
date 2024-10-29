package com.dorm.controller.admin.manager;

import com.alibaba.fastjson.JSONObject;
import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.*;
import com.dorm.dao.model.Admin;
import com.dorm.dao.model.Fee;
import com.dorm.dao.model.Msg;
import com.dorm.dao.model.Occupation;
import com.dorm.service.QuestionService;
import com.dorm.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
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

/**
 * 水电费管理
 */
@RequestMapping("/manager/fee")
@RestController
public class FeeManagerApi {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private FeeMapper feeMapper;
    @Autowired
    private DormitoryMapper dormitoryMapper;
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private OccupationMapper occMapper;
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
     * 查询水电费列表
     * @param page
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult feeList(HttpServletRequest request, PageView<Fee> page, Integer feeType, Integer buildingId, Integer dormId, Integer status, String[] dateRange) {
        Admin expert = (Admin) request.getAttribute("loginAdmin");
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Integer year0=null, year1=null;
        Integer month0=null, month1=null;
        if(dateRange != null && dateRange.length > 0){
            year0 = Integer.valueOf(dateRange[0].split("-")[0]);
            year1 = Integer.valueOf(dateRange[1].split("-")[0]);
            month0 = Integer.valueOf(dateRange[0].split("-")[1]);
            month1 = Integer.valueOf(dateRange[1].split("-")[1]);
        }
        //根据条件查询水电费
        List<Fee> list = feeMapper.selectFeeList(feeType, buildingId, dormId, status,
                year0, month0, year1, month1, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(feeMapper.selectFeeCount(feeType, buildingId, dormId, status, year0, month0, year1, month1));
        result.setData(page);
        return result;
    }

    @RequestMapping("save")
    @AuthRequired
    public JsonResult saveFee(HttpServletRequest request, Fee fee) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        try {
            feeMapper.insertSelective(fee);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 更新账单状态（管理员可以线下收费）
     * @return
     */
    @RequestMapping("update/status")
    @AuthRequired
    public JsonResult updateStatus(HttpServletRequest request, Integer feeId, Integer status) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        try {
            Fee fee = feeMapper.selectByPrimaryKey(feeId);
            fee.setStatus(status);
            fee.setPayTime(new Date());
            feeMapper.updateByPrimaryKeySelective(fee);
            if(status==1){
                //将催缴记录设置为过期，不再显示
                Msg msg = new Msg();
                msg.setFeeId(fee.getFeeId());
                List<Msg> list = msgMapper.select(msg);
                for(Msg m:list){
                    m.setIsRead(1);
                    msgMapper.updateByPrimaryKeySelective(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("send/message")
    @AuthRequired
    public JsonResult sendMessage(Integer feeId) {
        JsonResult result = new JsonResult(true, "0", "发送成功");
        try {
            Fee fee = feeMapper.selectByPrimaryKey(feeId);
            //生成催缴信息给这个宿舍的所有学生
            String title = "";
            StringBuilder sb = new StringBuilder();
            sb.append("同学您好，"+fee.getBillMonth()+"月的");
            if(fee.getFeeType()==1){
                sb.append("电费账单已经生成，使用度量为").append(fee.getUsages()).append("度，");
                title = fee.getBillMonth()+"月电费催缴通知";
            }else {
                sb.append("水费账单已经生成，使用度量为").append(fee.getUsages()).append("吨，");
                title = fee.getBillMonth()+"月水费催缴通知";
            }
            sb.append("合计").append(fee.getAmount()).append("元，请及时缴纳！");
            Occupation occ = new Occupation();
            occ.setDormId(fee.getDormId());
            occ.setCurrStatus(1);
            List<Occupation> occList = occMapper.select(occ);
            for(Occupation occupation:occList){
                Msg msg = new Msg();
                msg.setContent(sb.toString());
                msg.setTitle(title);
                msg.setIsRead(0);
                msg.setStudentId(occupation.getStudentId());
                msg.setMsgTime(new Date());
                msg.setFeeId(fee.getFeeId());
                msgMapper.insertSelective(msg);
            }
        }catch (Exception e) {
            e.printStackTrace();
            result.setMsg("发送失败");
            result.setSuccess(false);
        }
        return result;
    }
}
