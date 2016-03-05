package com.thinkgem.jeesite.modules.app.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.modules.funds.dao.PaymentOrderDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;

@Service
@Lazy(false)
public class QuartzJob {
	Logger log = LoggerFactory.getLogger(QuartzJob.class);
	
	@Autowired
	private PaymentOrderDao paymentOrderDao;
	
	@Scheduled(cron="0 */1 * * * ?")
	public void scanPaymentOrder() {
		log.info("------扫描未支付超时订单开始------");
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setOrderStatus("1");//未支付
		List<PaymentOrder> list = paymentOrderDao.findList(paymentOrder);
		Date dateNow = new Date();
		for(PaymentOrder tmpPaymentOrder : list) {
			if((dateNow.getTime()-tmpPaymentOrder.getOrderDate().getTime())/1000/60>15) {
				paymentOrderDao.delete(tmpPaymentOrder);
			}
		}
		log.info("------扫描未支付超时订单结束------");
	}
}
