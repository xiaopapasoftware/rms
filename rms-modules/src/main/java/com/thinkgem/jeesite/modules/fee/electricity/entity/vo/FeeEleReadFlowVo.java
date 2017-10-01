package com.thinkgem.jeesite.modules.fee.electricity.entity.vo;

import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import lombok.Data;

/**
 * @date 2017年09月18日 下午8:31
 */
@Data
public class FeeEleReadFlowVo extends FeeEleReadFlow {

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
