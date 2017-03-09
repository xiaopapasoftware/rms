package com.thinkgem.jeesite.modules.schedule.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by mabindong on 2015/12/10.
 */
@Service
@Lazy(false)
public class ContractJob {

    //合同房租 每天18:00执行
    @Scheduled(cron="0 0 18 * * *")
    public void contractExpireReminderJob(){
        System.out.println("do contractExpireReminderJob 合同到期房租");
    }


}
