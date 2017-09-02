package com.thinkgem.jeesite.modules.profit.enums;

public enum AreaTypeEnum {

    COMPANY("3"),

    CENTER("5"),

    AREA("6");

    AreaTypeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
