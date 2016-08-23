package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 交易款项状态
 * 
 * @author wangshujin
 *
 */
public enum PaymentTransStatusEnum {

    /**
     * 未到账登记
     */
    NO_SIGN("0"),

    /**
     * 部分到账登记
     */
    PART_SIGN("1"),

    /**
     * 完全到账登记
     */
    WHOLE_SIGN("2");

    PaymentTransStatusEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }

}