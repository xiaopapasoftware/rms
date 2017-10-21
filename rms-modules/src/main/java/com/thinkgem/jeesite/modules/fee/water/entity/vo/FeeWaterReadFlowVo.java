package com.thinkgem.jeesite.modules.fee.water.entity.vo;

import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月21日 上午10:54
 */
@Data
public class FeeWaterReadFlowVo extends FeeWaterReadFlow {

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
