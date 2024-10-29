package com.dorm.dao.mapper;

import com.dorm.dao.model.Notice;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NoticeMapper extends Mapper<Notice> {

    List<Notice> selectNoticeList(@Param("start") Integer start, @Param("end") Integer end);
}