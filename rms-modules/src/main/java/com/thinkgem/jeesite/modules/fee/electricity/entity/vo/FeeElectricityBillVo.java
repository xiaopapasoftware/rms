package com.thinkgem.jeesite.modules.fee.electricity.entity.vo;

import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import lombok.Data;

/**
 * @date 2017年09月18日 下午8:31
 */
@Data
public class FeeElectricityBillVo extends FeeElectricityBill {

    private String areaId;

    private String areaName;

    private String propertyName;

    private String buildingId;

    private String buildingName;

    private String houseNo;

    private String projectAddress;

}
