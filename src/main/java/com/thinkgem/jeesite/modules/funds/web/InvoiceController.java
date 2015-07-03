/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.funds.entity.Invoice;
import com.thinkgem.jeesite.modules.funds.service.InvoiceService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 发票信息Controller
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/invoice")
public class InvoiceController extends BaseController {

	@Autowired
	private InvoiceService invoiceService;
	
	@ModelAttribute
	public Invoice get(@RequestParam(required=false) String id) {
		Invoice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = invoiceService.get(id);
		}
		if (entity == null){
			entity = new Invoice();
		}
		return entity;
	}
	
	@RequiresPermissions("funds:invoice:view")
	@RequestMapping(value = {"list", ""})
	public String list(Invoice invoice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Invoice> page = invoiceService.findPage(new Page<Invoice>(request, response), invoice); 
		model.addAttribute("page", page);
		return "modules/funds/invoiceList";
	}

	@RequiresPermissions("funds:invoice:view")
	@RequestMapping(value = "form")
	public String form(Invoice invoice, Model model) {
		model.addAttribute("invoice", invoice);
		return "modules/funds/invoiceForm";
	}

	@RequiresPermissions("funds:invoice:edit")
	@RequestMapping(value = "save")
	public String save(Invoice invoice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, invoice)){
			return form(invoice, model);
		}
		invoiceService.save(invoice);
		addMessage(redirectAttributes, "保存发票信息成功");
		return "redirect:"+Global.getAdminPath()+"/funds/tradingAccounts/?repage";
	}
	
	@RequiresPermissions("funds:invoice:edit")
	@RequestMapping(value = "delete")
	public String delete(Invoice invoice, RedirectAttributes redirectAttributes) {
		invoiceService.delete(invoice);
		addMessage(redirectAttributes, "删除发票信息成功");
		return "redirect:"+Global.getAdminPath()+"/funds/invoice/?repage";
	}

	@RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "发票数据导入模板.xlsx";
    		List<Invoice> list = Lists.newArrayList(); list.add(new Invoice());
    		new ExportExcel("发票数据", Invoice.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/funds/invoice/list?repage";
    }
	
	@RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Invoice> list = ei.getDataList(Invoice.class);
			for (Invoice invoice : list){
				if(StringUtils.isEmpty(invoice.getInvoiceNo()) || StringUtils.isEmpty(invoice.getInvoiceType())
						||null == invoice.getInvoiceDate()||null == invoice.getInvoiceAmount()) continue;
				try{
					List<Invoice> resList = null;
					if(!StringUtils.isEmpty(invoice.getInvoiceNo())) {
						Invoice tmpInvoice = new Invoice();
						tmpInvoice.setInvoiceNo(invoice.getInvoiceNo());
						resList = this.invoiceService.findList(tmpInvoice);
					}
					if (null == resList || resList.size() < 1){
						invoiceService.save(invoice);
						successNum++;
					}else{
						failureMsg.append("<br/>发票号码 "+invoice.getInvoiceNo()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>发票号码 "+invoice.getInvoiceNo()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>发票号码 "+invoice.getInvoiceNo()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条发票，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条发票"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入发票失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/funds/invoice/list?repage";
    }
}