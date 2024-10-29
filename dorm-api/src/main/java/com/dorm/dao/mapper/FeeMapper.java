package com.dorm.dao.mapper;

import com.dorm.dao.model.Fee;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface FeeMapper extends Mapper<Fee> {

    List<Fee> selectFeeList(@Param("feeType") Integer feeType, @Param("buildingId") Integer buildingId, @Param("dormId") Integer dormId, @Param("status") Integer status,
                            @Param("billYear0") Integer billYear0, @Param("billMonth0") Integer billMonth0,
                            @Param("billYear1") Integer billYear1, @Param("billMonth1") Integer billMonth1,
                            @Param("start") Integer start, @Param("end") Integer end);

    Integer selectFeeCount(@Param("feeType") Integer feeType, @Param("buildingId") Integer buildingId, @Param("dormId") Integer dormId, @Param("status") Integer status,
                           @Param("billYear0") Integer billYear0, @Param("billMonth0") Integer billMonth0,
                           @Param("billYear1") Integer billYear1, @Param("billMonth1") Integer billMonth1);

    List<Fee> selectMyFeeList(@Param("feeType") Integer feeType, @Param("dormId") Integer dormId,
                            @Param("billYear0") Integer billYear0, @Param("billMonth0") Integer billMonth0,
                            @Param("billYear1") Integer billYear1, @Param("billMonth1") Integer billMonth1,
                            @Param("start") Integer start, @Param("end") Integer end);

    Integer selectMyFeeCount(@Param("feeType") Integer feeType, @Param("dormId") Integer dormId,
                           @Param("billYear0") Integer billYear0, @Param("billMonth0") Integer billMonth0,
                           @Param("billYear1") Integer billYear1, @Param("billMonth1") Integer billMonth1);

    Integer selectMyWaitPayFeeCount(@Param("dormId") Integer dormId);

    /**
     * 所有某楼栋未缴费的总数
     * @param buildingId
     * @return
     */
    Integer selectAllWaitPayFeeCount(@Param("buildingId") Integer buildingId);

    List<Map<String, Object>> staticsFeeByBuilding(@Param("feeType") Integer feeType, @Param("buildingId") Integer buildingId,
                                             @Param("billYear0") Integer billYear0, @Param("billMonth0") Integer billMonth0,
                                             @Param("billYear1") Integer billYear1, @Param("billMonth1") Integer billMonth1);

    /**
     * 水电费金额
     * @param feeType
     * @param dormId
     * @return
     */
    List<Map<String, Object>> staticsFeeByDorm(@Param("feeType") Integer feeType, @Param("dormId") Integer dormId);

    /**
     * 水电费用量
     * @param feeType
     * @param dormId
     * @return
     */
    List<Map<String, Object>> staticsUsageByDorm(@Param("feeType") Integer feeType, @Param("dormId") Integer dormId);
}