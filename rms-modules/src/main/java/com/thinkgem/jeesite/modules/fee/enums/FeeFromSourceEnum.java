package com.thinkgem.jeesite.modules.fee.enums;

/**
 * @author wangganggang
 * @date 2017年10月11日 14:51
 */
public enum FeeFromSourceEnum {
    //抄表
    READ_METER(0,"抄表"),
    // 账单
    ACCOUNT_BILL(1,"账单"),
    // 固定
    SYSTEM_FIX_SET(2,"固定");

    private int value;
    private String name;

    FeeFromSourceEnum(int status, String name){
        this.value = status;
        this.name = name;
    }

    public int getValue(){
        return value;
    }

    public String getName(){
        return name;
    }

    public static FeeBillStatusEnum fromValue(int value){
        for(FeeBillStatusEnum val : FeeBillStatusEnum.values()){
            if(val.getValue() == value){
                return val;
            }
        }
        return null;
    }
}
