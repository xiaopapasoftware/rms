package com.thinkgem.jeesite.modules.fee.config.entity.vo;

import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import lombok.Data;

/**
 * @author wangganggang
 * @date 2017年10月12日 17:29
 */
@Data
public class FeeConfigVo extends FeeConfig{

    private String feeTypeName;

    private String configTypeName;

    private String chargeMethodName;

}
