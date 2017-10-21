/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>燃气账单表实体类</p>
 * <p>Table: fee_gas_bill - --> FeeGasBill</p>
 * <p>燃气账单表</p>
 * @since 2017-10-20 06:26:27
 * @author wangganggang
 */
@Data
public class FeeGasBill extends DataEntity<FeeGasBill>{

	/** 主键 */
    private String id;

    /** batch_no - 审核批次号 */
    private String batchNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** house_gas_num - 户号 */
    private String houseGasNum;
    /** gas_bill_date - 账单日期 */
    private Date gasBillDate;
    /** gas_bill_amount - 账单金额 */
    private BigDecimal gasBillAmount;
    /** gas_degree - 仪表额度 */
    private Float gasDegree;
    /** bill_status - 状态 0:待提交1:待审核 2:审核通过 3:审核驳回 */
    private Integer billStatus;
}