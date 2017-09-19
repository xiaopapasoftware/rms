package com.thinkgem.jeesite.modules.profit.enums;

public enum GrossProfitTypeEnum {

    County("County", "区县"),Center("Center", "服务中心"),Area("Area", "区域"),Project("Project", "物业项目"),Building("Building", "楼宇"),House("House", "房屋");

    private String code;

    private String desc;

    GrossProfitTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
