/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.util.Date;

/**
 * <p>抄电表流水实体类</p>
 * <p>Table: fee_ele_read_flow - --> FeeEleReadFlow</p>
 * <p>抄电表流水</p>
 * @since 2017-09-18 08:24:39
 */
@Data
public class FeeEleReadFlow extends DataEntity<FeeEleReadFlow> implements Cloneable{

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

    @Override
    public FeeEleReadFlow clone(){
        FeeEleReadFlow feeEleReadFlow;
        try {
            feeEleReadFlow = (FeeEleReadFlow) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("服务异常请检查");
        }
        return feeEleReadFlow;
    }
}