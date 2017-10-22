package com.thinkgem.jeesite.modules.fee.gas.entity.vo;

import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月20日 下午9:13
 */
@Data
public class FeeGasReadFlowVo extends FeeGasReadFlow {

    private String areaId;

    private String areaName;

    private String projectName;

    private String buildingId;

    private String buildingName;

    private String houseNo;

    private String roomNo;

    private String projectAddress;

    private String intentMode;

    private String intentModeName;
}
