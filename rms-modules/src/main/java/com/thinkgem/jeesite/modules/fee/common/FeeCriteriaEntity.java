package com.thinkgem.jeesite.modules.fee.common;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import lombok.Data;

/**
 * @date 2017年09月18日 下午8:39
 */
@Data
public class FeeCriteriaEntity extends DataEntity<FeeCriteriaEntity> {

    private int pageSize = 15;

    private int pageNum = 1;

    private String feeDate;

    private String areaId;

    private String propertyId;

    private String buildId;

    private String houseId;

    /*0:待提交1:待审核 2:审核通过 3:审核驳回*/
    private String status;

    /*0:已录 1:未录*/
    private String isRecord;
}
