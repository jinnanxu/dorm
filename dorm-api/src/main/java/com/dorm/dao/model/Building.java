package com.dorm.dao.model;

import javax.persistence.*;

public class Building {
    /**
     * 物理主键
     */
    @Id
    @Column(name = "building_id")
    private Integer buildingId;

    /**
     * 楼宇名称
     */
    @Column(name = "building_name")
    private String buildingName;

    /**
     * 位置
     */
    private String position;

    /**
     * 总楼层
     */
    private String floor;

    private Integer status;

    /**
     * 总宿舍数
     */
    @Column(name = "romms_count")
    private Integer rommsCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取物理主键
     *
     * @return building_id - 物理主键
     */
    public Integer getBuildingId() {
        return buildingId;
    }

    /**
     * 设置物理主键
     *
     * @param buildingId 物理主键
     */
    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    /**
     * 获取楼宇名称
     *
     * @return building_name - 楼宇名称
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * 设置楼宇名称
     *
     * @param buildingName 楼宇名称
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName == null ? null : buildingName.trim();
    }

    /**
     * 获取位置
     *
     * @return position - 位置
     */
    public String getPosition() {
        return position;
    }

    /**
     * 设置位置
     *
     * @param position 位置
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    /**
     * 获取总楼层
     *
     * @return floor - 总楼层
     */
    public String getFloor() {
        return floor;
    }

    /**
     * 设置总楼层
     *
     * @param floor 总楼层
     */
    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    /**
     * 获取总宿舍数
     *
     * @return romms_count - 总宿舍数
     */
    public Integer getRommsCount() {
        return rommsCount;
    }

    /**
     * 设置总宿舍数
     *
     * @param rommsCount 总宿舍数
     */
    public void setRommsCount(Integer rommsCount) {
        this.rommsCount = rommsCount;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}