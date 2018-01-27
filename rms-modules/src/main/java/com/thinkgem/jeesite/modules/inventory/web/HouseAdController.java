package com.thinkgem.jeesite.modules.inventory.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.web.CommonBusinessController;
import com.thinkgem.jeesite.modules.inventory.entity.HouseAd;
import com.thinkgem.jeesite.modules.inventory.service.HouseAdService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 广告管理
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/ad")
public class HouseAdController extends CommonBusinessController {

    @Autowired
    private HouseAdService houseAdService;

    @ModelAttribute
    public HouseAd get(@RequestParam(required = false) String id) {
        HouseAd entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = houseAdService.get(id);
        }
        if (entity == null) {
            entity = new HouseAd();
        }
        return entity;
    }

    @RequiresPermissions("inventory:ad:view")
    @RequestMapping(value = {"list", ""})
    public String list(HouseAd houseAd, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<HouseAd> page = houseAdService.findPage(new Page<HouseAd>(request, response), houseAd);
        model.addAttribute("page", page);
        return "modules/inventory/houseAdList";
    }

    @RequiresPermissions("inventory:ad:view")
    @RequestMapping(value = "form")
    public String form(HouseAd houseAd, Model model) {
        model.addAttribute("houseAd", houseAd);
        commonInit2("projectList", "buildingList", "houseList", "roomList", model, houseAd.getPropertyProject(), houseAd.getBuilding(), houseAd.getHouse(), houseAd.getRoom());
        return "modules/inventory/houseAdForm";
    }

    @RequiresPermissions("inventory:ad:edit")
    @RequestMapping(value = "save")
    public String save(HouseAd houseAd, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, houseAd)) {
            return form(houseAd, model);
        }
        houseAdService.save(houseAd);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存广告管理成功");
        return "redirect:" + Global.getAdminPath() + "/inventory/ad/?repage";
    }

    @RequiresPermissions("inventory:ad:edit")
    @RequestMapping(value = "delete")
    public String delete(HouseAd houseAd, RedirectAttributes redirectAttributes) {
        houseAdService.delete(houseAd);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除广告管理成功");
        return "redirect:" + Global.getAdminPath() + "/inventory/ad/?repage";
    }

}
