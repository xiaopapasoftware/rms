package com.thinkgem.jeesite.modules.profit.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class GrossProfitAssistant {

    public static String getProfitPercent(double cost, double income) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if ((income -cost) == 0d) {
            return "0";
        } else {
        return decimalFormat.format((income - cost) * 100 / income);
        }
    }

    public static int[] parseDateToYMD(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int year = instance.get(Calendar.YEAR);
        //月份从0计数
        int month = instance.get(Calendar.MONTH) + 1;
        int day = instance.get(Calendar.DAY_OF_MONTH);
        return new int[]{year, month, day};
    }

}
