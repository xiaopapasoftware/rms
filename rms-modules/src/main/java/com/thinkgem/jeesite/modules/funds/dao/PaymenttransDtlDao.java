package com.thinkgem.jeesite.modules.funds.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymenttransDtl;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * fundsDAO接口
 * 
 * @author funds
 * @version 2017-10-21
 */
@MyBatisDao
public interface PaymenttransDtlDao extends CrudDao<PaymenttransDtl> {

    List<PaymenttransDtl> queryPaymenttransDtlListByContractIdList(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("contractIdList") List<String> contractIdList);

}
