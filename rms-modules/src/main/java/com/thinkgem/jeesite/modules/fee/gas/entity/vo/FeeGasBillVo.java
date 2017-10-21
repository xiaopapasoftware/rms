package com.thinkgem.jeesite.modules.fee.gas.entity.vo;

import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月20日 下午9:12
 */
@Data
public class FeeGasBillVo extends FeeGasBill{

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
