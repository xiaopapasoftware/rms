/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.Repair;
import com.thinkgem.jeesite.modules.app.service.RepairService;

/**
 * 报修Controller
 * @author daniel
 * @version 2016-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/app/repair")
public class RepairController extends BaseController {

	@Autowired
	private RepairService repairService;
	
	@ModelAttribute
	public Repair get(@RequestParam(required=false) String id) {
		Repair entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = repairService.get(id);
		}
		if (entity == null){
			entity = new Repair();
		}
		return entity;
	}
	

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = { "list", "" })
    public String list(Repair repair, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<Repair> page = repairService.findPage(new Page<Repair>(request, response), repair);
	PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
	String img_url = proper.getProperty("img.url");
	logger.debug("list:" + page.getList());
	for (Repair re : page.getList()) {
	    String picture = re.getPicture();
	    if (StringUtils.isNotEmpty(picture)) {
		String[] pics = picture.split("\\|");
		List picList = new ArrayList();
		for (String pic : pics) {
		    picList.add(img_url + pic);
		}
		re.setPictures(picList);
	    }
	}
	logger.debug("list after:" + page.getList());
	model.addAttribute("page", page);
	return "modules/app/repairList";
    }

	@RequestMapping(value = "form")
	public String form(Repair repair, Model model) {
		model.addAttribute("repair", repair);
		return "modules/app/repairForm";
	}


	@RequestMapping(value = "save")
	public String save(Repair repair, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, repair)){
			return form(repair, model);
		}
		repairService.save(repair);
		addMessage(redirectAttributes, "保存报修记录成功");
		return "redirect:"+Global.getAdminPath()+"/app/repair/?repage";
	}
	

	@RequestMapping(value = "delete")
	public String delete(Repair repair, RedirectAttributes redirectAttributes) {
		repairService.delete(repair);
		addMessage(redirectAttributes, "删除报修记录成功");
		return "redirect:"+Global.getAdminPath()+"/app/repair/?repage";
	}

}