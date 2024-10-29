package com.dorm.dao.model;

import javax.persistence.*;

public class Dormitory {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "dormitory_id")
    private Integer dormitoryId;

    /**
     * 楼宇ID
     */
    @Column(name = "building_id")
    private Integer buildingId;

    @Transient
    private Building building;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 宿舍号
     */
    @Column(name = "room_no")
    private String roomNo;

    /**
     * 最大可住人数
     */
    @Column(name = "max_capacity")
    private Integer maxCapacity;

    /**
     * 已入住人数
     */
    private Integer occupied;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 获取物理主键
     *
     * @return dormitory_id - 物理主键
     */
    public Integer getDormitoryId() {
        return dormitoryId;
    }

    /**
     * 设置物理主键
     *
     * @param dormitoryId 物理主键
     */
    public void setDormitoryId(Integer dormitoryId) {
        this.dormitoryId = dormitoryId;
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

    /**
     * 获取楼层
     *
     * @return floor - 楼层
     */
    public String getFloor() {
        return floor;
    }

    /**
     * 设置楼层
     *
     * @param floor 楼层
     */
    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    /**
     * 获取宿舍号
     *
     * @return room_no - 宿舍号
     */
    public String getRoomNo() {
        return roomNo;
    }

    /**
     * 设置宿舍号
     *
     * @param roomNo 宿舍号
     */
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo == null ? null : roomNo.trim();
    }

    /**
     * 获取最大可住人数
     *
     * @return max_capacity - 最大可住人数
     */
    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * 设置最大可住人数
     *
     * @param maxCapacity 最大可住人数
     */
    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * 获取已入住人数
     *
     * @return occupied - 已入住人数
     */
    public Integer getOccupied() {
        return occupied;
    }

    /**
     * 设置已入住人数
     *
     * @param occupied 已入住人数
     */
    public void setOccupied(Integer occupied) {
        this.occupied = occupied;
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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}