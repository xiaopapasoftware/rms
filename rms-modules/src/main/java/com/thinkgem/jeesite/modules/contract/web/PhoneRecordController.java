package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.PhoneRecord;
import com.thinkgem.jeesite.modules.contract.service.PhoneRecordService;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiao
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/phoneRecord")
public class PhoneRecordController extends BaseController {

  @Autowired
  private PhoneRecordService phoneRecordService;

  @Autowired
  private PropertyProjectService propertyProjectService;

  @Autowired
  private BuildingService buildingService;

  @Autowired
  private HouseService houseService;

  @Autowired
  private RoomService roomService;

  /**
   * 拨号记录报表-查询
   */
  @RequiresPermissions("contract:phoneRecord:view")
  @RequestMapping(value = {""})
  public String initPage(PhoneRecord phoneRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
    return "modules/contract/phoneRecordList";
  }

  @RequiresPermissions("contract:phoneRecord:view")
  @RequestMapping(value = {"list"})
  public String list(PhoneRecord phoneRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
    initSearchConditions(phoneRecord, model, request, response);
    model.addAttribute("page", phoneRecordService.findPage(new Page<>(request, response), phoneRecord));
    return "modules/contract/phoneRecordList";
  }

  private void initSearchConditions(PhoneRecord phoneRecord, Model model, HttpServletRequest request, HttpServletResponse response) {
    model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
    if (StringUtils.isNotBlank(phoneRecord.getBuildingId())) {
      model.addAttribute("buildingList", buildingService.getBuildingListByProjectId(phoneRecord.getProjectId()));
    }
    if (StringUtils.isNotBlank(phoneRecord.getHouseId())) {
      model.addAttribute("houseList", houseService.findHouseListByBuildingId(phoneRecord.getBuildingId()));
    }
    if (StringUtils.isNotBlank(phoneRecord.getRoomId())) {
      model.addAttribute("roomList", roomService.findRoomListByHouseId(phoneRecord.getHouseId()));
    }
  }

}
