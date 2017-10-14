package com.thinkgem.jeesite.modules.profit;

import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReportVO;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import com.thinkgem.jeesite.modules.profit.impl.*;
import com.thinkgem.jeesite.modules.profit.util.GrossProfitReportBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GrossProfitCalculateStrategy implements InitializingBean{

    @Autowired
    private CountyGrossProfitCalculate countyGrossProfitCalculate;

    @Autowired
    private CenterGrossProfitCalculate centerGrossProfitCalculate;

    @Autowired
    private AreaGrossProfitCalculate areaGrossProfitCalculate;

    @Autowired
    private ProjectGrossProfitCalculate projectGrossProfitCalculate;

    @Autowired
    private BuildingGrossProfitCalculate buildingGrossProfitCalculate;

    @Autowired
    private HouseGrossProfitCalculate houseGrossProfitCalculate;

    public volatile static Map<String, GrossProfitCalculate> strategyRegistry = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyRegistry.put(GrossProfitTypeEnum.COUNTY.getCode(), countyGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.CENTER.getCode(), centerGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.AREA.getCode(), areaGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.PROJECT.getCode(), projectGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.BUILDING.getCode(), buildingGrossProfitCalculate);
        strategyRegistry.put(GrossProfitTypeEnum.HOUSE.getCode(), houseGrossProfitCalculate);
    }

    public void registerStrategyIfAbsent(String name, GrossProfitCalculate calculate) {
        strategyRegistry.putIfAbsent(name, calculate);
    }

    public void registerStrategy(String name, GrossProfitCalculate calculate) {
        strategyRegistry.put(name, calculate);
    }

    public double calculateCost(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).calculateCost(condition);
    }

    public double calculateIncome(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).calculateIncome(condition);
    }

    public GrossProfitReport calculateGrossProfit(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).calculateGrossProfit(condition);
    }

    public List<GrossProfitCondition> getChildConditionList(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).getChildConditionList(condition);
    }

    public String getName(GrossProfitCondition condition) {
        return strategyRegistry.get(condition.getTypeEnum().getCode()).getName(condition);
    }

    public GrossProfitReportVO calculateReportVO(GrossProfitCondition condition) {
        GrossProfitReportVO reportVO = new GrossProfitReportVO();
        List<GrossProfitCondition> childConditionList = getChildConditionList(condition);
        if (!CollectionUtils.isEmpty(childConditionList)) {
            reportVO.setChildReportList(childConditionList.parallelStream().map(this::calculateGrossProfit).collect(Collectors.toList()));
            reportVO.setParent(buildParentByChildList(condition, reportVO.getChildReportList()));
        } else {
            reportVO.setParent(calculateGrossProfit(condition));
        }
        return reportVO;
    }

    private GrossProfitReport buildParentByChildList(GrossProfitCondition condition, List<GrossProfitReport> childReportList) {
        return new GrossProfitReportBuilder().typeEnum(condition.getTypeEnum())
                                             .cost(childReportList.stream().mapToDouble(GrossProfitReport::getCost).sum())
                                             .income(childReportList.stream().mapToDouble(GrossProfitReport::getIncome).sum())
                                             .name(getName(condition))
                                             .build();
    }

    private GrossProfitReport buildSumByChildList(GrossProfitCondition condition, List<GrossProfitReport> childReportList) {
        return new GrossProfitReportBuilder().typeEnum(condition.getTypeEnum())
                .cost(childReportList.stream().mapToDouble(GrossProfitReport::getCost).sum())
                .income(childReportList.stream().mapToDouble(GrossProfitReport::getIncome).sum())
                .name(getName(condition))
                .build();
    }

    public GrossProfitReportVO calculateReportCompareVO(List<GrossProfitCondition> conditionList) {
        GrossProfitReportVO reportVO = new GrossProfitReportVO();
        if (!CollectionUtils.isEmpty(conditionList)) {
            List<GrossProfitReport> childReportList = conditionList.parallelStream().map(this::calculateGrossProfit).collect(Collectors.toList());
            reportVO.setChildReportList(childReportList);
            reportVO.setParent(new GrossProfitReportBuilder().typeEnum(GrossProfitTypeEnum.SUM)
                    .cost(childReportList.stream().mapToDouble(GrossProfitReport::getCost).sum())
                    .income(childReportList.stream().mapToDouble(GrossProfitReport::getIncome).sum())
                    .name("合计")
                    .build());
        }
        return reportVO;
    }
}
