package com.thinkgem.jeesite.modules.fee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wangganggang
 * @date 2017年09月23日 下午12:41
 */
@Controller
@RequestMapping(value = "${adminPath}/fee/")
public class FeeRedirectController {

    @RequestMapping("electricity/bill/index")
    public String electricityBillIndex() {
        return "modules/fee/electricity/feeElectricityBill";
    }

    @RequestMapping("electricity/read/index")
    public String eleReadFlowIndex() {
        return "modules/fee/electricity/feeEleReadFlow";
    }
}
