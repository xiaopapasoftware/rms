package com.thinkgem.jeesite.modules.inventory.web;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.modules.contract.web.CommonBusinessController;
import com.thinkgem.jeesite.modules.inventory.entity.*;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.service.HouseOwnerService;
import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;
import com.thinkgem.jeesite.modules.lease.entity.LeaseStatistics;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.service.OwnerService;
import com.thinkgem.jeesite.modules.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "${adminPath}/inventory/house")
public class HouseController extends CommonBusinessController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private HouseOwnerService houseOwnerService;

    @ModelAttribute
    public House get(@RequestParam(required = false) String id) {
        House entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = houseService.get(id);
        }

        if (entity == null) {
            entity = new House();
        }
        return entity;
    }

    @RequiresPermissions("inventory:house:view")
    @RequestMapping(value = {"list"})
    public String listQuery(House house, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<House> page = houseService.findPage(new Page<>(request, response), house);
        model.addAttribute("page", page);
        // 查询房屋下所有的业主信息
        if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
            for (House h : page.getList()) {
                String ownerNamesOfHouse = "";
                HouseOwner ho = new HouseOwner();
                ho.setHouseId(h.getId());
                List<HouseOwner> hos = houseOwnerService.findList(ho);
                if (CollectionUtils.isNotEmpty(hos)) {
                    for (HouseOwner tempHO : hos) {
                        Owner owner = new Owner();
                        owner.setId(tempHO.getOwnerId());
                        Owner tempO = ownerService.get(owner);
                        if (tempO != null && StringUtils.isNotEmpty(tempO.getName())) {
                            if (StringUtils.isEmpty(ownerNamesOfHouse)) {
                                ownerNamesOfHouse = tempO.getName();
                            } else {
                                ownerNamesOfHouse = ownerNamesOfHouse + "，" + tempO.getName();
                            }
                        }
                    }
                }
                h.setOwnerNamesOfHouse(ownerNamesOfHouse);
            }
        }
        initQueryCondition(model, house);
        return "modules/inventory/houseList";
    }

    @RequiresPermissions("inventory:house:view")
    @RequestMapping(value = {""})
    public String listNoQuery(House house, Model model) {
        initQueryCondition(model, house);
        return "modules/inventory/houseList";
    }

    private void initQueryCondition(Model model, House house) {
        model.addAttribute("listOwner", ownerService.findList(new Owner())); // 组装业主搜索条件值
        initPropertyProjectAndBuildings("listPropertyProject", "listBuilding", model, house.getPropertyProject());// 组装楼宇搜索条件值
    }

    @RequiresPermissions("inventory:house:view")
    @RequestMapping(value = {"findList"})
    @ResponseBody
    public List<House> findList(Building building) {
        House house = new House();
        house.setBuilding(building);
        house.setChoose(building.getChoose());// 过滤不可用
        return houseService.findList(house);
    }

    @RequestMapping(value = "exportHouse")
    public void exportHouse(Model model, House house, HttpServletResponse response) {
        String fileName = "房屋导出报表" + DateUtils.formatDate(new Date());
        List<House> houses = houseService.findList(house);

        List<HouseVO> houseVOS = houses.stream().map(h->{
            HouseVO houseVO = new HouseVO();
            BeanUtils.copyProperties(h,houseVO);
            houseVO.setProjectName(h.getPropertyProject().getProjectName());
            houseVO.setBuildingName(h.getBuilding().getBuildingName());
            houseVO.setIsFeature(DictUtils.getDictLabel(h.getIsFeature(),"yes_no",""));
            houseVO.setBuildingType(DictUtils.getDictLabel(h.getBuilding().getType(),"building_type",""));
            houseVO.setHouseStatus(DictUtils.getDictLabel(h.getHouseStatus(),"house_status",""));
            houseVO.setOriStruName("" + h.getOriStrucRoomNum() + "房" + h.getOriStrucCusspacNum() + "厅" + h.getOriStrucWashroNum() + "卫");
            houseVO.setDecoraStrucName("" + h.getDecoraStrucRoomNum() + " 房" + h.getDecoraStrucCusspacNum() + "厅" + h.getDecoraStrucWashroNum() + "卫");
            houseVO.setAlipayName(StringUtils.equals("1","" + h.getAlipayStatus())?"已同步":"未同步");
            if(StringUtils.equals("1","" + h.getAlipayStatus()) && StringUtils.equals("" + h.getUp(),"1")){
                houseVO.setAlipayStatusName("已上架");
            }else {
                houseVO.setAlipayStatusName("未上架");
            }

            return houseVO;
        }).collect(Collectors.toList());

        try {
            new ExportExcel(fileName, HouseVO.class).setDataList(houseVOS).write(response, fileName  + ".xlsx").dispose();
        } catch (Exception e) {
            addMessage(model, ViewMessageTypeEnum.ERROR, "导出房屋报表失败！失败信息：" + e.getMessage());
        }
    }

    @RequiresPermissions("inventory:house:view")
    @RequestMapping(value = "form")
    public String form(House house, Model model) {
        if (house.getIsNewRecord()) {
            Integer currentValidHouseNum = houseService.getCurrentValidHouseNum();
            currentValidHouseNum = currentValidHouseNum + 1;
            house.setHouseCode(currentValidHouseNum.toString());
            house.setShareAreaConfigList(DictUtils.convertToDictListFromSelVal(("0,1,2,3,4,8,10,11,12")));
        }
        initPropertyProjectAndBuildings("listPropertyProject", "listBuilding", model, house.getPropertyProject());
        if (!house.getIsNewRecord()) {
            house.setOwnerList(ownerService.findByHouse(house));
        }
        if (StringUtils.isNotEmpty(house.getShareAreaConfig())) {
            house.setShareAreaConfigList(DictUtils.convertToDictListFromSelVal(house.getShareAreaConfig()));
        }
        model.addAttribute("ownerList", ownerService.findList(new Owner()));
        model.addAttribute("house", house);
        setHousingFeeConfigInfo(house, house.getFeeConfigInfo());
        return "modules/inventory/houseForm";
    }

    @RequiresPermissions("inventory:house:edit")
    @RequestMapping(value = "add")
    public String add(House house, Model model) {
        Integer currentValidHouseNum = houseService.getCurrentValidHouseNum();
        currentValidHouseNum = currentValidHouseNum + 1;
        house.setHouseCode(currentValidHouseNum.toString());
        if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
            List<Building> list = new ArrayList<>();
            list.add(buildingService.get(house.getBuilding()));
            model.addAttribute("listBuilding", list);
        }
        List<PropertyProject> list = new ArrayList<>();
        list.add(propertyProjectService.get(house.getPropertyProject()));
        model.addAttribute("listPropertyProject", list);
        model.addAttribute("listOwner", ownerService.findList(new Owner()));

        List<Owner> ownerList;
        if (!house.getIsNewRecord()) {
            if (null == house.getOwner()) {
                ownerList = ownerService.findByHouse(house);
            } else {
                ownerList = new ArrayList<>();
                ownerList.add(ownerService.get(house.getOwner().getId()));
            }
            house.setOwnerList(ownerList);
        }
        if (StringUtils.isNotEmpty(house.getShareAreaConfig())) {
            house.setShareAreaConfigList(DictUtils.convertToDictListFromSelVal(house.getShareAreaConfig()));
        }
        model.addAttribute("ownerList", ownerService.findList(new Owner()));
        model.addAttribute("house", house);
        return "modules/inventory/houseAdd";
    }

    @RequiresPermissions("inventory:house:edit")
    @RequestMapping(value = "finishDirect")
    @ResponseBody
    public String finishDirect(House house) {
        House h = houseService.get(house);
        houseService.finishHouseDirect4Status(h);
        return "SUCCESS";
    }

    @RequiresPermissions("inventory:house:edit")
    @RequestMapping(value = "save")
    public String save(House house, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, house)) {
            return form(house, model);
        }
        List<House> houses = houseService.findHouseListByProPrjAndBuildingAndHouseNo(house);
        if (!house.getIsNewRecord()) {// 更新
            if (CollectionUtils.isNotEmpty(houses)) {
                house.setId(houses.get(0).getId());
            }
            if (CollectionUtils.isNotEmpty(house.getShareAreaConfigList())) {
                house.setShareAreaConfig(DictUtils.convertToStrFromList(house.getShareAreaConfigList()));
            }
            house.setFeeConfigInfo(convertToFeeStr(house));
            houseService.saveHouse(house);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改房屋信息成功");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        } else {// 新增
            if (CollectionUtils.isNotEmpty(houses)) {
                addMessage(model, ViewMessageTypeEnum.WARNING, "该物业项目及该楼宇下的房屋号已被使用，不能重复添加");
                initPropertyProjectAndBuildings("listPropertyProject", "listBuilding", model, house.getPropertyProject());
                model.addAttribute("ownerList", ownerService.findList(new Owner()));
                return "modules/inventory/houseForm";
            } else {
                house.setHouseStatus(HouseStatusEnum.TO_RENOVATION.getValue());
                String houseCode = house.getHouseCode();
                if (houseCode.contains("-")) {
                    house.setHouseCode(houseCode.split("-")[0] + "-" + (houseService.getCurrentValidHouseNum() + 1));
                } else {
                    house.setHouseCode((houseService.getCurrentValidHouseNum() + 1) + "");
                }
                if (CollectionUtils.isNotEmpty(house.getShareAreaConfigList())) {
                    house.setShareAreaConfig(DictUtils.convertToStrFromList(house.getShareAreaConfigList()));
                }
                house.setFeeConfigInfo(convertToFeeStr(house));
                houseService.saveHouse(house);
                addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存房屋信息成功");
                return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
            }
        }
    }

    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(House house, Model model) {
        JSONObject jsonObject = new JSONObject();
        List<House> houses = houseService.findHouseListByProPrjAndBuildingAndHouseNo(house);
        if (CollectionUtils.isNotEmpty(houses)) {
            if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
                List<Building> list = new ArrayList<>();
                list.add(buildingService.get(house.getBuilding()));
                model.addAttribute("listBuilding", list);
            }
            List<PropertyProject> list = new ArrayList<>();
            list.add(propertyProjectService.get(house.getPropertyProject()));
            model.addAttribute("listPropertyProject", list);
            model.addAttribute("listOwner", ownerService.findList(new Owner()));
            addMessage(jsonObject, ViewMessageTypeEnum.ERROR, "该物业项目及该楼宇下的房屋号已被使用，不能重复添加！");
        } else {
            if (StringUtils.isBlank(house.getHouseStatus()))
                house.setHouseStatus(HouseStatusEnum.TO_RENOVATION.getValue());
            String houseCode = house.getHouseCode();
            if (houseCode.contains("-")) {
                house.setHouseCode(houseCode.split("-")[0] + "-" + (houseService.getCurrentValidHouseNum() + 1));
            } else {
                house.setHouseCode((houseService.getCurrentValidHouseNum() + 1) + "");
            }
            if (CollectionUtils.isNotEmpty(house.getShareAreaConfigList())) {
                house.setShareAreaConfig(DictUtils.convertToStrFromList(house.getShareAreaConfigList()));
            }
            house.setFeeConfigInfo(convertToFeeStr(house));
            houseService.saveHouse(house);
            jsonObject.put("id", house.getId());
            jsonObject.put("name", house.getHouseNo());
        }
        return jsonObject.toString();
    }

    @RequiresPermissions("inventory:house:edit")
    @RequestMapping(value = "delete")
    public String delete(House house, RedirectAttributes redirectAttributes) {
        House queryhouse = houseService.get(house);
        String houseStatus = queryhouse.getHouseStatus();
        if (HouseStatusEnum.BE_RESERVED.getValue().equals(houseStatus) || HouseStatusEnum.PART_RENT.getValue().equals(houseStatus) || HouseStatusEnum.WHOLE_RENT.getValue().equals(houseStatus)) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋已预定或已出租，不能删除！");
        } else {
            houseService.delete(house);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除房屋、房间及其图片信息成功");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
    }
}
