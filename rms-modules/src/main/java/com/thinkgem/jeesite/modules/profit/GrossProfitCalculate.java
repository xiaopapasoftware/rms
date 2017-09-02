package com.thinkgem.jeesite.modules.profit;

import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.util.GrossProfitAssistant;

import java.util.List;
import java.util.Optional;

public interface GrossProfitCalculate {

    default GrossProfitReport calculateGrossProfit(GrossProfitCondition condition) {
        GrossProfitReport report = new GrossProfitReport();
        report.setType(condition.getTypeEnum().getCode());
        report.setIncome(calculateIncome(condition));
        report.setCost(calculateCost(condition));
        report.setTotalProfit(report.getIncome() - report.getCost());
        report.setProfitPercent(GrossProfitAssistant.getProfitPercent(report.getIncome(), report.getCost()));
        report.setName(getName(condition));
        return report;
    }

    String getName(GrossProfitCondition condition);

    default double calculateCost(GrossProfitCondition condition){
        return Optional.ofNullable(this.getChildIdList(condition))
                .map(list -> list.stream().mapToDouble(this::calculateCost).sum())
                .orElse(0d);
    }

    default double calculateIncome(GrossProfitCondition condition){
        return Optional.ofNullable(this.getChildIdList(condition))
                .map(list -> list.stream().mapToDouble(this::calculateIncome).sum())
                .orElse(0d);
    }

    List<GrossProfitCondition> getChildIdList(GrossProfitCondition condition);

}
