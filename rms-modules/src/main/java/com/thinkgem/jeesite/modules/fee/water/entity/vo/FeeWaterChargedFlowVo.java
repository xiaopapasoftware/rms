package com.thinkgem.jeesite.modules.fee.water.entity.vo;

import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterChargedFlow;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月21日 上午10:59
 */
@Data
public class FeeWaterChargedFlowVo extends FeeWaterChargedFlow {
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
