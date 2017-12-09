package com.thinkgem.jeesite.modules.report;

import com.thinkgem.jeesite.modules.cache.MyCache;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import com.thinkgem.jeesite.modules.cache.enums.MyCacheConstant;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculateStrategy;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.util.GrossProfitAssistant;
import com.thinkgem.jeesite.modules.profit.util.GrossProfitReportBuilder;

import java.util.List;
import java.util.Optional;

public interface EletricityFeeCalculate {

  MyCache softCache = MyCacheBuilder.getInstance().getSoftCache(MyCacheConstant.SOFT_NAME);

  default GrossProfitReport calculateGrossProfit(GrossProfitCondition condition) {
    return new GrossProfitReportBuilder().typeEnum(condition.getTypeEnum()).cost(calculateCost(condition)).income(calculateIncome(condition)).name(getNameFromCache(condition)).build();
  }

  default String getNameFromCache(GrossProfitCondition condition) {
    String cacheKey = GrossProfitAssistant.getCacheKey(condition);
    String result;
    Object name = softCache.getObject(cacheKey);
    if (name != null) {
      result = name.toString();
    } else {
      result = getName(condition);
      softCache.putObject(cacheKey, result);
    }
    return result;
  }

  String getName(GrossProfitCondition condition);

  default double calculateCost(GrossProfitCondition condition) {
    return Optional.ofNullable(this.getChildConditionList(condition))
        .map(list -> list.stream().mapToDouble(subCondition -> GrossProfitCalculateStrategy.strategyRegistry.get(subCondition.getTypeEnum().getCode()).calculateCost(subCondition)).sum()).orElse(0d);
  }

  default double calculateIncome(GrossProfitCondition condition) {
    return Optional.ofNullable(this.getChildConditionList(condition))
        .map(list -> list.stream().mapToDouble(subCondition -> GrossProfitCalculateStrategy.strategyRegistry.get(subCondition.getTypeEnum().getCode()).calculateIncome(subCondition)).sum()).orElse(0d);
  }

  List<GrossProfitCondition> getChildConditionList(GrossProfitCondition condition);

}
