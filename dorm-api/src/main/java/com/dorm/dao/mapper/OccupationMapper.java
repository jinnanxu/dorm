package com.dorm.dao.mapper;

import com.dorm.dao.model.Occupation;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OccupationMapper extends Mapper<Occupation> {

    List<Occupation> selectOccupation(@Param("buildingId") Integer buildingId, @Param("dormId") Integer dormId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectOccupationCount(@Param("buildingId") Integer buildingId, @Param("dormId") Integer dormId);

    List<Occupation> selectExistOccupation(@Param("studentId") Integer studentId);

    Integer selectCheckInCount(@Param("buildingId") Integer buildingId, @Param("d1") Date d1, @Param("d2") Date d2);

    Integer selectCheckOutCount(@Param("buildingId") Integer buildingId, @Param("d1") Date d1, @Param("d2") Date d2);

    List<Map<String, Object>> selectBuildingOccCount();
}