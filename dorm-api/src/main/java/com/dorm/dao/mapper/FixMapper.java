package com.dorm.dao.mapper;

import com.dorm.dao.model.Fix;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface FixMapper extends Mapper<Fix> {

    List<Fix> selectFixList(@Param("buildingId") Integer buildingId, @Param("status") Integer status, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectFixCount(@Param("buildingId") Integer buildingId, @Param("status") Integer status);

    List<Fix> selectMyFixList(@Param("studentId") Integer studentId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectMyFixCount(@Param("studentId") Integer studentId);

    Integer selectWaitFixCount(@Param("buildingId") Integer buildingId);
}