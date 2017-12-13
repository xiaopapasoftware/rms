package com.thinkgem.jeesite.task.service;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.entity.FeeReport;
import com.thinkgem.jeesite.modules.report.entity.FeeReportTypeEnum;
import com.thinkgem.jeesite.modules.report.service.FeeReportService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Lazy(false)
public class ElectricityFeeTask {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ElectricFeeService electricFeeService;

    @Autowired
    private FeeReportService feeReportService;

    @Autowired
    private RentContractService rentContractService;

    @Autowired
    private SmsService smsService;

    private final String INIT_SMS_RECORD = "00";

    private final int size = 200;

    /**
     * 更新下剩余费用
     */
    @Scheduled(cron = "0 0/20 1-6 * * ?")
    public void updateRoom() {
        List<FeeReport> feeReportList = feeReportService.getFeeReportList(size);
        feeReportList.forEach(this::updateFeeReport);
    }

    private void updateFeeReport(FeeReport feeReport) {
        String result = electricFeeService.getRemainFeeByMeterNo(feeReport.getFeeNo());
        RentContract rentContract = rentContractService.getByRoomId(feeReport.getRoomId());
        //整租合同以及无正确返回结果
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

    private void sendSmsByFeeReportId(String feeReportId, String differentContent) {
        FeeReport feeReport = feeReportService.get(feeReportId);
        List<String> phoneList = rentContractService.getTenantPhoneByRoomId(feeReport.getRoomId());
        String dateTime = DateUtils.formatDateTime(feeReport.getFeeTime());
        String content = "电费提醒服务：至" + dateTime + "，你的电费余额为" + feeReport.getRemainFee() + "元，" + differentContent + ",请及时充值。如您已充值，请忽略此短信。";
        if (CollectionUtils.isNotEmpty(phoneList)) {
            phoneList.forEach(phone -> smsService.sendSms(phone, content));
        }
    }

    /**
     * 将没在t_fee_report中出现过但符合条件的room保存进去
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void saveNewRoom() {
        List<Room> roomList = roomService.getValidFeeRoomList();
        roomList.stream().map(this::buildFeeReportByRoom).filter(Objects::nonNull).forEach(feeReportService::save);
    }

    private FeeReport buildFeeReportByRoom(Room room) {
        RentContract rentContract = rentContractService.getByRoomId(room.getId());
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
        feeReport.setRentContractId(rentContract.getId());
        return feeReport;
    }

    private double formatSum(Double sum) {
        BigDecimal b = new BigDecimal(sum);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}
