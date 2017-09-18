/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>电费收取流水实体类</p>
 * <p>Table: fee_ele_charged_flow - --> FeeEleChargedFlow</p>
 * <p>电费收取流水</p>
 * @since 2017-09-18 08:24:32
 */
public class FeeEleChargedFlow extends DataEntity<FeeEleChargedFlow>{

    /** business_id - 关联业务ID */
    private String businessId;
    /** from_source - 来源 0: 抄表1: 账单2:固定模式 */
    private Integer fromSource;
    /** order_no - 订单号 */
    private String orderNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** room_id - 房号 */
    private String roomId;
    /** rent_type - 出租类型 0：整租 1：合租 */
    private Integer rentType;
    /** house_ele_num - 户号 */
    private String houseEleNum;
    /** ele_calculate_date - 计算时间 */
    private Date eleCalculateDate;
    /** ele_amount - 收取金额 */
    private BigDecimal eleAmount;
    /** ele_peak_amount - 峰金额 */
    private BigDecimal elePeakAmount;
    /** ele_valley_amount - 谷金额 */
    private BigDecimal eleValleyAmount;
    /** generate_order - 是否已生成订单 0:是 -1:否 */
    private Integer generateOrder;

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
    public String getOrderNo(){
        return this.orderNo;
    }
    public void setOrderNo(String orderNo){
        this.orderNo = orderNo;
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
    public Integer getRentType(){
        return this.rentType;
    }
    public void setRentType(Integer rentType){
        this.rentType = rentType;
    }
    public String getHouseEleNum(){
        return this.houseEleNum;
    }
    public void setHouseEleNum(String houseEleNum){
        this.houseEleNum = houseEleNum;
    }
    public Date getEleCalculateDate(){
        return this.eleCalculateDate;
    }
    public void setEleCalculateDate(Date eleCalculateDate){
        this.eleCalculateDate = eleCalculateDate;
    }
    public BigDecimal getEleAmount(){
        return this.eleAmount;
    }
    public void setEleAmount(BigDecimal eleAmount){
        this.eleAmount = eleAmount;
    }
    public BigDecimal getElePeakAmount(){
        return this.elePeakAmount;
    }
    public void setElePeakAmount(BigDecimal elePeakAmount){
        this.elePeakAmount = elePeakAmount;
    }
    public BigDecimal getEleValleyAmount(){
        return this.eleValleyAmount;
    }
    public void setEleValleyAmount(BigDecimal eleValleyAmount){
        this.eleValleyAmount = eleValleyAmount;
    }
    public Integer getGenerateOrder(){
        return this.generateOrder;
    }
    public void setGenerateOrder(Integer generateOrder){
        this.generateOrder = generateOrder;
    }
}