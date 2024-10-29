package com.dorm.dao.mapper;

import com.dorm.dao.model.LeaveInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface LeaveInfoMapper extends Mapper<LeaveInfo> {

    List<LeaveInfo> selectMyLeaveList(@Param("studentId") Integer studentId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectMyLeaveCount(@Param("studentId") Integer studentId);

    List<LeaveInfo> selectLeaveList(@Param("buildingId") Integer buildingId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectLeaveCount(@Param("buildingId") Integer buildingId);

    List<Map<String, Object>> selectLeaveStatistic();

}