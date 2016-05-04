package com.thinkgem.jeesite.modules.lock.entity;

import java.util.Date;

/**
 * Created by mabindong on 2016/3/5.
 */
public class ScienerKey {

    private String lockId;
    private String date;
    private String keyStatus;
    private String keyId;
    private Date startDate;
    private Date endDate;
    private String username;
    private String room_id;
    private String lock_alias;
    private String lock_name;
    private String lock_mac;
    private String remark;
    private String openid;
    private String keyType;

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeyStatus() {
        return keyStatus;
    }

    public void setKeyStatus(String keyStatus) {
        this.keyStatus = keyStatus;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getLock_alias() {
        return lock_alias;
    }

    public void setLock_alias(String lock_alias) {
        this.lock_alias = lock_alias;
    }

    public String getLock_name() {
        return lock_name;
    }

    public void setLock_name(String lock_name) {
        this.lock_name = lock_name;
    }

    public String getLock_mac() {
        return lock_mac;
    }

    public void setLock_mac(String lock_mac) {
        this.lock_mac = lock_mac;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "ScienerKey{" +
                "lockId='" + lockId + '\'' +
                ", date='" + date + '\'' +
                ", keyStatus='" + keyStatus + '\'' +
                ", keyId='" + keyId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", username='" + username + '\'' +
                ", room_id='" + room_id + '\'' +
                ", lock_alias='" + lock_alias + '\'' +
                ", lock_name='" + lock_name + '\'' +
                ", lock_mac='" + lock_mac + '\'' +
                ", remark='" + remark + '\'' +
                ", openid='" + openid + '\'' +
                '}';
    }
}
