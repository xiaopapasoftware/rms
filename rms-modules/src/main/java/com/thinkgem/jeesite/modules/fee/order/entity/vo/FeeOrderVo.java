package com.thinkgem.jeesite.modules.fee.order.entity.vo;

import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年11月08日 14:17
 */
@Data
public class FeeOrderVo extends FeeOrder {

    private String areaId;

    private String areaName;

    private String projectName;

    private String buildingId;

    private String buildingName;

    private String houseNo;

    private String projectAddress;

}
