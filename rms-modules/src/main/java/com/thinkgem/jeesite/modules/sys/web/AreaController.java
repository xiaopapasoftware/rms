package com.thinkgem.jeesite.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.service.AreaService;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 区域Controller
 * 
 * @author ThinkGem
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

  @Autowired
  private AreaService areaService;

  @ModelAttribute("area")
  public Area get(@RequestParam(required = false) String id) {
    if (StringUtils.isNotBlank(id)) {
      return areaService.get(id);
    } else {
      return new Area();
    }
  }

  @RequiresPermissions("sys:area:view")
  @RequestMapping(value = {"list", ""})
  public String list(Area area, Model model) {
    model.addAttribute("list", areaService.findAll());
    return "modules/sys/areaList";
  }

  @RequiresPermissions("sys:area:view")
  @RequestMapping(value = "form")
  public String form(Area area, Model model) {
    if (area.getParent() == null || area.getParent().getId() == null) {
      area.setParent(UserUtils.getUser().getOffice().getArea());
    }
    area.setParent(areaService.get(area.getParent().getId()));
    model.addAttribute("area", area);
    return "modules/sys/areaForm";
  }

  @RequiresPermissions("sys:area:edit")
  @RequestMapping(value = "save")
  public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
    if (Global.isDemoMode()) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "演示模式，不允许操作！");
      return "redirect:" + adminPath + "/sys/area";
    }
    if (!beanValidator(model, area)) {
      return form(area, model);
    }
    areaService.save(area);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存区域'" + area.getName() + "'成功");
    return "redirect:" + adminPath + "/sys/area/";
  }

  @RequiresPermissions("sys:area:edit")
  @RequestMapping(value = "delete")
  public String delete(Area area, RedirectAttributes redirectAttributes) {
    if (Global.isDemoMode()) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "演示模式，不允许操作！");
      return "redirect:" + adminPath + "/sys/area";
    }
    areaService.delete(area);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除区域成功");
    return "redirect:" + adminPath + "/sys/area/";
  }

  @ResponseBody
  @RequestMapping(value = "treeData")
  public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
    List<Map<String, Object>> mapList = Lists.newArrayList();
    List<Area> list = areaService.findAll();
    for (int i = 0; i < list.size(); i++) {
      Area e = list.get(i);
      if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", e.getId());
        map.put("pId", e.getParentId());
        map.put("name", e.getName());
        mapList.add(map);
      }
    }
    return mapList;
  }
}
