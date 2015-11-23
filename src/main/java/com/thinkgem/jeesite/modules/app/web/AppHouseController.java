package com.thinkgem.jeesite.modules.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Controller
@RequestMapping("house")
public class AppHouseController {
	Logger log = LoggerFactory.getLogger(AppHouseController.class);
	
	@Autowired
    private HouseService houseService;
	@Autowired
	private ContractBookService contractBookService;
	
	@RequestMapping(value = "findFeatureList")
    @ResponseBody
    public ResponseData findFeatureList(HttpServletRequest request, HttpServletResponse response) {
    	ResponseData data = new ResponseData();
    	if(null == request.getParameter("p_n") || null == request.getParameter("p_s")) {
    		data.setCode("101");
    		return data;
    	}
    	Integer p_n = Integer.valueOf(request.getParameter("p_n"));
    	Integer p_s = Integer.valueOf(request.getParameter("p_s"));
		
		Page<House> page = new Page<House>();
		page.setPageSize(p_s);
		page.setPageNo(p_n);
		page = houseService.findFeaturePage(page, new House());
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> mp = new HashMap<String,Object>();
		List<House> pageList = page.getList();
		for(House h:pageList) {
			mp.put("id", h.getId());
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
		    for(String p:path) {
		    	cover = img_url+p+",";
		    }
		    if(cover.endsWith(","))
		    	cover = StringUtils.substringBeforeLast(cover, ",");
		}
		map.put("previews", cover);
		map.put("layout", house.getDecoraStrucRoomNum()+"房"+house.getDecoraStrucCusspacNum()+"厅"+house.getDecoraStrucWashroNum()+"卫");
		map.put("area", house.getHouseSpace());
		map.put("decorate", 1);
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
    	
		data.setData(map);
    	data.setCode("200");
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
			
			ContractBook contractBook = new ContractBook();
			//contractBook
			contractBookService.save(contractBook);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			this.log.error("save contractbook error:",e);
		}
		return data;
	}
}
