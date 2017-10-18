package com.thinkgem.jeesite.modules.fee.enums;

/**
 * @author wangganggang
 * @date 2017年09月28日 9:39
 */
public enum FeeTypeEnum {
    /**
     * 0：电费单价 1:电费谷单价 2:电费峰单价 3:宽带单价 4:有线电视费单价 5:水费单价 6：燃气费单价
     */
    ELECTRICITY_UNIT(0, "电费单价"),
    ELE_VALLEY_UNIT(1, "电费谷单价"),
    ELE_PEAK_UNIT(2, "电费峰单价"),
    NET_UNIT(3, "宽带单价"),
    TV_UNIT(4, "有线电视费单价"),
    WATER_UNIT(5, "水费单价"),
    GAS_UNIT(6, "燃气费单价");

    private int value;
    private String name;

    FeeTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
