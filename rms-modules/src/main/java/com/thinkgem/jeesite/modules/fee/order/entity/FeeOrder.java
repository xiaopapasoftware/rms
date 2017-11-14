/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>订单实体类</p>
 * <p>Table: fee_order - --> FeeOrder</p>
 * <p>订单</p>
 * @since 2017-11-05 10:05:53
 * @author wangganggang
 */
@Data
public class FeeOrder extends DataEntity<FeeOrder>{

	/** 主键 */
	private String id;

	/** 订单号*/
    private String orderNo;
    /** batch_no - 审核编号 */
    private String batchNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** room_id - 房号 */
    private String roomId;
    /** order_type - 费用类型 0：电费 1：水费 2 燃气费  3：宽带 4：电视 5:房租 6:房租押金 7:定金 8:违约金 */
    private Integer orderType;
    /** payer - 支付者 0：租客 1：公司*/
    private Integer payer;
    /** order_date - 订单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date orderDate;
    /** amount - 账单金额 */
    private BigDecimal amount;
    /** order_status - 状态 0:待审核1:待缴费2：已缴3：驳回 */
    private Integer orderStatus;
}