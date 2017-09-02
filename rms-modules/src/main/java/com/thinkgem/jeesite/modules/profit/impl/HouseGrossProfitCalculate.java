package com.thinkgem.jeesite.modules.profit.impl;

import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractCondition;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractDtl;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTradeService;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculate;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitNumDeposit;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.util.DataParser;
import com.thinkgem.jeesite.modules.profit.util.GrossProfitAssistant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class HouseGrossProfitCalculate implements GrossProfitCalculate{

    @Autowired
    private LeaseContractService contractService;

    @Autowired
    private RentContractService rentContractService;

    @Autowired
    private TradingAccountsService tradingAccountsService;

    @Autowired
    private PaymentTradeService paymentTradeService;

    @Autowired
    private PaymentTransService paymentTransService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Override
    public String getName(GrossProfitCondition condition) {
        House house = houseService.getHouseById(condition.getId());
        String name = "";
        if (house != null) {
            name += propertyProjectService.get(house.getPropertyProject().getId()).getProjectName() + house.getHouseNo();
        }
        return name;
    }

    @Override
    public double calculateCost(GrossProfitCondition condition) {
        CompletableFuture<Double> result = CompletableFuture.supplyAsync(() -> this.calculateDepositSum(condition)).thenCombine(
                CompletableFuture.supplyAsync(() -> this.calculateThrowLeaseSum(condition)),
                (depositSum, leaseSum) -> depositSum + leaseSum);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            return 0;
        }
    }

    @Override
    public double calculateIncome(GrossProfitCondition condition) {
        List<RentContract> contractList = rentContractService.queryHousesByHouseIdAndTime(condition.getStartDate(), condition.getEndDate(), condition.getId());
        List<String> accountIdList = contractList.stream().map(RentContract::getId)
                                                            .map(tradingAccountsService::queryIncomeTradeAccountsByTradeId)
                                                            .flatMap(List::stream)
                                                            .distinct()
                                                            .map(TradingAccounts::getId)
                                                            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(accountIdList)) {
            return Optional.ofNullable(paymentTradeService.getTradeListByTradeIds(accountIdList))
                    .map( list -> list.stream()
                                    .map(PaymentTrade::getTradeId)
                                    .map(tradeId -> paymentTransService.queryIncomePaymentByTransIdAndTime(condition.getStartDate(), condition.getEndDate(), tradeId))
                                    .flatMap(List::stream)
                                    .mapToDouble(PaymentTrans::getTradeAmount)
                                    .sum())
                    .orElse(0d);

        }
        return 0d;
    }

    @Override
    public List<GrossProfitCondition> getChildIdList(GrossProfitCondition condition) {
        return null;
    }

    private double calculateThrowLeaseSum(GrossProfitCondition condition) {
        List<RentContract> contractList = rentContractService.queryHousesByHouseIdAndTime(condition.getStartDate(), condition.getEndDate(), condition.getId());
        List<String> accountIdList = contractList.stream().map(RentContract::getId)
                .map(tradingAccountsService::queryCostTradeAccountsByTradeId)
                .flatMap(List::stream)
                .distinct()
                .map(TradingAccounts::getId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(accountIdList)) {
            return Optional.ofNullable(paymentTradeService.getTradeListByTradeIds(accountIdList))
                    .map( list -> list.stream()
                            .map(PaymentTrade::getTradeId)
                            .map(tradeId -> paymentTransService.queryCostPaymentByTransIdAndTime(condition.getStartDate(), condition.getEndDate(), tradeId))
                            .flatMap(List::stream)
                            .mapToDouble(PaymentTrans::getTradeAmount)
                            .sum())
                    .orElse(0d);

        }
        return 0;
    }

    private double calculateDepositSum(GrossProfitCondition condition) {
        LeaseContractCondition contractCondition = new LeaseContractCondition();
        BeanUtils.copyProperties(condition, contractCondition);
        contractCondition.setHouseId(condition.getId());
        List<LeaseContract> contractList = contractService.findLeaseContractListByCondition(contractCondition);
        int startNum = DataParser.calculateNum(GrossProfitAssistant.parseDateToYMD(condition.getStartDate()), false);
        int endNum = DataParser.calculateNum(GrossProfitAssistant.parseDateToYMD(condition.getEndDate()), false) - 1;
        //每份房屋只会对应一份租赁合同
        if (!CollectionUtils.isEmpty(contractList)) {
            LeaseContract contract = contractList.get(0);
            List<LeaseContractDtl> contractDtlList = contract.getLeaseContractDtlList();
            List<GrossProfitNumDeposit> depositList = contractDtlList.stream()
                    .map(dtl -> DataParser.parse(dtl.getStartDate(), dtl.getEndDate(), dtl.getDeposit()))
                    .filter(numDeposit -> numDeposit.getEnd() < startNum || numDeposit.getStart() > endNum)
                    .sorted(Comparator.comparing(GrossProfitNumDeposit::getStart))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(depositList)) {
                return 0;
            } else {
                return calculateDepositList(depositList, startNum, endNum);
            }
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
        } else if (startNum <= end && endNum > end) {
            sum += (end - startNum + 1) * deposit;
        } else if (startNum < start && endNum > end) {
            sum += (numDeposit.getSize()) * deposit;
        }
        return sum;
    }

}
