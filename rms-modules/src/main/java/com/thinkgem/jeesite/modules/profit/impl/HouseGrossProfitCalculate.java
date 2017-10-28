package com.thinkgem.jeesite.modules.profit.impl;

import com.thinkgem.jeesite.modules.cache.MyCache;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.PaymenttransDtl;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.PaymenttransDtlService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculate;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitNumDeposit;
import com.thinkgem.jeesite.modules.profit.util.DataParser;
import com.thinkgem.jeesite.modules.profit.util.GrossProfitAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HouseGrossProfitCalculate implements GrossProfitCalculate{

    @Autowired
    private LeaseContractService contractService;

    @Autowired
    private RentContractService rentContractService;

    @Autowired
    private PaymentTransService paymentTransService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private PaymenttransDtlService paymenttransDtlService;

    private MyCache depositListCache = MyCacheBuilder.getInstance().getScheduledCache("deposit");

    @Override
    public String getName(GrossProfitCondition condition) {
        return Optional.ofNullable(houseService.getHouseById(condition.getId())).map(House::getHouseNo).orElse("");
    }

    @Override
    public double calculateCost(GrossProfitCondition condition) {
        return this.calculateDepositSum(condition);
    }

    @Override
    public double calculateIncome(GrossProfitCondition condition) {
        List<RentContract> contractList = rentContractService.queryHousesByHouseId(condition.getId());
        if (!CollectionUtils.isEmpty(contractList)) {
            List<String> contractIdList = contractList.stream().map(RentContract::getId).collect(Collectors.toList());
            double income = paymentTransService.queryIncomePaymentByTransIdAndTime(condition.getStartDate(), condition.getEndDate(), contractIdList)
                    .stream().mapToDouble(PaymentTrans::getTradeAmount).sum();
            List<PaymenttransDtl> dtlList = paymenttransDtlService.queryPaymenttransDtlListByContractIdList(condition.getStartDate(), condition.getEndDate(), contractIdList);
            BigDecimal result = BigDecimal.valueOf(income);
            for (PaymenttransDtl paymenttransDtl : dtlList) {
                if ("0".equals(paymenttransDtl.getDirection())) {
                    result = result.subtract(BigDecimal.valueOf(paymenttransDtl.getAmount()));
                } else {
                    result = result.add(BigDecimal.valueOf(paymenttransDtl.getAmount()));
                }
            }
            return result.doubleValue();
        }
        return 0d;
    }

    @Override
    public List<GrossProfitCondition> getChildConditionList(GrossProfitCondition condition) {
        return null;
    }

    @SuppressWarnings("unchecked")
    private double calculateDepositSum(GrossProfitCondition condition) {
        List<LeaseContract> contractList = contractService.findLeaseContractListByHouseId(condition.getId());
        int startNum = DataParser.calculateNum(GrossProfitAssistant.parseDateToYMD(condition.getStartDate()), false);
        int endNum = DataParser.calculateNum(GrossProfitAssistant.parseDateToYMD(condition.getEndDate()), false) - 1;
        //每份房屋只会对应一份租赁合同
        if (!CollectionUtils.isEmpty(contractList)) {
            LeaseContract contract = contractList.get(0);
            if (depositListCache.getObject(contract.getId()) == null) {
                depositListCache.putObject(contract.getId(), contract.getLeaseContractDtlList().stream()
                        .map(dtl -> DataParser.parse(dtl.getStartDate(), dtl.getEndDate(), dtl.getDeposit()))
                        .sorted(Comparator.comparing(GrossProfitNumDeposit::getStart))
                        .collect(Collectors.toList()));
            }
            List<GrossProfitNumDeposit> depositList = ((List<GrossProfitNumDeposit>)depositListCache.getObject(contract.getId()))
                    .stream().filter(numDeposit -> !(numDeposit.getEnd() < startNum || numDeposit.getStart() > endNum))
                    .collect(Collectors.toList());
            return CollectionUtils.isEmpty(depositList) ? 0 : calculateDepositList(depositList, startNum, endNum);
        }
        return 0;
    }

    private double calculateDepositList(List<GrossProfitNumDeposit> depositList, int startNum ,int endNum) {
        return depositList.stream().mapToDouble(numDeposit -> calculateDepositSingle(numDeposit, startNum, endNum)).sum();
    }

    private double calculateDepositSingle(GrossProfitNumDeposit numDeposit, int startNum, int endNum) {
        double sum = 0;
        int start = numDeposit.getStart();
        int end = numDeposit.getEnd();
        double deposit = numDeposit.getDeposit();
        if (startNum >= start && endNum <= end) {
            sum += (endNum - startNum + 1) * deposit;
        } else if (startNum < start && endNum <= end) {
            sum += (endNum - start + 1) * deposit;
        } else if (startNum >= start && endNum > end) {
            sum += (end - startNum + 1) * deposit;
        } else if (startNum < start && endNum > end) {
            sum += (numDeposit.getSize()) * deposit;
        }
        return sum;
    }

}
