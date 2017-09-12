package com.tangchao.fee.bill.common;

import com.tangchao.common.base.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author wangganggang
 * @date 2017年09月07日 下午8:55
 */
@Data
public class FeeBillBase extends BaseModel {

    /*主键*/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /*审核批次号*/
    @Column(name = "batch_no")
    private String batchNo;

    /*物业ID*/
    @Column(name = "property_id")
    private String propertyId;

    /*房屋ID*/
    @Column(name = "house_id")
    private String houseId;

    /*状态*/
    @Column(name = "bill_status")
    private String billStatus;

}
