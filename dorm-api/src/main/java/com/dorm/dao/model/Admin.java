package com.dorm.dao.model;

import javax.persistence.*;

public class Admin {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "admin_id")
    private Integer adminId;

    /**
     * 职工号
     */
    @Column(name = "employee_no")
    private String employeeNo;

    /**
     * 姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 性别
     */
    private String gender;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 状态
     */
    private Integer status;

    @Column(name = "building_id")
    private Integer buildingId;

    @Transient
    private Building building;

    /**
     * 角色：1为系统管理员，2为宿舍管理员
     */
    private Integer role;

    /**
     * 获取物理主键
     *
     * @return admin_id - 物理主键
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置物理主键
     *
     * @param adminId 物理主键
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 获取职工号
     *
     * @return employee_no - 职工号
     */
    public String getEmployeeNo() {
        return employeeNo;
    }

    /**
     * 设置职工号
     *
     * @param employeeNo 职工号
     */
    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo == null ? null : employeeNo.trim();
    }

    /**
     * 获取姓名
     *
     * @return real_name - 姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置姓名
     *
     * @param realName 姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取性别
     *
     * @return gender - 性别
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    /**
     * 获取密码
     *
     * @return pwd - 密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置密码
     *
     * @param pwd 密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取角色：1为系统管理员，2为宿舍管理员
     *
     * @return role - 角色：1为系统管理员，2为宿舍管理员
     */
    public Integer getRole() {
        return role;
    }

    /**
     * 设置角色：1为系统管理员，2为宿舍管理员
     *
     * @param role 角色：1为系统管理员，2为宿舍管理员
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}