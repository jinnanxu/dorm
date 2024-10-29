package com.dorm.dao.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import javax.persistence.*;

public class Student {

    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @ExcelProperty(value="学号",index = 0)
    @Column(name = "student_no")
    private String studentNo;

    @ExcelProperty(value="姓名",index = 1)
    @Column(name = "real_name")
    private String realName;

    @ExcelProperty(value="性别",index = 2)
    private String gender;

    @ExcelProperty(value="年级",index = 3)
    private String grade;

    @ExcelProperty(value="院系",index = 4)
    private String college;

    @ExcelProperty(value="专业班别",index = 5)
    @Column(name = "major_class")
    private String majorClass;

    @ExcelProperty(value="初始密码",index = 6)
    @Column(name = "pwd")
    private String pwd;

    @ExcelProperty(value="手机号",index = 7)
    private String mobile;

    private Integer status;

    @Transient
    private Occupation occ;

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
     * @return student_no
     */
    public String getStudentNo() {
        return studentNo;
    }

    /**
     * @param studentNo
     */
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo == null ? null : studentNo.trim();
    }

    /**
     * @return real_name
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @return college
     */
    public String getCollege() {
        return college;
    }

    /**
     * @param college
     */
    public void setCollege(String college) {
        this.college = college == null ? null : college.trim();
    }

    /**
     * @return major_class
     */
    public String getMajorClass() {
        return majorClass;
    }

    /**
     * @param majorClass
     */
    public void setMajorClass(String majorClass) {
        this.majorClass = majorClass == null ? null : majorClass.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Occupation getOcc() {
        return occ;
    }

    public void setOcc(Occupation occ) {
        this.occ = occ;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentNo='" + studentNo + '\'' +
                ", realName='" + realName + '\'' +
                ", gender='" + gender + '\'' +
                ", grade='" + grade + '\'' +
                ", college='" + college + '\'' +
                ", majorClass='" + majorClass + '\'' +
                ", pwd='" + pwd + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", occ=" + occ +
                '}';
    }
}