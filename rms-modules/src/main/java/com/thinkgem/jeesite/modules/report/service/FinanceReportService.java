package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.utils.MapKeyHandle;
import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import com.thinkgem.jeesite.modules.report.dao.FinanceReportDao;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangganggang on 17/2/26.
 */
@Service
public class FinanceReportService {

    @Autowired
    private FinanceReportDao financeReportDao;

    public List queryFinace(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return financeReportDao.queryFinance(new Criterion(propertyFilters, sorts));
    }

    public List convertInFinance(List<Map> financeData) {
        List<Map> dataList = new ArrayList<>();
        financeData.stream().collect(Collectors.groupingBy(map -> MapUtils.getString(map,"trade_id")))
        .forEach((k,v) ->{
            Map data = new HashMap(v.get(0));
            data.put("house_amount", 0);
            data.put("water_deposit", 0);
            data.put("house_deposit", 0);
            data.put("agree_amount", 0);
            data.put("service_amount", 0);
            data.put("net_amount", 0);
            data.put("water_amount", 0);
            data.put("tv_amount", 0);
            final double[] totalAmount = {0};
            v.forEach(m -> {
                int paymentType = MapUtils.getInteger(m, "payment_type");
                double receiptAmount = MapUtils.getDoubleValue(m,"receipt_amount",0);
                switch (paymentType){
                    case 6:
                        data.put("house_amount", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    case 2:
                    case 3:
                        data.put("water_deposit", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    case 4:
                    case 5:
                        data.put("house_deposit", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    case 0:
                        data.put("agree_amount", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    case 22:
                        data.put("service_amount", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    case 20:
                        data.put("net_amount", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    case 24:
                        data.put("water_amount", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    case 18:
                        data.put("tv_amount", receiptAmount);
                        totalAmount[0] += receiptAmount;
                        break;
                    default:

                }
            });
            data.put("total_amount",totalAmount[0]);
            dataList.add(MapKeyHandle.keyToJavaProperty(data));
        });
        return dataList;
    }


    public List convertOutFinance(List<Map> financeData) {
        List<Map> dataList = new ArrayList<>();
        financeData.stream().collect(Collectors.groupingBy(map -> MapUtils.getString(map,"trade_id")))
                .forEach((k,v) ->{
                    Map data = new HashMap(v.get(0));
                    data.put("water_deposit_refund", 0);
                    data.put("house_deposit_refund", 0);
                    data.put("house_refund", 0);
                    data.put("ele_refund", 0);
                    data.put("water_refund", 0);
                    data.put("net_refund", 0);
                    data.put("tv_refund", 0);
                    data.put("other_refund", 0);
                    final double[] totalAmount = {0};
                    v.forEach(m -> {
                        int paymentType = MapUtils.getInteger(m, "payment_type");
                        double receiptAmount = MapUtils.getDoubleValue(m,"receipt_amount");
                        switch (paymentType){
                            case 2:
                                data.put("water_deposit_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                            case 4:
                                data.put("house_deposit_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                            case 7:
                                data.put("house_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                            case 13:
                                data.put("ele_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                            case 15:
                                data.put("water_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                            case 20:
                                data.put("net_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                            case 18:
                                data.put("tv_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                            default:
                                data.put("other_refund", receiptAmount);
                                totalAmount[0] += receiptAmount;
                                break;
                        }
                    });
                    data.put("total_amount",totalAmount[0]);
                    dataList.add(MapKeyHandle.keyToJavaProperty(data));
                });
        return dataList;
    }

    public Map calculateInTotalAmount(List<Map> financeData){
        Map totalMap =new HashMap();
        financeData.stream().forEach(map -> {
            totalMap.put("sumTotalAmount", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumTotalAmount",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"totalAmount",0.0))));

            totalMap.put("sumHouseAmount", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumHouseAmount",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"houseAmount",0.0))));

            totalMap.put("sumHouseDeposit", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumHouseDeposit",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"houseDeposit",0.0))));

            totalMap.put("sumWaterDeposit", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumWaterDeposit",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"waterDeposit",0.0))));

            totalMap.put("sumAgreeAmount", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumAgreeAmount",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"agreeAmount",0.0))));

            totalMap.put("sumServiceAmount", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumServiceAmount",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"serviceAmount",0.0))));

            totalMap.put("sumNetAmount", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumNetAmount",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"netAmount",0.0))));

            totalMap.put("sumWaterAmount", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumWaterAmount",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"waterAmount",0.0))));

            totalMap.put("sumTvAmount", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumTvAmount",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"tvAmount",0.0))));
        });
        return totalMap;
    }

    public Map calculateOutTotalAmount(List<Map> financeData){
        Map totalMap =new HashMap();
        financeData.stream().forEach(map -> {
            totalMap.put("sumWaterDepositRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumWaterDepositRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"waterDepositRefund",0.0))));

            totalMap.put("sumHouseDepositRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumHouseDepositRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"houseDepositRefund",0.0))));

            totalMap.put("sumHouseRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumHouseRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"houseRefund",0.0))));

            totalMap.put("sumEleRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumEleRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"eleRefund",0.0))));

            totalMap.put("sumWaterRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumWaterRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"waterRefund",0.0))));

            totalMap.put("sumNetRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumNetRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"netRefund",0.0))));

            totalMap.put("sumTvRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumTvRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"tvRefund",0.0))));

            totalMap.put("sumOtherRefund", BigDecimal.valueOf(MapUtils.getDouble(totalMap,"sumOtherRefund",0.0))
                    .add(BigDecimal.valueOf(MapUtils.getDouble(map,"otherRefund",0.0))));
        });
        return totalMap;
    }


}
