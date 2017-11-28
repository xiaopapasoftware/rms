/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>宽带、电视费、其他账单表实体类</p>
 * <p>Table: fee_other_bill - --> FeeOtherBill</p>
 * <p>宽带、电视费、其他账单表</p>
 * @since 2017-11-28 03:02:34
 * @author wangganggang
 */
@Data
public class FeeOtherBill extends DataEntity<FeeOtherBill> {

	/** 主键 */
    private String id;

    /** batch_no - 审核批次号 */
    private String batchNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** bill_date - 账单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date billDate;
    /** bill_unit - 账单单位 0：月1：半年2:一年 */
    private Integer billUnit;
    /** bill_amount - 账单金额 */
    private BigDecimal billAmount;
    /** Bill_type - 仪表额度 */
    private Float billType;
    /** bill_status - 状态 0:待提交1:待审核 2:审核通过 3:审核驳回 */
    private Integer billStatus;
}