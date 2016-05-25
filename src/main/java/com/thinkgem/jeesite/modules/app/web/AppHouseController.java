package com.thinkgem.jeesite.modules.app.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.alipay.AlipayNotify;
import com.thinkgem.jeesite.modules.app.alipay.AlipayUtil;
import com.thinkgem.jeesite.modules.app.entity.AppToken;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.entity.Repairs;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.service.AppTokenService;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import com.thinkgem.jeesite.modules.app.service.RepairsService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.FileType;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.HouseAd;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseAdService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

@Controller
@RequestMapping("house")
public class AppHouseController {
	Logger log = LoggerFactory.getLogger(AppHouseController.class);

	@Autowired
	private HouseService houseService;
	@Autowired
	private ContractBookService contractBookService;
	@Autowired
	private AppTokenService appTokenService;
	@Autowired
	private DepositAgreementService depositAgreementService;
	@Autowired
	private PropertyProjectService propertyProjectService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private PaymentTransService paymentTransService;
	@Autowired
	private TradingAccountsService tradingAccountsService;
	@Autowired
	private RentContractService rentContractService;

	@Autowired
	private RepairsService repairService;

	@Autowired
	private AttachmentDao attachmentDao;

	@Autowired
	private HouseAdService houseAdService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private ElectricFeeService electricFeeService;
	@Autowired
	private TradingAccountsDao tradingAccountsDao;
	
	public AppHouseController() {
	}

	@RequestMapping(value = "ad")
	@ResponseBody
	public ResponseData ad(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		try {
			List<HouseAd> listAd = houseAdService.findList(new HouseAd());

			Map<String, Object> map = new HashMap<String, Object>();

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String img_url = proper.getProperty("img.url");
			for (HouseAd ad : listAd) {
				Map<String, String> adMap = new HashMap<String, String>();

				adMap.put("id", ad.getId());
				adMap.put("type", ad.getAdType());
				if ("1".equals(ad.getAdType())) {
					String value = "";
					if (null != ad.getRoom() && !StringUtils.isBlank(ad.getRoom().getId()))
						value = ad.getRoom().getId();
					else
						value = ad.getHouse().getId();
					adMap.put("value", value);
				}
				adMap.put("url", img_url + ad.getAdUrl());
				list.add(adMap);
			}

			map.put("ad", list);
			data.setData(map);

			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("list ad error:", e);
		}

		return data;
	}

	/**
	 * 精选房源列表
	 */
	@RequestMapping(value = "findFeatureList")
	@ResponseBody
	public ResponseData findFeatureList(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		if (null == request.getParameter("p_n") || null == request.getParameter("p_s")) {
			data.setCode("101");
			return data;
		}
		try {
			Integer p_n = Integer.valueOf(request.getParameter("p_n"));
			Integer p_s = Integer.valueOf(request.getParameter("p_s"));

			Page<House> page = new Page<House>();
			page.setPageSize(p_s);
			page.setPageNo(p_n);
			page = houseService.findFeaturePage(page, new House());

			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<House> pageList = page.getList();
			for (House h : pageList) {
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("id", h.getId());
				mp.put("house_code", h.getHouseCode());
				mp.put("price", h.getRental());
				mp.put("short_desc", h.getShortDesc());
				mp.put("short_location", h.getShortLocation());
				mp.put("pay_way", h.getPayWay());
				String cover = "";
				if (!StringUtils.isEmpty(h.getAttachmentPath())) {
					PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
					String img_url = proper.getProperty("img.url");
					cover = img_url + StringUtils.split(h.getAttachmentPath(), "|")[0];
				}
				mp.put("cover", cover);
				list.add(mp);
			}

			map.put("houses", list);
			map.put("p_t", page.getCount());

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("findFeatureList error:", e);
		}
		return data;
	}

	/**
	 * 获取房屋/房间详情
	 */
	@RequestMapping(value = "getFeatureInfo")
	@ResponseBody
	public ResponseData getFeatureInfo(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		if (null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}
		try {
			String house_id = request.getParameter("house_id");
			House house = new House();
			house.setId(house_id);
			house = houseService.getFeatureInfo(house);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("house_code", house.getHouseCode());
			map.put("id", house.getId());
			map.put("title", house.getShortDesc());
			map.put("price", house.getRental());
			map.put("pay_way", house.getPayWay());
			String cover = "";
			if (!StringUtils.isEmpty(house.getAttachmentPath())) {
				PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
				String img_url = proper.getProperty("img.url");
				String path[] = StringUtils.split(house.getAttachmentPath(), "|");
				if (null != path && path.length > 0) {
					for (String p : path) {
						cover += img_url + p + ",";
					}
					if (cover.endsWith(","))
						cover = StringUtils.substringBeforeLast(cover, ",");
				}
			}
			map.put("previews", cover);// 预览图片多图`,`拼接
			map.put("area", house.getHouseSpace());// 房屋面积
			String decorate = "1";
			map.put("decorate", decorate);// 装修情况=精装修
			map.put("summary", house.getShortLocation());// 概况
			map.put("floor", house.getHouseFloor());// 楼层
			String orientate = "";
			if (!StringUtils.isEmpty(house.getOrientation())) {
				orientate = StringUtils.split(house.getOrientation(), ",")[0];
				orientate = DictUtils.getDictLabel(orientate, "orientation", "");
			}
			map.put("orientate", orientate);// 朝向
			map.put("address", house.getProjectAddr());// 地址
			map.put("equipment", "1101111111");// (宽带、电视、沙发、洗衣机、床、冰箱、空调、衣柜、热水器、写字台)，1代表有0代表没有
			
			/* 获取房屋房屋管家手机号码 */
			String contact_phone = "4006-269-069";
			String userId = house.getServcieUserName();
			if(!StringUtils.isBlank(userId)) {
				User user = this.systemService.getUser(userId);
				String mobile = user.getMobile();
				if (!StringUtils.isBlank(mobile))
					contact_phone = mobile;
			}
			
			map.put("contact_phone", contact_phone);// 联系电话
			Integer fang = null, ting = null, wei = null;
			if (null != this.roomService.get(house.getId())) {
				house = this.houseService.get(this.roomService.get(house.getId()).getHouse().getId());
			}
			fang = house.getDecoraStrucRoomNum();
			ting = house.getDecoraStrucCusspacNum();
			wei = house.getDecoraStrucWashroNum();
			String layout = "";
			if (null != fang) {
				layout += fang + "房";
			}
			if (null != ting) {
				layout += ting + "厅";
			}
			if (null != wei) {
				layout += wei + "卫";
			}
			map.put("layout", layout);// 户型

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("getFeatureInfo error:", e);
		}
		return data;
	}

