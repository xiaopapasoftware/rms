package com.thinkgem.jeesite.modules.fee.other.entity.vo;

import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherBill;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年11月28日 15:19
 */
@Data
public class FeeOtherBillVo extends FeeOtherBill {

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
