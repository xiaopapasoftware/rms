/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>订单台账实体类</p>
 * <p>Table: fee_order_account - --> FeeOrderAccount</p>
 * <p>订单台账</p>
 * @since 2017-11-05 10:05:47
 * @author wangganggang
 */
@Data
public class FeeOrderAccount extends DataEntity<FeeOrderAccount>{
    /** order_no - 订单号 */
    private String orderNo;
    /** property_id - 物业ID */
    private String propertyId;
    /** house_id - 房屋ID */
    private String houseId;
    /** room_id - 房号 */
    private String roomId;
    /** order_type - 费用类型 0：电费 1：水费 2 燃气费 3：宽带 4：电视 5:房租 6:房租押金 7:定金 8:违约金 */
    private Integer orderType;
    /** pay_date - 支付时间 */
    private Date payDate;
    /** amount - 账单金额 */
    private BigDecimal amount;
    /** order_status - 状态 0:待审核1:待缴费2：已缴3：驳回 */
    private Integer orderStatus;
}