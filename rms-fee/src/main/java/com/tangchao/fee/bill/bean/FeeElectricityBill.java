package com.tangchao.fee.bill.bean;

import com.tangchao.fee.bill.common.FeeBase;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author wangganggang
 * @date 2017年09月07日 下午8:25
 */
@Table(name = "Fee_electricity_bill")
@Data
public class FeeElectricityBill extends FeeBase {

    @Column(name = "house_ele_num")
    private String houseEleNum;

    @Column(name = "ele_bill_date")
    private String eleBillDate;

    @Column(name = "ele_bill_amount")
    private String eleBillAmount;

    @Column(name = "ele_degree")
    private String eleDegree;

    @Column(name = "ele_peak_degree")
    private String elePeakDegree;

    @Column(name = "ele_valley_degree")
    private String eleValleyDegree;

    @Column(name = "bill_status")
    private String billStatus;
}
