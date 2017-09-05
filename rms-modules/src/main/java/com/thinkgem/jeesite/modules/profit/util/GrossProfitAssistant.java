package com.thinkgem.jeesite.modules.profit.util;

import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;

import java.util.Calendar;
import java.util.Date;

public class GrossProfitAssistant {

    public static int[] parseDateToYMD(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int year = instance.get(Calendar.YEAR);
        //月份从0计数
        int month = instance.get(Calendar.MONTH) + 1;
        int day = instance.get(Calendar.DAY_OF_MONTH);
        return new int[]{year, month, day};
    }

    public static String getCacheKey(GrossProfitCondition condition) {
        return condition.getTypeEnum().getCode() + condition.getId();
    }

}
