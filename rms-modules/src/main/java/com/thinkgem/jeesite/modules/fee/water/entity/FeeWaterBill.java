/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>水费账单表实体类</p>
 * <p>Table: fee_water_bill - --> FeeWaterBill</p>
 * <p>水费账单表</p>
 * @since 2017-10-20 06:25:59
 * @author wangganggang
 */
@Data
public class FeeWaterBill extends DataEntity<FeeWaterBill>{

	/** 主键 */
    private String id;

    /** batch_no - 审核批次号 */
    private String batchNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** house_water_num - 户号 */
    private String houseWaterNum;
    /** water_bill_date - 账单日期 */
    private Date waterBillDate;
    /** water_bill_amount - 账单金额 */
    private BigDecimal waterBillAmount;
    /** water_degree - 水表读数 */
    private Float waterDegree;
    /** bill_status - 状态 0:待提交1:待审核 2:审核通过 3:审核驳回 */
    private Integer billStatus;
}