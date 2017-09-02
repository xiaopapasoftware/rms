package com.thinkgem.jeesite.modules.funds.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 款项交易DAO接口
 */
@MyBatisDao
public interface PaymentTransDao extends CrudDao<PaymentTrans> {

  /**
   * 退租删除未到账的款项之回滚，包括： 交易类型【新签合同、续签合同】；款项类型【房租金额、水费金额、燃气金额、有线电视费、宽带费、服务费】
   */
  void rollbackDelete(PaymentTrans p);

  /**
   * 查询某个合同指定时间内进账款项
   */
  List<PaymentTrans> queryIncomePaymentByTransIdAndTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("transId") String transId);

  /**
   * 查询某个合同指定时间内出账款项
   */
  List<PaymentTrans> queryCostPaymentByTransIdAndTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("transId") String transId);
}
