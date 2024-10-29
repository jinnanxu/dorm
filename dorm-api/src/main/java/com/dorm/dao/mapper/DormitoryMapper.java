package com.dorm.dao.mapper;

import com.dorm.dao.model.Dormitory;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface DormitoryMapper extends Mapper<Dormitory> {

    Integer selectAvDormitory(@Param("buildingId") Integer buildingId);

    Integer selectTotalDormitory(@Param("buildingId") Integer buildingId);
}