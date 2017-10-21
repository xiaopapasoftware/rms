/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>煤气收取流水实体类</p>
 * <p>Table: fee_gas_charged_flow - --> FeeGasChargedFlow</p>
 * <p>煤气收取流水</p>
 * @since 2017-10-20 06:26:33
 * @author wangganggang
 */
@Data
public class FeeGasChargedFlow extends DataEntity<FeeGasChargedFlow>{

	/** 主键 */
    private String id;

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
    /** house_gas_num - 户号 */
    private String houseGasNum;
    /** gas_calculate_date - 计算时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date gasCalculateDate;
    /** gas_amount - 收取金额 */
    private BigDecimal gasAmount;
    /** generate_order - 是否已生成订单 0:是 -1:否 */
    private Integer generateOrder;
}