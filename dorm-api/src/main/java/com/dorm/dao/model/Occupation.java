package com.dorm.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Occupation {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "occupation_id")
    private Integer occupationId;

    /**
     * 学生ID
     */
    @Column(name = "student_id")
    private Integer studentId;

    @Transient
    private Student student;

    /**
     * 操作人ID
     */
    @Column(name = "operator_id")
    private Integer operatorId;

    /**
     * 入住时间
     */
    @Column(name = "check_in_time")
    private Date checkInTime;

    /**
     * 搬离时间
     */
    @Column(name = "check_out_time")
    private Date checkOutTime;

    /**
     * 当前状态
     */
    @Column(name = "curr_status")
    private Integer currStatus;

    /**
     * 宿舍ID
     */
    @Column(name = "dorm_id")
    private Integer dormId;

    @Transient
    private Dormitory dorm;

    /**
     * 备注
     */
    private String remark;

    /**
     * 楼宇ID
     */
    @Column(name = "building_id")
    private Integer buildingId;

    @Transient
    private Building building;

    /**
     * 获取物理主键
     *
     * @return occupation_id - 物理主键
     */
    public Integer getOccupationId() {
        return occupationId;
    }

    /**
     * 设置物理主键
     *
     * @param occupationId 物理主键
     */
    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
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
     * 获取操作人ID
     *
     * @return operator_id - 操作人ID
     */
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作人ID
     *
     * @param operatorId 操作人ID
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 获取入住时间
     *
     * @return check_in_time - 入住时间
     */
    public Date getCheckInTime() {
        return checkInTime;
    }

    /**
     * 设置入住时间
     *
     * @param checkInTime 入住时间
     */
    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    /**
     * 获取搬离时间
     *
     * @return check_out_time - 搬离时间
     */
    public Date getCheckOutTime() {
        return checkOutTime;
    }

    /**
     * 设置搬离时间
     *
     * @param checkOutTime 搬离时间
     */
    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    /**
     * 获取当前状态
     *
     * @return curr_status - 当前状态
     */
    public Integer getCurrStatus() {
        return currStatus;
    }

    /**
     * 设置当前状态
     *
     * @param currStatus 当前状态
     */
    public void setCurrStatus(Integer currStatus) {
        this.currStatus = currStatus;
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
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取楼宇ID
     *
     * @return building_id - 楼宇ID
     */
    public Integer getBuildingId() {
        return buildingId;
    }

    /**
     * 设置楼宇ID
     *
     * @param buildingId 楼宇ID
     */
    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Dormitory getDorm() {
        return dorm;
    }

    public void setDorm(Dormitory dorm) {
        this.dorm = dorm;
    }
}