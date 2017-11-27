package com.thinkgem.jeesite.modules.lease;

import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;
import com.thinkgem.jeesite.modules.lease.entity.LeaseStatisticsVO;
import com.thinkgem.jeesite.modules.lease.impl.AreaLeaseCalculate;
import com.thinkgem.jeesite.modules.lease.impl.CenterLeaseCalculate;
import com.thinkgem.jeesite.modules.lease.impl.CountyLeaseCalculate;
import com.thinkgem.jeesite.modules.lease.impl.ProjectLeaseCalculate;
import com.thinkgem.jeesite.modules.lease.util.LeaseStatisticsVOBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LeaseCalculateStrategy implements InitializingBean {

    @Autowired
    private CountyLeaseCalculate countyLeaseCalculate;

    @Autowired
    private CenterLeaseCalculate centerLeaseCalculate;

    @Autowired
    private AreaLeaseCalculate areaLeaseCalculate;

    @Autowired
    private ProjectLeaseCalculate projectLeaseCalculate;

    public volatile static Map<String, LeaseCalculate> strategyRegistry = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyRegistry.put(SelectItemConstants.COUNTY, countyLeaseCalculate);
        strategyRegistry.put(SelectItemConstants.CENTER, centerLeaseCalculate);
        strategyRegistry.put(SelectItemConstants.AREA, areaLeaseCalculate);
        strategyRegistry.put(SelectItemConstants.PROJECT, projectLeaseCalculate);
    }

    public String getName(LeaseStatisticsCondition condition) {
        return strategyRegistry.get(condition.getType()).getName(condition);
    }

    public LeaseStatisticsVO calculateLease(LeaseStatisticsCondition condition) {
        return new LeaseStatisticsVOBuilder().name(getName(condition)).totalRooms(getTotalRooms(condition)).leasedRooms(getLeasedRooms(condition))
                .rentSum(getRentSum(condition)).build();
    }

    public int getTotalRooms(LeaseStatisticsCondition condition) {
        return strategyRegistry.get(condition.getType()).getTotalRooms(condition);
    }

    public int getLeasedRooms(LeaseStatisticsCondition condition) {
        return strategyRegistry.get(condition.getType()).getLeasedRooms(condition);
    }

    public double getRentSum(LeaseStatisticsCondition condition) {
        return strategyRegistry.get(condition.getType()).getRentSum(condition);
    }

    public void registerStrategyIfAbsent(String name, LeaseCalculate calculate) {
        strategyRegistry.putIfAbsent(name, calculate);
    }

    public void registerStrategy(String name, LeaseCalculate calculate) {
        strategyRegistry.put(name, calculate);
    }


}
