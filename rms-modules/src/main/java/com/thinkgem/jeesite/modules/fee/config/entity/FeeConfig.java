/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.config.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * <p>费用配置项实体类</p>
 * <p>Table: fee_config - --> FeeConfig</p>
 * <p>费用配置项</p>
 * @since 2017-09-18 08:14:27
 * @author wangganggang
 */
public class FeeConfig extends DataEntity<FeeConfig>{

    /** fee_type - 费用类型 0：电费单价 1:电费谷单价 2:电费峰单价 3:宽带单价 4:有线电视费单价 5:水费单价 6：燃气费单价 */
    private Integer feeType;
    /** config_type - 配置范围 0：默认 1:公司 2:省份 3:地市 4:区县 5: 服务中心 6: 运营区域 7: 小区 8:楼宇 9：房号 10:房间 */
    private Integer configType;
    /** charge_method - 收取方式 0:固定模式 1:账单模式 */
    private Integer chargeMethod;
    /** business_id - 相应范围ID */
    private String businessId;
    /** show_name - 范围名称 */
    private String showName;
    /** config_value - 配置的值 */
    private String configValue;
    /** config_status - 配置状态 0:启用 1停用 */
    private Integer configStatus;

    public Integer getFeeType(){
        return this.feeType;
    }
    public void setFeeType(Integer feeType){
        this.feeType = feeType;
    }
    public Integer getConfigType(){
        return this.configType;
    }
    public void setConfigType(Integer configType){
        this.configType = configType;
    }
    public Integer getChargeMethod(){
        return this.chargeMethod;
    }
    public void setChargeMethod(Integer chargeMethod){
        this.chargeMethod = chargeMethod;
    }
    public String getBusinessId(){
        return this.businessId;
    }
    public void setBusinessId(String businessId){
        this.businessId = businessId;
    }
    public String getShowName(){
        return this.showName;
    }
    public void setShowName(String showName){
        this.showName = showName;
    }
    public String getConfigValue(){
        return this.configValue;
    }
    public void setConfigValue(String configValue){
        this.configValue = configValue;
    }
    public Integer getConfigStatus(){
        return this.configStatus;
    }
    public void setConfigStatus(Integer configStatus){
        this.configStatus = configStatus;
    }
}