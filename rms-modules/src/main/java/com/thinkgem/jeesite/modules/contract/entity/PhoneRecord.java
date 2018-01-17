package com.thinkgem.jeesite.modules.contract.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.Date;

public class PhoneRecord extends DataEntity<PhoneRecord> {

    private static final long serialVersionUID = -3863546511822805338L;

    private String projectId;
    private String buildingId;
    private String houseId;
    private String roomId;
    private Integer flatsTag;
    private String aliUserId;
    private String zhimaOpenId;
    private String roomCode;
    private Date recordTime;

    private String projectName;
    private String buildingName;
    private String houseCode;
    private String roomNo;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getFlatsTag() {
        return flatsTag;
    }

    public void setFlatsTag(Integer flatsTag) {
        this.flatsTag = flatsTag;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    public String getZhimaOpenId() {
        return zhimaOpenId;
    }

    public void setZhimaOpenId(String zhimaOpenId) {
        this.zhimaOpenId = zhimaOpenId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
