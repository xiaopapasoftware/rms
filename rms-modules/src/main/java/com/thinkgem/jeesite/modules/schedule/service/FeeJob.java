package com.thinkgem.jeesite.modules.schedule.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Created by mabindong on 2015/12/10.
 */
@Service
@Lazy(false)
@Deprecated
public class FeeJob {


    //催缴房租 每天18:00执行
    //@Scheduled(cron="0 0 18 * * *")
    public void rendReminderJob(){
        System.out.println("do rendReminderJob 催缴房租");
    }

    //电费到期提醒 每天18:00执行
    //@Scheduled(cron="0 0 18 * * *")
    public void electricityReminderJob(){
        System.out.println("do electricityReminderJob 电费到期提醒");
    }
}
