package com.thinkgem.jeesite.modules.fee.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.enums.*;
import com.thinkgem.jeesite.modules.fee.dao.PostpaidFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.PostpaidFee;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class PostpaidFeeService extends CrudService<PostpaidFeeDao, PostpaidFee> {

  @Autowired
  private PaymentTransService paymentTransService;

  @Override
  public List<PostpaidFee> findList(PostpaidFee entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findList(entity);
  }

  @Override
  public Page<PostpaidFee> findPage(Page<PostpaidFee> page, PostpaidFee entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findPage(page, entity);
  }

  @Transactional(readOnly = false)
  public void saveBusiPostpaidFee(PostpaidFee postpaidFee) {
    if (postpaidFee.getIsNewRecord()) {// 新增
      process(postpaidFee);
    } else {// 更新
      // 到账收据待登记，删除款项重新生成
      // 账务审核拒绝， 删除已经到账的款项记录，款项账务关联记录，账务记录，重新生成款项
      paymentTransService.deletePaymentTransAndTradingAcctounsWithPostpaidFee(postpaidFee.getId());
      process(postpaidFee);
    }
  }

  private void  process(PostpaidFee postpaidFee) {
    Date nowDate = new Date();
    postpaidFee.setPayDate(nowDate);
    postpaidFee.setPayStatus(PublicFeePayStatusEnum.TO_SIGN.getValue());
    String postpaidFeeId = super.saveAndReturnId(postpaidFee);
    String tradeType = TradeTypeEnum.PUB_FEE_POSTPAID.getValue();
    String rentContractId = postpaidFee.getRentContractId();
    String tradeDirec = TradeDirectionEnum.IN.getValue();
    String signStatus = PaymentTransStatusEnum.NO_SIGN.getValue();

    Double eleSelAmt = postpaidFee.getElectricSelfAmt();
    if (eleSelAmt != null && eleSelAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue(), rentContractId, tradeDirec, eleSelAmt, eleSelAmt, 0D, signStatus, postpaidFee.getElectricSelfAmtStartDate(), postpaidFee.getElectricSelfAmtEndDate(),
          postpaidFeeId);
    }
    Double eleShareAmt = postpaidFee.getElectricShareAmt();
    if (eleShareAmt != null && eleShareAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.ELECT_SHARE_AMOUNT.getValue(), rentContractId, tradeDirec, eleShareAmt, eleShareAmt, 0D, signStatus, postpaidFee.getElectricShareAmtStartDate(),
          postpaidFee.getElectricShareAmtEndDate(), postpaidFeeId);
    }
    Double waterAmt = postpaidFee.getWaterAmt();
    if (waterAmt != null && waterAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.WATER_AMOUNT.getValue(), rentContractId, tradeDirec, waterAmt, waterAmt, 0D, signStatus, postpaidFee.getWaterAmtStartDate(), postpaidFee.getWaterAmtEndDate(),
          postpaidFeeId);
    }
    Double gasAmt = postpaidFee.getGasAmt();
    if (gasAmt != null && gasAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.GAS_AMOUNT.getValue(), rentContractId, tradeDirec, gasAmt, gasAmt, 0D, signStatus, postpaidFee.getGasAmtStartDate(), postpaidFee.getGasAmtEndDate(),
          postpaidFeeId);
    }
    Double tvAmt = postpaidFee.getTvAmt();
    if (tvAmt != null && tvAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.TV_AMOUNT.getValue(), rentContractId, tradeDirec, tvAmt, tvAmt, 0D, signStatus, postpaidFee.getTvAmtStartDate(), postpaidFee.getTvAmtEndDate(), postpaidFeeId);
    }
    Double netAmt = postpaidFee.getNetAmt();
    if (netAmt != null && netAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.NET_AMOUNT.getValue(), rentContractId, tradeDirec, netAmt, netAmt, 0D, signStatus, postpaidFee.getNetAmtStartDate(), postpaidFee.getNetAmtEndDate(),
          postpaidFeeId);
    }
    Double serviceAmt = postpaidFee.getServiceAmt();
    if (serviceAmt != null && serviceAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.SERVICE_AMOUNT.getValue(), rentContractId, tradeDirec, serviceAmt, serviceAmt, 0D, signStatus, postpaidFee.getServiceAmtStartDate(), postpaidFee.getServiceAmtEndDate(),
          postpaidFeeId);
    }

    Double houseRentAmt = postpaidFee.getHouseRentAmt();
    if (houseRentAmt != null && houseRentAmt > 0) {
      paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.RENT_AMOUNT.getValue(), rentContractId, tradeDirec, houseRentAmt, houseRentAmt, 0D, signStatus, postpaidFee.getHouseRentAmtStartDate(), postpaidFee.getHouseRentAmtEndDate(),
              postpaidFeeId);
    }
  }

  @Transactional(readOnly = false)
  public void delRentContract(String rentContractId) {
    PostpaidFee postpaidFee = new PostpaidFee();
    postpaidFee.preUpdate();
    postpaidFee.setRentContractId(rentContractId);
    super.delete(postpaidFee);
  }

}
