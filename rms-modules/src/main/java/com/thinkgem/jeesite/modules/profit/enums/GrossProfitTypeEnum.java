package com.thinkgem.jeesite.modules.profit.enums;

public enum GrossProfitTypeEnum {

    COUNTY("COUNTY", "区县"),CENTER("CENTER", "服务中心"),AREA("AREA", "区域"),PROJECT("PROJECT", "物业项目"),BUILDING("BUILDING", "楼宇"),HOUSE("HOUSE", "房屋"),SUM("SUM", "合计");

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
