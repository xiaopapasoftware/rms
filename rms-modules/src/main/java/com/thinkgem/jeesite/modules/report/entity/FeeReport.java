package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.Date;

public class FeeReport extends DataEntity<FeeReport> {

    private static final long serialVersionUID = -5983971812959950028L;

    private String roomId;

    private String fullName;

    private String feeNo;

    private String feeType;

    private String smsRecord;

    private Double remainFee;

    private Date feeTime;

    private Date smsTime;

    private String rentContractId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFeeNo() {
        return feeNo;
    }

    public void setFeeNo(String feeNo) {
        this.feeNo = feeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getSmsRecord() {
        return smsRecord;
    }

    public void setSmsRecord(String smsRecord) {
        this.smsRecord = smsRecord;
    }

    public Double getRemainFee() {
        return remainFee;
    }

    public void setRemainFee(Double remainFee) {
        this.remainFee = remainFee;
    }

    public Date getFeeTime() {
        return feeTime;
    }

    public void setFeeTime(Date feeTime) {
        this.feeTime = feeTime;
    }

    public Date getSmsTime() {
        return smsTime;
    }

    public void setSmsTime(Date smsTime) {
        this.smsTime = smsTime;
    }

    public String getRentContractId() {
        return rentContractId;
    }

    public void setRentContractId(String rentContractId) {
        this.rentContractId = rentContractId;
    }
}
