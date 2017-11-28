package com.thinkgem.jeesite.modules.fee.other.entity.vo;

import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherChargedFlow;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年11月28日 15:22
 */
@Data
public class FeeOtherChargedFlowVo extends FeeOtherChargedFlow {

    private String areaId;

    private String areaName;

    private String projectName;

    private String buildingName;

    private String houseNo;

    private String roomNo;

    private String intentMode;

    private String projectAddress;

    private String intentModeName;
}
