package com.thinkgem.jeesite.modules.app.enums;

/**
 * 公寓类型
 *
 * @author xiao
 */
public enum BuildingTypeEnum {

    /**
     * 分散式公寓
     */
    DISPERSION("1"),

    /**
     * 集中式公寓
     */
    CONCENTRATION("2");

    BuildingTypeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
