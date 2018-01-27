package com.thinkgem.jeesite.modules.inventory.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 楼宇
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/building")
public class BuildingController extends BaseController {

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Autowired
    private BuildingService buildingService;

    @ModelAttribute
    public Building get(@RequestParam(required = false) String id) {
        Building entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = buildingService.get(id);
        }
        if (entity == null) {
            entity = new Building();
        }
        return entity;
    }

    @RequiresPermissions("inventory:building:view")
    @RequestMapping(value = {"list"})
    public String listQuery(Building building, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Building> page = buildingService.findPage(new Page<>(request, response), building);
        model.addAttribute("page", page);
        model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
        return "modules/inventory/buildingList";
    }

    @RequiresPermissions("inventory:building:view")
    @RequestMapping(value = {""})
    public String listNoQuery(Model model) {
        model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
        return "modules/inventory/buildingList";
    }

    @RequestMapping(value = {"findList"})
    @ResponseBody
    public List<Building> findList(String id) {
        Building building = new Building();
        PropertyProject tmpPropertyProject = new PropertyProject();
        tmpPropertyProject.setId(id);
        building.setPropertyProject(tmpPropertyProject);
        return buildingService.findList(building);
    }

    @RequiresPermissions("inventory:building:view")
    @RequestMapping(value = "form")
    public String form(Building building, Model model) {
        model.addAttribute("building", building);
        model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
        return "modules/inventory/buildingForm";
    }

    @RequiresPermissions(" contract:rentContract:edit")
    @RequestMapping(value = "add")
    public String add(Building building, Model model) {
        model.addAttribute("building", building);
        List<PropertyProject> list = new ArrayList<>();
        list.add(propertyProjectService.get(building.getPropertyProject()));
        model.addAttribute("listPropertyProject", list);
        return "modules/inventory/buildingAdd";
    }

    @RequiresPermissions("inventory:building:edit")
    @RequestMapping(value = "save")
    public String save(Building building, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, building)) {
            return form(building, model);
        }
        List<Building> blds = buildingService.findBuildingByBldNameAndProProj(building);
        if (!building.getIsNewRecord()) {// 更新
            if (CollectionUtils.isNotEmpty(blds)) {
                building.setId(blds.get(0).getId());
            }
            buildingService.save(building);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改楼宇成功");
            return "redirect:" + Global.getAdminPath() + "/inventory/building/?repage";
        } else {// 新增
            if (CollectionUtils.isNotEmpty(blds)) {
                addMessage(model, ViewMessageTypeEnum.WARNING, "该楼宇名称及楼宇所属物业项目已被使用，不能重复添加");
                model.addAttribute("propertyProject", building.getPropertyProject());
                model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
                return "modules/inventory/buildingForm";
            } else {
                buildingService.save(building);
                addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存楼宇成功");
                return "redirect:" + Global.getAdminPath() + "/inventory/building/?repage";
            }
        }
    }

    @RequiresPermissions(" contract:rentContract:edit")
    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(Building building, Model model) {
        JSONObject jsonObject = new JSONObject();
        List<Building> blds = buildingService.findBuildingByBldNameAndProProj(building);
        if (CollectionUtils.isNotEmpty(blds)) {
            List<PropertyProject> list = new ArrayList<>();
            list.add(propertyProjectService.get(building.getPropertyProject()));
            model.addAttribute("listPropertyProject", list);
            addMessage(jsonObject, ViewMessageTypeEnum.ERROR, "该楼宇名称及楼宇所属物业项目已被使用，不能重复添加");
        } else {
            buildingService.save(building);
            jsonObject.put("id", building.getId());
            jsonObject.put("name", building.getBuildingName());
        }
        return jsonObject.toString();
    }

    @RequiresPermissions("inventory:building:edit")
    @RequestMapping(value = "delete")
    public String delete(Building building, RedirectAttributes redirectAttributes) {
        buildingService.delete(building);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除楼宇和图片及其房屋和图片、房间和图片成功");
        return "redirect:" + Global.getAdminPath() + "/inventory/building/?repage";
    }

}
