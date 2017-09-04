package com.thinkgem.jeesite.modules.profit;

import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.util.GrossProfitReportBuilder;

import java.util.List;
import java.util.Optional;

public interface GrossProfitCalculate {

    default GrossProfitReport calculateGrossProfit(GrossProfitCondition condition) {
        return new GrossProfitReportBuilder().typeEnum(condition.getTypeEnum())
                                             .cost(calculateCost(condition))
                                             .income(calculateIncome(condition))
                                             .name(getName(condition))
                                             .build();
    }

    String getName(GrossProfitCondition condition);

    default double calculateCost(GrossProfitCondition condition){
        return Optional.ofNullable(this.getChildConditionList(condition))
                .map(list -> list.stream().mapToDouble(this::calculateCost).sum())
                .orElse(0d);
    }

    default double calculateIncome(GrossProfitCondition condition){
        return Optional.ofNullable(this.getChildConditionList(condition))
                .map(list -> list.stream().mapToDouble(this::calculateIncome).sum())
                .orElse(0d);
    }

    List<GrossProfitCondition> getChildConditionList(GrossProfitCondition condition);

}
