package com.thinkgem.jeesite.modules.inventory.enums;

/**
 * 房源朝向
 */
public enum OrientationTypeEnum {

    /**
     * 东
     */
    EAST("0"),

    /**
     * 南
     */
    SOUTH("1"),

    /**
     * 西
     */
    WEST("2"),

    /**
     * 北
     */
    NORTH("3"),

    /**
     * 东南
     */
    EAST_SOUTH("4"),

    /**
     * 东北
     */
    EAST_NORTH("5"),

    /**
     * 西南
     */
    WEST_SOUTH("6"),

    /**
     * 西北
     */
    WEST_NORTH("7"),

    /**
     * 东西
     */
    EAST_WEST("8"),

    /**
     * 南北
     */
    SOUTH_NORTH("9");

    OrientationTypeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
