package com.thinkgem.jeesite.modules.fee.electricity.entity.vo;

import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月16日 下午9:49
 */
@Data
public class FeeEleChargedFlowVo extends FeeEleChargedFlow{

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
