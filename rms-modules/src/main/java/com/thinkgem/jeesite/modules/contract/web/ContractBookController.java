package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.enums.BookStatusEnum;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.entity.User;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
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
  private CustomerService customerService;

  @Autowired
  private SmsService smsService;

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

  @RequiresPermissions("contract:contractBook:edit")
  @RequestMapping(value = "distribution")
  public String distribution(@RequestParam("customerIdList") String customerIdList, @RequestParam("salesId") String salesId) {
    List<String> idList = Arrays.asList(customerIdList.split("\\|"));
    ContractBook contractBook = new ContractBook();
    contractBook.setIdList(idList);
    contractBook.setSalesId(salesId);
    contractBook.setBookStatus(BookStatusEnum.BOOK_SUCCESS.value());
    contractBookService.distribution(contractBook);
    idList.forEach(this::sendDistributionSms);
    return "redirect:" + Global.getAdminPath() + "/contract/book/?repage";
  }

  private void sendDistributionSms(String contractBookId) {
    ContractBook contractBook = contractBookService.get(contractBookId);
    String addressInfo = contractBook.getProjectName() + contractBook.getBuildingName()  + "楼" + contractBook.getHouseNo()  + "号";
    if (StringUtils.isNotBlank(contractBook.getRoomNo())) {
      addressInfo += contractBook.getRoomNo() + "室房源";
    }
    User user = UserUtils.get(contractBook.getSalesId());
    String saleName = user.getName();
    String content = saleName + "你好，姓名：" + contractBook.getCustomer().getTrueName() + "，手机号为：" + contractBook.getBookPhone() + "的客户预约在" + DateUtils.formatDate(contractBook.getBookDate()) + "日期看" + addressInfo  + "，请提前联系用户做好带看准备。";
    smsService.sendSms(user.getPhone(), content);
  }

//  @RequiresPermissions("contract:contractBook:edit")
//  @RequestMapping(value = "cancel")
//  public String cancel(ContractBook contractBook, RedirectAttributes redirectAttributes) {
//    contractBook.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
//    contractBook = contractBookService.get(contractBook);
//    contractBook.setBookStatus("3");// 管家取消预约
//    contractBook.setSalesId(UserUtils.getUser().getId());
//    contractBookService.save(contractBook);
//    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "取消预约信息成功");
//    Message message = new Message();
//    message.setContent("您的预约申请已被管家取消,请联系管家!");
//    return "redirect:" + Global.getAdminPath() + "/contract/book/?repage";
//  }
}
