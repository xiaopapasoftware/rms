package com.thinkgem.jeesite.modules.fee.gas.entity.vo;

import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月20日 下午9:14
 */
@Data
public class FeeGasChargedFlowVo extends FeeGasChargedFlow {

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
