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
 * <p>电费收取流水实体类</p>
 * <p>Table: fee_ele_charged_flow - --> FeeEleChargedFlow</p>
 * <p>电费收取流水</p>
 * @since 2017-09-18 08:24:32
 */
@Data
public class FeeEleChargedFlow extends DataEntity<FeeEleChargedFlow> implements Cloneable{

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
    /** payer - 支付者 0：租客 1：公司*/
    private Integer payer;
    /** house_ele_num - 户号 */
    private String houseEleNum;
    /** ele_calculate_date - 计算时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date eleCalculateDate;
    /** ele_amount - 收取金额 */
    private BigDecimal eleAmount;
    /** ele_peak_amount - 峰金额 */
    private BigDecimal elePeakAmount;
    /** ele_valley_amount - 谷金额 */
    private BigDecimal eleValleyAmount;
    /** generate_order - 是否已生成订单 1:是 0:否 */
    private Integer generateOrder;

    @Override
    public FeeEleChargedFlow clone(){
        FeeEleChargedFlow feeEleChargedFlow;
        try {
            feeEleChargedFlow = (FeeEleChargedFlow)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("服务异常请检查");
        }
        return feeEleChargedFlow;
    }
}