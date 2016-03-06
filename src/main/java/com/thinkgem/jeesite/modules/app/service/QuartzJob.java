package com.thinkgem.jeesite.modules.app.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.funds.dao.PaymentOrderDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

@Service
@Lazy(false)
public class QuartzJob {
	Logger log = LoggerFactory.getLogger(QuartzJob.class);
	
	@Autowired
	private PaymentOrderDao paymentOrderDao;
	@Autowired
	private HouseDao houseDao;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private TradingAccountsService tradingAccountsService;
	@Autowired
	private DepositAgreementService depositAgreementService;
	
	@Scheduled(cron="0 */1 * * * ?")
	public void scanPaymentOrder() {
		log.info("------扫描未支付超时订单开始------");
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setOrderStatus("1");//未支付
		List<PaymentOrder> list = paymentOrderDao.findList(paymentOrder);
		Date dateNow = new Date();
		PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
		String orderTimeout = proper.getProperty("order.timeout");
		for(PaymentOrder tmpPaymentOrder : list) {
			if((dateNow.getTime()-tmpPaymentOrder.getOrderDate().getTime())/1000/60>Long.valueOf(orderTimeout)) {
				paymentOrderDao.delete(tmpPaymentOrder);
				
				//房屋、房间状态回退
				if(StringUtils.isNotBlank(tmpPaymentOrder.getHouseId())) {
					String houseId = tmpPaymentOrder.getHouseId();
					House house = houseDao.get(houseId);
					if(null != house) {
						house.setHouseStatus("1");//待出租可预订
						houseDao.updateHouseStatus(house);
					} else if(null != roomDao.get(houseId)) {
						Room room = roomDao.get(houseId);
						room.setRoomStatus("1");//待出租可预订
						roomDao.updateRoomStatus(room);
					}
				}
				
				//定金协议/合同删除
				if(StringUtils.isNoneBlank(tmpPaymentOrder.getTradeId())) {
					TradingAccounts tradingAccounts = tradingAccountsService.get(tmpPaymentOrder.getTradeId());
					if(null != tradingAccounts) {
						String depositAgreementId = tradingAccounts.getTradeId();
						if(StringUtils.isNoneBlank(depositAgreementId)) {
							DepositAgreement depositAgreement = depositAgreementService.get(depositAgreementId);
							if(null != depositAgreement) {
								depositAgreementService.delete(depositAgreement);
							}
						}
						//删除账务交易
						tradingAccountsService.delete(tradingAccounts);
					}
				}
			}
		}
		log.info("------扫描未支付超时订单结束------");
	}
}
