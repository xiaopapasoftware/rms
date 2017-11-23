package com.thinkgem.jeesite.task.service;

import com.thinkgem.jeesite.common.filter.search.MatchType;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.PropertyType;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.filter.search.builder.PropertyFilterBuilder;
import com.thinkgem.jeesite.common.filter.search.builder.SortBuilder;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.util.JsonUtil;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.report.service.RentDueUrgeReportService;
import com.thinkgem.jeesite.modules.report.service.ReportComponentService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangganggang on 17/3/12.
 *
 * @author wangganggang
 * @date 2017/03/12
 */
@Service
@Lazy(false)
public class RentDueUrgeTask {


    protected Logger logger = LoggerFactory.getLogger(RentDueUrgeTask.class);

    private static String sms_template = "尊敬的客户:您的房费即将于%s号到期。为了保障正常居住,请及时缴费。";

    @Autowired
    private SmsService smsService;

    @Autowired
    private RentDueUrgeReportService rentDueUrgeReportService;

    @Autowired
    private ReportComponentService reportComponentService;

    @Scheduled(cron = "0 0 12 * * ?")
    public void reportCurrentTime() {

        List<Sort> sorts = SortBuilder.create().addAsc("trc.start_date").end();
        List<PropertyFilter> propertyFilters = PropertyFilterBuilder.create().matchTye(MatchType.IN)
                .propertyType(PropertyType.I).add("temp.free_day", "7,3").end();
        List<Map> reportEntities = rentDueUrgeReportService.queryRentDueUrge(propertyFilters, sorts);
        reportEntities = reportComponentService.fillTenantInfo(reportEntities);
        sendMsg(reportEntities);

    }

    private void sendMsg(List<Map> maps) {
        logger.info("短信发送开始时间 : " + DateUtils.getDateTime());
        final int[] count = {0};
        List<Map> reminderList = new ArrayList<>();

        logger.debug("需要发短信的合同为 " + maps.toString());
        maps.stream().forEach(map -> {
            String expiredDate = MapUtils.getString(map, "expiredDate");
            String prePayDate = MapUtils.getString(map, "prePayDate");
            String tenantName = MapUtils.getString(map, "tenantName");
            String cellPhone = MapUtils.getString(map, "cellPhone");
            String[] tenantNames = StringUtils.split(tenantName, ";");
            String[] cellPhones = StringUtils.split(cellPhone, ";");
            for (int i = 0; tenantNames !=null && i < cellPhones.length; i++) {
                count[0]++;
                Map remindMap = new HashMap();
                remindMap.put("name", tenantNames[i]);
                remindMap.put("cellphone", cellPhones[i]);
                reminderList.add(remindMap);
                String str = cellPhones[i];
                //str = "18621509689";
                logger.debug(DateUtils.getDateTime() + "开始给" + str + "发送房租交费短信提心");
                logger.debug("发送内容为:" + String.format(sms_template, prePayDate));
                smsService.sendSms(str, String.format(sms_template, prePayDate));
                logger.debug(DateUtils.getDateTime() + "给" + str + "发送房租交费短信提心结束");
            }

        });

        logger.info(String.format("短信发送结束时间:%s;总共发送%d条,发送人信息为:%s",DateUtils.getDateTime(), count[0], JsonUtil.object2Json(reminderList)));
    }
}
