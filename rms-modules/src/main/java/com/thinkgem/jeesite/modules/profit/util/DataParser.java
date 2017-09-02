package com.thinkgem.jeesite.modules.profit.util;

import com.thinkgem.jeesite.modules.profit.entity.GrossProfitNumDeposit;
import java.util.Date;

public class DataParser {

    //以2000为基数
    private static final int BASE_YEAR = 2000;

    private static final int MONTHS = 12;

    //以25为分隔
    private static final int SPLIT_DAY = 25;

    public static GrossProfitNumDeposit parse(Date startDate, Date endDate, Double deposit) {
        //日是否大于等于25标记
        boolean flag = false;
        int[] start = GrossProfitAssistant.parseDateToYMD(startDate);
        int[] end = GrossProfitAssistant.parseDateToYMD(endDate);
        if (start[2] >= SPLIT_DAY) {
            flag = true;
        }
        return new GrossProfitNumDeposit(calculateNum(start, flag), calculateNum(end, flag) - 1, deposit);
    }

    public static int calculateNum(int[] ymd, boolean flag) {
        if (flag) {
            return (ymd[0] - BASE_YEAR) * MONTHS + ymd[1] + 1;
        } else {
            return (ymd[0] - BASE_YEAR) * MONTHS + ymd[1];
        }
    }

}
