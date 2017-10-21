/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.Date;

/**
 * <p>抄电表流水实体类</p>
 * <p>Table: fee_ele_read_flow - --> FeeEleReadFlow</p>
 * <p>抄电表流水</p>
 * @since 2017-09-18 08:24:39
 */
public class FeeEleReadFlow extends DataEntity<FeeEleReadFlow>{

    /** business_id - 关联业务ID */
    private String businessId;
    /** from_source - 来源 0: 抄表1: 账单 */
    private Integer fromSource;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** room_id - 房号 */
    private String roomId;
    /** house_ele_num - 户号 */
    private String houseEleNum;
    /** ele_read_date - 抄表日期 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date eleReadDate;
    /** ele_degree - 电表额度 */
    private Float eleDegree;
    /** ele_peak_degree - 峰值数 */
    private Float elePeakDegree;
    /** ele_valley_degree - 谷值数 */
    private Float eleValleyDegree;

    public String getBusinessId(){
        return this.businessId;
    }
    public void setBusinessId(String businessId){
        this.businessId = businessId;
    }
    public Integer getFromSource(){
        return this.fromSource;
    }
    public void setFromSource(Integer fromSource){
        this.fromSource = fromSource;
    }
    public String getPropertyId(){
        return this.propertyId;
    }
    public void setPropertyId(String propertyId){
        this.propertyId = propertyId;
    }
    public String getHouseId(){
        return this.houseId;
    }
    public void setHouseId(String houseId){
        this.houseId = houseId;
    }
    public String getRoomId(){
        return this.roomId;
    }
    public void setRoomId(String roomId){
        this.roomId = roomId;
    }
    public String getHouseEleNum(){
        return this.houseEleNum;
    }
    public void setHouseEleNum(String houseEleNum){
        this.houseEleNum = houseEleNum;
    }
    public Date getEleReadDate(){
        return this.eleReadDate;
    }
    public void setEleReadDate(Date eleReadDate){
        this.eleReadDate = eleReadDate;
    }
    public Float getEleDegree(){
        return this.eleDegree;
    }
    public void setEleDegree(Float eleDegree){
        this.eleDegree = eleDegree;
    }
    public Float getElePeakDegree(){
        return this.elePeakDegree;
    }
    public void setElePeakDegree(Float elePeakDegree){
        this.elePeakDegree = elePeakDegree;
    }
    public Float getEleValleyDegree(){
        return this.eleValleyDegree;
    }
    public void setEleValleyDegree(Float eleValleyDegree){
        this.eleValleyDegree = eleValleyDegree;
    }
}