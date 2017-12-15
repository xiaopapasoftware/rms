/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>电费账单表实体类</p>
 * <p>Table: fee_electricity_bill - --> FeeElectricityBill</p>
 * <p>电费账单表</p>
 * @since 2017-09-18 08:24:24
 */
@Data
public class FeeElectricityBill extends DataEntity<FeeElectricityBill>{

    /** batch_no - 审核批次号 */
    private String batchNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** house_ele_num - 户号 */
    private String houseEleNum;
    /** ele_bill_date - 账单日期 */
    @JsonFormat(pattern = "yyyy-MM",timezone="GMT+8")
    private Date eleBillDate;
    /** ele_bill_amount - 账单金额 */
    private BigDecimal eleBillAmount;
    /** ele_peak_degree - 峰值数 */
    private Double elePeakDegree;
    /** ele_valley_degree - 谷值数 */
    private Double eleValleyDegree;
    /** bill_status - 状态 0:待提交1:待审核 2:审核通过 3:审核驳回 */
    private Integer billStatus;
}