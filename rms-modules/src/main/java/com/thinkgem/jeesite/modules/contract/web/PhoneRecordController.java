package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.contract.entity.PhoneRecord;
import com.thinkgem.jeesite.modules.contract.service.PhoneRecordService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiao
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/phoneRecord")
public class PhoneRecordController extends CommonBusinessController {

    @Autowired
    private PhoneRecordService phoneRecordService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    /**
     * 拨号记录报表-查询
     */
    @RequiresPermissions("contract:phoneRecord:view")
    @RequestMapping(value = {""})
    public String initPage(PhoneRecord phoneRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
        return "modules/contract/phoneRecordList";
    }

    @RequiresPermissions("contract:phoneRecord:view")
    @RequestMapping(value = {"list"})
    public String list(PhoneRecord phoneRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        initSearchConditions(phoneRecord, model);
        model.addAttribute("page", phoneRecordService.findPage(new Page<>(request, response), phoneRecord));
        return "modules/contract/phoneRecordList";
    }

    private void initSearchConditions(PhoneRecord phoneRecord, Model model) {
        PropertyProject pp = new PropertyProject();
        pp.setId(phoneRecord.getProjectId());
        Building b = new Building();
        b.setId(phoneRecord.getBuildingId());
        House h = new House();
        h.setId(phoneRecord.getHouseId());
        commonInit("projectList", "buildingList", "houseList", "roomList", model, pp, b, h);
    }
}