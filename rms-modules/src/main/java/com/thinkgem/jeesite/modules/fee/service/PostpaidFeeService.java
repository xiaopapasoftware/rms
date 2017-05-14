package com.thinkgem.jeesite.modules.fee.service;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.PublicFeePayStatusEnum;
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

    private void process(PostpaidFee postpaidFee) {
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
            paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue(), rentContractId, tradeDirec, eleSelAmt, eleSelAmt, 0D, signStatus, nowDate, nowDate,
                    postpaidFeeId);
        }
        Double eleShareAmt = postpaidFee.getElectricShareAmt();
        if (eleShareAmt != null && eleShareAmt > 0) {
            paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.ELECT_SHARE_AMOUNT.getValue(), rentContractId, tradeDirec, eleShareAmt, eleShareAmt, 0D, signStatus, nowDate,
                    nowDate, postpaidFeeId);
        }
        Double waterAmt = postpaidFee.getWaterAmt();
        if (waterAmt != null && waterAmt > 0) {
            paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.WATER_AMOUNT.getValue(), rentContractId, tradeDirec, waterAmt, waterAmt, 0D, signStatus, nowDate, nowDate,
                    postpaidFeeId);
        }
        Double gasAmt = postpaidFee.getGasAmt();
        if (gasAmt != null && gasAmt > 0) {
            paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.GAS_AMOUNT.getValue(), rentContractId, tradeDirec, gasAmt, gasAmt, 0D, signStatus, nowDate, nowDate,
                    postpaidFeeId);
        }
        Double tvAmt = postpaidFee.getTvAmt();
        if (tvAmt != null && tvAmt > 0) {
            paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.TV_AMOUNT.getValue(), rentContractId, tradeDirec, tvAmt, tvAmt, 0D, signStatus, nowDate, nowDate, postpaidFeeId);
        }
        Double netAmt = postpaidFee.getNetAmt();
        if (netAmt != null && netAmt > 0) {
            paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.NET_AMOUNT.getValue(), rentContractId, tradeDirec, netAmt, netAmt, 0D, signStatus, nowDate, nowDate,
                    postpaidFeeId);
        }
        Double serviceAmt = postpaidFee.getServiceAmt();
        if (serviceAmt != null && serviceAmt > 0) {
            paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.SERVICE_AMOUNT.getValue(), rentContractId, tradeDirec, serviceAmt, serviceAmt, 0D, signStatus, nowDate, nowDate,
                    postpaidFeeId);
        }
    }
}
