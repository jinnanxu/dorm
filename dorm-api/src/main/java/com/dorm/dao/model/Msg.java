package com.dorm.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Msg {
    @Id
    @Column(name = "msg_id")
    private Integer msgId;

    private String title;

    @Column(name = "student_id")
    private Integer studentId;

    private String content;

    @Column(name = "msg_time")
    private Date msgTime;

    @Column(name = "is_read")
    private Integer isRead;

    @Column(name = "fee_id")
    private Integer feeId;

    /**
     * @return msg_id
     */
    public Integer getMsgId() {
        return msgId;
    }

    /**
     * @param msgId
     */
    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
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

    /**
     * @return msg_time
     */
    public Date getMsgTime() {
        return msgTime;
    }

    /**
     * @param msgTime
     */
    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    /**
     * @return is_read
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * @param isRead
     */
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    /**
     * @return fee_id
     */
    public Integer getFeeId() {
        return feeId;
    }

    /**
     * @param feeId
     */
    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }
}