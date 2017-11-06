package com.thinkgem.jeesite.modules.fee.enums;

/**
 * @author wangganggang
 * @date 2017年09月28日 下午9:29
 */
public enum OrderStatusEnum {
    //申请
    APP(0,"申请"),
    // 待审批
    COMMIT(1,"待缴费"),
    // 同意
    PASS(2,"已缴3"),
    //驳回
    REJECT(3,"驳回");

    private int value;
    private String name;

    OrderStatusEnum(int status, String name){
        this.value = status;
        this.name = name;
    }

    public int getValue(){
        return value;
    }

    public String getName(){
        return name;
    }

    public static OrderStatusEnum fromValue(int value){
        for(OrderStatusEnum val : OrderStatusEnum.values()){
            if(val.getValue() == value){
                return val;
            }
        }
        return null;
    }
}
