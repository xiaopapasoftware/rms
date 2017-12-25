package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.enums.AreaTypeEnum;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cache.MyCache;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.condition.ElectricityFeeCondition;
import com.thinkgem.jeesite.modules.report.entity.ElectricityFeeVO;
import com.thinkgem.jeesite.modules.report.entity.FeeReport;
import com.thinkgem.jeesite.modules.report.service.FeeReportService;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiao
 */
@Controller
@RequestMapping(value = "${adminPath}/report/electricity")
public class ElectricityReportController extends BaseController {

  @Autowired
  private SelectItemService selectItemService;

  @Autowired
  private FeeReportService feeReportService;

  @Autowired
  private RoomService roomService;

  @Autowired
  private HouseService houseService;

  @Autowired
  private RentContractService rentContractService;

  @Autowired
  private SmsService smsService;

  @Autowired
  private BuildingService buildingService;

  @Autowired
  private AreaService areaService;

  @Autowired
  private PropertyProjectService propertyProjectService;

  private MyCache idListCache = MyCacheBuilder.getInstance().getScheduledCache("electricityReport");

  /**
   * 电费催缴统计报表-查询
   */
  @RequestMapping(value = "index")
  public String index(Model model) {
    model.addAttribute("countyList", getCountyList());
    return "modules/report/electricity/electricityFee";
  }

  private List<SelectItem> getCountyList() {
    SelectItemCondition condition = new SelectItemCondition();
    condition.setBusiness(SelectItemConstants.ORG);
    condition.setType(SelectItemConstants.COUNTY);
    return selectItemService.getSelectListByBusinessCode(condition);
  }

  @RequestMapping(value = "list")
  @ResponseBody
  public List<ElectricityFeeVO> list(ElectricityFeeCondition condition) {
    String cacheKey = buildCacheKey(condition);
    List<String> idList = (List<String>)idListCache.getObject(cacheKey);
    if (CollectionUtils.isEmpty(idList)) {
      idList = getIdListFromCondition(condition);
    }
    if (CollectionUtils.isNotEmpty(idList)) {
      idListCache.putObject(cacheKey, idList);
      return buildVOByRoomIdList(idList, condition.getMinValue(), condition.getMaxValue());
    }
    return null;
  }

  private String buildCacheKey(ElectricityFeeCondition condition) {
    return condition.getCounty() + "-" + condition.getCenter() + "-" + condition.getArea() + "-" + condition.getProject()
            + "-" + condition.getBuilding() + "-" + condition.getHouse()  + "-" + condition.getRoom();
  }

  private List<String> getIdListFromCondition(ElectricityFeeCondition condition) {
    List<Room> roomList = new ArrayList<>();
    if (StringUtils.isNotEmpty(condition.getRoom())) {
      return Collections.singletonList(condition.getRoom());
    } else if (StringUtils.isNotEmpty(condition.getHouse())) {
      roomList = roomService.findRoomListByHouseId(condition.getHouse());
    } else if (StringUtils.isNotEmpty(condition.getBuilding())) {
      roomList = houseService.findHouseListByBuildingId(condition.getBuilding()).stream()
              .map(House::getId).map(roomService::findRoomListByHouseId)
              .flatMap(Collection::stream).collect(Collectors.toList());
    } else if (StringUtils.isNotEmpty(condition.getProject())) {
      roomList = buildingService.getBuildingListByProjectId(condition.getProject()).stream()
              .map(building -> houseService.findHouseListByBuildingId(building.getId()))
              .flatMap(Collection::stream).map(House::getId).map(roomService::findRoomListByHouseId)
              .flatMap(Collection::stream).collect(Collectors.toList());
    } else if (StringUtils.isNotEmpty(condition.getArea())) {
      roomList = propertyProjectService.getPropertyProjectByAreaId(condition.getArea()).stream()
              .map(project -> buildingService.getBuildingListByProjectId(project.getId()))
              .flatMap(Collection::stream).map(building -> houseService.findHouseListByBuildingId(building.getId()))
              .flatMap(Collection::stream).map(House::getId).map(roomService::findRoomListByHouseId)
              .flatMap(Collection::stream).collect(Collectors.toList());
    } else if (StringUtils.isNotEmpty(condition.getCenter())) {
      roomList = areaService.getAreaByParentId(condition.getCenter(), AreaTypeEnum.AREA.getValue()).stream()
              .map(area -> propertyProjectService.getPropertyProjectByAreaId(area.getId()))
              .flatMap(Collection::stream).map(project -> buildingService.getBuildingListByProjectId(project.getId()))
              .flatMap(Collection::stream).map(building -> houseService.findHouseListByBuildingId(building.getId()))
              .flatMap(Collection::stream).map(House::getId).map(roomService::findRoomListByHouseId)
              .flatMap(Collection::stream).collect(Collectors.toList());
    }  else if (StringUtils.isNotEmpty(condition.getCounty())) {
      roomList = areaService.getAreaByParentId(condition.getCenter(), AreaTypeEnum.CENTER.getValue()).stream()
              .map(center -> areaService.getAreaByParentId(condition.getCenter(), AreaTypeEnum.AREA.getValue()))
              .flatMap(Collection::stream).map(area -> propertyProjectService.getPropertyProjectByAreaId(area.getId()))
              .flatMap(Collection::stream).map(project -> buildingService.getBuildingListByProjectId(project.getId()))
              .flatMap(Collection::stream).map(building -> houseService.findHouseListByBuildingId(building.getId()))
              .flatMap(Collection::stream).map(House::getId).map(roomService::findRoomListByHouseId)
              .flatMap(Collection::stream).collect(Collectors.toList());
    }
    return roomList.stream().map(Room::getId).collect(Collectors.toList());
  }

  private List<ElectricityFeeVO> buildVOByRoomIdList(List<String> idList, Double minValue, Double maxValue) {
    List<FeeReport> reportList = feeReportService.getFeeReportByRoomIdList(idList, minValue, maxValue);
    if (CollectionUtils.isNotEmpty(reportList)) {
      return reportList.parallelStream().map(this::buildVOByFeeReport).collect(Collectors.toList());
    }
    return null;
  }
  private ElectricityFeeVO buildVOByFeeReport(FeeReport report) {
    ElectricityFeeVO vo = new ElectricityFeeVO();
    vo.setId(report.getId());
    vo.setRoomId(report.getRoomId());
    vo.setFee(report.getRemainFee());
    vo.setUpdateDate(DateUtils.formatDateTime(report.getFeeTime()));
    vo.setName(report.getFullName());
    return vo;
  }

  @RequestMapping(value = "sendSms")
  @ResponseBody
  public String sendSms(String id) {
    sendSmsByFeeReportId(id);
    return "true";
  }

  private void sendSmsByFeeReportId(String feeReportId) {
    FeeReport feeReport = feeReportService.get(feeReportId);
    List<String> phoneList = rentContractService.getTenantPhoneByRoomId(feeReport.getRoomId());
    String dateTime = DateUtils.formatDateTime(feeReport.getFeeTime());
    String content = "电费提醒服务：至" + dateTime + "，你的电费余额为" + feeReport.getRemainFee() + "元，即将断电或已断电,请及时充值。如您已充值，请忽略此短信。";
    if (CollectionUtils.isNotEmpty(phoneList)) {
      phoneList.forEach(phone -> smsService.sendSms(phone, content));
    }
  }

}
