package com.dorm.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class Fix {
    @Id
    @Column(name = "fix_id")
    private Integer fixId;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "dorm_id")
    private Integer dormId;

    @Column(name = "building_id")
    private Integer buildingId;

    @Column(name = "worker_id")
    private Integer workerId;

    @Transient
    private Admin worker;

    @Transient
    private Building building;

    @Transient
    private Dormitory dorm;

    @Column(name = "create_time")
    private Date createTime;

    private String title;

    @Column(name = "fix_result")
    private String fixResult;

    @Column(name = "fixed_time")
    private Date fixedTime;

    @Column(name = "confirm_pic")
    private String confirmPic;

    private String pics;

    @Transient
    private List<String> picList;

    private String status;

    private String content;

    /**
     * @return fix_id
     */
    public Integer getFixId() {
        return fixId;
    }

    /**
     * @param fixId
     */
    public void setFixId(Integer fixId) {
        this.fixId = fixId;
    }

    /**
     * @return student_id
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * @return dorm_id
     */
    public Integer getDormId() {
        return dormId;
    }

    /**
     * @param dormId
     */
    public void setDormId(Integer dormId) {
        this.dormId = dormId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * @return fix_result
     */
    public String getFixResult() {
        return fixResult;
    }

    /**
     * @param fixResult
     */
    public void setFixResult(String fixResult) {
        this.fixResult = fixResult == null ? null : fixResult.trim();
    }

    /**
     * @return fixed_time
     */
    public Date getFixedTime() {
        return fixedTime;
    }

    /**
     * @param fixedTime
     */
    public void setFixedTime(Date fixedTime) {
        this.fixedTime = fixedTime;
    }

    /**
     * @return pics
     */
    public String getPics() {
        return pics;
    }

    /**
     * @param pics
     */
    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Dormitory getDorm() {
        return dorm;
    }

    public void setDorm(Dormitory dorm) {
        this.dorm = dorm;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
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

    public String getConfirmPic() {
        return confirmPic;
    }

    public void setConfirmPic(String confirmPic) {
        this.confirmPic = confirmPic;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Admin getWorker() {
        return worker;
    }

    public void setWorker(Admin worker) {
        this.worker = worker;
    }
}