package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.service.CustomerService;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约看房
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/book")
public class ContractBookController extends BaseController {

  @Autowired
  private ContractBookService contractBookService;

  @Autowired
  private MessageService messageService;// APP消息推送

  @Autowired
  private CustomerService customerService;

  @ModelAttribute
  public ContractBook get(@RequestParam(required = false) String id) {
    ContractBook entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = contractBookService.get(id);
    }
    if (entity == null) {
      entity = new ContractBook();
    }
    return entity;
  }

  @RequiresPermissions("contract:contractBook:view")
  @RequestMapping(value = {"list", ""})
  public String list(ContractBook contractBook, HttpServletRequest request, HttpServletResponse response, Model model) {
    if (contractBook.getCustomer() != null && StringUtils.isNotBlank(contractBook.getCustomer().getTrueName())) {
      List<Customer> customerList = customerService.findList(contractBook.getCustomer());
      if (CollectionUtils.isEmpty(customerList)) {
        contractBook.setCustomerIdList(Collections.singletonList("test"));
      } else {
        contractBook.setCustomerIdList(customerList.stream().map(Customer::getId).collect(Collectors.toList()));
      }
    }
    Page<ContractBook> page = contractBookService.findPage(new Page<>(request, response), contractBook);
    model.addAttribute("page", page);
    return "modules/contract/contractBookList";
  }

  @RequiresPermissions("contract:contractBook:view")
  @RequestMapping(value = "form")
  public String form(ContractBook contractBook, Model model) {
    model.addAttribute("contractBook", contractBook);
    return "modules/contract/contractBookForm";
  }

  @RequiresPermissions("contract:contractBook:edit")
  @RequestMapping(value = "save")
  public String save(ContractBook contractBook, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, contractBook)) {
      return form(contractBook, model);
    }
    contractBook.setSalesId(UserUtils.getUser().getId());
    contractBookService.save(contractBook);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存预约看房信息成功");
    return "redirect:" + Global.getAdminPath() + "/contract/book/?repage";
  }

  @RequiresPermissions("contract:contractBook:edit")
  @RequestMapping(value = "confirm")
  public String confirm(ContractBook contractBook, RedirectAttributes redirectAttributes) {
    contractBook.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
    contractBook = contractBookService.get(contractBook);
    contractBook.setBookStatus("1");// 预约成功
    contractBook.setSalesId(UserUtils.getUser().getId());
    contractBookService.save(contractBook);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "确认预约信息成功");
    Message message = new Message();
    message.setContent("您的预约申请已被确认,请按约定日期联系管家看房!");
    message.setTitle("预约提醒");
    message.setType("预约提醒");
    message.setReceiver(contractBook.getBookPhone());
    messageService.addMessage(message, true);
    return "redirect:" + Global.getAdminPath() + "/contract/book/?repage";
  }

  @RequiresPermissions("contract:contractBook:edit")
  @RequestMapping(value = "cancel")
  public String cancel(ContractBook contractBook, RedirectAttributes redirectAttributes) {
    contractBook.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
    contractBook = contractBookService.get(contractBook);
    contractBook.setBookStatus("3");// 管家取消预约
    contractBook.setSalesId(UserUtils.getUser().getId());
    contractBookService.save(contractBook);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "取消预约信息成功");
    Message message = new Message();
    message.setContent("您的预约申请已被管家取消,请联系管家!");
    message.setTitle("预约提醒");
    message.setType("预约提醒");
    message.setReceiver(contractBook.getBookPhone());
    messageService.addMessage(message, true);
    return "redirect:" + Global.getAdminPath() + "/contract/book/?repage";
  }
}
