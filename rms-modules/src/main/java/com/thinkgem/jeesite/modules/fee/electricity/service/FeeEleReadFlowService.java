/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeEleReadFlowDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleReadFlowVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* <p>抄电表流水实现类 service</p>
* <p>Table: fee_ele_read_flow - 抄电表流水</p>
* @since 2017-09-18 08:24:39
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeEleReadFlowService extends CrudService<FeeEleReadFlowDao, FeeEleReadFlow> {

    public List<FeeEleReadFlowVo> getFeeEleReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity){
        return dao.getFeeEleReadFlowWithAllInfo(feeCriteriaEntity);
    }
}