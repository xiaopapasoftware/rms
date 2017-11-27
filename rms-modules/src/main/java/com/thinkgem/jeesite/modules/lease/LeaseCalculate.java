package com.thinkgem.jeesite.modules.lease;

import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;

import java.util.List;
import java.util.Optional;

public interface LeaseCalculate {

    String getName(LeaseStatisticsCondition condition);

    default int getTotalRooms(LeaseStatisticsCondition condition) {
        return Optional.ofNullable(this.getChildConditionList(condition))
                .map(list -> list.stream().mapToInt(subCondition -> LeaseCalculateStrategy.strategyRegistry.get(subCondition.getType()).getTotalRooms(subCondition)).sum())
                .orElse(0);
    }

    default int getLeasedRooms(LeaseStatisticsCondition condition) {
        return Optional.ofNullable(this.getChildConditionList(condition))
                .map(list -> list.stream().mapToInt(subCondition -> LeaseCalculateStrategy.strategyRegistry.get(subCondition.getType()).getLeasedRooms(subCondition)).sum())
                .orElse(0);
    }

    default double getRentSum(LeaseStatisticsCondition condition) {
        return Optional.ofNullable(this.getChildConditionList(condition))
                .map(list -> list.stream().mapToDouble(subCondition -> LeaseCalculateStrategy.strategyRegistry.get(subCondition.getType()).getRentSum(subCondition)).sum())
                .orElse(0d);
    }

    List<LeaseStatisticsCondition> getChildConditionList(LeaseStatisticsCondition condition);

}
