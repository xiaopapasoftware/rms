/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.util.Date;

/**
 * <p>抄水表流水实体类</p>
 * <p>Table: fee_water_read_flow - --> FeeWaterReadFlow</p>
 * <p>抄水表流水</p>
 * @since 2017-10-20 06:26:14
 * @author wangganggang
 */
@Data
public class FeeWaterReadFlow extends DataEntity<FeeWaterReadFlow>{

	/** 主键 */
    private String id;

    /** business_id - 关联业务ID */
    private String businessId;
    /** from_source - 来源 0: 抄表1: 账单 */
    private Integer fromSource;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** house_water_num - 户号 */
    private String houseWaterNum;
    /** water_read_date - 抄表日期 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date waterReadDate;
    /** water_degree - 仪表额度 */
    private Float waterDegree;
}