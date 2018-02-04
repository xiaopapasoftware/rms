package com.thinkgem.jeesite.modules.app.entity;

/**
 * 支付宝租房房源同步对象-集中式
 */
public class FocusSynchronizeHousingModel extends BaseSyncHousingModel {

    private String nick_name;
    private String max_amount;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(String max_amount) {
        this.max_amount = max_amount;
    }
}
