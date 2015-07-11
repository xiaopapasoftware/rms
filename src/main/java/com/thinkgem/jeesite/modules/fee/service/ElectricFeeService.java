/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
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
import com.thinkgem.jeesite.modules.device.dao.DevicesDao;
import com.thinkgem.jeesite.modules.device.dao.RoomDevicesDao;
import com.thinkgem.jeesite.modules.device.entity.Devices;
import com.thinkgem.jeesite.modules.device.entity.RoomDevices;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 电费结算Service
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
	private RoomDevicesDao roomDevicesDao;
	@Autowired
	private DevicesDao devicesDao;
	
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
		/*生成款项*/
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		paymentTrans.setTradeType("11");//电费充值
		paymentTrans.setPaymentType("11");//电费自用金额
		paymentTrans.setTransId(electricFee.getRentContractId());//合同
		paymentTrans.setTradeDirection("1");//收款
		paymentTrans.setStartDate(new Date());
		paymentTrans.setExpiredDate(new Date());
		paymentTrans.setTradeAmount(electricFee.getPersonFee());
		paymentTrans.setLastAmount(electricFee.getPersonFee());
		paymentTrans.setTransAmount(0D);
		paymentTrans.setTransStatus("0");//未到账登记
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		paymentTransDao.insert(paymentTrans);
		
		/*智能电表充值*/
		String id = "";
		RentContract rentContract = rentContractDao.get(electricFee.getRentContractId());
		
		if(null != rentContract && "0".equals(rentContract.getRentMode())) {//整租
			House house = rentContract.getHouse();
			RoomDevices roomDevices = new RoomDevices();
			roomDevices.setHouseId(house.getId());
			roomDevices.setDelFlag("0");
			List<RoomDevices> list = roomDevicesDao.findList(roomDevices);
			if(null != list && list.size()>0) {
				roomDevices = list.get(0);
				if(null != roomDevices) {
					Devices device = devicesDao.get(roomDevices.getDeviceId());
					if(null != device) {
						DecimalFormat df=new DecimalFormat("0");
						id = charge(device.getDeviceId(),df.format(electricFee.getPersonFee()));
					}
				}
			}
		} else if(null != rentContract && "1".equals(rentContract.getRentMode())) {//单间
			Room room = rentContract.getRoom();
			RoomDevices roomDevices = new RoomDevices();
			roomDevices.setRoomId(room.getId());
			roomDevices.setDelFlag("0");
			List<RoomDevices> list = roomDevicesDao.findList(roomDevices);
			if(null != list && list.size()>0) {
				roomDevices = list.get(0);
				if(null != roomDevices) {
					Devices device = devicesDao.get(roomDevices.getDeviceId());
					if(null != device) {
						DecimalFormat df=new DecimalFormat("0");
						id = charge(device.getDeviceId(),df.format(electricFee.getPersonFee()));
					}
				}
			}
		}
		
		if(!StringUtils.isBlank(id)) {
			Pattern pattern = Pattern.compile("[0-9]*"); 
			Matcher isNum = pattern.matcher(id);
			if(isNum.matches()) {
				electricFee.setChargeStatus("0");//充值中
				electricFee.setChargeId(id);
			}
		}
		electricFee.setPaymentTransId(paymentTrans.getId());
		electricFee.setSettleStatus("1");//结算待审核
		super.save(electricFee);
	}
	
	public String getMeterValue(String rentContractId) {
		String value = "";
		Devices device = null;
		RentContract rentContract = rentContractDao.get(rentContractId);
		
		if(null != rentContract && "0".equals(rentContract.getRentMode())) {//整租
			House house = rentContract.getHouse();
			RoomDevices roomDevices = new RoomDevices();
			roomDevices.setHouseId(house.getId());
			roomDevices.setDelFlag("0");
			List<RoomDevices> list = roomDevicesDao.findList(roomDevices);
			if(null != list && list.size()>0) {
				roomDevices = list.get(0);
				if(null != roomDevices) {
					device = devicesDao.get(roomDevices.getDeviceId());
				}
			}
		} else if(null != rentContract && "1".equals(rentContract.getRentMode())) {//单间
			Room room = rentContract.getRoom();
			RoomDevices roomDevices = new RoomDevices();
			roomDevices.setRoomId(room.getId());
			roomDevices.setDelFlag("0");
			List<RoomDevices> list = roomDevicesDao.findList(roomDevices);
			if(null != list && list.size()>0) {
				roomDevices = list.get(0);
				if(null != roomDevices) {
					device = devicesDao.get(roomDevices.getDeviceId());
				}
			}
		}
		
		if(null != device && !StringUtils.isBlank(device.getDeviceId())) {
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String meterurl = proper.getProperty("meter.url")+"read_val.action?addr="+device.getDeviceId();
			
			String result = "";
			BufferedReader read=null;
			try {
				URL url=new URL(meterurl);
				URLConnection connection=url.openConnection();
				connection.connect();
				read = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
	            String line;
	            while ((line = read.readLine()) != null) {
	                result += line;
	            }
	            this.logger.info("call meter get value result:"+result);
	            
	            if(!StringUtils.isBlank(result)) {
	            	String values = StringUtils.substringAfterLast(result, ",");
	            	Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"); 
	    			Matcher isNum = pattern.matcher(values);
	    			if(isNum.matches()) {
	    				value = values;
	    			}
	            }
			} catch (Exception e) {
				this.logger.error("call meter get value error:",e);
			} finally {
				try {
					if(null != read) read.close();
				} catch (IOException e) {
					logger.error("close io error:",e);
				}
			}
		}
		
		return value;
	}
	
	public String getMeterFee(String rentContractId) {
		String value = "";
		Devices device = null;
		RentContract rentContract = rentContractDao.get(rentContractId);
		
		if(null != rentContract && "0".equals(rentContract.getRentMode())) {//整租
			House house = rentContract.getHouse();
			RoomDevices roomDevices = new RoomDevices();
			roomDevices.setHouseId(house.getId());
			roomDevices.setDelFlag("0");
			List<RoomDevices> list = roomDevicesDao.findList(roomDevices);
			if(null != list && list.size()>0) {
				roomDevices = list.get(0);
				if(null != roomDevices) {
					device = devicesDao.get(roomDevices.getDeviceId());
				}
			}
		} else if(null != rentContract && "1".equals(rentContract.getRentMode())) {//单间
			Room room = rentContract.getRoom();
			RoomDevices roomDevices = new RoomDevices();
			roomDevices.setRoomId(room.getId());
			roomDevices.setDelFlag("0");
			List<RoomDevices> list = roomDevicesDao.findList(roomDevices);
			if(null != list && list.size()>0) {
				roomDevices = list.get(0);
				if(null != roomDevices) {
					device = devicesDao.get(roomDevices.getDeviceId());
				}
			}
		}
		
		if(null != device && !StringUtils.isBlank(device.getDeviceId())) {
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String meterurl = proper.getProperty("meter.url")+"read_remain_val.action?addr="+device.getDeviceId();
			
			String result = "";
			BufferedReader read=null;
			try {
				URL url=new URL(meterurl);
				URLConnection connection=url.openConnection();
				connection.connect();
				read = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
	            String line;
	            while ((line = read.readLine()) != null) {
	                result += line;
	            }
	            this.logger.info("call meter get fee result:"+result);
	            
	            if(!StringUtils.isBlank(result)) {
	            	String values = StringUtils.substringAfterLast(result, ",");
	            	Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"); 
	    			Matcher isNum = pattern.matcher(values);
	    			if(isNum.matches()) {
	    				value = values;
	    			}
	            }
			} catch (Exception e) {
				this.logger.error("call meter get fee error:",e);
			} finally {
				try {
					if(null != read) read.close();
				} catch (IOException e) {
					logger.error("close io error:",e);
				}
			}
		}
		
		return value;
	}
	
	public String charge(String meterNo,String value) {
		PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
		String meterurl = proper.getProperty("meter.url")+"pay.action?addr="+meterNo+"&pay_value="+value;
		
		String result = "";
		BufferedReader read=null;
		try {
			URL url=new URL(meterurl);
			URLConnection connection=url.openConnection();
			connection.connect();
			read = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = read.readLine()) != null) {
                result += line;
            }
            this.logger.info("call meter charge result:"+result);
		} catch (Exception e) {
			this.logger.error("call meter charge error:",e);
		} finally {
			try {
				if(null != read) read.close();
			} catch (IOException e) {
				logger.error("close io error:",e);
			}
		}
		return result;
	}
	
	@Transactional(readOnly = false)
	public void delete(ElectricFee electricFee) {
		super.delete(electricFee);
	}
	
}