package com.thinkgem.jeesite.modules.fee.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wangganggang
 * @date 2017年09月23日 下午12:41
 */
@Controller
@RequestMapping(value = "${adminPath}/fee/")
public class FeeRedirectController {

    @RequestMapping("config/index")
    public String feeConfigIndex() {
        return "modules/fee/config/feeConfig";
    }

    @RequestMapping("electricity/bill/index")
    public String electricityBillIndex() {
        return "modules/fee/electricity/feeElectricityBill";
    }

    @RequestMapping("electricity/read/index")
    public String eleReadFlowIndex() {
        return "modules/fee/electricity/feeEleReadFlow";
    }

    @RequestMapping("electricity/charge/index")
    public String eleChargeFlowIndex() {
        return "modules/fee/electricity/feeEleChargeFlow";
    }


    @RequestMapping("gas/bill/index")
    public String gasBillIndex() {
        return "modules/fee/gas/feeGasBill";
    }

    @RequestMapping("gas/read/index")
    public String gasReadFlowIndex() {
        return "modules/fee/gas/feeGasReadFlow";
    }

    @RequestMapping("gas/charge/index")
    public String gasChargeFlowIndex() {
        return "modules/fee/gas/feeGasChargeFlow";
    }


    @RequestMapping("water/bill/index")
    public String waterBillIndex() {
        return "modules/fee/water/feeWaterBill";
    }

    @RequestMapping("water/read/index")
    public String waterReadFlowIndex() {
        return "modules/fee/water/feeWaterReadFlow";
    }

    @RequestMapping("water/charge/index")
    public String waterChargeFlowIndex() {
        return "modules/fee/water/feeWaterChargeFlow";
    }

    @RequestMapping("other/bill/index")
    public String otherBillIndex() {
        return "modules/fee/other/feeOtherBill";
    }

    @RequestMapping("other/charge/index")
    public String otherChargeFlowIndex() {
        return "modules/fee/other/feeOtherChargeFlow";
    }

    @RequestMapping("order/index")
    public String feeOrderIndex() {
        return "modules/fee/order/feeOrder";
    }
}
