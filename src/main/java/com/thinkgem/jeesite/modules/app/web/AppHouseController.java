package com.thinkgem.jeesite.modules.app.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.entity.FileType;
import com.thinkgem.jeesite.modules.repair.entity.Repair;
import com.thinkgem.jeesite.modules.repair.service.RepairService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.alipay.AlipayUtil;
import com.thinkgem.jeesite.modules.app.entity.AppToken;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.service.AppTokenService;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
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
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

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
    private RepairService repairService;

    @Autowired
    private AttachmentDao attachmentDao;
    
    @Autowired
	private HouseAdService houseAdService;
    
    @RequestMapping(value = "ad")
    @ResponseBody
    public ResponseData ad(HttpServletRequest request, HttpServletResponse response) {
    	ResponseData data = new ResponseData();
    	
    	try {
			List<HouseAd> listAd = houseAdService.findList(new HouseAd());
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
		    String img_url = proper.getProperty("img.url");
			for(HouseAd ad : listAd) {
				Map<String,String> adMap = new HashMap<String,String>();
				
				adMap.put("id", ad.getId());
				adMap.put("type", ad.getAdType());
				if("1".equals(ad.getAdType())) {
					String value = "";
					if(null != ad.getRoom() && !StringUtils.isBlank(ad.getRoom().getId()))
						value = ad.getRoom().getId();
					else
						value = ad.getHouse().getId();
					adMap.put("value", value);
				}
				adMap.put("url", img_url+ad.getAdUrl());
				list.add(adMap);
			}
			
			map.put("ad", list);
			data.setData(map);
			
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("list ad error:",e);
		}
    	
    	return data;
    }

    @RequestMapping(value = "findFeatureList")
    @ResponseBody
    public ResponseData findFeatureList(HttpServletRequest request, HttpServletResponse response) {
    	ResponseData data = new ResponseData();
    	if(null == request.getParameter("p_n") || null == request.getParameter("p_s")) {
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
			
			Map<String,Object> map = new HashMap<String,Object>();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<House> pageList = page.getList();
			for(House h:pageList) {
				Map<String,Object> mp = new HashMap<String,Object>();
				mp.put("id", h.getId());
				mp.put("house_num", h.getHouseCode());
				mp.put("price", h.getRental());
				mp.put("short_desc", h.getShortDesc());
				mp.put("short_location", h.getShortLocation());
				mp.put("pay_way", h.getPayWay());
				String cover = "";
				if(!StringUtils.isEmpty(h.getAttachmentPath())) {
					PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
				    String img_url = proper.getProperty("img.url");
					cover = img_url+StringUtils.split(h.getAttachmentPath(), "|")[0];
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
			log.error("findFeatureList error:",e);
		}    
    	return data;
    }
	
	@RequestMapping(value = "getFeatureInfo")
    @ResponseBody
    public ResponseData getFeatureInfo(HttpServletRequest request, HttpServletResponse response) {
    	ResponseData data = new ResponseData();
    	if(null == request.getParameter("house_id")) {
    		data.setCode("101");
    		return data;
    	}
    	
    	try {
			String house_id = request.getParameter("house_id");
			
			House house = new House();
			house.setId(house_id);
			house = houseService.getFeatureInfo(house);
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("house_num", house.getHouseCode());
			map.put("id", house.getId());
			map.put("title", house.getShortDesc());
			map.put("price", house.getRental());
			map.put("pay_way", house.getPayWay());
			String cover = "";
			if(!StringUtils.isEmpty(house.getAttachmentPath())) {
				PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			    String img_url = proper.getProperty("img.url");
			    String path[] = StringUtils.split(house.getAttachmentPath(), "|");
			    if(null != path && path.length > 0) {
				    for(String p:path) {
				    	cover = img_url+p+",";
				    }
				    if(cover.endsWith(","))
				    	cover = StringUtils.substringBeforeLast(cover, ",");
			    }
			}
			map.put("previews", cover);
			map.put("area", house.getHouseSpace());
			String decorate = "";
			map.put("decorate", decorate);
			map.put("summary", house.getShortLocation());
			map.put("floor", house.getHouseFloor());
			String orientate = "";
			if(!StringUtils.isEmpty(house.getOrientation())) {
				orientate = StringUtils.split(house.getOrientation(),",")[0];
				orientate = DictUtils.getDictLabel(orientate, "orientation","");
			}
			map.put("orientate", orientate);
			map.put("address", house.getProjectAddr());
			map.put("equipment", "1101111111");//(宽带、电视、沙发、洗衣机、床、冰箱、空调、衣柜、热水器、写字台) 1代表有0代表没有
			map.put("contact_phone", "4006-269-069");
			
			Integer fang = null,ting = null,wei=null;
			if(null != this.roomService.get(house.getId())) {
				house = this.houseService.get(this.roomService.get(house.getId()).getHouse().getId());
			}
			fang = house.getDecoraStrucRoomNum();
			ting = house.getDecoraStrucCusspacNum();
			wei = house.getDecoraStrucWashroNum();
			String layout = "";
			if(null != fang) {
				layout += fang+"房";
			}
			if(null != ting) {
				layout += ting+"厅";
			}
			if(null != wei) {
				layout += wei+"卫";
			}
			map.put("layout", layout);
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("getFeatureInfo error:",e);
		}
    	return data;
	}
	
	@RequestMapping(value="booking")
	@ResponseBody
	public ResponseData booking(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id") || null == request.getParameter("b_time") 
				|| null == request.getParameter("b_name") || null == request.getParameter("phone")) {
			data.setCode("101");
			return data;
		}
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserId(apptoken.getPhone());
			
			House house = new House();
			house.setId(request.getParameter("house_id"));
			house = this.houseService.getHouseByHouseId(house);
			
			contractBook.setHouseId(house.getHouseId());
			contractBook.setRoomId(house.getRoomId());
			contractBook.setUserName(request.getParameter("b_name"));
			contractBook.setUserPhone(request.getParameter("phone"));
			contractBook.setUserGender(request.getParameter("sex"));
			contractBook.setRemarks(request.getParameter("msg"));
			contractBook.setBookDate(DateUtils.parseDate(request.getParameter("b_time")));
			contractBook.setBookStatus("0");//管家确认中
			contractBookService.save(contractBook);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			this.log.error("save contractbook error:",e);
		}
		return data;
	}
	
	@RequestMapping(value="booking_list")
	@ResponseBody
	public ResponseData bookingList(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserId(apptoken.getPhone());
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<ContractBook> dataList = contractBookService.findList(contractBook);
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			for(ContractBook tmpContractBook : dataList) {
				Map<String,Object> mp = new HashMap<String,Object>();
				mp.put("id", StringUtils.isEmpty(tmpContractBook.getHouseId())?tmpContractBook.getRoomId():tmpContractBook.getHouseId());
				mp.put("time", DateFormatUtils.format(tmpContractBook.getBookDate(), "yyyy-MM-dd HH:mm"));
				mp.put("progress", tmpContractBook.getBookStatus());
				mp.put("short_desc", tmpContractBook.getShortDesc());
				String path[] = StringUtils.split(tmpContractBook.getAttachmentPath(), "|");
				if(null != path && path.length > 0) {
					mp.put("cover", proper.getProperty("img.url")+path[0]);					
				}
				list.add(mp);
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("book", list);
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookingList error:",e);
		}
		return data;
	}
	
	@RequestMapping(value="booking_info")
	@ResponseBody
	public ResponseData bookingInfo(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			ContractBook contractBook = new ContractBook();
			contractBook.setHouseId(request.getParameter("house_id"));
			contractBook.setRoomId(request.getParameter("house_id"));
			contractBook = this.contractBookService.findOne(contractBook);
			
			Map<String,Object> map = new HashMap<String,Object>();
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			map.put("progress", contractBook.getBookStatus());
			String path[] = StringUtils.split(contractBook.getAttachmentPath(), "|");
			if(null != path && path.length > 0)
				map.put("cover", proper.getProperty("img.url")+path[0]);
			map.put("short_desc", contractBook.getShortDesc());
			map.put("short_location", contractBook.getShortLocation());
			map.put("house_num", contractBook.getHouseId());
			map.put("b_time", DateFormatUtils.format(contractBook.getBookDate(), "yyyy-MM-dd HH:mm"));
			map.put("b_name", contractBook.getUserName());
			map.put("phone", contractBook.getUserPhone());
			map.put("sex", contractBook.getUserGender());
			map.put("msg", contractBook.getRemarks());
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookingInfo error:",e);
		}
		return data;
	}
	
	@RequestMapping(value="booking_cancel")
	@ResponseBody
	public ResponseData bookingCancel(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			ContractBook contractBook = new ContractBook();
			contractBook.setHouseId(request.getParameter("house_id"));
			contractBook.setRoomId(request.getParameter("house_id"));
			contractBookService.delete(contractBook);
		} catch (Exception e) {
			data.setCode("500");
			log.error("cancel booking error:",e);
		}
		
		data.setCode("200");
		return data;
	}
	
	/**
	 * 预定
	 */
	@RequestMapping(value="booked")
	@ResponseBody
	public ResponseData booked(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id") || null == request.getParameter("sign_date")
				|| null == request.getParameter("end_date")) {
			data.setCode("101");
			return data;
		}
		
		try {
			House house = new House();
			house.setId(request.getParameter("house_id"));
			house = houseService.get(house);
			
			Room room = null;
			if(null == house) {
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
			
			DepositAgreement depositAgreement = new DepositAgreement();
			depositAgreement.setAgreementCode(propertyProject.getProjectSimpleName()+"-"+(depositAgreementService.getTotalValidDACounts() + 1)+"-XY");
			String agreementName = propertyProject.getProjectName()+"-"+building.getBuildingName()+"-"+house.getHouseNo();
			if(null != room) {
				agreementName += "-"+room.getRoomNo();
				depositAgreement.setRoom(room);
			}
			depositAgreement.setAgreementName(agreementName);
			depositAgreement.setRentMode(null == room?"0":"1");//0:整租 1:单间
			depositAgreement.setPropertyProject(propertyProject);
			depositAgreement.setBuilding(building);
			depositAgreement.setHouse(house);
			
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			Tenant tenant = new Tenant();
			tenant.setCellPhone(appUser.getPhone());
			List<Tenant> tenantList = tenantService.findTenantByPhone(tenant);
			if(null == tenantList || tenantList.size() <= 0) {
				tenantList = new ArrayList<Tenant>();
				tenant.setTenantName(appUser.getName());
				tenant.setGender(appUser.getSex());
				tenant.setCellPhone(appUser.getPhone());
				tenantService.save(tenant);
				
				tenantList.add(tenant);	
			}
			depositAgreement.setTenantList(tenantList);
			depositAgreement.setSignDate(new Date());
			depositAgreement.setAgreementDate(DateUtils.parseDate(request.getParameter("sign_date"), "yyyy-MM-dd"));
			depositAgreement.setValidatorFlag("0");//暂存
			depositAgreement.setDataSource("2");//APP
			depositAgreement.setRemarks(request.getParameter("msg"));
			depositAgreement.setAgreementStatus("6");//暂存
			depositAgreement.setStartDate(DateUtils.parseDate(request.getParameter("sign_date"), "yyyy-MM-dd"));
			depositAgreement.setExpiredDate(DateUtils.parseDate(request.getParameter("end_date"), "yyyy-MM-dd"));
			depositAgreementService.save(depositAgreement);
		} catch(Exception e) {
			data.setCode("500");
			this.log.error("save contract book error:",e);
		}
		
		data.setCode("200");
		return data;
	}
	
	@RequestMapping(value="booked_list")
	@ResponseBody
	public ResponseData bookedList(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
	
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);
			
			List<Map<String,String>> dataList = new ArrayList<Map<String,String>>();
			Map<String,Object> map = new HashMap<String,Object>();
			for(ContractBook tmpContractBook : list) {
				Map<String,String> mp = new HashMap<String,String>();
				mp.put("house_id", tmpContractBook.getHouseId());
				mp.put("desc", tmpContractBook.getShortDesc());
				mp.put("time", DateFormatUtils.format(tmpContractBook.getCreateDate(),"yyyy-MM-dd"));
				
				/*0:等待管家确认
				  1:等待用户确认
				  2:支付成功
				  3:管家已取消
				  4.等待用户支付
				  5.用户已取消
				  6.支付失败*/				
				String status = "";
				if ("6".equals(tmpContractBook.getBookStatus())) {
					status = "0";
				} else if ("0".equals(tmpContractBook.getBookStatus())) {
					status = "1";
				} else if ("1".equals(tmpContractBook.getBookStatus())) {
					status = "4";
				} else if ("5".equals(tmpContractBook.getBookStatus())) {
					status = "2";
				}
				if("1".equals(tmpContractBook.getDelFlag())) {
					status = "3";
					if(apptoken.getPhone().equals(tmpContractBook.getUpdateBy())) {
						status = "5";
					}
				}
				mp.put("status", status);
				
				String path[] = StringUtils.split(tmpContractBook.getAttachmentPath(), "|");
				if(null != path && path.length>0) {
					PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
					mp.put("cover", proper.getProperty("img.url")+path[0]);
				}
				dataList.add(mp);
			}
			map.put("houses", dataList);
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookedList error:",e);
		}
		return data;
	}
	
	@RequestMapping(value="booked_protocol")
	@ResponseBody
	public ResponseData bookedProtocol(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}		
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			contractBook.setHouseId(request.getParameter("house_id"));
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);
			
			if(null != list && list.size() > 0) {
				contractBook = list.get(0);
			}
			
			DepositAgreement depositAgreement = this.depositAgreementService.get(contractBook.getDepositId());
			
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			StringBuffer html = new StringBuffer("<div>");
			html.append("<p>");
			html.append("<h3><center>唐巢人才公寓定金协议</center></h3>");
			html.append("&nbsp;&nbsp;今收到<u>"+appUser.getName()+"</u>先生/女士(以下简称'承租人')，");
			html.append("为承租上海市<u>"+contractBook.getShortDesc()+"</u>");
			html.append("部位房屋所支付的租金定金，人民币：<u>"+depositAgreement.getDepositAmount()+"</u>元整。");
			
			String rentType = "";
			if((new Integer(3)).equals(depositAgreement.getRenMonths()) && (new Integer(1)).equals(depositAgreement.getDepositMonths())) {
				rentType = "付三押一";
			} else if((new Integer(2)).equals(depositAgreement.getRenMonths()) && (new Integer(2)).equals(depositAgreement.getDepositMonths())) {
				rentType = "付二押二";
			} else {
				rentType = "付三押一";
			}
			String startDate = (null!=depositAgreement.getStartDate())?DateFormatUtils.format(depositAgreement.getStartDate(),"yyyy-MM-dd"):"";
			String endDate = (null!=depositAgreement.getExpiredDate())?DateFormatUtils.format(depositAgreement.getExpiredDate(),"yyyy-MM-dd"):"";
			html.append("该房屋月租金人民币：<u>"+depositAgreement.getHousingRent()+"</u>元整。付款方式为:<u>"+rentType+"</u>。<br/>");
			html.append("&nbsp;&nbsp;租期：<u>"+startDate+"</u>");
			html.append("至 <u>"+endDate+"</u><br/>");
			html.append("&nbsp;&nbsp;承租人：<u>"+appUser.getName()+"</u><br/>");
			html.append("&nbsp;&nbsp;承租电话：<u>"+apptoken.getPhone()+"</u><br/>");
			html.append("&nbsp;&nbsp;身份证号码：<u>"+appUser.getIdCardNo()+"</u><br/>");
			html.append("&nbsp;&nbsp;上述定金有效时间为<u>30</u>天，出租人不得将该房屋转借给他人，如承租人有效期内未签约，则视为承租人放弃承租该房屋，");
			html.append("则该笔定金作为违约金没收；如签订租房合同后，该定金转为部分租金。<br/>");
			html.append("&nbsp;&nbsp;承租人确认：<u>"+appUser.getName()+"</u><br/>");
			html.append("&nbsp;&nbsp;日期：<u>"+startDate+"</u><br/>");
			html.append("&nbsp;&nbsp;签收人：<u>唐巢人才公寓</u><br/>");
		    html.append("</p></div>");
			map.put("str_html", html);
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("find booked protocol error:",e);
		}
		
		return data;
	}
	
	@RequestMapping(value="booked_order")
	@ResponseBody
	public ResponseData bookedOrder(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			contractBook.setHouseId(request.getParameter("house_id"));
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);
			
			if(null != list && list.size() > 0) {
				contractBook = list.get(0);
			}
			
			DepositAgreement depositAgreement = this.depositAgreementService.get(contractBook.getDepositId());
			
			/*款项*/
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setTransId(depositAgreement.getId());
			List<PaymentTrans> paymentTransList = paymentTransService.findList(paymentTrans);
			String transIds = "";
			double tradeAmount = 0;
			List<Receipt> receiptList = new ArrayList<Receipt>();
			for(PaymentTrans tmpPaymentTrans : paymentTransList) {
				transIds += tmpPaymentTrans.getId()+",";
				tradeAmount += tmpPaymentTrans.getTradeAmount();
				
				Receipt receipt = new Receipt();
				receipt.setTradeMode("4");//支付宝
				receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
				receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
				receiptList.add(receipt);
			}
			if(org.apache.commons.lang3.StringUtils.endsWith(transIds, ",")) {
				transIds = org.apache.commons.lang3.StringUtils.substringBeforeLast(transIds, ",");
			}
			
			/*生成账务交易*/
			TradingAccounts tradingAccounts = new TradingAccounts();
			tradingAccounts.setTradeId(depositAgreement.getId());
			tradingAccountsService.delete(tradingAccounts);
			tradingAccounts.setTransIds(transIds);
			tradingAccounts.setTradeStatus("0");//待审核
			tradingAccounts.setTradeType("1");//预约定金
			tradingAccounts.setTradeAmount(tradeAmount);
			tradingAccounts.setTradeDirection("1");//入账
			tradingAccounts.setPayeeType("1");//个人
			tradingAccounts.setPayeeName(appUser.getName());
			tradingAccounts.setReceiptList(receiptList);
			tradingAccountsService.save(tradingAccounts);
			
			/*订单生成*/
			PaymentOrder paymentOrder = new PaymentOrder();
			paymentOrder.setOrderId(contractBookService.generateOrderId());
			paymentOrder.setOrderDate(new Date());
			paymentOrder.setOrderStatus("1");//未支付
			paymentOrder.setTradeId(tradingAccounts.getId());
			paymentOrder.setOrderAmount(tradingAccounts.getTradeAmount());
			paymentOrder.setCreateDate(new Date());
			this.contractBookService.saveOrder(paymentOrder);
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("order_id", paymentOrder.getOrderId());
			map.put("price", paymentOrder.getOrderAmount());
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookedOrder error:",e);
		}
		
		return data;
	}
	
	@RequestMapping(value="booked_info")
	@ResponseBody
	public ResponseData bookedInfo(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			contractBook.setHouseId(request.getParameter("house_id"));
			List<ContractBook> list = this.contractBookService.findBookedContract(contractBook);
			
			if(null != list && list.size() > 0) {
				contractBook = list.get(0);
			}
			
			DepositAgreement depositAgreement = this.depositAgreementService.get(contractBook.getDepositId());
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("house_id", contractBook.getHouseId());
			map.put("desc", contractBook.getShortDesc());
			map.put("location", contractBook.getShortLocation());
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String path[] = StringUtils.split(contractBook.getAttachmentPath(), "|");
			if(null != path && path.length > 0)
				map.put("cover", proper.getProperty("img.url")+path[0]);
			map.put("rent_amount", depositAgreement.getHousingRent());
			map.put("start_date", DateFormatUtils.format(depositAgreement.getStartDate(),"yyyy-MM-dd"));
			map.put("end_date", DateFormatUtils.format(depositAgreement.getExpiredDate(),"yyyy-MM-dd"));
			map.put("deposit_amount", depositAgreement.getDepositAmount());
			map.put("rent_name", appUser.getName());
			map.put("id_no", appUser.getIdCardNo());
			map.put("rent_gender", appUser.getSex());
			map.put("rent_phone", appUser.getPhone());
			map.put("note", depositAgreement.getRemarks());
			map.put("status", contractBook.getBookStatus().equals("6")?"0":"1");
			map.put("sign_date", DateFormatUtils.format(depositAgreement.getSignDate(),"yyyy-MM-dd"));
			map.put("contract_date", DateFormatUtils.format(depositAgreement.getAgreementDate(),"yyyy-MM-dd"));
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("bookedInfo error:",e);
		}
		
		return data;
	}
	
	/**
	 * 取消预订
	 */
	@RequestMapping(value="booked_cancel")
	@ResponseBody
	public ResponseData bookedCancel(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		if(null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			String houseId = request.getParameter("house_id");
			
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			DepositAgreement depositAgreement = new DepositAgreement();
			depositAgreement.setHouseNo(houseId);
			depositAgreement.setRoomNo(houseId);
			depositAgreement = depositAgreementService.getByHouseId(depositAgreement);
			depositAgreement.setUpdateUser(apptoken.getPhone());
			this.depositAgreementService.delete(depositAgreement);
		} catch (Exception e) {
			log.error("",e);
		}
		
		data.setCode("200");
		return data;
	}
	
	/**
	 * 签约
	 */
	@RequestMapping(value="sign")
	@ResponseBody
	public ResponseData sign(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id") || null == request.getParameter("end_date")) {
			data.setCode("101");
			return data;
		}
		
		try {
			House house = new House();
			house.setId(request.getParameter("house_id"));
			house = houseService.get(house);
			
			Room room = null;
			if(null == house) {
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
			
			RentContract rentContract = new RentContract();
			rentContract.setSignType("0");//新签
			rentContract.setContractCode(propertyProject.getProjectSimpleName()+"-"+(rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
			String contractName = propertyProject.getProjectName()+"-"+building.getBuildingName()+"-"+house.getHouseNo();
			String payWay = "";
			if(null != room) {
				contractName += "-"+room.getRoomNo();
				rentContract.setRoom(room);
				rentContract.setRental(room.getRental());
				
				payWay = room.getPayWay();
			} else {
				rentContract.setRental(house.getRental());
				
				payWay = house.getPayWay();
			}
			if("0".equals(payWay)) {//付三押一
				rentContract.setRenMonths(3);
				rentContract.setDepositMonths(1);
			} else if("1".equals(payWay)) {//付二押二
				rentContract.setRenMonths(2);
				rentContract.setDepositMonths(2);
			}
			rentContract.setDepositAmount(rentContract.getRental()*rentContract.getDepositMonths());
			rentContract.setContractName(contractName);
			rentContract.setRentMode(null == room?"0":"1");//0:整租 1:单间
			rentContract.setPropertyProject(propertyProject);
			rentContract.setBuilding(building);
			rentContract.setHouse(house);
			
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			Tenant tenant = new Tenant();
			tenant.setIdType("0");//身份证
			tenant.setIdNo(appUser.getIdCardNo());
			List<Tenant> tenantList = tenantService.findTenantByIdTypeAndNo(tenant);
			if(null == tenantList || tenantList.size() <= 0) {
				tenantList = new ArrayList<Tenant>();
				tenant.setTenantName(appUser.getName());
				tenant.setGender(appUser.getSex());
				tenant.setCellPhone(appUser.getPhone());
				tenantService.save(tenant);
				
				tenantList.add(tenant);	
			}
			rentContract.setTenantList(tenantList);
			rentContract.setLiveList(tenantList);
			rentContract.setContractSource("1");//本部
			rentContract.setValidatorFlag("0");//暂存
			rentContract.setDataSource("2");//APP
			rentContract.setSignDate(new Date());
			rentContract.setStartDate(new Date());
			rentContract.setExpiredDate(DateUtils.parseDate(request.getParameter("end_date"), "yyyy-MM-dd"));
			rentContract.setRemarks(request.getParameter("msg"));
			rentContract.setContractStatus("0");//暂存
			
			/*判断该用户是否有预订,有则为定金转合同流程*/
			//TODO:
			this.rentContractService.save(rentContract);
			
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("sign contract error:",e);
		}
		
		return data;
	}
	
	@RequestMapping(value="sign_order")
	@ResponseBody
	public ResponseData signOrder(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("house_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			contractBook.setHouseId(request.getParameter("house_id"));
			List<ContractBook> list = this.contractBookService.findRentContract(contractBook);
			
			if(null != list && list.size() > 0) {
				contractBook = list.get(0);
			}
			
			RentContract rentContract = this.rentContractService.get(contractBook.getDepositId());
			
			/*款项*/
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setTransId(rentContract.getId());
			List<PaymentTrans> paymentTransList = paymentTransService.findList(paymentTrans);
			String transIds = "";
			double tradeAmount = 0;
			List<Receipt> receiptList = new ArrayList<Receipt>();
			//首次需付费：水电押金、房租押金、房租*n
			int rentMonthes = rentContract.getRenMonths();//首付房租月数
			int rentMonthesCount = 0;
			for(PaymentTrans tmpPaymentTrans : paymentTransList) {
				if("2".equals(tmpPaymentTrans.getPaymentType())) {//水电押金
					transIds += tmpPaymentTrans.getId()+",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();
					
					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");//支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);
				} else if("4".equals(tmpPaymentTrans.getPaymentType())) {//房租押金
					transIds += tmpPaymentTrans.getId()+",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();
					
					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");//支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);
				} else if("6".equals(tmpPaymentTrans.getPaymentType()) && rentMonthesCount<rentMonthes) {//房租
					transIds += tmpPaymentTrans.getId()+",";
					tradeAmount += tmpPaymentTrans.getTradeAmount();
					
					Receipt receipt = new Receipt();
					receipt.setTradeMode("4");//支付宝
					receipt.setPaymentType(tmpPaymentTrans.getPaymentType());
					receipt.setReceiptAmount(tmpPaymentTrans.getTradeAmount());
					receiptList.add(receipt);
					
					rentMonthesCount++;
				}
			}
			if(org.apache.commons.lang3.StringUtils.endsWith(transIds, ",")) {
				transIds = org.apache.commons.lang3.StringUtils.substringBeforeLast(transIds, ",");
			}
			
			/*生成账务交易*/
			TradingAccounts tradingAccounts = new TradingAccounts();
			tradingAccounts.setTradeId(rentContract.getId());
			tradingAccountsService.delete(tradingAccounts);
			tradingAccounts.setTradeStatus("0");//待审核
			tradingAccounts.setTransIds(transIds);
			tradingAccounts.setTradeType("3");//新签合同
			tradingAccounts.setTradeAmount(tradeAmount);
			tradingAccounts.setTradeDirection("1");//入账
			tradingAccounts.setPayeeType("1");//个人
			tradingAccounts.setPayeeName(appUser.getName());
			tradingAccounts.setReceiptList(receiptList);
			tradingAccountsService.save(tradingAccounts);
			
			/*订单生成*/
			PaymentOrder paymentOrder = new PaymentOrder();
			paymentOrder.setOrderId(contractBookService.generateOrderId());
			paymentOrder.setOrderDate(new Date());
			paymentOrder.setOrderStatus("1");//未支付
			paymentOrder.setTradeId(tradingAccounts.getId());
			paymentOrder.setOrderAmount(tradingAccounts.getTradeAmount());
			this.contractBookService.saveOrder(paymentOrder);
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("order_id", paymentOrder.getOrderId());
			map.put("price", paymentOrder.getOrderAmount());
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("signOrder error:",e);
		}
	
		return data;
	}
	
	@RequestMapping(value="contract_list")
	@ResponseBody
	public ResponseData contractList(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			
			ContractBook contractBook = new ContractBook();
			contractBook.setUserPhone(apptoken.getPhone());
			List<ContractBook> list = this.contractBookService.findRentContract(contractBook);
			
			List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String,Object>();
			for(ContractBook tmpContractBook : list) {
				Map<String,Object> mp = new HashMap<String,Object>();
				mp.put("contract_id", tmpContractBook.getDepositId());
				mp.put("contract_code", tmpContractBook.getContractCode());
				PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
				String path[] = StringUtils.split(tmpContractBook.getAttachmentPath(), "|");
				if(null != path && path.length > 0)
					mp.put("cover", proper.getProperty("img.url")+path[0]);
				mp.put("short_desc", tmpContractBook.getShortDesc());
				mp.put("house_desc", tmpContractBook.getShortLocation());
				mp.put("rent", tmpContractBook.getRent());
				mp.put("status", tmpContractBook.getBookStatus().equals("0")?"0":"1");
				dataList.add(mp);
			}
			map.put("contracts", dataList);
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("contractList error:",e);
		}
		
		return data;
	}
	
	@RequestMapping(value="contract_info")
	@ResponseBody
	public ResponseData contractInfo(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("contract_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			RentContract rentContract = this.rentContractService.get(request.getParameter("contract_id"));
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			String shortDesc = "",attachmentPath = "",houseDesc = "";
			if(null != rentContract.getRoom() && !StringUtils.isBlank(rentContract.getRoom().getId())) {
				Room room = this.roomService.get(rentContract.getRoom().getId());
				shortDesc = room.getShortDesc();
				attachmentPath = room.getAttachmentPath();
				houseDesc = room.getShortLocation();
			} else {
				House house = this.houseService.get(rentContract.getHouse().getId());
				shortDesc = house.getShortDesc();
				attachmentPath = house.getAttachmentPath();
				houseDesc = house.getShortLocation();
			}
			
			map.put("contract_id", rentContract.getId());
			map.put("contract_code", rentContract.getContractCode());
			map.put("short_desc", shortDesc);
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			String path[] = StringUtils.split(attachmentPath, "|");
			if(null != path && path.length > 0)
				map.put("cover", proper.getProperty("img.url")+path[0]);
			map.put("house_desc", houseDesc);
			map.put("rent", rentContract.getRental());
			String rentType = "";
			if((new Integer(3)).equals(rentContract.getRenMonths()) && (new Integer(1)).equals(rentContract.getDepositMonths())) {
				rentType = "0";
			} else if((new Integer(2)).equals(rentContract.getRenMonths()) && (new Integer(2)).equals(rentContract.getDepositMonths())) {
				rentType = "1";
			}
			map.put("rent_type", rentType);
			map.put("deposit_amount", rentContract.getDepositAmount());
			map.put("we_deposit_amount", rentContract.getDepositElectricAmount());
			map.put("sign_date", DateFormatUtils.format(rentContract.getSignDate(),"yyyy-MM-dd"));
			map.put("start_date", DateFormatUtils.format(rentContract.getStartDate(),"yyyy-MM-dd"));
			map.put("end_date", DateFormatUtils.format(rentContract.getExpiredDate(),"yyyy-MM-dd"));
			map.put("remind_date", DateFormatUtils.format(rentContract.getRemindTime(),"yyyy-MM-dd"));
			map.put("status", rentContract.getContractStatus().equals("0")?"0":"1");
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("contract_info error:",e);
		}
		
		return data;
	}
	
	@RequestMapping(value="notice")
	@ResponseBody
	public ResponseData notice(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			StringBuffer html = new StringBuffer();
			html.append("<div><p>");
			html.append("<h3><center>租房须知</center></h3>");
			html.append("&nbsp;&nbsp;1.为了确保居住环境的安全，本公寓实行实名制入住，凡入住前必须提供身份证原件，出租方将对信息进行核实；");
			html.append("乙方入住该房屋之日起的五日内，需按照当地警署的要求办理外来人口暂住手续；</br>");
			html.append("&nbsp;&nbsp;2.</br>");
			html.append("&nbsp;&nbsp;3.</br>");
			html.append("</p></div>");
			
			map.put("str_html", html);
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
		}
		
		return data;
	}
	
	@RequestMapping(value="contract")
	@ResponseBody
	public ResponseData contract(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("contract_id")) {
			data.setCode("101");
			return data;
		}
		
		try {
			String token = (String) request.getHeader("token");
			AppToken apptoken = new AppToken();
			apptoken.setToken(token);
			apptoken = appTokenService.findByToken(apptoken);
			AppUser appUser = new AppUser();
			appUser.setPhone(apptoken.getPhone());
			appUser = appUserService.getByPhone(appUser);
			
			RentContract rentContract = this.rentContractService.get(request.getParameter("contract_id"));
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			StringBuffer html = new StringBuffer();
			html.append("<div><p>");
			html.append("<h3><center>唐巢人才公寓租赁合同</center></h3>");
			html.append("&nbsp;&nbsp;合同编号："+rentContract.getContractCode()+"</br>");
			html.append("&nbsp;&nbsp;出租方(甲方)：上海唐巢投资有限公司</br>");
			html.append("&nbsp;&nbsp;出租方(乙方)："+appUser.getName()+"</br></br>");
			html.append("&nbsp;&nbsp;根据《中华人民共和国合同法》、《上海市房屋租赁条例》、《上海市居住房租赁管理办法》的规定，");
			html.append("甲、乙双方在平等、自愿、公平和诚实信用的基础上，经协商一致，就乙方承租甲方可依法出租的以下房屋，");
			html.append("订立本合同。</br>");
			html.append("&nbsp;&nbsp;一、</br>");
			html.append("&nbsp;&nbsp;二、</br>");
			html.append("&nbsp;&nbsp;三、</br>");
			html.append("&nbsp;&nbsp;四、</br>");
			html.append("&nbsp;&nbsp;五、</br></br>");
			html.append("&nbsp;&nbsp;出租方：上海唐巢投资有限公司</br>");
			html.append("&nbsp;&nbsp;代理人：丁</br>");
			html.append("&nbsp;&nbsp;联系地址：上海市创新西路333弄46号</br>");
			html.append("&nbsp;&nbsp;联系电话：021-31006969</br>");
			html.append("&nbsp;&nbsp;签约日期："+DateFormatUtils.format(rentContract.getSignDate(),"yyyy-MM-dd")+"</br></br>");
			html.append("&nbsp;&nbsp;承租方："+appUser.getName()+"</br>");
			html.append("&nbsp;&nbsp;身份证："+appUser.getIdCardNo()+"</br>");
			html.append("&nbsp;&nbsp;联系地址：</br>");
			html.append("&nbsp;&nbsp;联系电话："+appUser.getPhone()+"</br>");
			html.append("&nbsp;&nbsp;签约日期："+DateFormatUtils.format(rentContract.getSignDate(),"yyyy-MM-dd")+"</br>");
			html.append("</p></div>");
			
			map.put("str_html", html);
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("contract_info error:",e);
		}
		
		return data;
	}
	
	@RequestMapping(value="pay_sign_booked")
	@ResponseBody
	public ResponseData paysignBooked(HttpServletRequest request, HttpServletResponse response) {
		ResponseData data = new ResponseData();
		
		if(null == request.getParameter("order_id")) {
			data.setCode("101");
			return data;
		}
		
		String orderId = request.getParameter("order_id");
		PaymentOrder paymentOrder = this.contractBookService.findByOrderId(orderId);
		DecimalFormat df  = new DecimalFormat("######0.00"); 
		Double orderAmount = paymentOrder.getOrderAmount();
		String signStr = AlipayUtil.buildRequest(orderId, df.format(orderAmount));
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sign", signStr);
		
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
            String mobile = request.getParameter("mobile");

            Repair repair = new Repair();
            repair.setId(IdGen.uuid());
//            repair.setUserId(mobile);
            repair.setUserMobile(request.getParameter("user_mobile"));
//            repair.setContractId(request.getParameter("contract_id"));
            repair.setRoomId(request.getParameter("room_id"));
            repair.setStatus("01");
            repair.setDescription(request.getParameter("description"));
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


            data.setCode("200");
            data.setMsg("报修已提交");
        } catch (Exception e) {
            data.setCode("500");
            log.error("create repair error:", e);
        }
        return data;
    }
}
