package com.thinkgem.jeesite.modules.contract.web;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.BaseSyncHousingModel;
import com.thinkgem.jeesite.modules.inventory.entity.*;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
    protected String convertToFeeStr(BaseHousingEntity baseHousingEntity) {
        StringBuilder resultSB = new StringBuilder();
        String feeAmt1 = baseHousingEntity.getFeeAmt1();
        String feeAmt2 = baseHousingEntity.getFeeAmt2();
        String feeAmt3 = baseHousingEntity.getFeeAmt3();
        String feeAmt4 = baseHousingEntity.getFeeAmt4();
        String feeAmt5 = baseHousingEntity.getFeeAmt5();
        String feeDesc1 = baseHousingEntity.getFeeDesc1();
        String feeDesc2 = baseHousingEntity.getFeeDesc2();
        String feeDesc3 = baseHousingEntity.getFeeDesc3();
        String feeDesc4 = baseHousingEntity.getFeeDesc4();
        String feeDesc5 = baseHousingEntity.getFeeDesc5();
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
        if (StringUtils.isNotEmpty(resultSB.toString())) {
            return resultSB.deleteCharAt(resultSB.length() - 1).toString();
        } else {
            return "";
        }
    }

    /**
     * 把数据库配置的feeConfigInfo值转为页面可渲染的费用信息
     * 配置形如：feeName1=feeAmt1,feeName2=feeAmt2,feeName3=feeAmt3,feeName4=feeAmt4,feeName5=feeAmt5
     */
    protected void setHousingFeeConfigInfo(BaseHousingEntity baseHousingEntity, String feeConfigInfo) {
        if (StringUtils.isNotEmpty(feeConfigInfo)) {
            List<BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount> list = convertFeeToList(feeConfigInfo);
            if (CollectionUtils.isNotEmpty(list)) {
                int size = list.size();
                if (baseHousingEntity != null) {
                    if (size > 0) {
                        if (list.get(0) != null) {
                            baseHousingEntity.setFeeDesc1(list.get(0).getName());
                            baseHousingEntity.setFeeAmt1(list.get(0).getAmount());
                        }
                    }
                    if (size > 1) {
                        if (list.get(1) != null) {
                            baseHousingEntity.setFeeDesc2(list.get(1).getName());
                            baseHousingEntity.setFeeAmt2(list.get(1).getAmount());
                        }
                    }
                    if (size > 2) {
                        if (list.get(2) != null) {
                            baseHousingEntity.setFeeDesc3(list.get(2).getName());
                            baseHousingEntity.setFeeAmt3(list.get(2).getAmount());
                        }
                    }
                    if (size > 3) {
                        if (list.get(3) != null) {
                            baseHousingEntity.setFeeDesc4(list.get(3).getName());
                            baseHousingEntity.setFeeAmt4(list.get(3).getAmount());
                        }
                    }
                    if (size > 4) {
                        if (list.get(4) != null) {
                            baseHousingEntity.setFeeDesc5(list.get(4).getName());
                            baseHousingEntity.setFeeAmt5(list.get(4).getAmount());
                        }
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
    protected List<BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount> convertFeeToList(String feeConfigInfo) {
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
