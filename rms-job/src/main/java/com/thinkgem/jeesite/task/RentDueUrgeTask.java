package com.thinkgem.jeesite.task;

import com.thinkgem.jeesite.common.filter.search.MatchType;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.PropertyType;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.filter.search.builder.PropertyFilterBuilder;
import com.thinkgem.jeesite.common.filter.search.builder.SortBuilder;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.MapKeyHandle;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.util.JsonUtil;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.report.service.RentDueUrgeReportService;
import com.thinkgem.jeesite.modules.report.service.ReportComponentSrervice;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
@Component
public class RentDueUrgeTask {

    protected Logger logger = LoggerFactory.getLogger(RentDueUrgeTask.class);

    private static String sms_template = "尊敬的客户:您的房费即将于%s号到期。为了保障正常居住,请及时缴费。";

    @Autowired
    private SmsService smsService;

    @Autowired
    private RentDueUrgeReportService rentDueUrgeReportService;

    @Autowired
    private ReportComponentSrervice reportComponentSrervice;

    @Scheduled(cron = "${rent.due.urge.cron}")
    public void reportCurrentTime() {
        List<Sort> sorts = SortBuilder.create().addAsc("trc.start_date").end();
        List<PropertyFilter> propertyFilters = PropertyFilterBuilder.create().matchTye(MatchType.IN)
                .propertyType(PropertyType.I).add("temp.free_day", "7,15").end();
        List<Map> reportEntities = rentDueUrgeReportService.queryRentDueUrge(propertyFilters, sorts);
        reportComponentSrervice.fillTenantInfo(reportEntities);
        reportEntities = MapKeyHandle.keyToJavaProperty(reportEntities);

        sendMsg(reportEntities);

    }

    private void sendMsg(List<Map> maps) {
        logger.info("短信发送开始时间 : " + DateUtils.getDateTime());
        final int[] count = {0};
        List<Map> reminderList = new ArrayList<>();

        logger.debug("需要发短信的合同为 " + maps.toString());
        maps.stream().forEach(map -> {
            String expiredDate = MapUtils.getString(map, "expiredDate");
            String tenantNameLead = MapUtils.getString(map, "tenantNameLead");
            String cellPhoneLead = MapUtils.getString(map, "cellPhoneLead");
            String[] tenantNames = StringUtils.split(tenantNameLead, ";");
            String[] cellPhones = StringUtils.split(cellPhoneLead, ";");
            for (int i = 0; i < cellPhones.length; i++) {
                count[0]++;
                Map remindMap = new HashMap();
                remindMap.put("name", tenantNames[i]);
                remindMap.put("cellphone", cellPhones[i]);
                reminderList.add(remindMap);
                String str = cellPhones[i];
                //str = "18621509689";
                logger.debug(DateUtils.getDateTime() + "开始给" + str + "发送房租交费短信提心");
                smsService.sendSms(str, String.format(sms_template, expiredDate));
                logger.debug(DateUtils.getDateTime() + "给" + str + "发送房租交费短信提心结束");
            }

        });

        logger.info(String.format("短信发送结束时间:%s;总共发送%d条,发送人信息为:%s",DateUtils.getDateTime(), count[0], JsonUtil.object2Json(reminderList)));
    }
}