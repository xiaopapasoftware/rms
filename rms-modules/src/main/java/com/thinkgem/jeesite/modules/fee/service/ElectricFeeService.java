package com.thinkgem.jeesite.modules.fee.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.*;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class ElectricFeeService extends CrudService<ElectricFeeDao, ElectricFee> {
    @Autowired
    private PaymentTransService paymentTransService;
    @Autowired
    private RentContractDao rentContractDao;
    @Autowired
    private RoomDao roomDao;

    public ElectricFee get(String id) {
        return super.get(id);
    }

    public List<ElectricFee> findList(ElectricFee electricFee) {
        areaScopeFilter(electricFee, "dsf", "tp.area_id=sua.area_id");
        return super.findList(electricFee);
    }

    public Page<ElectricFee> findPage(Page<ElectricFee> page, ElectricFee electricFee) {
        areaScopeFilter(electricFee, "dsf", "tp.area_id=sua.area_id");
        return super.findPage(page, electricFee);
    }

    @Transactional(readOnly = false)
    public void save(ElectricFee electricFee) {
        Date nowDate = new Date();
        paymentTransService.deletePaymentTransAndTradingAcctounsWithChargeFee(electricFee.getId());
        String id = paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.ELECTRICITY_CHARGE.getValue(), PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue(), electricFee.getRentContractId(),
                TradeDirectionEnum.IN.getValue(), electricFee.getChargeAmount(), electricFee.getChargeAmount(), 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), nowDate, nowDate, null);
        electricFee.setChargeDate(nowDate);
        electricFee.setChargeStatus(ElectricChargeStatusEnum.PROCESSING.getValue());
        electricFee.setPaymentTransId(id);
        electricFee.setSettleStatus(FeeSettlementStatusEnum.NOT_SETTLED.getValue());
        super.save(electricFee);
    }

    @Transactional(readOnly = false)
    public void update(ElectricFee electricFee) {
        super.save(electricFee);
    }

    /**
     * 查看电表消费情况
     *
     * @param beginDate 开始日期 格式：2015-11-01
     * @param endDate   结束日期 格式：2015-11-21
     * @return Map<String,String> 返回结果为：
     * 0=直接存放未经处理过的电表系统的返回值；
     * 1=个人住户使用掉的总电量（度）；
     * 2= 公共区域使用掉的总电量（度）；
     * 3=该智能电表还剩余的总可用电量（度）；
     * 4=个人住户电量单价（元/度）；
     * 5=公共区域电量单价（元/度）；
     */
    public Map<Integer, String> getMeterFee(String rentContractId, String beginDate, String endDate) {
        Map<Integer, String> resultMap = new HashMap();
        RentContract rentContract = rentContractDao.get(rentContractId);
        String meterNo = "";
        if (null != rentContract && RentModelTypeEnum.JOINT_RENT.getValue().equals(rentContract.getRentMode())) {
            Room room = rentContract.getRoom();
            room = roomDao.get(room);
            meterNo = room.getMeterNo();
        }
        getMeterFee(meterNo, beginDate, endDate, resultMap);
        return resultMap;
    }

    public void getMeterFee(String meterNo, String beginDate, String endDate, Map<Integer, String> resultMap) {
        // 电表系统返回值
        String result = "";
        if (StringUtils.isNotBlank(meterNo)) {
            String meterUrl = Global.getConfig("meter.remain.url") + "read_all_val.action?addr=" + meterNo + "&startDate=" + beginDate + "&endDate=" + endDate;
            try {
                logger.debug("get electric fee request is:{}", meterUrl);
                result = openHttpsConnection(meterUrl, "UTF-8", 600000, 600000);
                logger.debug("get electric fee response is:{}", result);
                if (!StringUtils.isBlank(result)) {
                    // 人工在结尾添加,
                    result = result + ",";
                    Pattern p = Pattern.compile("(.*?)\\,(.*?)");
                    Matcher m = p.matcher(result);
                    int i = 0;
                    while (m.find()) {
                        i++;
                        resultMap.put(i, StringUtils.isEmpty(m.group(1)) ? "0" : m.group(1));
                    }
                }
            } catch (Exception e) {
                logger.error("call meter get fee error:", e);
            }
        }
        // 直接存放智能电表系统的返回值
        resultMap.put(0, result);
    }

    /**
     * 获得剩余电费
     */
    public String getRemainFeeByMeterNo(String meterNo) {
        String result = "";
        if (StringUtils.isNotBlank(meterNo)) {
            String url = Global.getConfig("meter.remain.url") + "read_remain_val.action?addr=" + meterNo;
            logger.debug("call meter remain fee requestUrl is:{}", url);
            try {
                result = openHttpsConnection(url, "UTF-8", 60000, 60000);
                logger.debug("call meter remain fee response  is:{}", result);
            } catch (Exception e) {
                logger.error("read_remain_val errors!" + url, e);
            }
        }
        return result;
    }

    @Transactional(readOnly = false)
    public void delete(ElectricFee electricFee) {
        super.delete(electricFee);
    }

    private String openHttpsConnection(final String urlPath, String charset, int connectTimeout, int readTimeout) throws Exception {
        HttpURLConnection conn;
        URL url;
        try {
            url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; " + "SailBrowser; Maxthon; Alexa Toolbar; .NET CLR 2.0.50727)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName(charset)));
            String str = null;
            StringBuffer returnStr = new StringBuffer(200);
            while ((str = reader.readLine()) != null) {
                returnStr.append(str);
            }
            conn.disconnect();
            return returnStr.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            conn = null;
            url = null;
        }
    }
}
