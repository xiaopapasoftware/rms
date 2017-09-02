package com.thinkgem.jeesite.modules.profit;


import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import com.thinkgem.jeesite.modules.profit.impl.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GrossProfitCalculateStrategy implements InitializingBean{

    @Autowired
    private CompanyGrossProfitCalculate companyGrossProfitCalculate;

    @Autowired
    private CenterGrossProfitCalculate centerGrossProfitCalculate;

    @Autowired
    private AreaGrossProfitCalculate areaGrossProfitCalculate;

    @Autowired
    private ProjectGrossProfitCalculate projectGrossProfitCalculate;

    @Autowired
    private HouseGrossProfitCalculate houseGrossProfitCalculate;

    private volatile Map<String, GrossProfitCalculate> strategyRegistry = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyRegistry.put(GrossProfitTypeEnum.Company.getCode(), companyGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.Center.getCode(), centerGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.Area.getCode(), areaGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.Project.getCode(), projectGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.House.getCode(), houseGrossProfitCalculate);
    }

    public GrossProfitReport calculateGrossProfit(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).calculateGrossProfit(condition);
    }

    public double calculateIncome(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).calculateIncome(condition);
    }

    public double calculateCost(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).calculateCost(condition);
    }

    public void registerStrategyIfAbsent(String name, GrossProfitCalculate calculate) {
        strategyRegistry.putIfAbsent(name, calculate);
    }

    public void registerStrategy(String name, GrossProfitCalculate calculate) {
        strategyRegistry.put(name, calculate);
    }

}
