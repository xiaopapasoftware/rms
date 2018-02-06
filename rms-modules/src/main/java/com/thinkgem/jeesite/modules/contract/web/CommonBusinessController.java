package com.thinkgem.jeesite.modules.contract.web;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.BaseSyncHousingModel;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonBusinessController extends BaseController {

    @Autowired
    protected PropertyProjectService propertyProjectService;
    @Autowired
    protected BuildingService buildingService;
    @Autowired
    protected HouseService houseService;
    @Autowired
    protected RoomService roomService;

    protected void commonInit(String propertyProjectKeyName, String buildingKeyName, String houseKeyName, String roomKeyName, Model model, PropertyProject paraPP, Building papaBuild, House ppHouse) {
        initProperBuildAndHouse(propertyProjectKeyName, buildingKeyName, houseKeyName, model, paraPP, papaBuild);
        if (null != ppHouse && StringUtils.isNotEmpty(ppHouse.getId())) {
            Room room = new Room();
            House house = new House();
            house.setId(ppHouse.getId());
            room.setHouse(house);
            List<Room> roomList = roomService.findList(room);
            model.addAttribute(roomKeyName, roomList);
        }
    }

    protected void commonInit2(String propertyProjectKeyName, String buildingKeyName, String housekeyName, String roomKeyName, Model model, PropertyProject paraPP, Building papaBuild, House ppHouse, Room ppRoom) {
        initPropertyProjectAndBuildings(propertyProjectKeyName, buildingKeyName, model, paraPP);
        if (null != papaBuild) {
            House house = new House();
            Building building = new Building();
            building.setId(papaBuild.getId());
            house.setBuilding(building);
            house.setChoose("1");
            List<House> houseList = houseService.findList(house);
            if (null != ppHouse) houseList.add(houseService.get(ppHouse));
            model.addAttribute(housekeyName, houseList);
        }
        if (null != ppHouse) {
            Room room = new Room();
            House house = new House();
            house.setId(ppHouse.getId());
            room.setHouse(house);
            room.setChoose("1");
            List<Room> roomList = roomService.findList(room);
            if (null != ppRoom) {
                Room rm = roomService.get(ppRoom);
                if (null != rm) roomList.add(rm);
            }
            model.addAttribute(roomKeyName, CollectionUtils.isNotEmpty(roomList) ? roomList : Lists.newArrayList());
        }
    }

    protected void initProperBuildAndHouse(String propertyKeyName, String buildingKeyName, String houseKeyName, Model model, PropertyProject paraPP, Building papaBuild) {
        initPropertyProjectAndBuildings(propertyKeyName, buildingKeyName, model, paraPP);
        if (null != papaBuild && StringUtils.isNotEmpty(papaBuild.getId())) {
            House house = new House();
            Building building = new Building();
            building.setId(papaBuild.getId());
            house.setBuilding(building);
            List<House> houseList = houseService.findList(house);
            model.addAttribute(houseKeyName, houseList);
        }
    }

    protected void initPropertyProjectAndBuildings(String propertyKeyName, String buildingKeyName, Model model, PropertyProject paraPP) {
        model.addAttribute(propertyKeyName, propertyProjectService.findList(new PropertyProject()));
        setBuildingToModel(buildingKeyName, model, paraPP);
    }

    private void setBuildingToModel(String keyName, Model model, PropertyProject paraPP) {
        if (null != paraPP && StringUtils.isNotEmpty(paraPP.getId())) {
            Building building = new Building();
            PropertyProject propertyProject = new PropertyProject();
            propertyProject.setId(paraPP.getId());
            building.setPropertyProject(propertyProject);
            List<Building> buildingList = buildingService.findList(building);
            model.addAttribute(keyName, buildingList);
        }
    }

    /**
     * 处理房源的费用配置信息，转为可存到数据库的数据。
     * 配置形如：feeName1=feeAmt1,feeName2=feeAmt2,feeName3=feeAmt3,feeName4=feeAmt4,feeName5=feeAmt5
     */
    protected String collectFeesToConifg(House house, Room room) {
        StringBuilder resultSB = new StringBuilder();
        String feeDesc1 = null;
        String feeAmt1 = null;
        String feeDesc2 = null;
        String feeAmt2 = null;
        String feeDesc3 = null;
        String feeAmt3 = null;
        String feeDesc4 = null;
        String feeAmt4 = null;
        String feeDesc5 = null;
        String feeAmt5 = null;
        if (house != null) {
            feeAmt1 = house.getFeeAmt1();
            feeAmt2 = house.getFeeAmt2();
            feeAmt3 = house.getFeeAmt3();
            feeAmt4 = house.getFeeAmt4();
            feeAmt5 = house.getFeeAmt5();
            feeDesc1 = house.getFeeDesc1();
            feeDesc2 = house.getFeeDesc2();
            feeDesc3 = house.getFeeDesc3();
            feeDesc4 = house.getFeeDesc4();
            feeDesc5 = house.getFeeDesc5();
        }
        if (room != null) {
            feeAmt1 = room.getFeeAmt1();
            feeAmt2 = room.getFeeAmt2();
            feeAmt3 = room.getFeeAmt3();
            feeAmt4 = room.getFeeAmt4();
            feeAmt5 = room.getFeeAmt5();
            feeDesc1 = room.getFeeDesc1();
            feeDesc2 = room.getFeeDesc2();
            feeDesc3 = room.getFeeDesc3();
            feeDesc4 = room.getFeeDesc4();
            feeDesc5 = room.getFeeDesc5();
        }
        if (StringUtils.isNotEmpty(feeDesc1) && StringUtils.isNotEmpty(feeAmt1)) {
            resultSB.append(feeDesc1).append("=").append(feeAmt1).append(",");
        }
        if (StringUtils.isNotEmpty(feeDesc2) && StringUtils.isNotEmpty(feeAmt2)) {
            resultSB.append(feeDesc2).append("=").append(feeAmt2).append(",");
        }
        if (StringUtils.isNotEmpty(feeDesc3) && StringUtils.isNotEmpty(feeAmt3)) {
            resultSB.append(feeDesc3).append("=").append(feeAmt3).append(",");
        }
        if (StringUtils.isNotEmpty(feeDesc4) && StringUtils.isNotEmpty(feeAmt4)) {
            resultSB.append(feeDesc4).append("=").append(feeAmt4).append(",");
        }
        if (StringUtils.isNotEmpty(feeDesc5) && StringUtils.isNotEmpty(feeAmt5)) {
            resultSB.append(feeDesc5).append("=").append(feeAmt5).append(",");
        }
        return resultSB.deleteCharAt(resultSB.length() - 1).toString();
    }

    /**
     * 把数据库配置的feeConfigInfo值转为页面可渲染的费用信息
     * 配置形如：feeName1=feeAmt1,feeName2=feeAmt2,feeName3=feeAmt3,feeName4=feeAmt4,feeName5=feeAmt5
     */
    protected void collectFeesToConifg(House house, Room room, String feeConfigInfo) {
        if (StringUtils.isNotEmpty(feeConfigInfo)) {
            List<BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount> list = collectFeesToConifg(feeConfigInfo);
            if (house != null) {
                if (CollectionUtils.isNotEmpty(list)) {
                    if (list.get(0) != null) {
                        house.setFeeDesc1(list.get(0).getName());
                        house.setFeeAmt1(list.get(0).getAmount());
                    }
                    if (list.get(1) != null) {
                        house.setFeeDesc2(list.get(1).getName());
                        house.setFeeAmt2(list.get(1).getAmount());
                    }
                    if (list.get(2) != null) {
                        house.setFeeDesc3(list.get(2).getName());
                        house.setFeeAmt3(list.get(2).getAmount());
                    }
                    if (list.get(3) != null) {
                        house.setFeeDesc4(list.get(3).getName());
                        house.setFeeAmt4(list.get(3).getAmount());
                    }
                    if (list.get(4) != null) {
                        house.setFeeDesc5(list.get(4).getName());
                        house.setFeeAmt5(list.get(4).getAmount());
                    }
                }
            }
            if (room != null) {
                if (CollectionUtils.isNotEmpty(list)) {
                    if (list.get(0) != null) {
                        room.setFeeDesc1(list.get(0).getName());
                        room.setFeeAmt1(list.get(0).getAmount());
                    }
                    if (list.get(1) != null) {
                        room.setFeeDesc2(list.get(1).getName());
                        room.setFeeAmt2(list.get(1).getAmount());
                    }
                    if (list.get(2) != null) {
                        room.setFeeDesc3(list.get(2).getName());
                        room.setFeeAmt3(list.get(2).getAmount());
                    }
                    if (list.get(3) != null) {
                        room.setFeeDesc4(list.get(3).getName());
                        room.setFeeAmt4(list.get(3).getAmount());
                    }
                    if (list.get(4) != null) {
                        room.setFeeDesc5(list.get(4).getName());
                        room.setFeeAmt5(list.get(4).getAmount());
                    }
                }
            }
        }
    }

    /**
     * 把数据库配置的feeConfigInfo值转为页面可渲染的费用信息
     * 配置形如：feeName1=feeAmt1,feeName2=feeAmt2,feeName3=feeAmt3,feeName4=feeAmt4,feeName5=feeAmt5
     * 转化为List<BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount>
     */
    protected List<BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount> collectFeesToConifg(String feeConfigInfo) {
        List<BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(feeConfigInfo)) {
            String[] array1 = feeConfigInfo.split(",");
            if (ArrayUtils.isNotEmpty(array1)) {
                for (String ele1 : array1) {
                    String[] array2 = ele1.split("=");
                    if (ArrayUtils.isNotEmpty(array2)) {
                        BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount feeEle = new BaseSyncHousingModel().new AlipayEcoRenthouseOtherAmount();
                        feeEle.setName(array2[0]);
                        feeEle.setAmount(array2[1]);
                        list.add(feeEle);
                    }
                }
            }
        }
        return list;
    }

}
