/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.company.web;

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
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.company.entity.WorkRecord;
import com.thinkgem.jeesite.modules.company.service.WorkRecordService;

/**
 * 工作记录Controller
 * 
 * @author huangsc
 * @version 2015-09-12
 */
@Controller
@RequestMapping(value = "${adminPath}/company/workRecord")
public class WorkRecordController extends BaseController {

  @Autowired
  private WorkRecordService workRecordService;

  @ModelAttribute
  public WorkRecord get(@RequestParam(required = false) String id) {
    WorkRecord entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = workRecordService.get(id);
    }
    if (entity == null) {
      entity = new WorkRecord();
    }
    return entity;
  }

  // @RequiresPermissions("company:workRecord:view")
  @RequestMapping(value = {"list", ""})
  public String list(WorkRecord workRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<WorkRecord> page = workRecordService.findPage(new Page<WorkRecord>(request, response), workRecord);
    model.addAttribute("page", page);
    return "modules/company/workRecordList";
  }

  // @RequiresPermissions("company:workRecord:view")
  @RequestMapping(value = "form")
  public String form(WorkRecord workRecord, Model model) {
    model.addAttribute("workRecord", workRecord);
    return "modules/company/workRecordForm";
  }

  // @RequiresPermissions("company:workRecord:edit")
  @RequestMapping(value = "save")
  public String save(WorkRecord workRecord, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, workRecord)) {
      return form(workRecord, model);
    }
    workRecord.setRecordStatus("1");// 待审阅
    workRecordService.save(workRecord);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存工作记录成功");
    return "redirect:" + Global.getAdminPath() + "/company/workRecord/?repage";
  }

  // @RequiresPermissions("company:workRecord:edit")
  @RequestMapping(value = "delete")
  public String delete(WorkRecord workRecord, RedirectAttributes redirectAttributes) {
    workRecordService.delete(workRecord);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除工作记录成功");
    return "redirect:" + Global.getAdminPath() + "/company/workRecord/?repage";
  }

  @RequestMapping(value = "review")
  public String review(WorkRecord workRecord, RedirectAttributes redirectAttributes) {
    workRecordService.review(workRecord);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "审阅工作记录成功");
    return "redirect:" + Global.getAdminPath() + "/company/workRecord/?repage";
  }
}