	@RequestMapping(value = "booking")
	@ResponseBody
	public ResponseData booking(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("house_id") || null == request.getParameter("b_time")
				|| null == request.getParameter("b_name") || null == request.getParameter("phone")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			ContractBook contractBook = new ContractBook();
			contractBook.setUserId(apptoken.getPhone());

			House house = new House();
			house.setId(request.getParameter("house_id"));
			house = this.houseService.getHouseByHouseId(house);

			/* 同一手机号不能预约同一房 */
			contractBook.setHouseId(house.getHouseId());
			contractBook.setRoomId(StringUtils.isNotBlank(house.getRoomId())?house.getRoomId():null);
			boolean ifCanBook = contractBookService.checkByUser(contractBook);
			if (!ifCanBook) {
				data.setCode("400");
				data.setMsg("您已预约该房间,不能重复预约!");
				return data;
			}

			contractBook.setUserName(request.getParameter("b_name"));
			contractBook.setUserPhone(request.getParameter("phone"));
			contractBook.setUserGender(request.getParameter("sex"));
			contractBook.setRemarks(request.getParameter("msg"));
			contractBook.setBookDate(DateUtils.parseDate(request.getParameter("b_time")));
			contractBook.setBookStatus("0");// 管家确认中
			contractBookService.save(contractBook);
			data.setCode("200");

			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			/* 获取房屋房屋管家手机号码 */
			String mobile = proper.getProperty("service.manager.mobile");
			String userId = house.getServcieUserName();
			if(!StringUtils.isBlank(userId)) {
				User user = this.systemService.getUser(userId);
				if(null != user && !StringUtils.isBlank(user.getMobile()))
					mobile = user.getMobile();
			}
			/* 给服务管家发送短信 */
			String content = proper.getProperty("service.sms.content");
			this.smsService.sendSms(mobile, content);				
		} catch (Exception e) {
			data.setCode("500");
			this.log.error("save contractbook error:", e);
		}
		return data;
	}

	@RequestMapping(value = "booking_list")
	@ResponseBody
	public ResponseData bookingList(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			ContractBook contractBook = new ContractBook();
			contractBook.setUserId(apptoken.getPhone());

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<ContractBook> dataList = contractBookService.findList(contractBook);
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			for (ContractBook tmpContractBook : dataList) {
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("id", tmpContractBook.getId());
				mp.put("time", DateFormatUtils.format(tmpContractBook.getBookDate(), "yyyy-MM-dd HH:mm"));
				mp.put("progress", tmpContractBook.getBookStatus());
				mp.put("short_desc", tmpContractBook.getShortDesc());
				String path[] = StringUtils.split(tmpContractBook.getAttachmentPath(), "|");
				if (null != path && path.length > 0) {
					mp.put("cover", proper.getProperty("img.url") + path[0]);
				}
				list.add(mp);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("book", list);

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookingList error:", e);
		}
		return data;
	}

	@RequestMapping(value = "booking_info")
	@ResponseBody
	public ResponseData bookingInfo(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		if (null == request.getParameter("id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			ContractBook contractBook = new ContractBook();
			contractBook.setUserId(apptoken.getPhone());
			contractBook.setId(request.getParameter("id"));
			contractBook = this.contractBookService.findOne(contractBook);

			Map<String, Object> map = new HashMap<String, Object>();
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			map.put("progress", contractBook.getBookStatus());
			String path[] = StringUtils.split(contractBook.getAttachmentPath(), "|");
			if (null != path && path.length > 0)
				map.put("cover", proper.getProperty("img.url") + path[0]);
			map.put("short_desc", contractBook.getShortDesc());
			map.put("short_location", contractBook.getShortLocation());
			map.put("id", contractBook.getId());
			String houseCode = contractBook.getHouseCode();
			if (!StringUtils.isBlank(contractBook.getRoomNo()))
				houseCode += "-" + contractBook.getRoomNo();
			map.put("house_code", houseCode);
			map.put("b_time", DateFormatUtils.format(contractBook.getBookDate(), "yyyy-MM-dd HH:mm"));
			map.put("b_name", contractBook.getUserName());
			map.put("phone", contractBook.getUserPhone());
			map.put("sex", contractBook.getUserGender());
			map.put("msg", contractBook.getRemarks());

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookingInfo error:", e);
		}
		return data;
	}

	@RequestMapping(value = "booking_cancel")
	@ResponseBody
	public ResponseData bookingCancel(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("id")) {
			data.setCode("101");
			return data;
		}

		try {
			ContractBook contractBook = new ContractBook();
			contractBook.setId(request.getParameter("id"));
			contractBook.setBookStatus("2");// 用户取消预约
			contractBookService.updateStatusByHouseId(contractBook);
		} catch (Exception e) {
			data.setCode("500");
			log.error("cancel booking error:", e);
		}

		data.setCode("200");
		return data;
	}

	/**
	 * 预定
	 */
	@RequestMapping(value = "booked")
	@ResponseBody
	public ResponseData booked(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("house_id") || null == request.getParameter("sign_date") || null == request.getParameter("book_cycle")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			if(null != appUser && StringUtils.isBlank(appUser.getIdCardNo())) {
				data.setCode("400");
				data.setMsg("请填写身份证号码");
				return data;
			}
			
			String houseId = request.getParameter("house_id");
			//如何是从预约过来,house_id实际是预约的id
			ContractBook hasContractBook = new ContractBook();
			hasContractBook.setId(houseId);
			hasContractBook.setUserId(appUser.getPhone());
			hasContractBook = contractBookService.get(hasContractBook);
			if(null != hasContractBook) {
				if(StringUtils.isNotBlank(hasContractBook.getRoomId()))
					houseId = hasContractBook.getRoomId();
				else
					houseId = hasContractBook.getHouseId();
			}
			log.info("------houseId:"+houseId);
			
			if(!checkHouseStatus(houseId)) {
				data.setCode("400");
				data.setMsg("房屋已出租");
				return data;
			}
			
			ContractBook contractBookCheck = new ContractBook();
			contractBookCheck.setId(houseId);
			contractBookCheck.setUserId(apptoken.getPhone());
			contractBookCheck = this.contractBookService.get(contractBookCheck);
			if(null != contractBookCheck) {
				if(StringUtils.isNoneBlank(contractBookCheck.getRoomId())) {
					houseId = contractBookCheck.getRoomId();
				} else {
					houseId = contractBookCheck.getHouseId();
				}
			}
			
			House house = new House();
			house.setId(houseId);
			house = houseService.get(house);

			Room room = null;
			if (null == house) {
				room = new Room();
				room.setId(houseId);
				room = this.roomService.get(room);

				house = new House();
				house.setId(room.getHouse().getId());
				house = houseService.get(house);
			}
			
			ContractBook contractBook = new ContractBook();
			contractBook.setIdNo(appUser.getIdCardNo());
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);
			if(null != list && list.size()>0) {
				for(ContractBook tmpContractBook : list) {
					if(houseId.equals(tmpContractBook.getHouseId()) && !"2".equals(tmpContractBook.getBookStatus())) {
						data.setCode("400");
						data.setMsg("您已预订该房间,不能重复预订!");
						break;
					}
				}
			}
			if("400".equals(data.getCode()))
				return data;
			
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(house.getPropertyProject().getId());
			propertyProject = this.propertyProjectService.get(propertyProject);

			Building building = new Building();
			building.setId(house.getBuilding().getId());
			building = this.buildingService.get(building);

			DepositAgreement depositAgreement = new DepositAgreement();
			depositAgreement.setAgreementCode(propertyProject.getProjectSimpleName() + "-"
					+ (depositAgreementService.getTotalValidDACounts() + 1) + "-XY");
			String agreementName = propertyProject.getProjectName() + "-" + building.getBuildingName() + "-"
					+ house.getHouseNo();
			if (null != room) {
				agreementName += "-" + room.getRoomNo();
				depositAgreement.setRoom(room);
			}
			depositAgreement.setAgreementName(agreementName);
			depositAgreement.setRentMode(null == room ? "0" : "1");// 0:整租 1:单间
			depositAgreement.setPropertyProject(propertyProject);
			depositAgreement.setBuilding(building);
			depositAgreement.setHouse(house);

			Tenant tenant = new Tenant();
			tenant.setIdNo(appUser.getIdCardNo());
			tenant.setIdType("0");//身份证
			List<Tenant> tenantList = tenantService.findTenantByIdTypeAndNo(tenant);
			if (null == tenantList || tenantList.size() <= 0) {
				tenantList = new ArrayList<Tenant>();
				tenant.setTenantName(appUser.getName());
				tenant.setGender(appUser.getSex());
				tenant.setCellPhone(appUser.getPhone());
				tenant.setIdType("0");
				tenant.setIdNo(appUser.getIdCardNo());
				tenantService.save(tenant);

				tenantList.add(tenant);
			}
	    	Date signDate = DateUtils.parseDate(request.getParameter("sign_date"), "yyyy-MM-dd");
        	depositAgreement.setTenantList(tenantList);
        	depositAgreement.setSignDate(new Date());
        	depositAgreement.setAgreementDate(signDate);
        	depositAgreement.setValidatorFlag("0");// 暂存
        	depositAgreement.setDataSource("2");// APP
        	depositAgreement.setRemarks(request.getParameter("msg"));
        	depositAgreement.setAgreementStatus("6");// 暂存
        	depositAgreement.setStartDate(signDate);
        	depositAgreement.setExpiredDate(DateUtils.dateAddMonth2(signDate, Integer.valueOf(request.getParameter("book_cycle"))));
        	
        	//租客身份证照片
        	if(StringUtils.isNotBlank(appUser.getIdCardPhoto＿front())) {
        		PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
    			String img_url = proper.getProperty("img.url");
        		depositAgreement.setDepositCustomerIDFile(img_url+appUser.getIdCardPhoto＿front());
        		
        		if(StringUtils.isNotBlank(appUser.getIdCardPhoto＿back())) {
        			depositAgreement.setDepositCustomerIDFile(depositAgreement.getDepositCustomerIDFile()+"|"+img_url+appUser.getIdCardPhoto＿back());
        		}
        	}
        	
			depositAgreementService.save(depositAgreement);
			
			/* 获取房屋房屋管家手机号码 */
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String mobile = proper.getProperty("service.manager.mobile");
			String userId = house.getServcieUserName();
			if(!StringUtils.isBlank(userId)) {
				User user = this.systemService.getUser(userId);
				if(null != user && !StringUtils.isBlank(user.getMobile()))
					mobile = user.getMobile();
			}
			/* 给服务管家发送短信 */
			String content = proper.getProperty("booked.sms.content");
			this.smsService.sendSms(mobile, content);
			
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			data.setMsg("预订失败,请重新预订!");
			this.log.error("save contract book error:", e);
		}

		return data;
	}

	@RequestMapping(value = "booked_list")
	@ResponseBody
	public ResponseData bookedList(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setIdNo(appUser.getIdCardNo());
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);

			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			Map<String, Object> map = new HashMap<String, Object>();
			for (ContractBook tmpContractBook : list) {
				Map<String, String> mp = new HashMap<String, String>();
				mp.put("id", tmpContractBook.getDepositId());
				mp.put("desc", tmpContractBook.getShortDesc());
				mp.put("time", DateFormatUtils.format(tmpContractBook.getCreateDate(), "yyyy-MM-dd"));

				/*
				 * 0:等待管家确认 1:等待用户确认 2:支付成功 3:管家已取消 4.等待用户支付 5.用户已取消 6.支付失败
				 */
				String status = "";
				if ("6".equals(tmpContractBook.getBookStatus())) {
					status = "0";
				} else if ("0".equals(tmpContractBook.getBookStatus())) {
					status = "1";
				} else if ("1".equals(tmpContractBook.getBookStatus()) || "3".equals(tmpContractBook.getBookStatus())) {
					status = "4";
				} else if ("5".equals(tmpContractBook.getBookStatus())) {
					status = "2";
				}
				if ("2".equals(tmpContractBook.getBookStatus())) {
					status = "3";
					if (apptoken.getPhone().equals(tmpContractBook.getUpdateUser())) {
						status = "5";
					}
				}
				mp.put("status", status);

				String path[] = StringUtils.split(tmpContractBook.getAttachmentPath(), "|");
				if (null != path && path.length > 0) {
					PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
					mp.put("cover", proper.getProperty("img.url") + path[0]);
				}
				dataList.add(mp);
			}
			map.put("houses", dataList);

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookedList error:", e);
		}
		return data;
	}

	@RequestMapping(value = "booked_protocol")
	@ResponseBody
	public ResponseData bookedProtocol(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			ContractBook contractBook = new ContractBook();
			contractBook.setDepositId(request.getParameter("id"));
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);

			if (null != list && list.size() > 0) {
				contractBook = list.get(0);
			}

			DepositAgreement depositAgreement = this.depositAgreementService.get(contractBook.getDepositId());

			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);

			Map<String, Object> map = new HashMap<String, Object>();

			String address = this.propertyProjectService.get(depositAgreement.getPropertyProject().getId()).getProjectAddr();
			address += this.buildingService.get(depositAgreement.getBuilding().getId()).getBuildingName();
			address += this.houseService.get(depositAgreement.getHouse().getId()).getHouseNo()+"室";
			if(null != depositAgreement.getRoom() && StringUtils.isNoneBlank(depositAgreement.getRoom().getId())) {
				address += this.roomService.get(depositAgreement.getRoom().getId()).getRoomNo()+"部位";
			}
			
			if(null == depositAgreement.getDepositAmount())
				depositAgreement.setDepositAmount(0d);
			if(null == depositAgreement.getHousingRent())
				depositAgreement.setHousingRent(0d);
			
			StringBuffer html = new StringBuffer("<div>");
			html.append("<p>");
			html.append("<h3><center>唐巢人才公寓定金协议</center></h3>");
			html.append("&nbsp;&nbsp;出租方(甲方)：上海唐巢投资有限公司</br>");
			html.append("&nbsp;&nbsp;承租方(乙方)：" + appUser.getName() + "</br></br>");
			html.append("&nbsp;&nbsp;根据《中华人民共和国合同法》、《上海市房屋租赁条例》、《上海市居住房屋租赁管理办法》的规定，甲、乙双方在平等、自愿、公平和诚实信用的基础上，经协商一致，"
					+ "乙方承租甲方位于<span style='text-decoration:underline;'>"
					+address+"</span>房屋，特向甲方支付房屋定金人民币：<span style='text-decoration:underline;'> "+depositAgreement.getDepositAmount()+" </span>元,"
					+ "大写:<span style='text-decoration:underline;'>"+getChineseNum(depositAgreement.getDepositAmount())+"</span>。且双方达成以下约定：</br>");
			html.append("&nbsp;&nbsp;一、该房屋月租金为人民币：<span style='text-decoration:underline;'>"+depositAgreement.getHousingRent()+"</span>元,大写:<span style='text-decoration:underline;'>"+getChineseNum(depositAgreement.getHousingRent())+"</span>元整；"
					+ "付款方式为付<span style='text-decoration:underline;'>&nbsp;"
					+(null==depositAgreement.getRenMonths()?"":depositAgreement.getRenMonths())+"&nbsp;</span>押<span style='text-decoration:underline;'>&nbsp;"
					+(null==depositAgreement.getDepositMonths()?"":depositAgreement.getDepositMonths())+"&nbsp;</span>；"
					+ "租期为<span style='text-decoration:underline;'>"+DateUtils.getMonthSpace(depositAgreement.getStartDate(),depositAgreement.getExpiredDate())+"</span>个月；"
					+ "期限为<span style='text-decoration:underline;'>"+DateUtils.formatDate(depositAgreement.getStartDate(),"yyyy年MM月dd日")+"</span>至<span style='text-decoration:underline;'>"+DateUtils.formatDate(depositAgreement.getExpiredDate(),"yyyy年MM月dd日")+"</span>并于<span style='text-decoration:underline;'>"+DateUtils.formatDate(depositAgreement.getAgreementDate(),"yyyy年MM月dd日")+"</span>前甲、乙双方签订此房屋租赁合同。</br>");
			html.append("&nbsp;&nbsp;二、若甲方在约定的签约时间内将该房屋出租给他人，则需双倍退还乙方租房定金；</br>");
			html.append("&nbsp;&nbsp;三、若因乙方原因未能如期与甲方签订租赁合同（签约标准按房屋现状且承租人无转租权），则视为乙方放弃承租该房屋，该笔定金作为违约金处理。若签订租赁合同后,该笔定金自动转为部分租金。</br>");
			html.append("&nbsp;&nbsp;四、若因国家政策性原因或自然灾害等不可抗拒的原因致使甲方无法出租该房屋，本协议则自然终止。甲方应如数无息退还乙方所付定金。</br>");
			html.append("&nbsp;&nbsp;五、本协议壹式贰份，甲、乙双方各执壹份，每份具有同等效力。</br></br>");
			html.append("&nbsp;&nbsp;出租方(甲方)：上海唐巢投资有限公司</br>");
			html.append("&nbsp;&nbsp;代理人：</br>");
			html.append("&nbsp;&nbsp;联系地址：创新西路357号</br>");
			html.append("&nbsp;&nbsp;联系电话：021-68876662 4006-269-069</br>");
			html.append("&nbsp;&nbsp;签约日期：" + DateFormatUtils.format(depositAgreement.getSignDate(), "yyyy-MM-dd")
			+ "</br></br>");
			html.append("&nbsp;&nbsp;承租方(乙方)：" + appUser.getName() + "</br>");
			html.append("&nbsp;&nbsp;代理人：</br>");
			html.append("&nbsp;&nbsp;身份证号码：" + appUser.getIdCardNo() + "</br>");
			html.append("&nbsp;&nbsp;联系地址：</br>");
			html.append("&nbsp;&nbsp;联系电话：" + appUser.getPhone() + "</br>");
			html.append("&nbsp;&nbsp;紧急联系人电话：" + appUser.getPhone() + "</br>");
			html.append(
					"&nbsp;&nbsp;签约日期：" + DateFormatUtils.format(depositAgreement.getSignDate(), "yyyy-MM-dd") + "</br>");
			html.append("</p></div>");
			html.append("</p></div>");
			map.put("str_html", html);

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("find booked protocol error:", e);
		}

		return data;
	}

	@RequestMapping(value = "booked_order")
	@ResponseBody
	public ResponseData bookedOrder(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);

			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			contractBook.setDepositId(request.getParameter("id"));
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);

			if (null != list && list.size() > 0) {
				contractBook = list.get(0);
			}

			DepositAgreement depositAgreement = this.depositAgreementService.get(contractBook.getDepositId());

			/* 款项 */
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setTransId(depositAgreement.getId());
			List<PaymentTrans> paymentTransList = paymentTransService.findList(paymentTrans);
			String transIds = "";
			double tradeAmount = 0;
			List<Receipt> receiptList = new ArrayList<Receipt>();
			for (PaymentTrans tmpPaymentTrans : paymentTransList) {
				transIds += tmpPaymentTrans.getId() + ",";
				tradeAmount += tmpPaymentTrans.getTradeAmount();

				Receipt receipt = new Receipt();
				receipt.setTradeMode("4");// 支付宝
				receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
				receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
				receiptList.add(receipt);
			}
			if (org.apache.commons.lang3.StringUtils.endsWith(transIds, ",")) {
				transIds = org.apache.commons.lang3.StringUtils.substringBeforeLast(transIds, ",");
			}

			/* 生成账务交易 */
			TradingAccounts tradingAccounts = new TradingAccounts();
			tradingAccounts.setTradeId(depositAgreement.getId());
			List<TradingAccounts> listTradingAccounts = tradingAccountsService.findList(tradingAccounts);
			if(null != listTradingAccounts && listTradingAccounts.size() > 0) {
				String oldTradingAccountsId = listTradingAccounts.get(0).getId();
				
				PaymentOrder delPaymentOrder = new PaymentOrder();
				delPaymentOrder.setTradeId(oldTradingAccountsId);
				contractBookService.deleteByTradeId(delPaymentOrder);
			}
			tradingAccountsService.delete(tradingAccounts);
			
			tradingAccounts.setTransIds(transIds);
			tradingAccounts.setTradeStatus("0");// 待审核
			tradingAccounts.setTradeType("1");// 预约定金
			tradingAccounts.setTradeAmount(tradeAmount);
			tradingAccounts.setTradeDirection("1");// 入账
			tradingAccounts.setPayeeType("1");// 个人
			tradingAccounts.setPayeeName(appUser.getName());
			tradingAccounts.setReceiptList(receiptList);
			tradingAccountsService.save(tradingAccounts);

			/* 订单生成 */
			String houseId = "";
			if(StringUtils.isNotBlank(contractBook.getRoomId()))
				houseId = contractBook.getRoomId();
			else
				houseId = contractBook.getHouseId();
			PaymentOrder paymentOrder = new PaymentOrder();
			paymentOrder.setOrderId(contractBookService.generateOrderId());
			paymentOrder.setOrderDate(new Date());
			paymentOrder.setOrderStatus("1");// 未支付
			paymentOrder.setTradeId(tradingAccounts.getId());
			paymentOrder.setOrderAmount(tradingAccounts.getTradeAmount());
			paymentOrder.setCreateDate(new Date());
			paymentOrder.setHouseId(houseId);
			this.contractBookService.saveOrder(paymentOrder);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", paymentOrder.getOrderId());
			map.put("price", paymentOrder.getOrderAmount());

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookedOrder error:", e);
		}

		return data;
	}

	@RequestMapping(value = "booked_info")
	@ResponseBody
	public ResponseData bookedInfo(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);

			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			contractBook.setDepositId(request.getParameter("id"));
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);

			if (null != list && list.size() > 0) {
				contractBook = list.get(0);
			}

			DepositAgreement depositAgreement = this.depositAgreementService.get(contractBook.getDepositId());

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("id", contractBook.getDepositId());
			map.put("house_code", contractBook.getRoomNo());
			map.put("pay_way", contractBook.getPayWay());
			map.put("desc", contractBook.getShortDesc());
			map.put("location", contractBook.getShortLocation());
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String path[] = StringUtils.split(contractBook.getAttachmentPath(), "|");
			if (null != path && path.length > 0)
				map.put("cover", proper.getProperty("img.url") + path[0]);
			map.put("rent_amount", depositAgreement.getHousingRent());
			map.put("start_date", DateFormatUtils.format(depositAgreement.getStartDate(), "yyyy-MM-dd"));
			map.put("end_date", DateFormatUtils.format(depositAgreement.getExpiredDate(), "yyyy-MM-dd"));
			map.put("deposit_amount", depositAgreement.getDepositAmount());
			map.put("rent_name", appUser.getName());
			map.put("id_no", appUser.getIdCardNo());
			map.put("rent_gender", appUser.getSex());
			map.put("rent_phone", appUser.getPhone());
			map.put("note", depositAgreement.getRemarks());

			/*
			 * 0:等待管家确认 1:等待用户确认 2:支付成功 3:管家已取消 4.等待用户支付 5.用户已取消 6.支付失败
			 */
			String status = "";
			if ("6".equals(contractBook.getBookStatus())) {
				status = "0";
			} else if ("0".equals(contractBook.getBookStatus())) {
				status = "1";
			} else if ("1".equals(contractBook.getBookStatus()) || "3".equals(contractBook.getBookStatus())) {
				status = "4";
			} else if ("5".equals(contractBook.getBookStatus())) {
				status = "2";
			}
			if ("2".equals(contractBook.getBookStatus())) {
				status = "3";
				if (apptoken.getPhone().equals(contractBook.getUpdateUser())) {
					status = "5";
				}
			}
			map.put("status", status);
			map.put("sign_date", DateFormatUtils.format(depositAgreement.getSignDate(), "yyyy-MM-dd"));
			map.put("contract_date", DateFormatUtils.format(depositAgreement.getAgreementDate(), "yyyy-MM-dd"));

			PaymentOrder paymentOrder = new PaymentOrder();
			paymentOrder.setHouseId(contractBook.getHouseId());
			paymentOrder = this.contractBookService.findByHouseId(paymentOrder);
			if (null != paymentOrder)
				map.put("order_id", paymentOrder.getOrderId());

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookedInfo error:", e);
		}

		return data;
	}

	/**
	 * 取消预订
	 */
	@RequestMapping(value = "booked_cancel")
	@ResponseBody
	public ResponseData bookedCancel(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		if (null == request.getParameter("id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			AuditHis auditHis = new AuditHis();
			auditHis.setUpdateUser(apptoken.getPhone());
			auditHis.setObjectId(request.getParameter("id"));
			auditHis.setAuditStatus("2");
			depositAgreementService.audit(auditHis);
			
			data.setCode("200");
		} catch (Exception e) {
			log.error("取消预订失败:", e);
		}

		return data;
	}

	/**
	 * 签约
	 */
	@RequestMapping(value = "sign")
	@ResponseBody
	public ResponseData sign(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("house_id") || null == request.getParameter("contract_cycle")) {
			data.setCode("101");
			return data;
		}

		try {
			if(!checkHouseStatus(request.getParameter("house_id"))) {
				data.setCode("400");
				data.setMsg("房屋已出租");
				return data;
			}
			
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			List<ContractBook> list = this.contractBookService.findRentContract(contractBook);
			for(ContractBook tmpContractBook : list) {
				if(StringUtils.isNoneBlank(tmpContractBook.getHouseId()) && tmpContractBook.getHouseId().equals(request.getParameter("house_id")) 
						&& !"3".equals(tmpContractBook.getBookStatus())) {
					data.setCode("400");
					data.setMsg("您已签约该房间,不能重复签约.");
					break;
				}
			}
			if("400".equals(data.getCode()))
				return data;
			
			House house = new House();
			house.setId(request.getParameter("house_id"));
			house = houseService.get(house);

			Room room = null;
			if (null == house) {
				room = new Room();
				room.setId(request.getParameter("house_id"));
				room = this.roomService.get(room);

				house = new House();
				house.setId(room.getHouse().getId());
				house = houseService.get(house);
			}

			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(house.getPropertyProject().getId());
			propertyProject = this.propertyProjectService.get(propertyProject);

			Building building = new Building();
			building.setId(house.getBuilding().getId());
			building = this.buildingService.get(building);

			/* 判断该用户是否有预订,有则为定金转合同流程 */
			boolean hasBooked = false; 
			String depositId = null;
			ContractBook booked = new ContractBook();
			booked.setUserPhone(apptoken.getPhone());
			List<ContractBook> bookedList = this.contractBookService.findBookedContract(booked);
			for(ContractBook tContractBook : bookedList) {
				if(request.getParameter("house_id").equals(tContractBook.getHouseId())) {
					//定金转合同
					hasBooked = true;
					depositId = tContractBook.getDepositId();
					break;
				}
			}
			
			if(!hasBooked) {
				RentContract rentContract = new RentContract();
				rentContract.setSignType("0");// 新签
				rentContract.setContractCode(propertyProject.getProjectSimpleName() + "-"
						+ (rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
				String contractName = propertyProject.getProjectName() + "-" + building.getBuildingName() + "-"
						+ house.getHouseNo();
				String payWay = "";
				if (null != room) {
					contractName += "-" + room.getRoomNo();
					rentContract.setRoom(room);
					rentContract.setRental(room.getRental());
	
					payWay = room.getPayWay();
				} else {
					rentContract.setRental(house.getRental());
	
					payWay = house.getPayWay();
				}
				if ("0".equals(payWay)) {// 付三押一
					rentContract.setRenMonths(3);
					rentContract.setDepositMonths(1);
				} else if ("1".equals(payWay)) {// 付二押二
					rentContract.setRenMonths(2);
					rentContract.setDepositMonths(2);
				}
				rentContract.setDepositAmount(rentContract.getRental() * rentContract.getDepositMonths());
				rentContract.setContractName(contractName);
				rentContract.setRentMode(null == room ? "0" : "1");// 0:整租 1:单间
				rentContract.setPropertyProject(propertyProject);
				rentContract.setBuilding(building);
				rentContract.setHouse(house);
	
				Tenant tenant = new Tenant();
				tenant.setIdType("0");// 身份证
				tenant.setIdNo(appUser.getIdCardNo());
				List<Tenant> tenantList = tenantService.findTenantByIdTypeAndNo(tenant);
				if (null == tenantList || tenantList.size() <= 0) {
					tenantList = new ArrayList<Tenant>();
					tenant.setTenantName(appUser.getName());
					tenant.setGender(appUser.getSex());
					tenant.setCellPhone(appUser.getPhone());
					tenantService.save(tenant);
	
					tenantList.add(tenant);
				}
				rentContract.setTenantList(tenantList);
				rentContract.setLiveList(tenantList);
				rentContract.setContractSource("1");// 本部
				rentContract.setValidatorFlag("0");// 暂存
				rentContract.setDataSource("2");// APP
				Date d = new Date();
				rentContract.setSignDate(d);
				rentContract.setStartDate(d);
				rentContract.setExpiredDate(DateUtils.dateAddMonth2(d, Integer.valueOf(request.getParameter("contract_cycle"))));
				rentContract.setRemarks(request.getParameter("msg"));
				rentContract.setContractStatus("0");// 暂存
			
				//租客身份证照片
	        	if(StringUtils.isNotBlank(appUser.getIdCardPhoto＿front())) {
	        		PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
	    			String img_url = proper.getProperty("img.url");
	    			rentContract.setRentContractOtherFile(img_url+appUser.getIdCardPhoto＿front());
	        	}
	        	
				this.rentContractService.save(rentContract);
			} else {
				DepositAgreement depositAgreement = depositAgreementService.get(depositId);
				
				RentContract rentContract = new RentContract();
				rentContract.setSignType("0");// 新签
				String contractName = propertyProject.getProjectName() + "-" + building.getBuildingName() + "-"
						+ house.getHouseNo();
				if (null != room) {
					contractName += "-" + room.getRoomNo();
				}
				rentContract.setContractName(contractName);
				rentContract.setRentMode(depositAgreement.getRentMode());
				rentContract.setPropertyProject(depositAgreement.getPropertyProject());
				rentContract.setBuilding(depositAgreement.getBuilding());
				rentContract.setHouse(depositAgreement.getHouse());
				rentContract.setRoom(depositAgreement.getRoom());
				rentContract.setRental(depositAgreement.getHousingRent());
				rentContract.setRenMonths(depositAgreement.getRenMonths());
				rentContract.setDepositMonths(depositAgreement.getDepositMonths());
				Date d = new Date();
				rentContract.setSignDate(d);
				rentContract.setStartDate(d);
				rentContract.setExpiredDate(DateUtils.dateAddMonth2(d, Integer.valueOf(request.getParameter("contract_cycle"))));
				rentContract.setUser(depositAgreement.getUser());
				rentContract.setTenantList(depositAgreement.getTenantList());
				rentContract.setRemarks(depositAgreement.getRemarks());
				rentContract.setAgreementId(depositAgreement.getId());
				rentContract.setContractCode((rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
				rentContract.setContractSource("1");// 本部
				rentContract.setValidatorFlag("0");// 暂存
				rentContract.setDataSource("2");// APP
				rentContract.setRemarks(request.getParameter("msg"));
				rentContract.setContractStatus("0");// 暂存
				
				Tenant tenant = new Tenant();
				tenant.setIdType("0");// 身份证
				tenant.setIdNo(appUser.getIdCardNo());
				List<Tenant> tenantList = tenantService.findTenantByIdTypeAndNo(tenant);
				if (null == tenantList || tenantList.size() <= 0) {
					tenantList = new ArrayList<Tenant>();
					tenant.setTenantName(appUser.getName());
					tenant.setGender(appUser.getSex());
					tenant.setCellPhone(appUser.getPhone());
					tenantService.save(tenant);
	
					tenantList.add(tenant);
				}
				rentContract.setTenantList(tenantList);
				rentContract.setLiveList(tenantList);
				
				//租客身份证照片
	        	if(StringUtils.isNotBlank(appUser.getIdCardPhoto＿front())) {
	        		PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
	    			String img_url = proper.getProperty("img.url");
	    			rentContract.setRentContractOtherFile(img_url+appUser.getIdCardPhoto＿front());
	    			
	    			if(StringUtils.isNotBlank(appUser.getIdCardPhoto＿back())) {
	    				rentContract.setRentContractOtherFile(rentContract.getRentContractOtherFile()+"|"+img_url+appUser.getIdCardPhoto＿back());
	        		}
	        	}
	        	
				this.rentContractService.save(rentContract);
			}

			data.setCode("200");
			
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			/* 获取房屋房屋管家手机号码 */
			String mobile = proper.getProperty("service.manager.mobile");
			String userId = house.getServcieUserName();
			if(!StringUtils.isBlank(userId)) {
				User user = this.systemService.getUser(userId);
				if(null != user && !StringUtils.isBlank(user.getMobile()))
					mobile = user.getMobile();
			}
			/* 给服务管家发送短信 */
			String content = proper.getProperty("sign.sms.content");
			this.smsService.sendSms(mobile, content);
		} catch (Exception e) {
			data.setCode("500");
			log.error("sign contract error:", e);
		}

		return data;
	}

	/**
	 * 取消签约
	 */
	@RequestMapping(value = "signed_cancel")
	@ResponseBody
	public ResponseData signedCancel(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		if (null == request.getParameter("contract_id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			//RentContract rentContract = new RentContract();
			//rentContract.setId(request.getParameter("contract_id"));
			//rentContract = rentContractService.get(rentContract);
			//rentContract.setUpdateUser(apptoken.getPhone());
			//this.rentContractService.delete(rentContract);
			
			AuditHis auditHis = new AuditHis();
			auditHis.setObjectId(request.getParameter("contract_id"));
			auditHis.setAuditStatus("3");
			auditHis.setUpdateUser(apptoken.getPhone());
			this.rentContractService.audit(auditHis);
		} catch (Exception e) {
			log.error("", e);
		}

		data.setCode("200");
		return data;
	}
	
	/**
	 * 续签
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "contract_continue")
	@ResponseBody
	public ResponseData contractContinue(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if (StringUtils.isEmpty(request.getParameter("contract_id")) || StringUtils.isEmpty(request.getParameter("contract_cycle"))) {
			data.setCode("101");
			return data;
		}
		
		try {
			this.log.info("------开始续签------");
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			String contractId = request.getParameter("contract_id");
			RentContract rentContract = this.rentContractService.get(contractId);
			House house = null;
			if(null != rentContract && null != rentContract.getHouse()) {
			  house = this.houseService.get(rentContract.getHouse().getId());
			}
			String contractName = rentContract.getContractName();
			Date originalEndDate = rentContract.getExpiredDate();//原来合同的结束日期
			RentContract rentContractOld = rentContract;
			rentContract = new RentContract();
			rentContract.setContractId(contractId);
			rentContract.setRentMode(rentContractOld.getRentMode());
			rentContract.setPropertyProject(rentContractOld.getPropertyProject());
			rentContract.setBuilding(rentContractOld.getBuilding());
			rentContract.setHouse(rentContractOld.getHouse());
			rentContract.setRoom(rentContractOld.getRoom());
			rentContract.setRental(rentContractOld.getRental());
			rentContract.setDepositElectricAmount(0d);
			rentContract.setDepositAmount(0d);
			rentContract.setRenMonths(rentContractOld.getRenMonths());
			rentContract.setDepositMonths(rentContractOld.getDepositMonths());
			Tenant tenant = new Tenant();
			tenant.setIdType("0");// 身份证
			tenant.setIdNo(appUser.getIdCardNo());
			List<Tenant> tenantList = tenantService.findTenantByIdTypeAndNo(tenant);
			if (null == tenantList || tenantList.size() <= 0) {
				tenantList = new ArrayList<Tenant>();
				tenant.setTenantName(appUser.getName());
				tenant.setGender(appUser.getSex());
				tenant.setCellPhone(appUser.getPhone());
				tenantService.save(tenant);

				tenantList.add(tenant);
			}
			rentContract.setTenantList(tenantList);
			rentContract.setLiveList(tenantList);
			rentContract.setContractSource("1");// 本部
			rentContract.setValidatorFlag("0");// 暂存
			rentContract.setDataSource("2");// APP
			rentContract.setSignDate(new Date());
			Date beginDate = DateUtils.dateAddDay(originalEndDate, 1);
			rentContract.setStartDate(beginDate);
			rentContract.setExpiredDate(DateUtils.dateAddMonth2(beginDate, Integer.valueOf(request.getParameter("contract_cycle"))));
			rentContract.setRemarks(request.getParameter("msg"));
			rentContract.setContractStatus("0");// 暂存
			rentContract.setSignType("1");//正常人工续签
			rentContract.setContractName(contractName.concat("(续签)"));
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(house.getPropertyProject().getId());
			propertyProject = this.propertyProjectService.get(propertyProject);
			rentContract.setContractCode(propertyProject.getProjectSimpleName() + "-"
					+ (rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");

			rentContract.setChargeType(rentContractOld.getChargeType());
			rentContract.setHasTv(rentContractOld.getHasTv());
			rentContract.setTvFee(rentContractOld.getTvFee());
			rentContract.setHasNet(rentContractOld.getHasNet());
			rentContract.setNetFee(rentContractOld.getNetFee());
			rentContract.setWaterFee(rentContractOld.getWaterFee());
			
			/* 判断该用户是否有预订,有则为定金转合同流程 */
			this.rentContractService.save(rentContract);
			data.setCode("200");
			
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			/* 获取房屋房屋管家手机号码 */
			String mobile = proper.getProperty("service.manager.mobile");
			String userId = house.getServcieUserName();
			if(!StringUtils.isBlank(userId)) {
				User user = this.systemService.getUser(userId);
				if(null != user && !StringUtils.isBlank(user.getMobile()))
					mobile = user.getMobile();
			}
			/* 给服务管家发送短信 */
			String content = proper.getProperty("sign.sms.content");
			this.smsService.sendSms(mobile, content);
		} catch (Exception e) {
			this.log.error("[续签异常]:",e);
		}
		
		this.log.info("------结束续签------");
		return data;
	}

	@RequestMapping(value = "sign_order")
	@ResponseBody
	public ResponseData signOrder(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("contract_id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);

			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			contractBook.setContractId(request.getParameter("contract_id"));
			List<ContractBook> list = this.contractBookService.findRentContract(contractBook);

			if (null != list && list.size() > 0) {
				contractBook = list.get(0);
			}

			RentContract rentContract = this.rentContractService.get(contractBook.getContractId());

			/* 款项 */
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setTransId(rentContract.getId());
			List<PaymentTrans> paymentTransList = paymentTransService.findList(paymentTrans);
			String transIds = "";
			double tradeAmount = 0;
			List<Receipt> receiptList = new ArrayList<Receipt>();
			// 首次需付费：水电押金、房租押金、房租*n
			int rentMonthes = rentContract.getRenMonths();// 首付房租月数
			int rentMonthesCount = 0;
			for (PaymentTrans tmpPaymentTrans : paymentTransList) {
				if(rentMonthesCount >= rentMonthes) break;
				if ("2".equals(tmpPaymentTrans.getPaymentType()) || "3".equals(tmpPaymentTrans.getPaymentType())) {// 水电押金/续补水电费押金
					transIds += tmpPaymentTrans.getId() + ",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();

					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");// 支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);
				} else if ("4".equals(tmpPaymentTrans.getPaymentType()) || "5".equals(tmpPaymentTrans.getPaymentType())) {// 房租押金/续补房租押金
					transIds += tmpPaymentTrans.getId() + ",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();

					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");// 支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);
				} else if ("6".equals(tmpPaymentTrans.getPaymentType())) {// 房租
					transIds += tmpPaymentTrans.getId() + ",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();

					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");// 支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);

					rentMonthesCount++;
				}
			}
			rentMonthesCount = 0;
			for (PaymentTrans tmpPaymentTrans : paymentTransList) {
				if(rentMonthesCount >= rentMonthes) break;
				if ("18".equals(tmpPaymentTrans.getPaymentType())) {//电视费
					transIds += tmpPaymentTrans.getId() + ",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();

					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");// 支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);

					rentMonthesCount++;
				}
			}
			rentMonthesCount = 0;
			for (PaymentTrans tmpPaymentTrans : paymentTransList) {
				if(rentMonthesCount >= rentMonthes) break;
				if ("20".equals(tmpPaymentTrans.getPaymentType())) {//宽带费
					transIds += tmpPaymentTrans.getId() + ",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();

					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");// 支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);

					rentMonthesCount++;
				}
			}
			rentMonthesCount = 0;
			for (PaymentTrans tmpPaymentTrans : paymentTransList) {
				if(rentMonthesCount >= rentMonthes) break;
				if ("14".equals(tmpPaymentTrans.getPaymentType())) {//水费
					transIds += tmpPaymentTrans.getId() + ",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();

					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");// 支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);

					rentMonthesCount++;
				}
			}
			if (org.apache.commons.lang3.StringUtils.endsWith(transIds, ",")) {
				transIds = org.apache.commons.lang3.StringUtils.substringBeforeLast(transIds, ",");
			}

			/* 生成账务交易 */
			TradingAccounts tradingAccounts = new TradingAccounts();
			tradingAccounts.setTradeId(rentContract.getId());
			List<TradingAccounts> listTradingAccounts = tradingAccountsService.findList(tradingAccounts);
			if(null != listTradingAccounts && listTradingAccounts.size() > 0) {
				String oldTradingAccountsId = listTradingAccounts.get(0).getId();
				
				PaymentOrder delPaymentOrder = new PaymentOrder();
				delPaymentOrder.setTradeId(oldTradingAccountsId);
				contractBookService.deleteByTradeId(delPaymentOrder);
			}
			tradingAccountsService.delete(tradingAccounts);
			
			tradingAccounts.setTradeStatus("0");// 待审核
			tradingAccounts.setTransIds(transIds);
			if("0".equals(rentContract.getSignType()))
				tradingAccounts.setTradeType("3");// 新签合同
			else
				tradingAccounts.setTradeType("4");//正常人工续签
			tradingAccounts.setTradeAmount(tradeAmount);
			tradingAccounts.setTradeDirection("1");// 入账
			tradingAccounts.setPayeeType("1");// 个人
			tradingAccounts.setPayeeName(appUser.getName());
			tradingAccounts.setReceiptList(receiptList);
			tradingAccountsService.save(tradingAccounts);

			/* 订单生成 */
			String houseId = "";
			if(StringUtils.isNoneBlank(contractBook.getRoomId()))
				houseId = contractBook.getRoomId();
			else
				houseId = contractBook.getHouseId();
			
			PaymentOrder paymentOrder = new PaymentOrder();
			paymentOrder.setOrderId(contractBookService.generateOrderId());
			paymentOrder.setOrderDate(new Date());
			paymentOrder.setOrderStatus("1");// 未支付
			paymentOrder.setTradeId(tradingAccounts.getId());
			paymentOrder.setOrderAmount(tradingAccounts.getTradeAmount());
			paymentOrder.setCreateDate(new Date());
			paymentOrder.setHouseId(houseId);
			this.contractBookService.saveOrder(paymentOrder);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_id", paymentOrder.getOrderId());
			map.put("price", paymentOrder.getOrderAmount());

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("signOrder error:", e);
		}

		return data;
	}

	@RequestMapping(value = "contract_list")
	@ResponseBody
	public ResponseData contractList(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		try {
			//0:查询所有可续签的合同列表；1:查询所有可退租的合同列表；2:查询所有可报修的合同列表；
			//3：查询我的账单前的所有合同列表；4:查询该登录号名下的所有合同列表
			String type = request.getParameter("type");
			if(StringUtils.isBlank(type))
				type = "4";
			
			String contractBusiStatus = "";
			if("0".equals(type) || "1".equals(type) || "2".equals(type) || "3".equals(type))
				contractBusiStatus = "0";//有效
			
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}

			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			if(!StringUtils.isBlank(contractBusiStatus))
				contractBook.setContractBusiStatus(contractBusiStatus);
			List<ContractBook> list = this.contractBookService.findRentContract(contractBook);

			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			for (ContractBook tmpContractBook : list) {
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("contract_id", tmpContractBook.getContractId());
				mp.put("contract_code", tmpContractBook.getContractCode());
				PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
				String path[] = StringUtils.split(tmpContractBook.getAttachmentPath(), "|");
				if (null != path && path.length > 0)
					mp.put("cover", proper.getProperty("img.url") + path[0]);
				mp.put("short_desc", tmpContractBook.getShortDesc());
				mp.put("house_desc", tmpContractBook.getShortLocation());
				mp.put("rent", tmpContractBook.getRent());
				String status = "";
				if ("0".equals(tmpContractBook.getBookStatus()))
					status = "0";
				else if ("1".equals(tmpContractBook.getBookStatus()))
					status = "4";
				else if ("2".equals(tmpContractBook.getBookStatus())||"4".equals(tmpContractBook.getBookStatus()))
					status = "1";
				else if ("6".equals(tmpContractBook.getBookStatus()))
					status = "2";
				// 0:等待管家确认 1:在线签约成功等待支付 2:在线签约支付成功 3:管家取消在线签约 4:管家确认成功请您核实
				// 5:用户取消在线签约
				if("3".equals(tmpContractBook.getBookStatus())) {
					status = "3";
					if (apptoken.getPhone().equals(tmpContractBook.getUpdateUser())) {
						status = "5";
					}
				}
				mp.put("end_date", DateUtils.formatDate(tmpContractBook.getEndDate(), "yyyy-MM-dd"));
				mp.put("status", status);
				dataList.add(mp);
			}
			map.put("contracts", dataList);

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("contractList error:", e);
		}

		return data;
	}

	@RequestMapping(value = "contract_info")
	@ResponseBody
	public ResponseData contractInfo(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("contract_id")) {
			data.setCode("101");
			return data;
		}

		try {
			RentContract rentContract = this.rentContractService.get(request.getParameter("contract_id"));

			Map<String, Object> map = new HashMap<String, Object>();

			String shortDesc = "", attachmentPath = "", houseDesc = "", houseCode = "";
			Room room = null;House house = null;
			if (null != rentContract.getRoom() && !StringUtils.isBlank(rentContract.getRoom().getId())) {
				room = this.roomService.get(rentContract.getRoom().getId());
				shortDesc = room.getShortDesc();
				attachmentPath = room.getAttachmentPath();
				houseDesc = room.getShortLocation();

				house = this.houseService.get(rentContract.getHouse().getId());
				houseCode = house.getHouseCode() + "-" + room.getRoomNo();
			} else {
				house = this.houseService.get(rentContract.getHouse().getId());
				shortDesc = house.getShortDesc();
				attachmentPath = house.getAttachmentPath();
				houseDesc = house.getShortLocation();
				houseCode = house.getHouseCode();
			}

			map.put("contract_id", rentContract.getId());
			map.put("contract_code", rentContract.getContractCode());
			map.put("short_desc", shortDesc);
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String path[] = StringUtils.split(attachmentPath, "|");
			if (null != path && path.length > 0)
				map.put("cover", proper.getProperty("img.url") + path[0]);
			map.put("house_desc", houseDesc);
			map.put("rent", rentContract.getRental());
			String rentType = "";
			if ((new Integer(3)).equals(rentContract.getRenMonths())
					&& (new Integer(1)).equals(rentContract.getDepositMonths())) {
				rentType = "0";
			} else if ((new Integer(2)).equals(rentContract.getRenMonths())
					&& (new Integer(2)).equals(rentContract.getDepositMonths())) {
				rentType = "1";
			}
			map.put("rent_type", rentType);
			map.put("sign_date", DateFormatUtils.format(rentContract.getSignDate(), "yyyy-MM-dd"));
			map.put("start_date", DateFormatUtils.format(rentContract.getStartDate(), "yyyy-MM-dd"));
			map.put("end_date", DateFormatUtils.format(rentContract.getExpiredDate(), "yyyy-MM-dd"));
			if (null != rentContract.getRemindTime())
				map.put("remind_date", DateFormatUtils.format(rentContract.getRemindTime(), "yyyy-MM-dd"));
			String status = "";
			if ("0".equals(rentContract.getContractStatus()))
				status = "0";
			else if ("1".equals(rentContract.getContractStatus()))
				status = "4";
			else if ("2".equals(rentContract.getContractStatus())||"4".equals(rentContract.getContractStatus()))
				status = "1";
			else if ("6".equals(rentContract.getContractStatus()))
				status = "2";
			// 0:等待管家确认 1:在线签约成功等待支付 2:在线签约支付成功 3:管家取消在线签约 4:管家确认成功请您核实
			// 5:用户取消在线签约
			if("3".equals(rentContract.getContractStatus())) {
				status = "3";
			}
			map.put("status", status);
			map.put("house_code", houseCode);
			String hasTv = "0";
			if("1".equals(rentContract.getHasTv()))
				hasTv = "1";
			map.put("has_tv", hasTv);//是否开通有线电视 1:是 0:否
			double tvFee = 0;
			if(null != rentContract.getTvFee())
				tvFee = rentContract.getTvFee();
			map.put("tv_fee", tvFee);
			double netFee = 0;
			if(null != rentContract.getNetFee())
				netFee = rentContract.getNetFee();
			map.put("net_fee", netFee);
			double waterFee = 0;
			if(null != rentContract.getWaterFee())
				waterFee = rentContract.getWaterFee();
			map.put("water_fee", waterFee);
			
			//房租押金差额
			if(!"0".equals(rentContract.getSignType())) { 
				if(!StringUtils.isBlank(rentContract.getContractBusiStatus())) {
					if(null != rentContract.getDepositAmount()) {
						map.put("re_deposit_amount", rentContract.getDepositAmount());
					}
					if(null != rentContract.getDepositElectricAmount()) {
						map.put("re_we_deposit_amount", rentContract.getDepositElectricAmount());
					}
				}
			} else {
				map.put("deposit_amount", rentContract.getDepositAmount());
				map.put("we_deposit_amount", rentContract.getDepositElectricAmount());
			}
			
			//首付房租月数
			if(null != rentContract.getRenMonths()) {
				map.put("pre_rent_month", rentContract.getRenMonths());
			}
			//首付押金月数
			if(null != rentContract.getDepositMonths()) {
				map.put("pre_deposit_month", rentContract.getDepositMonths());
			}
			
			PaymentOrder paymentOrder = new PaymentOrder();
			paymentOrder.setHouseId(null != room ? room.getId() : house.getId());
			paymentOrder = this.contractBookService.findByHouseId(paymentOrder);
			if (null != paymentOrder)
				map.put("order_id", paymentOrder.getOrderId());

			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("contract_info error:", e);
		}

		return data;
	}

	@RequestMapping(value = "notice")
	@ResponseBody
	public ResponseData notice(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuffer html = new StringBuffer();
			html.append("<div><p>");
			html.append("<h3><center>租房须知</center></h3>");
			html.append("&nbsp;&nbsp;1.为了确保居住环境的安全，本公寓实行实名制入住，凡入住前必须提供身份证原件，出租方将对信息进行核实；");
			html.append("乙方入住该房屋之日起的五日内，需按照当地警署的要求办理外来人口暂住手续；</br>");
			html.append("&nbsp;&nbsp;2.承租户不得以任何理由拖欠房租，如不及时缴纳房租的，则视为承租户违约，所缴纳房租押金不予退还，出租方有权将该房屋租给他人。</br>");
			html.append("&nbsp;&nbsp;3.合租户在租赁期内须按时交纳水、电、网络、宽带等费用。门禁卡发放是壹张身份证发放壹张卡（每间最多发放两张卡），如遗失补办卡20元/张；门锁若为密码开启方式，请妥善保管好密码，预防泄露造成损失；一楼楼道门禁属于物业管辖，每个房间标配一张门禁卡，如需增加门禁卡可向物业公司购买或委托人才公寓代为购买，每张卡以物业公司售价为准。</br>");
			html.append("&nbsp;&nbsp;4.整租户水、电、有线等公共事业费按国家标准收取。</br>");
			html.append("&nbsp;&nbsp;5.自房屋出租之日起，房屋内所有设施如损坏由承租户照价赔偿，赔偿费用详见附件二。室内设施包括照明、床、衣柜、空调、床头柜、床垫等家具；门、窗、窗帘、电视等电器；公共区域包括餐桌椅、整体橱柜、脱排油烟机、电磁炉、冰箱、洗衣机、马桶、龙头、淋浴等设备设施；禁止在室内钉钉子、挂画等破坏房内设施及结构的行为。公共区域设备损坏赔偿如未发现责任人，则由所有承租人均摊。入住时人才公寓将房屋保洁干净交付承租人使用，退房时承租人也需保洁干净退还人才公寓，若房屋脏乱退还需收取一次性保洁费200元。</br>");
			html.append("&nbsp;&nbsp;6.请各位承租户自觉爱护居住环境，生活垃圾必须装入垃圾袋内自觉丢入小区物业指定的垃圾桶内，剩菜、剩饭等垃圾不得随意乱丢乱抛到室外；请勿在过道或楼梯间摆放个人物品；不得在房屋内楼梯间和走道等公共区域的墙面上涂写及留下脚印和污渍；不要将纸巾、果壳、卫生巾等杂物丢入马桶内，否则堵塞下水道将由承租户自行解决, 如须甲方派人处理，所需费用（50元/次）由承租户承担。</br>");
			html.append("&nbsp;&nbsp;7.严禁在天台随意堆物及晾晒衣服，严禁在楼道内、楼道门口违规停放非机动车。若执意乱停车、乱堆杂物，一经发现物业管理处将对您的车辆或物品清理，后果自负。</br>");
			html.append("&nbsp;&nbsp;8.严禁往楼下扔杂物或抛污水，严重的高空抛物将追究法律责任。</br>");
			html.append("&nbsp;&nbsp;9.禁止在楼道利用公共设施给电瓶车充电，一经发现，物业管理处将给予处罚。</br>");
			html.append("&nbsp;&nbsp;10.为了大家的居住环境和健康卫生着想，人才公寓内禁止饲养各种宠物（如狗、猫）。</br>");
			html.append("&nbsp;&nbsp;11.承租户必须遵守人才公寓关于消防安全的规定，严禁在公寓内私拉电线、网线、不得在房间内使用电磁炉、热得快、电褥等违章电器，禁止在室内做饭。</br>");
			html.append("&nbsp;&nbsp;12.禁止在室内焚烧杂物，存放易燃、易爆、剧毒等危险品。</br>");
			html.append("&nbsp;&nbsp;13.出租房内不许吸毒，赌博，打架斗殴，如发现马上拨打110举报。</br>");
			html.append("&nbsp;&nbsp;14.夜间晚归的承租户不得大声喧哗，吵闹以免影响他人休息。</br>");
			html.append("&nbsp;&nbsp;15.由于承租户自身原因导致无法进入房间（如忘带门卡、忘办理续卡、忘记密码等）人才公寓8：30至20：00可提供开门服务,超过20：00至次日8:30每次开门需收取50元服务费。</br>");
			html.append("&nbsp;&nbsp;16.承租户在出门前要注意检查门窗是否锁好，保管好自己的贵重物品，以免丢失。</br>");
			html.append("&nbsp;&nbsp;17.人才公寓联系方式  地址：创新西路357号  咨询热线：4006-269-069 </br>");
			html.append("</p></div>");

			map.put("str_html", html);
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
		}

		return data;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "alipaynNotify")
	public void alipaynNotify(HttpServletRequest request, HttpServletResponse response) {
		this.log.info("rms start alipay notify......");
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
				params.put(name, valueStr);
			}

			// 交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			this.log.info("trade_status:" + trade_status);
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			this.log.info("out_trade_no:" + out_trade_no);
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			if (AlipayNotify.verify(params)) {// 验证成功
				this.log.info("verify success");
				if (trade_status.equals("TRADE_SUCCESS")) {
					this.log.info("更改订单状态.");
					// 1.更改订单状态
					PaymentOrder paymentOrder = this.contractBookService.findByOrderId(out_trade_no);
					PaymentOrder updatePaymentOrder = new PaymentOrder();
					updatePaymentOrder.setOrderId(out_trade_no);
					updatePaymentOrder.setOrderStatus("2");// 已支付
					this.contractBookService.updateStatusByOrderId(updatePaymentOrder);
					// 2.根据订单号获取账务交易ID
					paymentOrder = new PaymentOrder();
					paymentOrder.setOrderId(out_trade_no);
					paymentOrder.setOrderStatus("2");// 已支付
					paymentOrder.setTransId(trade_no);
					paymentOrder = this.contractBookService.findByOrderId(paymentOrder);
					// 3.走账务交易审核通过流程
					String tradeId = paymentOrder.getTradeId();
					AuditHis auditHis = new AuditHis();
					auditHis.setObjectId(tradeId);
					auditHis.setAuditMsg("手机在线支付");
					auditHis.setAuditStatus("1");// 通过
					this.tradingAccountsService.audit(auditHis);
					
					//充电费
					TradingAccounts tradingAccounts = this.tradingAccountsService.get(tradeId);
					if("11".equals(tradingAccounts.getTradeType())) {
						RentContract rentContract = this.rentContractService.get(tradingAccounts.getTradeId());
						String meterNo = "";
						if (null != rentContract && "1".equals(rentContract.getRentMode())) {// 单间
							Room room = rentContract.getRoom();
							room = this.roomService.get(room);
							meterNo = room.getMeterNo();
						}
						this.tradingAccountsService.charge(meterNo, String.format("%.0f",tradingAccounts.getTradeAmount()));
					}
					
					response.getWriter().write("success");
				}
			} else {
				this.log.info("verify failed.");
			}
		} catch (Exception e) {
			this.log.error("rms alipay notify error:", e);
		}

		this.log.info("rms end alipay notify......");
	}

	@RequestMapping(value = "contract")
	@ResponseBody
	public ResponseData contract(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("contract_id")) {
			data.setCode("101");
			return data;
		}

		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			if(null == apptoken) {
				data.setCode("400");
				data.setMsg("请重新登录");
				return data;
			}
			
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);

			RentContract rentContract = this.rentContractService.get(request.getParameter("contract_id"));

			Map<String, Object> map = new HashMap<String, Object>();

			String address = this.propertyProjectService.get(rentContract.getPropertyProject().getId()).getProjectAddr();
			address += this.buildingService.get(rentContract.getBuilding().getId()).getBuildingName();
			address += this.houseService.get(rentContract.getHouse().getId()).getHouseNo()+"室";
			if(null != rentContract.getRoom() && StringUtils.isNotBlank(rentContract.getRoom().getId())) {
				Room room = this.roomService.get(rentContract.getRoom().getId());
				if(null != room) {
					address += room.getRoomNo()+"部位";
				}
			}
			
			StringBuffer html = new StringBuffer();
			
			if("0".equals(rentContract.getSignType())) {//新签
				html.append("<div><p>");
				html.append("<h3><center>唐巢人才公寓租赁合同</center></h3>");
				html.append("&nbsp;&nbsp;(合同编号：" + rentContract.getContractCode() + ")</br>");
				html.append("&nbsp;&nbsp;出租方(甲方)：上海唐巢投资有限公司</br>");
				html.append("&nbsp;&nbsp;承租方(乙方)：" + appUser.getName() + "</br></br>");
				html.append("&nbsp;&nbsp;根据《中华人民共和国合同法》、《上海市房屋租赁条例》、《上海市居住房屋租赁管理办法》的规定，"
						+ "甲、乙双方在平等、自愿、公平和诚实信用的基础上，经协商一致，就乙方承租甲方可依法出租的以下房屋，订立本合同。</br>");
				html.append("&nbsp;&nbsp;一、出租房屋情况及用途</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;1-1房屋地址：<span style='text-decoration:underline;'>"
						+address+"</span>【以下简称“该房屋”】。具体配置【详见附件三】。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;1-2乙方向甲方承诺，乙方承租该房屋仅作为乙方居住使用。 </br>");
				html.append("&nbsp;&nbsp;二、租赁期限</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;2-1该房屋的租赁期为<span style='text-decoration:underline;'>"
						+DateUtils.getMonthSpace(rentContract.getStartDate(),rentContract.getExpiredDate())+"</span>个月，"
						+ "自<span style='text-decoration:underline;'>"+DateUtils.formatDate(rentContract.getStartDate(),"yyyy年MM月dd日")
						+"</span>起至<span style='text-decoration:underline;'>"+DateUtils.formatDate(rentContract.getExpiredDate(),"yyyy年MM月dd日")+"</span>止。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;2-2租赁期满，如乙方需要继续承租该房屋的，则应于租赁期届满前<span style='text-decoration:underline;'> 30 </span>"
						+ "天提出续租的书面要求，经甲乙双方对续租期间的租金等主要条款协商一致后，双方签订续租合同，否则视为乙方放弃续租。</br>");
				html.append("&nbsp;&nbsp;三、租金、支付方式和限期</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;3-1该房屋的月租金为人民币：<span style='text-decoration:underline;'>"+rentContract.getRental()+"</span>元(大写:<span style='text-decoration:underline;'>"+getChineseNum(rentContract.getRental())+"</span>)。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;3-2支付方式：付<span style='text-decoration:underline;'> "+rentContract.getRenMonths()+" </span>押"
						+ "<span style='text-decoration:underline;'> "+rentContract.getDepositMonths()+" </span>，先付后用。乙方于本合同生效之日向甲方支付首期租金及押金。之后每期租期届满前向甲方支付下一期租金。乙方逾期支付的，按未付款额的日1%支付违约金。</br>");
				html.append("&nbsp;&nbsp;四、押金和其他费用</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;4-1本房屋租赁押金为<span style='text-decoration:underline;'> "+rentContract.getDepositMonths()+" </span>个月的租金，"
						+ "即人民币：<span style='text-decoration:underline;'> "+rentContract.getDepositAmount()+" </span>元(大写：<span style='text-decoration:underline;'>"+getChineseNum(rentContract.getDepositAmount())+"</span>)，押金作为乙方向甲方承诺履行本合同的保证，甲方收取押金后应向乙方开具收款凭证。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;4-2租赁关系终止时，甲方收取乙方该房屋的租赁押金除用以抵充合同约定由乙方承担的费用外，剩余部分无息归还乙方。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;4-3智能电表结算：乙方入住前需自行或委托甲方对房间电表进行不少于人民币伍佰元充值，当电量低于20度时需再次充值，若不及时充值智能电表会自动断电，充值后电量方可恢复。乙方合同终止时，甲方在与乙方所有费用结清后的15日内将剩余费用返还乙方。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;4-4非智能电表结算：乙方入住前需支付人民币伍佰元的水电煤、宽带、有线等使用费押金。水电煤、宽带、有线使用费由甲方先代为乙方缴纳，甲方再按固定周期向乙方进行收缴。乙方合同终止时，甲方在与乙方所有费用结清后的15日内将剩余使用费押金返还乙方。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;4-5该房屋的使用费说明：电费<span style='text-decoration:underline;'>  </span>；水费<span style='text-decoration:underline;'>  </span>；天燃气费<span style='text-decoration:underline;'>  </span>；宽带费<span style='text-decoration:underline;'>  </span>元/月；有线电视费<span style='text-decoration:underline;'>  </span>元/月；其他<span style='text-decoration:underline;'>  </span>。</br>");
				html.append("&nbsp;&nbsp;五、房屋使用要求和维修责任</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;5-1租赁期间，乙方发现该房屋及其附属设施有损坏或故障时，应及时通知甲方修复。甲方应在接到乙方通知后：急修在<span style='text-decoration:underline;'> 1 </span>个工作日内；其它维修在<span style='text-decoration:underline;'> 3 </span>个工作日内进行维修。或有特殊情况，甲乙双方另行协商解决。 </br>"            
						+ "&nbsp;&nbsp;&nbsp;&nbsp;5-2租赁期间，因乙方使用不当或不合理使用，致使该房屋及其附属设施损坏或发生故障的，乙方应负责维修。乙方拒不维修，甲方可代为维修，费用由乙方承担。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;5-3租赁期间，甲方对该房屋及设备设施应进行检查、养护，但应提前<span style='text-decoration:underline;'> 2 </span>日通知乙方。检查养护时，乙方应予以配合。甲方应减少对乙方使用该房屋的影响。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;5-4租赁期间，乙方承诺按照【附件一】《租房须知》中的条款，遵守相关规定，安全、文明租房。如乙方在租房期间发生因违反《租房须知》规定所造成的人员伤亡或安全等问题的，责任由乙方自负。</br>");
				html.append("&nbsp;&nbsp;六、房屋返还</br>");
				html.append("&nbsp;&nbsp;&nbsp;&nbsp;6-1除甲方同意乙方续租外，乙方应在本合同的租期届满之日内返还该房屋，未经甲方同意逾期返还房屋的，每逾期一日，乙方需按日租金的<span style='text-decoration:underline;'> 双 </span>倍支付该房屋占用使用费。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;6-2乙方返还该房屋应当符合正常使用后的状态，如该房屋内设施及设备配置的有损坏，乙方应照价赔偿【详见附件二】。返还时，应经甲方验收认可，并相互结清各自应当承担的费用。乙方自行添置的家具等设备设施的，应在乙方返还该房屋之日前自行处置或搬离，如乙方在返还房屋之日仍未搬出该房屋内自行添置的各种家具等设备设施的，所遗留物品甲方视为乙方已经遗弃，甲方有权自行处置，且无须另行支付对价或补偿。如在处置乙方遗弃物件时发生费用的，该费用由乙方另行偿付。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;6-3乙方确认不再续租或在退租前，应积极配合甲方对该房屋招租提供方便。</br>");
				html.append("&nbsp;&nbsp;七、房屋严禁擅自转租</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;7-1乙方入住六个月以后可以申请接力转让该房间（1月、2月和12月甲方不接受委托转租，办理居住证及社区公共户落户的甲方不接受委托转租），但要同时满足以下条件：</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（一）居住满六个月以上的在原价基础上上涨100元/月；</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（二）通过甲方推荐广告渠道再次成功出租该房屋需向甲方支付500元推荐服务费；</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（三）乙方自行找到接力租客成功出租的需支付100元合同变更手续费，对于乙方自行寻找的客户必须符合甲方的客户选择标准，确保无空置期；</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（四）甲方不承诺转租时限，转租期间乙方仍需支付房屋租金。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;7-2不办理转租手续擅自让承租人以外人入住的合同立即解除，甲方没收全部租赁押金。擅自转租或让承租人以外人入住的合同立即解除，甲方没收全部租赁押金。</br>");
				html.append("&nbsp;&nbsp;八、违约责任及解除本合同的条件</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;8-1甲、乙双方同意，在租赁期内，该房屋占用范围内的土地使用权或房屋被依法提前收回或依法征用的；或被依法列入房屋拆迁许可范围的；或发生不可抗力被损毁、灭失的。本合同终止，双方互不承担责任：</br>" 
						+ "&nbsp;&nbsp;&nbsp;&nbsp;8-2甲、乙双方同意，有下列情形之一的均视作根本违约，守约方有权解除本合同。同时，违约方应向守约方支付相当于      个月租金的违约金；如造成守约方损失的，违约方在支付的违约金不足抵付守约方损失的，还应赔偿相应的损失：</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（一）乙方未征得甲方同意改变该房屋用途、该房屋主体结构损坏的；</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（二）乙方擅自转租该房屋或与他人交换各自承租的房屋的；</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（三）乙方逾期不支付租金及水电煤、宽带、有线等使用费累计超过<span style='text-decoration:underline;'> 5 </span>天的；</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;（四）乙方不遵守该房屋的公寓管理制度或《租房须知》中相关规定的。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;8-3租赁期间，甲方擅自中途提前收回该房屋的，甲方应向乙方赔偿<span style='text-decoration:underline;'>  </span>个月租金的违约金； </br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;8-4租赁期间，乙方擅自中途提前退租的，乙方的房屋押金不予退回。剩余房款由甲方在与乙方所有费用结清后的 15日 内返还乙方；</br>");
				html.append("&nbsp;&nbsp;九、其它条款</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;9-1本合同未尽事宜，经甲、乙双方协商一致，可订立补充协议。本合同补充协议及附件均为本合同不可分割的一部分。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;9-2甲、乙双方在履行本合同过程中发生争议，应通过协商解决，协商不成的，可向该房屋所在地的人民法院提出诉讼。</br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;9-3本合同连同附件一式<span style='text-decoration:underline;'> 贰 </span>份。甲、乙双方各持一份，经甲乙双方签字或盖章之日起生效，并均具有同等效力。</br>");
				html.append("&nbsp;&nbsp;十、备注</br></br>");
				html.append("&nbsp;&nbsp;出租方(甲方)：上海唐巢投资有限公司</br>");
				html.append("&nbsp;&nbsp;代理人：</br>");
				html.append("&nbsp;&nbsp;联系地址：创新西路357号</br>");
				html.append("&nbsp;&nbsp;联系电话：021-68876662 4006-269-069</br>");
				html.append("&nbsp;&nbsp;签约日期：" + DateFormatUtils.format(rentContract.getSignDate(), "yyyy-MM-dd")
				+ "</br></br>");
				html.append("&nbsp;&nbsp;承租方(乙方)：" + appUser.getName() + "</br>");
				html.append("&nbsp;&nbsp;代理人：</br>");
				html.append("&nbsp;&nbsp;身份证号码：" + appUser.getIdCardNo() + "</br>");
				html.append("&nbsp;&nbsp;联系地址：</br>");
				html.append("&nbsp;&nbsp;联系电话：" + appUser.getPhone() + "</br>");
				html.append("&nbsp;&nbsp;紧急联系人电话：" + appUser.getPhone() + "</br>");
				html.append(
						"&nbsp;&nbsp;签约日期：" + DateFormatUtils.format(rentContract.getSignDate(), "yyyy-MM-dd") + "</br>");
				html.append("</p></div>");
			} else {//续签
				String oldId = rentContract.getContractId();
				RentContract oldRentContract = null;
				if(StringUtils.isNoneBlank(oldId))
					oldRentContract = this.rentContractService.get(oldId);
				else
					oldRentContract = new RentContract();
				html.append("<div><p>");
				html.append("<h3><center>唐巢人才公寓续租合同</center></h3>");
				html.append("&nbsp;&nbsp;(合同编号：" + rentContract.getContractCode() + ")</br>");
				html.append("&nbsp;&nbsp;出租方(甲方)：上海唐巢投资有限公司</br>");
				html.append("&nbsp;&nbsp;承租方(乙方)：" + appUser.getName() + "</br></br>");
				html.append("&nbsp;&nbsp;根据《中华人民共和国合同法》、《上海市房屋租赁条例》甲乙双方在平等、自愿、公平的基础上经协商一致，"
						+ "同意就原租赁房屋：<span style='text-decoration:underline;'>"
						+address+"</span>续租事宜达成下列协议并共同遵守：</br>");
				html.append("&nbsp;&nbsp;一、本续租协议未涉及的内容，仍按原《租赁合同》及《租房须知》执行并保持不变。本续租协议是原《租赁合同》不可分割部分，与原《租赁合同》具有同等法律效力。</br>");
				html.append("&nbsp;&nbsp;二、该房屋原月租金为人民币<span style='text-decoration:underline;'>"+oldRentContract.getRental()+"</span>元，原租期自<span style='text-decoration:underline;'>"+DateUtils.formatDate(oldRentContract.getStartDate(),"yyyy年MM月dd日")
						+"</span>至<span style='text-decoration:underline;'>"+DateUtils.formatDate(oldRentContract.getExpiredDate(),"yyyy年MM月dd日")+"</span>止，"
					    + "共<span style='text-decoration:underline;'> "+DateUtils.getMonthSpace(oldRentContract.getStartDate(), oldRentContract.getExpiredDate())+" </span>个月；房屋押金为"
						+ "<span style='text-decoration:underline;'> "+oldRentContract.getDepositAmount()+" </span>元。</br>");
				html.append("&nbsp;&nbsp;三、续租期限自<span style='text-decoration:underline;'>"+DateUtils.formatDate(rentContract.getStartDate(),"yyyy年MM月dd日")
						+"</span>至<span style='text-decoration:underline;'>"+DateUtils.formatDate(rentContract.getExpiredDate(),"yyyy年MM月dd日")
						+"</span>止，共<span style='text-decoration:underline;'> "+DateUtils.getMonthSpace(rentContract.getStartDate(), rentContract.getExpiredDate())+" </span>个月。"
						+ "续租月租金为<span style='text-decoration:underline;'>"+rentContract.getRental()+"</span>人民币元整（ 大写：<span style='text-decoration:underline;'>"+getChineseNum(rentContract.getRental())+"</span>元整）；"
						+ "该房屋押金现为人民币<span style='text-decoration:underline;'>"+rentContract.getDepositAmount()+"</span>元整(大写：<span style='text-decoration:underline;'>"+getChineseNum(rentContract.getDepositAmount())+"</span>元整)，若房屋押金不足乙方需补足。</br>");
				html.append("&nbsp;&nbsp;四、付款方式为乙方续租后以<span style='text-decoration:underline;'> "+rentContract.getRenMonths()+" </span>个月为一期支付。</br>");
				html.append("&nbsp;&nbsp;五、签订本续租协议当日，乙方应向甲方交纳房屋租金人民币<span style='text-decoration:underline;'>"+rentContract.getRenMonths()*rentContract.getRental()+"</span>元整( 大写：<span style='text-decoration:underline;'>"+getChineseNum(rentContract.getRenMonths()*rentContract.getRental())+"</span>,收据号：)及(其它)费用人民币__元整(大写__元,收据号：__)。</br>");
				html.append("&nbsp;&nbsp;六、本续租协议履行中发生争议，双方应采取协商办法解决，协商不成，可向当地人民法院起诉。</br>");
				html.append("&nbsp;&nbsp;七、本续租协议一式贰份，甲方一份，乙方一份，自双方签字之日起生效。</br>");
				html.append("&nbsp;&nbsp;</br>");
				html.append("&nbsp;&nbsp;出租方(甲方)：上海唐巢投资有限公司</br>");
				html.append("&nbsp;&nbsp;代理人：</br>");
				html.append("&nbsp;&nbsp;联系地址：创新西路357号</br>");
				html.append("&nbsp;&nbsp;联系电话：021-68876662 4006-269-069</br>");
				html.append("&nbsp;&nbsp;签约日期：" + DateFormatUtils.format(rentContract.getSignDate(), "yyyy-MM-dd")
				+ "</br></br>");
				html.append("&nbsp;&nbsp;承租方(乙方)：" + appUser.getName() + "</br>");
				html.append("&nbsp;&nbsp;代理人：</br>");
				html.append("&nbsp;&nbsp;身份证号码：" + appUser.getIdCardNo() + "</br>");
				html.append("&nbsp;&nbsp;联系地址：</br>");
				html.append("&nbsp;&nbsp;联系电话：" + appUser.getPhone() + "</br>");
				html.append("&nbsp;&nbsp;紧急联系人电话：" + appUser.getPhone() + "</br>");
				html.append(
						"&nbsp;&nbsp;签约日期：" + DateFormatUtils.format(rentContract.getSignDate(), "yyyy-MM-dd") + "</br>");
				html.append("</p></div>");
			}

			map.put("str_html", html);
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("contract_info error:", e);
		}

		return data;
	}

	@RequestMapping(value = "pay_sign_booked")
	@ResponseBody
	public ResponseData paysignBooked(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("order_id")) {
			data.setCode("101");
			return data;
		}

		String orderId = request.getParameter("order_id");
		PaymentOrder paymentOrder = this.contractBookService.findByOrderId(orderId);
		if(null == paymentOrder) {
			data.setCode("400");
			data.setMsg("订单已过期");
			return data;
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		//测试用
		paymentOrder.setOrderAmount(0.01d);
		Double orderAmount = paymentOrder.getOrderAmount();
		String signStr = "";
		try {
			signStr = AlipayUtil.buildRequest(orderId, df.format(orderAmount));
		} catch (Exception e) {
			this.log.error("get alipay sign error:", e);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sign", signStr);

		data.setData(map);
		data.setCode("200");
		return data;
	}
	
	@RequestMapping(value = "pay_sign_contract")
	@ResponseBody
	public ResponseData paysignContract(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("order_id")) {
			data.setCode("101");
			return data;
		}

		String orderId = request.getParameter("order_id");
		PaymentOrder paymentOrder = this.contractBookService.findByOrderId(orderId);
		if(null == paymentOrder) {
			data.setCode("400");
			data.setMsg("订单已过期");
			return data;
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		//测试用
		paymentOrder.setOrderAmount(0.01d);
		Double orderAmount = paymentOrder.getOrderAmount();
		String signStr = "";
		try {
			signStr = AlipayUtil.buildRequest(orderId, df.format(orderAmount));
		} catch (Exception e) {
			this.log.error("get alipay sign error:", e);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sign", signStr);

		data.setData(map);
		data.setCode("200");
		return data;
	}
	
	@RequestMapping(value = "recharge")
	@ResponseBody
	public ResponseData recharge(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("contract_id") || null == request.getParameter("fee")) {
			data.setCode("101");
			return data;
		}
		
		try {
			RentContract rentContract = this.rentContractService.get(request.getParameter("contract_id"));
			String meterNo = "";
			if (null != rentContract && "1".equals(rentContract.getRentMode())) {// 单间
			    Room room = rentContract.getRoom();
			    room = this.roomService.get(room);
			    meterNo = room.getMeterNo();
			} 
			
			if(StringUtils.isBlank(meterNo)) {
				data.setCode("400");
				data.setMsg("没有电表需要充值");
				return data;
			}
			
			if(StringUtils.isNoneBlank(meterNo)) {
				String chargeValue = String.format("%.0f",Double.valueOf(request.getParameter("fee")));
				
				//记录款项
				PaymentTrans paymentTrans = new PaymentTrans();
				paymentTrans.setTradeType("11");//电费充值
				paymentTrans.setTransId(request.getParameter("contract_id"));
				paymentTrans.setTradeDirection("1");//收款
				paymentTrans.setStartDate(new Date());
				paymentTrans.setExpiredDate(new Date());
				paymentTrans.setTradeAmount(Double.valueOf(chargeValue));
				paymentTrans.setLastAmount(0d);
				paymentTrans.setTransAmount(Double.valueOf(chargeValue));
				paymentTrans.setTransStatus("2");//完全到账登记
			    paymentTrans.setPaymentType("11");//电费自用金额
				paymentTrans.setCreateDate(new Date());
				paymentTrans.setUpdateDate(new Date());
				paymentTrans.setDelFlag("0");
				this.paymentTransService.save(paymentTrans);
				
				//账务交易
				TradingAccounts tradingAccounts = new TradingAccounts();
				tradingAccounts.setId(IdGen.uuid());
				tradingAccounts.setTradeId(rentContract.getId());
				tradingAccounts.setTransIds(paymentTrans.getId());
				tradingAccounts.setTradeStatus("0");//待审核
				tradingAccounts.setTradeType("11");//电费充值
				tradingAccounts.setTradeAmount(Double.valueOf(chargeValue));
				tradingAccounts.setTradeDirection("1");//入账
				tradingAccounts.setPayeeType("1");//个人
				String token = (String) request.getHeader("token");
				AppToken apptoken = new AppToken();
				apptoken.setToken(token);
				apptoken = appTokenService.findByToken(apptoken);
				if(null == apptoken) {
					data.setCode("400");
					data.setMsg("请重新登录");
					return data;
				}
				tradingAccounts.setPayeeName(apptoken.getPhone());
				List<Receipt> receiptList = new ArrayList<Receipt>();
				Receipt receipt = new Receipt();
				receipt.setTradeMode("4");//支付宝
				receipt.setPaymentType("11");//电费自用金额
				receipt.setReceiptAmount(Double.valueOf(chargeValue));
				receiptList.add(receipt);
				tradingAccounts.setReceiptList(receiptList);
				tradingAccountsDao.insert(tradingAccounts);

				/* 订单生成 */
				String houseId = "";
				if(null != rentContract.getRoom())
					houseId = rentContract.getRoom().getId();
				else
					houseId = rentContract.getHouse().getId();
				
				PaymentOrder paymentOrder = new PaymentOrder();
				paymentOrder.setOrderId(contractBookService.generateOrderId());
				paymentOrder.setOrderDate(new Date());
				paymentOrder.setOrderStatus("1");// 未支付
				paymentOrder.setTradeId(tradingAccounts.getId());
				paymentOrder.setOrderAmount(tradingAccounts.getTradeAmount());
				paymentOrder.setCreateDate(new Date());
				paymentOrder.setHouseId(houseId);
				this.contractBookService.saveOrder(paymentOrder);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("order_id", paymentOrder.getOrderId());
				map.put("price", paymentOrder.getOrderAmount());
				
				data.setData(map);
				data.setCode("200");
			}
			
		} catch (Exception e) {
			data.setCode("400");
			data.setMsg("电费充值失败");
			this.log.error("电表充值失败",e);
		}
		
		return data;
	}
	
	@RequestMapping(value = "checkin_bill")
	@ResponseBody
	public ResponseData checkinBill(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("bill_id")) {
			data.setCode("101");
			return data;
		}
		
		String token = (String) request.getHeader("token");
		AppToken apptoken = new AppToken();
		apptoken.setToken(token);
		apptoken = appTokenService.findByToken(apptoken);
		if(null == apptoken) {
			data.setCode("400");
			data.setMsg("请重新登录");
			return data;
		}
		AppUser appUser = new AppUser();
		appUser.setPhone(apptoken.getPhone());
		appUser = appUserService.getByPhone(appUser);
		
		String billIds = request.getParameter("bill_id");
		List<Receipt> receiptList = new ArrayList<Receipt>();
		
		PaymentTrans paymentTrans = this.paymentTransService.get(billIds.split(",")[0]);
		String[] billIdArr = billIds.split(",");
		Double orderAmount = 0d;
		for(String bId : billIdArr) {
			PaymentTrans tmpPaymentTrans = this.paymentTransService.get(bId);
			if(!"2".equals(tmpPaymentTrans.getTransStatus()))
				orderAmount += tmpPaymentTrans.getLastAmount();
		}
		
		if(orderAmount <= 0) {
			data.setCode("400");
			data.setMsg("账单已付清");
			return data;
		}
		
		TradingAccounts tradingAccounts = new TradingAccounts();
		tradingAccounts.setTransIds(billIds);
		tradingAccounts.setTradeId(paymentTrans.getTransId());
		tradingAccounts.setTradeStatus("0");// 待审核
		tradingAccounts.setTradeType("3");// 新签合同
		tradingAccounts.setTradeDirection("1");// 入账
		tradingAccounts.setPayeeType("1");// 个人
		tradingAccounts.setPayeeName(appUser.getName());
		tradingAccounts.setReceiptList(receiptList);
		tradingAccounts.setTradeAmount(orderAmount);
		tradingAccountsService.save(tradingAccounts);
		
		/* 订单生成 */
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setOrderAmount(orderAmount);
		paymentOrder.setTradeId(tradingAccounts.getId());
		paymentOrder.setOrderId(contractBookService.generateOrderId());
		paymentOrder.setOrderDate(new Date());
		paymentOrder.setOrderStatus("1");// 未支付
		paymentOrder.setCreateDate(new Date());
		paymentOrder.setHouseId("");
		this.contractBookService.saveOrder(paymentOrder);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", paymentOrder.getOrderId());
	    map.put("price", paymentOrder.getOrderAmount());

		data.setData(map);
		data.setCode("200");
		return data;
	}
	
	@RequestMapping(value = "pay_bill")
	@ResponseBody
	public ResponseData payBill(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();

		if (null == request.getParameter("order_id")) {
			data.setCode("101");
			return data;
		}
		
		String orderId = request.getParameter("order_id");
		PaymentOrder paymentOrder = this.contractBookService.findByOrderId(orderId);
		if(null == paymentOrder) {
			data.setCode("400");
			data.setMsg("订单已过期");
			return data;
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		Double orderAmount = paymentOrder.getOrderAmount();
		String signStr = "";
		try {
			signStr = AlipayUtil.buildRequest(orderId, df.format(orderAmount));
		} catch (Exception e) {
			this.log.error("get alipay sign error:", e);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sign", signStr);

		data.setData(map);
		data.setCode("200");
		return data;
	}

	@RequestMapping(value = "bill")
	@ResponseBody
	public ResponseData bill(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if (null == request.getParameter("contract_id")) {
			data.setCode("101");
			data.setMsg("合同编号'contract_id'不能为空");
			return data;
		}

		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setTransId(request.getParameter("contract_id"));
		List<PaymentTrans> listPaymentTrans = paymentTransService.findList(paymentTrans);
		
		RentContract rentContract = this.rentContractService.get(request.getParameter("contract_id"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> mp = null;
		String bill_id = "";
		Double bill_amount = 0d;
		
		try {
			for(PaymentTrans tmpPaymentTrans : listPaymentTrans) {
				boolean check = true;
				for(int i=0;i<list.size();i++) {
					Map<String, Object> tmpMap = list.get(i);
					Date dt = DateUtils.parseDate(tmpMap.get("bill_start").toString(), "yyyy-MM-dd");
					double monthSpace = DateUtils.getMonthSpace(dt,tmpPaymentTrans.getStartDate());
					if(monthSpace < rentContract.getRenMonths()) {
						check = false;
						mp = tmpMap;
						bill_id = mp.get("bill_id")+","+tmpPaymentTrans.getId();
						mp.put("bill_id", bill_id);
						bill_amount = (Double)mp.get("bill_amount")+tmpPaymentTrans.getTradeAmount();
						mp.put("bill_amount", bill_amount);
						if("6".equals(tmpPaymentTrans.getPaymentType()))
							mp.put("rent_amount", Double.valueOf(mp.get("rent_amount").toString())+tmpPaymentTrans.getTradeAmount());//房租金额
						if("4".equals(tmpPaymentTrans.getPaymentType())) {
							mp.put("deposit_amount", Double.valueOf(mp.get("deposit_amount").toString())+tmpPaymentTrans.getTradeAmount());//房租押金
						}
						if("2".equals(tmpPaymentTrans.getPaymentType())) {
							mp.put("deposit_water_amount", Double.valueOf(mp.get("deposit_water_amount").toString())+tmpPaymentTrans.getTradeAmount());//水电费押金
						}
						if("20".equals(tmpPaymentTrans.getPaymentType())) {
							double netAmount = tmpPaymentTrans.getTradeAmount();
							if(null != mp.get("net_amount"))
								netAmount += Double.valueOf(mp.get("net_amount").toString());
							mp.put("net_amount", netAmount);//宽带费
						}
						if("14".equals(tmpPaymentTrans.getPaymentType())) {
							double water_amount = tmpPaymentTrans.getTradeAmount();
							if(null != mp.get("water_amount"))
								water_amount += Double.valueOf(mp.get("water_amount").toString());
							mp.put("water_amount", water_amount);//水费金额
						}
						mp.put("payment_type", tmpPaymentTrans.getPaymentType());
						if("18".equals(tmpPaymentTrans.getPaymentType())) {
							double tv_amount = tmpPaymentTrans.getTradeAmount();
							if(null != mp.get("tv_amount"))
								tv_amount += Double.valueOf(mp.get("tv_amount").toString());
							mp.put("tv_amount", tv_amount);//有线电视费
						}
						if("2".equals(tmpPaymentTrans.getTransStatus()))
							mp.put("bill_state", "1");
						else
							mp.put("bill_state", "0");
						mp.put("bill_end", DateFormatUtils.format(tmpPaymentTrans.getExpiredDate(), "yyyy-MM-dd"));
						list.set(i, mp);
						break;
					}
				}
				
				if(check) {
					mp = new HashMap<String, Object>();
					bill_id = tmpPaymentTrans.getId();
					bill_amount = tmpPaymentTrans.getTradeAmount();
					mp.put("bill_id", bill_id);
					mp.put("bill_amount", bill_amount);
					mp.put("bill_start", DateFormatUtils.format(tmpPaymentTrans.getStartDate(), "yyyy-MM-dd"));
					mp.put("bill_end", DateFormatUtils.format(tmpPaymentTrans.getExpiredDate(), "yyyy-MM-dd"));
					double rent_amount = 0;
					if("6".equals(tmpPaymentTrans.getPaymentType()))
						rent_amount = tmpPaymentTrans.getTradeAmount();
					mp.put("rent_amount", rent_amount);//房租金额
					double deposit_amount = 0;
					if("4".equals(tmpPaymentTrans.getPaymentType())) {
						deposit_amount = tmpPaymentTrans.getTradeAmount();
					}
					mp.put("deposit_amount", deposit_amount);//房租押金
					double deposit_water_amount = 0;
					if("2".equals(tmpPaymentTrans.getPaymentType())) {
						deposit_water_amount = tmpPaymentTrans.getTradeAmount();
					}
					mp.put("deposit_water_amount", deposit_water_amount);//水电费押金
					double net_amount = 0;
					if("20".equals(tmpPaymentTrans.getPaymentType()))
						net_amount = tmpPaymentTrans.getTradeAmount();
					mp.put("net_amount", net_amount);//宽带费
					double water_amount = 0;
					if("14".equals(tmpPaymentTrans.getPaymentType()))
						water_amount = tmpPaymentTrans.getTradeAmount();
					if("18".equals(tmpPaymentTrans.getPaymentType()))
						mp.put("tv_amount", tmpPaymentTrans.getTradeAmount());//有线电视费
					mp.put("water_amount", water_amount);//水费金额
					String current_electric_balance="0", bill_electric_balance="0", common_electric_amount="0", common_electric_balance="0";
					String electricPrice = "0";
					try {
						Map<Integer, String> meterMap = this.electricFeeService.getMeterFee(request.getParameter("contract_id"),DateFormatUtils.format(rentContract.getStartDate(), "yyyy-MM-dd"),DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
						if(null != meterMap) {
							if(null != meterMap.get(3))
								current_electric_balance = meterMap.get(3);
							if(null != meterMap.get(1))
								bill_electric_balance = meterMap.get(1);
							if(null != meterMap.get(2))
								common_electric_balance = meterMap.get(2);
							if(null != meterMap.get(4))
								electricPrice = meterMap.get(4);
						}
					} catch (Exception e) {
						this.log.error("查询电表异常:",e);
					}
					mp.put("current_electric_amount", Double.valueOf(current_electric_balance)*Double.valueOf(electricPrice));//当前电费余额
					mp.put("current_electric_balance", current_electric_balance);//当前电费度数
					mp.put("bill_electric_amount", Double.valueOf(bill_electric_balance)*Double.valueOf(electricPrice));//本期个人用电金额
					mp.put("bill_electric_balance", bill_electric_balance);//本期个人用电度数
					mp.put("common_electric_amount", common_electric_amount);//本期公共用电金额
					mp.put("common_electric_balance", common_electric_balance);//本期公共用电度数
					String bill_state = "0";
					if("2".equals(tmpPaymentTrans.getTransStatus()))
						bill_state = "1";
					mp.put("bill_state", bill_state);//0:未付 1:已付
					mp.put("payment_type", tmpPaymentTrans.getPaymentType());
					list.add(mp);
				}
			}
		} catch (Exception e) {
			log.error("",e);
		}

		map.put("bill", list);
		map.put("p_t", list.size());

		data.setData(map);
		data.setCode("200");
		return data;
	}

	@RequestMapping(value = "repair")
	@ResponseBody
	public ResponseData avatar(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		if (null == request.getParameter("mobile")) {
			data.setCode("101");
			return data;
		}

		try {
			// String mobile = request.getParameter("mobile");

			Repairs repair = new Repairs();
			repair.setId(IdGen.uuid());
			repair.setUserMobile(request.getParameter("user_mobile"));
			repair.setContractId(request.getParameter("contract_id"));
			repair.setRoomId(request.getParameter("room_id"));
			repair.setStatus("01");
			repair.setDescription(request.getParameter("description"));
            User user = appUserService.getServiceUserByContractId(request.getParameter("contract_id"));
            if(user == null){
                data.setCode("400");
                data.setMsg("合同没有匹配的服务管家");
                return data;
            }
            repair.setSteward(user.getName());
            repair.setStewardMobile(user.getMobile());
			repairService.save(repair);

			String attach_path = request.getParameter("attach_path");
			if (attach_path == null) {
				data.setCode("500");
				data.setMsg("上传有误");
				return data;
			}
			Attachment attachment = new Attachment();
			attachment.setId(IdGen.uuid());
			attachment.setAttachmentType(FileType.REPAIR_PICTURE.getValue());
			attachment.setAttachmentPath(attach_path);
			attachment.setCreateDate(new Date());
			attachment.setCreateBy(UserUtils.getUser());
			attachment.setUpdateDate(new Date());
			attachment.setUpdateBy(UserUtils.getUser());
			attachment.setDelFlag("0");
			attachment.setBizId(repair.getId());
			attachmentDao.insert(attachment);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("steward", repair.getSteward());
			map.put("steward_mobile", repair.getStewardMobile());
			data.setData(map);
			data.setCode("200");
			data.setMsg("报修已提交");
		} catch (Exception e) {
			data.setCode("500");
			log.error("create repair error:", e);
		}
		return data;
	}
	
	private boolean checkHouseStatus(String houseId) {
		House house = this.houseService.get(houseId);
		if(null == house) {
			Room room = this.roomService.get(houseId);
			if(null == room || (!"1".equals(room.getRoomStatus()) && !"4".equals(room.getRoomStatus()))) {
				//1:待出租可预订 4:已退租可预订
				return false;
			}
		} else {
			String houseStatus = house.getHouseStatus();
			if(!"1".equals(houseStatus) && !"3".equals(houseStatus) && !"5".equals(houseStatus)) {
				//1:待出租可预订 3:部分出租 5:已退待租
				return false;
			}
		}
		return true;
	}
	
	protected static String getChineseNum(double numberMoney) {
		BigDecimal numberOfMoney = new BigDecimal(numberMoney);
		//单位数组
		String[] CN_UPPER_MONETRAY_UNIT = new String[] { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾",
				"佰", "仟", "兆", "拾", "佰", "仟" };
		//中文大写数字数组
		String[] CN_UPPER_NUMBER = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String CN_FULL = "整";
		String CN_NEGATIVE = "负";
		final int MONEY_PRECISION = 2;
		String CN_ZEOR_FULL = "零元" + CN_FULL;

		StringBuffer sb = new StringBuffer();

		int signum = numberOfMoney.signum();
		if (signum == 0) {
			return CN_ZEOR_FULL;
		}

		long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
		long scale = number % 100;
		int numUnit = 0;
		int numIndex = 0;
		boolean getZero = false;
		if (!(scale > 0)) {
			numIndex = 2;
			number = number / 100;
			getZero = true;
		}
		if ((scale > 0) && (!(scale % 10 > 0))) {
			numIndex = 1;
			number = number / 10;
			getZero = true;
		}
		int zeroSize = 0;
		while (true) {
			if (number <= 0) {
				break;
			}
			//每次获取到最后一个数
			numUnit = (int) (number % 10);
			if (numUnit > 0) {
				if ((numIndex == 9) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
				}
				if ((numIndex == 13) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
				}
				sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				getZero = false;
				zeroSize = 0;
			} else {
				++zeroSize;
				if (!(getZero)) {
					sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				}
				if (numIndex == 2) {
					if (number > 0) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					}
				} else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				}
				getZero = true;
			}
			//让number每次都去掉最后一个数
			number = number / 10;
			++numIndex;
		}
		//如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
		if (signum == -1) {
			sb.insert(0, CN_NEGATIVE);
		}
		//输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
		if (!(scale > 0)) {
			sb.append(CN_FULL);
		}

		return sb.toString();
	}

    @RequestMapping(value = "keeper")
    @ResponseBody
    public ResponseData keeper(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        try {
            String token = (String) request.getHeader("token");
            AppToken apptoken = new AppToken();
            apptoken.setToken(token);
            apptoken = appTokenService.findByToken(apptoken);
            if(null == apptoken) {
                data.setCode("400");
                data.setMsg("请重新登录");
                return data;
            }
            AppUser appUser = new AppUser();
            appUser.setPhone(apptoken.getPhone());
            User serviceUser = appUserService.getServiceUserByPhone(appUser);
            if(serviceUser == null){
                data.setCode("400");
                data.setMsg("未分配管家!");
            }else{
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", serviceUser.getId());
                map.put("name", serviceUser.getName());
                map.put("phone", serviceUser.getMobile());
                map.put("photo", serviceUser.getPhoto());
                map.put("level", "");
                map.put("agency", serviceUser.getCompany().getName());
                data.setData(map);
                data.setCode("200");
            }
        } catch (Exception e) {
            data.setCode("500");
            this.log.error("get house keeper error:", e);
        }
        return data;

    }
}
