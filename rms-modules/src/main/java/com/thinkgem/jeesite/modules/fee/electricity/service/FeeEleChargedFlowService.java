/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeEleChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* <p>电费收取流水实现类 service</p>
* <p>Table: fee_ele_charged_flow - 电费收取流水</p>
* @since 2017-09-18 08:24:32
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeEleChargedFlowService extends CrudService<FeeEleChargedFlowDao, FeeEleChargedFlow> {

}