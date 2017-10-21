package com.thinkgem.jeesite.modules.fee.water.entity.vo;

import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月21日 上午10:44
 */
@Data
public class FeeWaterBillVo extends FeeWaterBill{

    private String areaId;

    private String areaName;

    private String projectName;

    private String buildingId;

    private String buildingName;

    private String houseNo;

    private String houseId;

    private String projectAddress;

    private String billStatusName;
}
