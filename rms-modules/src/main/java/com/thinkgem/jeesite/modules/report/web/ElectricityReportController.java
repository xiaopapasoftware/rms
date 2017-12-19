package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
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
import java.util.*;
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

  @Autowired
  private RentContractService rentContractService;

  @Autowired
  private SmsService smsService;

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
    if (StringUtils.isNotEmpty(condition.getRoom())) {
      idList = Collections.singletonList(condition.getRoom());
    } else if (StringUtils.isNotEmpty(condition.getHouse())) {
      idList = roomService.findRoomListByHouseId(condition.getHouse()).stream().map(Room::getId).collect(Collectors.toList());
    } else if (StringUtils.isNotEmpty(condition.getBuilding())) {
      List<Room> roomList = houseService.findHouseListByBuildingId(condition.getBuilding()).stream().map(House::getId).map(roomService::findRoomListByHouseId)
              .flatMap(Collection::stream).map(roomService::get).collect(Collectors.toList());
      idList = roomList.stream().map(Room::getId).collect(Collectors.toList());
    }
    if (CollectionUtils.isNotEmpty(idList)) {
      return buildVOByRoomIdList(idList, condition.getMinValue(), condition.getMaxValue());
    }
    return null;
  }

  private List<ElectricityFeeVO> buildVOByRoomIdList(List<String> idList, Double minValue, Double maxValue) {
    List<FeeReport> reportList = feeReportService.getFeeReportByRoomIdList(idList, minValue, maxValue);
    if (CollectionUtils.isNotEmpty(reportList)) {
      return reportList.stream().map(this::buildVOByFeeReport).collect(Collectors.toList());
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
    RentContract rentContract = rentContractService.getByRoomId(room.getId());
    String result = electricFeeService.getRemainFeeByMeterNo(room.getMeterNo());
    if (rentContract == null || RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode()) || StringUtils.isBlank(result) || ",,".equals(result)) {
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
    feeReport.setRentContractId(rentContract.getId());
    Room tempRoom = roomService.get(room.getId());
    feeReport.setFullName(tempRoom.getPropertyProject().getProjectName() + "-" + tempRoom.getBuilding().getBuildingName() + "-" + tempRoom.getHouse().getHouseNo() + "-" + tempRoom.getRoomNo());
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

  private void updateRoom() {
    List<FeeReport> feeReportList = feeReportService.getFeeReportList(200);
    feeReportList.forEach(this::updateFeeReport);
  }

  private void updateFeeReport(FeeReport feeReport) {
    String result = electricFeeService.getRemainFeeByMeterNo(feeReport.getFeeNo());
    RentContract rentContract = rentContractService.getByRoomId(feeReport.getRoomId());
    //整租合同进行逻辑删除
    if (rentContract == null || RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {
      feeReport.setDelFlag("1");
      feeReportService.save(feeReport);
      return;
    }
    //无正确返回结果
    if (StringUtils.isBlank(result) || ",,".equals(result) || RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {
      //更新下时间，表明更新过
      feeReportService.save(feeReport);
      return ;
    }
    String[] split = result.split(",");
    feeReport.setFeeTime(DateUtils.parseDate(split[0]));
    feeReport.setRemainFee(formatSum(new Double(split[1]) * new Double(split[2])));
    checkRentContractId(rentContract.getId(), feeReport);
    checkSmsTime(feeReport);
    judgeSendSms(feeReport);
    feeReportService.save(feeReport);
  }

  //如果是新合同则重置短信发送记录
  private void checkRentContractId(String rentContractId, FeeReport feeReport) {
    if (!rentContractId.equals(feeReport.getRentContractId())) {
      feeReport.setRentContractId(rentContractId);
      feeReport.setSmsTime(null);
      feeReport.setSmsRecord(INIT_SMS_RECORD);
    }
  }

  //过了一个月短信记录重置
  private void checkSmsTime(FeeReport feeReport) {
    if (feeReport.getSmsTime() != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(feeReport.getSmsTime());
      int smsTimeMonth = calendar.get(Calendar.MONTH);
      calendar.setTime(new Date());
      int now = calendar.get(Calendar.MONTH);
      if (smsTimeMonth != now) {
        feeReport.setSmsRecord(INIT_SMS_RECORD);
        feeReport.setSmsTime(null);
      }
    }
  }

  private void judgeSendSms(FeeReport feeReport) {
    if (feeReport.getRemainFee() < 10d && feeReport.getSmsRecord().charAt(0) == '0') {
      sendSmsByFeeReportId(feeReport.getId(),"已少于10元，避免造成用电不便");
      feeReport.setSmsRecord("1" + feeReport.getSmsRecord().substring(1));
      feeReport.setSmsTime(new Date());
    } else if (feeReport.getRemainFee() < 30d && feeReport.getSmsRecord().charAt(1) == '0') {
      sendSmsByFeeReportId(feeReport.getId(),"已少于30元，避免造成用电不便");
      feeReport.setSmsRecord(feeReport.getSmsRecord().substring(0, 1) + "1");
      feeReport.setSmsTime(new Date());
    }
  }

  @RequestMapping(value = "sendSms")
  @ResponseBody
  public String sendSms(String id) {
    sendSmsByFeeReportId(id, "即将断电或已断电");
    return "true";
  }

  private void sendSmsByFeeReportId(String feeReportId, String differentContent) {
    FeeReport feeReport = feeReportService.get(feeReportId);
    List<String> phoneList = rentContractService.getTenantPhoneByRoomId(feeReport.getRoomId());
    String dateTime = DateUtils.formatDateTime(feeReport.getFeeTime());
    String content = "电费提醒服务：至" + dateTime + "，你的电费余额为" + feeReport.getRemainFee() + "元，" + differentContent + ",请及时充值。如您已充值，请忽略此短信。";
    if (CollectionUtils.isNotEmpty(phoneList)) {
      phoneList.forEach(phone -> smsService.sendSms(phone, content));
    }
  }

}
