package com.dorm.dao.model;

import com.dorm.dao.mapper.BuildingMapper;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "leave_info")
public class LeaveInfo {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "leave_id")
    private Integer leaveId;

    /**
     * 学生ID
     */
    @Column(name = "student_id")
    private Integer studentId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 1离校、2留校
     */
    @Column(name = "leave_type")
    private Integer leaveType;

    /**
     * 离校、留校时间
     */
    @Column(name = "leave_time")
    private String leaveTime;

    /**
     * 原因
     */
    private String reason;

    /**
     * 请假单照片
     */
    private String pics;

    @Transient
    private List<String> picList;

    /**
     * 宿舍ID
     */
    @Column(name = "dorm_id")
    private Integer dormId;

    @Transient
    private Dormitory dorm;

    /**
     * 楼栋ID
     */
    @Column(name = "building_id")
    private Integer buildingId;

    @Transient
    private Building building;

    /**
     * 获取物理主键
     *
     * @return leave_id - 物理主键
     */
    public Integer getLeaveId() {
        return leaveId;
    }

    /**
     * 设置物理主键
     *
     * @param leaveId 物理主键
     */
    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    /**
     * 获取学生ID
     *
     * @return student_id - 学生ID
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * 设置学生ID
     *
     * @param studentId 学生ID
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取1离校、2留校
     *
     * @return leave_type - 1离校、2留校
     */
    public Integer getLeaveType() {
        return leaveType;
    }

    /**
     * 设置1离校、2留校
     *
     * @param leaveType 1离校、2留校
     */
    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    /**
     * 获取离校、留校时间
     *
     * @return leave_time - 离校、留校时间
     */
    public String getLeaveTime() {
        return leaveTime;
    }

    /**
     * 设置离校、留校时间
     *
     * @param leaveTime 离校、留校时间
     */
    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime == null ? null : leaveTime.trim();
    }

    /**
     * 获取原因
     *
     * @return reason - 原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置原因
     *
     * @param reason 原因
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * 获取请假单照片
     *
     * @return pics - 请假单照片
     */
    public String getPics() {
        return pics;
    }

    /**
     * 设置请假单照片
     *
     * @param pics 请假单照片
     */
    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    /**
     * 获取宿舍ID
     *
     * @return dorm_id - 宿舍ID
     */
    public Integer getDormId() {
        return dormId;
    }

    /**
     * 设置宿舍ID
     *
     * @param dormId 宿舍ID
     */
    public void setDormId(Integer dormId) {
        this.dormId = dormId;
    }

    /**
     * 获取楼栋ID
     *
     * @return building_id - 楼栋ID
     */
    public Integer getBuildingId() {
        return buildingId;
    }

    /**
     * 设置楼栋ID
     *
     * @param buildingId 楼栋ID
     */
    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Dormitory getDorm() {
        return dorm;
    }

    public void setDorm(Dormitory dorm) {
        this.dorm = dorm;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}