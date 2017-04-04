package com.thinkgem.jeesite.modules.fee.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.enums.FeeSettlementStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PublicFeePayStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PublicFeeTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.fee.dao.PostpaidFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.PostpaidFee;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class PostpaidFeeService extends CrudService<PostpaidFeeDao, PostpaidFee> {

  @Autowired
  private PaymentTransService paymentTransService;

  @Transactional(readOnly = false)
  public void saveBusiPostpaidFee(PostpaidFee postpaidFee) {
    Date nowDate = new Date();
    String feeType = postpaidFee.getPublicFeeType();
    String tradeType = "";
    if (PublicFeeTypeEnum.ELECT_SELF_AMOUNT.getValue().equals(feeType)) {
      tradeType = TradeTypeEnum.POSTPAID_SELF.getValue();
    } else if (PublicFeeTypeEnum.ELECT_SHARE_AMOUNT.getValue().equals(feeType)) {
      tradeType = TradeTypeEnum.POSTPAID_SHARE.getValue();
    } else if (PublicFeeTypeEnum.WATER_AMOUNT.getValue().equals(feeType)) {
      tradeType = TradeTypeEnum.POSTPAID_WATER.getValue();
    } else if (PublicFeeTypeEnum.GAS_AMOUNT.getValue().equals(feeType)) {
      tradeType = TradeTypeEnum.POSTPAID_GAS.getValue();
    } else if (PublicFeeTypeEnum.TV_AMOUNT.getValue().equals(feeType)) {
      tradeType = TradeTypeEnum.POSTPAID_TV.getValue();
    } else if (PublicFeeTypeEnum.NET_AMOUNT.getValue().equals(feeType)) {
      tradeType = TradeTypeEnum.POSTPAID_NET.getValue();
    } else if (PublicFeeTypeEnum.SERVICE_AMOUNT.getValue().equals(feeType)) {
      tradeType = TradeTypeEnum.POSTPAID_SERVICE.getValue();
    }
    String id = paymentTransService.generateAndSavePaymentTrans(tradeType, feeType, postpaidFee.getRentContractId(), TradeDirectionEnum.IN.getValue(), postpaidFee.getPayAmount(),
        postpaidFee.getPayAmount(), 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), nowDate, nowDate);
    postpaidFee.setPayDate(nowDate);
    postpaidFee.setPayStatus(PublicFeePayStatusEnum.PROCESSING.getValue());
    postpaidFee.setSettleStatus(FeeSettlementStatusEnum.NOT_SETTLED.getValue());
    postpaidFee.setPaymentTransId(id);
    super.save(postpaidFee);
  }

  public PostpaidFee getPostpaidFeeByTransId(String transId) {
    return dao.getPostpaidFeeByTransId(transId);
  }
}
