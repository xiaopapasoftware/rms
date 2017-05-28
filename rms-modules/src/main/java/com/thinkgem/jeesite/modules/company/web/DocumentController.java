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
import com.thinkgem.jeesite.modules.company.entity.Document;
import com.thinkgem.jeesite.modules.company.service.DocumentService;

/**
 * 办公文件管理Controller
 * 
 * @author huangsc
 * @version 2015-09-12
 */
@Controller
@RequestMapping(value = "${adminPath}/company/document")
public class DocumentController extends BaseController {

  @Autowired
  private DocumentService documentService;

  @ModelAttribute
  public Document get(@RequestParam(required = false) String id) {
    Document entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = documentService.get(id);
    }
    if (entity == null) {
      entity = new Document();
    }
    return entity;
  }

  // @RequiresPermissions("company:document:view")
  @RequestMapping(value = {"list", ""})
  public String list(Document document, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<Document> page = documentService.findPage(new Page<Document>(request, response), document);
    model.addAttribute("page", page);
    return "modules/company/documentList";
  }

  // @RequiresPermissions("company:document:view")
  @RequestMapping(value = "form")
  public String form(Document document, Model model) {
    model.addAttribute("document", document);
    return "modules/company/documentForm";
  }

  // @RequiresPermissions("company:document:edit")
  @RequestMapping(value = "save")
  public String save(Document document, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, document)) {
      return form(document, model);
    }
    documentService.save(document);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存办公文件管理成功");
    return "redirect:" + Global.getAdminPath() + "/company/document/?repage";
  }

  // @RequiresPermissions("company:document:edit")
  @RequestMapping(value = "delete")
  public String delete(Document document, RedirectAttributes redirectAttributes) {
    documentService.delete(document);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除办公文件管理成功");
    return "redirect:" + Global.getAdminPath() + "/company/document/?repage";
  }

}
