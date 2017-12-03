package com.thinkgem.jeesite.modules.lease.impl;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.cache.MyCache;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.ContractBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.AccountingService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.lease.LeaseCalculate;
import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProjectLeaseCalculate implements LeaseCalculate {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RentContractService rentContractService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Autowired
    private AccountingService accountingService;

    private MyCache leaseCache = MyCacheBuilder.getInstance().getScheduledCache("lease");

    private static final String SINGLE = "SINGLE";

    private static final String WHOLE = "WHOLE";

    @Override
    public List<LeaseStatisticsCondition> getChildConditionList(LeaseStatisticsCondition condition) {
        return null;
    }

    @Override
    public String getName(LeaseStatisticsCondition condition) {
        return Optional.ofNullable(propertyProjectService.getPropertyProjectById(condition.getId())).map(PropertyProject::getProjectName).orElse("");
    }

    public int getTotalRooms(LeaseStatisticsCondition condition) {
        return roomService.queryRoomsCountByProjectPropertyId(condition.getEndDate(), condition.getId());
    }

    public int getLeasedRooms(LeaseStatisticsCondition condition) {
        Set<String> roomsSet = new HashSet<>();
        //单间合同
        roomsSet.addAll(getSingleRoomContractList(condition).stream().map(contract -> contract.getRoom().getId()).collect(Collectors.toSet()));
        //整租合同
        roomsSet.addAll(getWholeRoomContractList(condition).stream().map(RentContract::getHouse).map(House::getId).flatMap(id -> roomService.findRoomListByHouseId(id).stream()).map(Room::getId).collect(Collectors.toSet()));
        return roomsSet.size();
    }

    public double getRentSum(LeaseStatisticsCondition condition) {
        Map<String, List<Double>> roomRent = getRoomRent(condition);
        double rentSum = 0d;
        for (Map.Entry<String, List<Double>> entry : roomRent.entrySet()) {
            List<Double> rentList = entry.getValue();
            if (rentList.size() == 1) {
                rentSum += rentList.get(0);
            } else {
                rentSum += rentList.stream().reduce(0d, (a, b) -> a + b) / rentList.size();
            }
        }
        return rentSum;
    }

    //得到每个roomId下的房租列表
    private Map<String, List<Double>> getRoomRent(LeaseStatisticsCondition condition) {
        Map<String, List<Double>> roomRent = getSingleRoomContractList(condition).stream().collect(Collectors.groupingBy(i -> i.getRoom().getId(), Collectors.mapping(RentContract::getRental, Collectors.toList())));
        for (RentContract rentContract : getWholeRoomContractList(condition)) {
            List<Room> roomList = roomService.findRoomListByHouseId(rentContract.getHouse().getId());
            int size = roomList.size();
            Double rental = rentContract.getRental();
            if (size != 0) {
                rental = rental / size;
            }
            for (Room room : roomList) {
                if (roomRent.get(room.getId()) != null) {
                    roomRent.get(room.getId()).add(rental);
                } else {
                    List<Double> list = new ArrayList<>();
                    list.add(rental);
                    roomRent.put(room.getId(), list);
                }
            }
        }
        return roomRent;
    }

    @SuppressWarnings("unchecked")
    private List<RentContract> getSingleRoomContractList(LeaseStatisticsCondition condition) {
        String cacheKey = buildCacheKey(SINGLE, condition);
        if (leaseCache.getObject(cacheKey) == null) {
            leaseCache.putObject(cacheKey, getContractList(condition).stream().filter(contract -> RentModelTypeEnum.JOINT_RENT.getValue().equals(contract.getRentMode())).collect(Collectors.toList()));
        }
        return (List<RentContract>) leaseCache.getObject(cacheKey);
    }

    @SuppressWarnings("unchecked")
    private List<RentContract> getWholeRoomContractList(LeaseStatisticsCondition condition) {
        String cacheKey = buildCacheKey(WHOLE, condition);
        if (leaseCache.getObject(cacheKey) == null) {
            leaseCache.putObject(cacheKey, getContractList(condition).stream().filter(contract -> RentModelTypeEnum.WHOLE_RENT.getValue().equals(contract.getRentMode())).collect(Collectors.toList()));
        }
        return (List<RentContract>) leaseCache.getObject(cacheKey);
    }

    private String buildCacheKey(String model, LeaseStatisticsCondition condition) {
        return model + condition.getId() + DateUtils.formatDate(condition.getStartDate()) + DateUtils.formatDate(condition.getEndDate());
    }

    private List<RentContract> getContractList(LeaseStatisticsCondition condition) {
        return rentContractService.queryContractListByProjectIdAndDate(condition.getId(), condition.getStartDate(), condition.getEndDate())
                .stream().filter(contract -> this.judgeValid(contract, condition.getStartDate())).collect(Collectors.toList());
    }

    //判断非正常合同的真正有效期
    private boolean judgeValid(RentContract rentContract, Date startDate){
        if (!ContractBusiStatusEnum.VALID.getValue().equals(rentContract.getContractStatus())) {
            Date feeDate = accountingService.getFeeDateByContractId(rentContract.getId());
            if (feeDate != null && startDate.after(feeDate)) {
                return false;
            }
        }
        return true;
    }

}
