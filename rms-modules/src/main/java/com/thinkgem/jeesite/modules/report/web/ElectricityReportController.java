package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.condition.ElectricityFeeCondition;
import com.thinkgem.jeesite.modules.report.entity.ElectricityFeeVO;
import com.thinkgem.jeesite.modules.report.entity.FeeReport;
import com.thinkgem.jeesite.modules.report.entity.FeeReportTypeEnum;
import com.thinkgem.jeesite.modules.report.service.FeeReportService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
  private ElectricFeeService electricFeeService;

  private final String INIT_SMS_RECORD = "00";

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
    List<String> idList = null;
    String prefix = "";
    if (StringUtils.isNotEmpty(condition.getRoom())) {
      idList = Collections.singletonList(condition.getRoom());
    } else if (StringUtils.isNotEmpty(condition.getHouse())) {
      idList = roomService.findRoomListByHouseId(condition.getHouse()).stream().map(Room::getId).collect(Collectors.toList());
    } else if (StringUtils.isNotEmpty(condition.getBuilding())) {
      idList = houseService.findHouseListByBuildingId(condition.getBuilding()).stream().map(House::getId).map(roomService::findRoomListByHouseId)
              .flatMap(Collection::stream).map(roomService::get).map(Room::getId).collect(Collectors.toList());
      prefix = houseService.get(condition.getHouse()).getHouseNo();
    }
    if (CollectionUtils.isNotEmpty(idList)) {
      List<ElectricityFeeVO> voList = buildVOByRoomIdList(idList);
      String finalPrefix = prefix;
      if (CollectionUtils.isNotEmpty(voList)) {
        voList.forEach(vo -> vo.setName(finalPrefix + vo.getName()));
      }
      return voList;
    }
    return null;
  }

  private List<ElectricityFeeVO> buildVOByRoomIdList(List<String> idList) {
    List<FeeReport> reportList = feeReportService.getFeeReportByRoomIdList(idList, FeeReportTypeEnum.ELECTRICITY.getValue());
    if (CollectionUtils.isNotEmpty(reportList)) {
      return reportList.stream().map(this::buildVOByFeeReport).collect(Collectors.toList());
    }
    return null;
  }
  private ElectricityFeeVO buildVOByFeeReport(FeeReport report) {
    ElectricityFeeVO vo = new ElectricityFeeVO();
    vo.setRoomId(report.getRoomId());
    vo.setFee(report.getRemainFee());
    vo.setUpdateDate(DateUtils.formatDateTime(report.getUpdateDate()));
    vo.setName(roomService.get(report.getRoomId()).getRoomNo());
    return vo;
  }

  @RequestMapping(value = "save")
  @ResponseBody
  public String save(Model model) {
    saveNewRoom();
    return "true";
  }

  private void saveNewRoom() {
    List<Room> roomList = roomService.getValidFeeRoomList();
    roomList.stream().map(this::buildFeeReportByRoom).filter(Objects::nonNull).forEach(feeReportService::save);
  }

  private FeeReport buildFeeReportByRoom(Room room) {
    String result = electricFeeService.getRemainFeeByMeterNo(room.getMeterNo());
    if (StringUtils.isBlank(result) || ",,".equals(result)) {
      return null;
    }
    String[] split = result.split(",");
    FeeReport feeReport = new FeeReport();
    feeReport.setRoomId(room.getId());
    feeReport.setFeeNo(room.getMeterNo());
    feeReport.setFeeType(FeeReportTypeEnum.ELECTRICITY.getValue());
    feeReport.setRemainFee(formatSum(new Double(split[1]) * new Double(split[2])));
    feeReport.setSmsRecord(INIT_SMS_RECORD);
    feeReport.setFeeTime(DateUtils.parseDate(split[0]));
    return feeReport;
  }

  private double formatSum(Double sum) {
    BigDecimal b = new BigDecimal(sum);
    return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  @RequestMapping(value = "update")
  @ResponseBody
  public String update(Model model) {
    updateRoom();
    return "true";
  }

  public void updateRoom() {
    List<FeeReport> feeReportList = feeReportService.getFeeReportList(200);
    feeReportList.forEach(this::updateFeeReport);
  }

  public void updateFeeReport(FeeReport feeReport) {
    String result = electricFeeService.getRemainFeeByMeterNo(feeReport.getFeeNo());
    if (StringUtils.isBlank(result) || ",,".equals(result)) {
      return ;
    }
    String[] split = result.split(",");
    feeReport.setFeeTime(DateUtils.parseDate(split[0]));
    feeReport.setRemainFee(formatSum(new Double(split[1]) * new Double(split[2])));
    feeReportService.save(feeReport);
  }
}
