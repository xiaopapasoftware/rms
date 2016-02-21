/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.service;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 电费结算Service
 * 
 * @author huangsc
 * @version 2015-07-04
 */
@Service
@Transactional(readOnly = true)
public class ElectricFeeService extends CrudService<ElectricFeeDao, ElectricFee> {
    @Autowired
    private PaymentTransDao paymentTransDao;
    @Autowired
    private RentContractDao rentContractDao;
    @Autowired
    private RoomDao roomDao;

    public ElectricFee get(String id) {
	return super.get(id);
    }

    public List<ElectricFee> findList(ElectricFee electricFee) {
	return super.findList(electricFee);
    }

    public Page<ElectricFee> findPage(Page<ElectricFee> page, ElectricFee electricFee) {
	return super.findPage(page, electricFee);
    }

    @Transactional(readOnly = false)
    public void save(ElectricFee electricFee) {
	Date nowDate = new Date();
	/* 生成款项 */
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.setId(IdGen.uuid());
	paymentTrans.setTradeType("11");// 电费充值
	paymentTrans.setPaymentType("11");// 电费自用金额
	paymentTrans.setTransId(electricFee.getRentContractId());// 合同
	paymentTrans.setTradeDirection("1");// 收款
	paymentTrans.setStartDate(nowDate);
	paymentTrans.setExpiredDate(nowDate);
	paymentTrans.setTradeAmount(electricFee.getChargeAmount());
	paymentTrans.setLastAmount(electricFee.getChargeAmount());
	paymentTrans.setTransAmount(0D);
	paymentTrans.setTransStatus("0");// 未到账登记
	paymentTrans.setCreateDate(nowDate);
	paymentTrans.setCreateBy(UserUtils.getUser());
	paymentTrans.setUpdateDate(nowDate);
	paymentTrans.setUpdateBy(UserUtils.getUser());
	paymentTrans.setDelFlag("0");
	if (0 != paymentTrans.getTradeAmount()) {
	    paymentTransDao.insert(paymentTrans);
	}
	electricFee.setChargeDate(nowDate);
	electricFee.setChargeStatus("0");// 充值中
	electricFee.setPaymentTransId(paymentTrans.getId());
	electricFee.setSettleStatus("0");// 待结算
	super.save(electricFee);
    }

    /**
     * 查看电表消费情况
     * 
     * @param beginDate
     *            开始日期 格式：2015-11-01
     * @param endDate
     *            结束日期 格式：2015-11-21
     * @return Map<String,String> 返回结果为：0=直接存放未经处理过的电表系统的返回值；1=个人住户使用掉的总电量（度）；2=
     *         公共区域使用掉的总电量（度）；
     *         3=该智能电表还剩余的总可用电量（度）；4=个人住户电量单价（元/度）；5=公共区域电量单价（元/度）；
     */
    public Map<Integer, String> getMeterFee(String rentContractId, String beginDate, String endDate) {
	Map<Integer, String> resultMap = new HashMap<Integer, String>();
	RentContract rentContract = rentContractDao.get(rentContractId);
	String meterNo = "";
	if (null != rentContract && "1".equals(rentContract.getRentMode())) {// 单间
	    Room room = rentContract.getRoom();
	    room = roomDao.get(room);
	    meterNo = room.getMeterNo();
	}
	String result = "";// 电表系统返回值
	if (!StringUtils.isBlank(meterNo)) {
	    String meterurl = new PropertiesLoader("jeesite.properties").getProperty("meter.url") + "read_all_val.action?addr=" + meterNo + "&startDate=" + beginDate + "&endDate=" + endDate;
	    try {
		result = openHttpsConnection(meterurl, "UTF-8", 600000, 600000);
		logger.info("call meter get fee result:" + result);
		if (!StringUtils.isBlank(result)) {
		    result = result + ",";// 人工在结尾添加,
		    Pattern p = Pattern.compile("(.*?)\\,(.*?)");
		    Matcher m = p.matcher(result);
		    int i = 0;
		    while (m.find()) {
			i++;
			resultMap.put(i, StringUtils.isEmpty(m.group(1)) ? "0" : m.group(1));
		    }
		}
	    } catch (Exception e) {
		this.logger.error("call meter get fee error:", e);
	    }
	}
	resultMap.put(0, result);// 直接存放智能电表系统的返回值
	return resultMap;
    }

    @Transactional(readOnly = false)
    public void delete(ElectricFee electricFee) {
	super.delete(electricFee);
    }

    private String openHttpsConnection(final String urlPath, String charset, int connectTimeout, int readTimeout) throws Exception {
	HttpURLConnection conn = null;
	URL url = null;
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