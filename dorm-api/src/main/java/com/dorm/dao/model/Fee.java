package com.dorm.dao.model;

import javax.persistence.*;
import java.util.Date;

public class Fee {

    /**
     * 物理主键
     */
    @Id
    @Column(name = "fee_id")
    private Integer feeId;

    /**
     * 费用类型，1为电费，2为水费
     */
    @Column(name = "fee_type")
    private Integer feeType;

    /**
     * 账单所属年份
     */
    @Column(name = "bill_year")
    private Integer billYear;

    /**
     * 账单所属月份
     */
    @Column(name = "bill_month")
    private Integer billMonth;

    /**
     * 水电用量，电费为度，水费为吨
     */
    private Double usages;

    /**
     * 单价
     */
    @Column(name = "unit_price")
    private Double unitPrice;

    /**
     * 总费用
     */
    private Double amount;

    /**
     * 宿舍ID
     */
    @Column(name = "dorm_id")
    private Integer dormId;

    @Column(name = "building_id")
    private Integer buildingId;

    @Transient
    private Building building;

    @Transient
    private Dormitory dorm;

    /**
     * 状态：0待缴费，1已缴费
     */
    private Integer status;

    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 获取物理主键
     *
     * @return fee_id - 物理主键
     */
    public Integer getFeeId() {
        return feeId;
    }

    /**
     * 设置物理主键
     *
     * @param feeId 物理主键
     */
    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    /**
     * 获取费用类型，1为电费，2为水费
     *
     * @return fee_type - 费用类型，1为电费，2为水费
     */
    public Integer getFeeType() {
        return feeType;
    }

    /**
     * 设置费用类型，1为电费，2为水费
     *
     * @param feeType 费用类型，1为电费，2为水费
     */
    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    /**
     * 获取账单所属年份
     *
     * @return bill_year - 账单所属年份
     */
    public Integer getBillYear() {
        return billYear;
    }

    /**
     * 设置账单所属年份
     *
     * @param billYear 账单所属年份
     */
    public void setBillYear(Integer billYear) {
        this.billYear = billYear;
    }

    /**
     * 获取账单所属月份
     *
     * @return bill_month - 账单所属月份
     */
    public Integer getBillMonth() {
        return billMonth;
    }

    /**
     * 设置账单所属月份
     *
     * @param billMonth 账单所属月份
     */
    public void setBillMonth(Integer billMonth) {
        this.billMonth = billMonth;
    }

    /**
     * 获取水电用量，电费为度，水费为吨
     *
     * @return usage - 水电用量，电费为度，水费为吨
     */
    public Double getUsages() {
        return usages;
    }

    /**
     * 设置水电用量，电费为度，水费为吨
     *
     * @param usages 水电用量，电费为度，水费为吨
     */
    public void setUsages(Double usages) {
        this.usages = usages;
    }

    /**
     * 获取单价
     *
     * @return unit_price - 单价
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    /**
     * 设置单价
     *
     * @param unitPrice 单价
     */
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 获取总费用
     *
     * @return amount - 总费用
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * 设置总费用
     *
     * @param amount 总费用
     */
    public void setAmount(Double amount) {
        this.amount = amount;
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
     * 获取状态：0待缴费，1已缴费
     *
     * @return status - 状态：0待缴费，1已缴费
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0待缴费，1已缴费
     *
     * @param status 状态：0待缴费，1已缴费
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Dormitory getDorm() {
        return dorm;
    }

    public void setDorm(Dormitory dorm) {
        this.dorm = dorm;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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