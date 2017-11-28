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
 * <p>宽带、电视费、其他账单收取流水实体类</p>
 * <p>Table: fee_other_charged_flow - --> FeeOtherChargedFlow</p>
 * <p>宽带、电视费、其他账单收取流水</p>
 * @since 2017-11-28 03:02:42
 * @author wangganggang
 */
@Data
public class FeeOtherChargedFlow extends DataEntity<FeeOtherChargedFlow> implements Cloneable{

	/** 主键 */
    private String id;

    /** order_no - 订单号 */
    private String orderNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** room_id - 房号 */
    private String roomId;
    /** calculate_date - 计算时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date calculateDate;
    /** amount - 收取金额 */
    private BigDecimal amount;
    /** type - 账单类型 0:宽带 1:电视 2:其它 */
    private Integer type;
    /** generate_order - 是否已生成订单 0:是 -1:否 */
    private Integer generateOrder;

    @Override
    public FeeOtherChargedFlow clone(){
        FeeOtherChargedFlow feeOtherChargedFlow;
        try {
            feeOtherChargedFlow = (FeeOtherChargedFlow) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("服务异常请检查");
        }
        return feeOtherChargedFlow;
    }
}