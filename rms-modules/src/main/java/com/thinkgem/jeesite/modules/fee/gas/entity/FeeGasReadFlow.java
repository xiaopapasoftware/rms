/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.util.Date;

/**
 * <p>抄燃气表流水实体类</p>
 * <p>Table: fee_gas_read_flow - --> FeeGasReadFlow</p>
 * <p>抄燃气表流水</p>
 * @since 2017-10-20 06:26:38
 * @author wangganggang
 */
@Data
public class FeeGasReadFlow extends DataEntity<FeeGasReadFlow>{

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
    /** house_gas_num - 户号 */
    private String houseGasNum;
    /** gas_read_date - 抄表日期 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date gasReadDate;
    /** gas_degree - 仪表额度 */
    private Float gasDegree;
}