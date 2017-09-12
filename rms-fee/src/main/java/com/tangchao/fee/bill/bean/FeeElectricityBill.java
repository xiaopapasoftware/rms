package com.tangchao.fee.bill.bean;

import com.tangchao.fee.bill.common.FeeBillBase;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangganggang
 * @date 2017年09月07日 下午8:25
 */
@Table(name = "Fee_electricity_bill")
@Data
public class FeeElectricityBill extends FeeBillBase {

    /*户号*/
    @Column(name = "house_ele_num")
    private String houseEleNum;

    /*账单日期*/
    @Column(name = "ele_bill_date")
    private String eleBillDate;

    /*账单金额*/
    @Column(name = "ele_bill_amount")
    private String eleBillAmount;

    /*峰值数*/
    @Column(name = "ele_peak_degree")
    private String elePeakDegree;

    /*谷值数*/
    @Column(name = "ele_valley_degree")
    private String eleValleyDegree;

}
