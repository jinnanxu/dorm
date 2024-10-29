package com.dorm.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.dorm.author.AuthRequired;
import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.*;
import com.dorm.dao.model.*;
import com.dorm.utils.HtmlFilterUtil;
import com.dorm.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 前台相关接口
 */
@RequestMapping("/front")
@RestController
public class FrontApi extends ApiBaseController{

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private FeeMapper feeMapper;
    @Autowired
    private OccupationMapper occupationMapper;
    @Autowired
    private MsgMapper msgMapper;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }


    /**
     * 查询列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("info/list")
    public JsonResult noticeList(PageView<Notice> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        page.setMaxresult(5);
        List<Notice> list = noticeMapper.selectNoticeList(page.getFirstResult(), 5);
        for (Notice notice:list){
            notice.setContent(HtmlFilterUtil.Html2Text(notice.getContent()));
        }
        page.setRecords(list);
        page.setTotalrecord(noticeMapper.selectCount(new Notice()));
        result.setData(page);
        return result;
    }

    /**
     * 通知详情
     * @param infoId
     * @return
     */
    @RequestMapping("info/detail")
    public JsonResult detail(Integer infoId) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        try {
            Notice notice = noticeMapper.selectByPrimaryKey(infoId);
            result.setData(notice);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 首页数据
     * @return
     */
    @RequestMapping("index/info")
    public JsonResult indexInfo(HttpServletRequest request) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        //宿管联系方式：
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);
        criteria.andEqualTo("role", 2);
        List<Admin> list = adminMapper.selectByExample(example);
        for(Admin admin:list){
            admin.setBuilding(buildingMapper.selectByPrimaryKey(admin.getBuildingId()));
        }
        JSONObject indexData = new JSONObject();
        indexData.put("contact", list);//宿管联系方式
        if(request.getAttribute("loginUser")!=null){
            Student student = (Student) request.getAttribute("loginUser");
            //催缴通知
            Msg msg = new Msg();
            msg.setStudentId(student.getStudentId());
            msg.setIsRead(0);//首页只显示未读信息
            List<Msg> msgList = msgMapper.select(msg);
            indexData.put("msg", msgList);
            result.setData(indexData);
        }
        return result;
    }

    @RequestMapping("index/read/msg")
    @AuthRequired
    public JsonResult readMessage(Integer msgId) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        Msg msg = msgMapper.selectByPrimaryKey(msgId);
        msg.setIsRead(1);
        msgMapper.updateByPrimaryKeySelective(msg);
        return result;
    }

    /**
     * 待交费账单
     * @param request
     * @return
     */
    @RequestMapping("index/wait/pay")
    @AuthRequired
    public JsonResult waitPayInfo(HttpServletRequest request) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        if(request.getAttribute("loginUser")==null){
            return result;
        }
        Student student = (Student) request.getAttribute("loginUser");
        List<Occupation> studentOcc = occupationMapper.selectExistOccupation(student.getStudentId());
        Occupation occ = null;
        if(studentOcc!=null&&studentOcc.size()>0){
            occ = studentOcc.get(0);
        }
        int count = feeMapper.selectMyWaitPayFeeCount(occ.getDormId());
        System.out.println("待支付账单："+count);
        result.setData(count);
        return result;
    }
}
