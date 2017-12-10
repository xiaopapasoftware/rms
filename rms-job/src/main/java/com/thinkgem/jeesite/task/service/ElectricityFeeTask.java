package com.thinkgem.jeesite.task.service;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.entity.FeeReport;
import com.thinkgem.jeesite.modules.report.entity.FeeReportTypeEnum;
import com.thinkgem.jeesite.modules.report.service.FeeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    private final String INIT_SMS_RECORD = "00";

    private final int size = 200;

    /**
     * 更新下剩余费用
     */
    @Scheduled(cron = "0 0/20 1-8 * * ?")
    public void updateRoom() {
        List<FeeReport> feeReportList = feeReportService.getFeeReportList(size);
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

    /**
     * 将没在t_fee_report中出现过但符合条件的room保存进去
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void saveNewRoom() {
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


}
