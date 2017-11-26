package com.thinkgem.jeesite.modules.lease.impl;

import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.lease.LeaseCalculate;
import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectLeaseCalculate implements LeaseCalculate {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RentContractService rentContractService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Override
    public List<LeaseStatisticsCondition> getChildConditionList(LeaseStatisticsCondition condition) {
        return null;
    }

    @Override
    public String getName(LeaseStatisticsCondition condition) {
        return Optional.ofNullable(propertyProjectService.getPropertyProjectById(condition.getId())).map(PropertyProject::getProjectName).orElse("");
    }

    public int getTotalRooms(LeaseStatisticsCondition condition) {
        return roomService.queryRoomsByProjectPropertyId(condition.getId());
    }

    public int getLeasedRooms(LeaseStatisticsCondition condition) {
        //单间合同数量就是出租房间数量
        int singleRooms = rentContractService.queryContractListByProjectId(condition.getId(), condition.getDate(), RentModelTypeEnum.JOINT_RENT.getValue()).size();
        //找到整租合同,再找到房屋下的房间数
        List<RentContract> wholeContractList = rentContractService.queryContractListByProjectId(condition.getId(), condition.getDate(), RentModelTypeEnum.WHOLE_RENT.getValue());
        int wholeRooms = wholeContractList.stream().map(RentContract::getHouse).map(House::getId).mapToInt(roomService::queryRoomsByHouseId).sum();
        return singleRooms + wholeRooms;
    }

    public double getRentSum(LeaseStatisticsCondition condition) {
        List<RentContract> singleContractList = rentContractService.queryContractListByProjectId(condition.getId(), condition.getDate(), RentModelTypeEnum.JOINT_RENT.getValue());
        List<RentContract> wholeContractList = rentContractService.queryContractListByProjectId(condition.getId(), condition.getDate(), RentModelTypeEnum.WHOLE_RENT.getValue());
        double singleMoney = singleContractList.stream().mapToDouble(RentContract::getRental).sum();
        double wholeMoney = wholeContractList.stream().mapToDouble(RentContract::getRental).sum();
        return singleMoney + wholeMoney;
    }

}
