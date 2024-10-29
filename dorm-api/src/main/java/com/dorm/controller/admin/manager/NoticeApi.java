package com.dorm.controller.admin.manager;

import com.dorm.controller.vo.JsonResult;
import com.dorm.dao.mapper.NoticeMapper;
import com.dorm.dao.model.Notice;
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
import tk.mybatis.mapper.entity.Example.Criteria;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 后台管理api：通知公告管理
 * @author xujinnan
 *
 */
@RequestMapping("/manager/notice")
@RestController
public class NoticeApi {

    @Autowired
    private NoticeMapper noticeMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    public JsonResult noticeList(PageView<Notice> page, String kw) {
        JsonResult result = new JsonResult(true, "0", "查询成功");
        Example example = new Example(Notice.class);
        Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("title", "%"+kw+"%");
        }
        example.orderBy("publishTime").desc();
        List<Notice> list = noticeMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(list);
        page.setTotalrecord(noticeMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    @RequestMapping("save")
    public JsonResult saveOrUpdateInfo(Notice info) {
        JsonResult result = new JsonResult(true, "0", "保存成功");
        try {
            if(info.getNoticeId()==null){
                info.setPublishTime(new Date());
                noticeMapper.insertSelective(info);
            }else{
                noticeMapper.updateByPrimaryKeySelective(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("保存失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除通知公告项目
     * @param infoId
     * @return
     */
    @RequestMapping("delete")
    public JsonResult delete(Integer infoId) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        try {
            noticeMapper.deleteByPrimaryKey(infoId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查询详情
     * @param infoId
     * @return
     */
    @RequestMapping("detail")
    public JsonResult detail(Integer infoId) {
        JsonResult result = new JsonResult(true, "0", "操作成功");
        try {
            Notice info = noticeMapper.selectByPrimaryKey(infoId);
            result.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }

}
