package com.thinkgem.jeesite.modules.app.entity;

/**
 * 支付宝租房房源同步对象-集中式
 */
public class FocusSynchronizeHousingModel extends BaseSyncHousingModel {

    private String all_room_num;//针对某房型的集中式单间总数，例如统计F店下b房型总数。F店下有两种房型a（40间），b（60间），则该值就是b房型（60间）。

    private String can_rent_num;//可租数量，也是针对该店下该房型的可租数量

    public String getAll_room_num() {
        return all_room_num;
    }

    public void setAll_room_num(String all_room_num) {
        this.all_room_num = all_room_num;
    }

    public String getCan_rent_num() {
        return can_rent_num;
    }

    public void setCan_rent_num(String can_rent_num) {
        this.can_rent_num = can_rent_num;
    }
}

