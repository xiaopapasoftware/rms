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
public class FeeBase extends BaseModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "property_id")
    private String propertyId;

    @Column(name = "house_id")
    private String houseId;

}
