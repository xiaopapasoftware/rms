package com.thinkgem.jeesite.modules.contract.web;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.entity.*;
import com.thinkgem.jeesite.modules.contract.enums.*;
import com.thinkgem.jeesite.modules.contract.service.*;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;
import com.thinkgem.jeesite.modules.fee.service.PostpaidFeeService;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTradeService;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.ReceiptService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.entity.Company;
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.CompanyService;
import com.thinkgem.jeesite.modules.person.service.PartnerService;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.report.service.FeeReportService;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/contract/rentContract")
public class RentContractController extends CommonBusinessController {

    @Autowired
    private RentContractService rentContractService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private PaymentTransService paymentTransService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private TradingAccountsService tradingAccountsService;
    @Autowired
    private PaymentTradeService paymentTradeService;
    @Autowired
    private ElectricFeeService electricFeeService;
    @Autowired
    private DepositAgreementService depositAgreementService;
    @Autowired
    private ContractTenantService contractTenantService;
    @Autowired
    private MessageService messageService;// APP消息推送
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private FeeReportService feeReportService;
    @Autowired
    private AgreementChangeService agreementChangeService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private AuditHisService auditHisService;
    @Autowired
    private PostpaidFeeService postpaidFeeService;

    @ModelAttribute
    public RentContract get(@RequestParam(required = false) String id) {
        RentContract entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = rentContractService.get(id);
        }
        if (entity == null) {
            entity = new RentContract();
        }
        return entity;
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = {""})
    public String initPage(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
        initContractSearchConditions(rentContract, model, request, response, false);
        return "modules/contract/rentContractList";
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = {"list"})
    public String list(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
        initContractSearchConditions(rentContract, model, request, response, true);
        return "modules/contract/rentContractList";
    }

    private void initContractSearchConditions(RentContract rentContract, Model model, HttpServletRequest request, HttpServletResponse response, boolean needQuery) {
        if (needQuery) {
            Page<RentContract> page = rentContractService.findPage(new Page<>(request, response), rentContract);
            model.addAttribute("page", page);
        }
        if (rentContract != null) {
            commonInit("projectList", "buildingList", "houseList", "roomList", model, rentContract.getPropertyProject(), rentContract.getBuilding(), rentContract.getHouse());
        }
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = {"rentContractDialogList"})
    public String rentContractDialogList(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
        rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue());
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.VALID.getValue());
        initContractSearchConditions(rentContract, model, request, response, true);
        return "modules/contract/rentContractDialog";
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = {"rentContractDialog"})
    public String rentContractDialog(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("rentContract", rentContract);
        initContractSearchConditions(rentContract, model, request, response, false);
        return "modules/contract/rentContractDialog";
    }

    @RequiresPermissions("contract:rentContract:audit")
    @RequestMapping(value = "audit")
    public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        rentContractService.audit(auditHis);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "审核操作已完成！");
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "cancel")
    public String cancel(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
        auditHis.setAuditStatus(AuditStatusEnum.REFUSE.getValue());
        auditHis.setUpdateUser(UserUtils.getUser().getId());
        rentContractService.audit(auditHis);
        return list(new RentContract(), request, response, model);
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = "form")
    public String form(RentContract rentContract, Model model, HttpServletRequest request) {
        if (rentContract.getIsNewRecord()) {
            rentContract.setSignType(ContractSignTypeEnum.NEW_SIGN.getValue());
            rentContract.setContractCode((rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
        }
        model.addAttribute("rentContract", rentContract);
        commonInit2("projectList", "buildingList", "houseList", "roomList", model, rentContract.getPropertyProject(), rentContract.getBuilding(), rentContract.getHouse(), rentContract.getRoom());
        model.addAttribute("partnerList", partnerService.findList(new Partner()));
        return "modules/contract/rentContractForm";
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = "ajaxQueryLivedTenants")
    @ResponseBody
    public String ajaxQueryLivedTenants(String rentContractId) {
        if (StringUtils.isNotBlank(rentContractId)) {
            RentContract rentContract = new RentContract();
            rentContract.setId(rentContractId);
            List<Tenant> livedTenants = rentContractService.findLiveTenant(rentContract);
            if (CollectionUtils.isNotEmpty(livedTenants)) {
                return getSelectOptionResult(livedTenants);
            }
        }
        return "";
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = "ajaxQueryLeasedTenants")
    @ResponseBody
    public String ajaxQueryLeasedTenants(String rentContractId) {
        if (StringUtils.isNotBlank(rentContractId)) {
            RentContract rentContract = new RentContract();
            rentContract.setId(rentContractId);
            List<Tenant> leaseTenants = rentContractService.findTenant(rentContract);
            if (CollectionUtils.isNotEmpty(leaseTenants)) {
                return getSelectOptionResult(leaseTenants);
            }
        }
        return "";
    }

    private String getSelectOptionResult(List<Tenant> tenants) {
        StringBuffer optionResultBuf = new StringBuffer();
        if (CollectionUtils.isNotEmpty(tenants)) {
            for (Tenant t : tenants) {
                if (t != null) {
                    String companyName = "";
                    if (t.getCompany() != null && StringUtils.isNotEmpty(t.getCompany().getId())) {
                        Company c = companyService.get(t.getCompany().getId());
                        companyName = c.getCompanyName();
                    }
                    if (StringUtils.isNotEmpty(companyName)) {
                        optionResultBuf.append("<option selected value='").append(t.getId()).append("'>").append(t.getTenantName()).append("-").append(t.getIdNo()).append("-").append(t.getCellPhone()).append("-")
                                .append(companyName).append("</option>");
                    } else {
                        optionResultBuf.append("<option selected value='").append(t.getId()).append("'>").append(t.getTenantName()).append("-").append(t.getIdNo()).append("-").append(t.getCellPhone())
                                .append("</option>");
                    }
                }
            }
        }
        return optionResultBuf.toString();
    }

    /**
     * 续签
     */
    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "renewContract")
    public String renewContract(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
        String contractId = rentContract.getId();
        if (paymentTransService.checkNotSignedPaymentTrans(contractId)) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "有款项未到账,不能续签！");
            return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
        }
        rentContract = rentContractService.get(contractId);
        rentContract.setOriEndDate(DateUtils.formatDate(rentContract.getExpiredDate()));// 为了实现续签合同的开始日期默认为原合同的结束日期，则把原合同的结束日期带到页面
        rentContract.setContractId(contractId);
        rentContract.setSignType(ContractSignTypeEnum.RENEW_SIGN.getValue());
        rentContract.setRenewCount(rentContract.getRenewCount() == null ? 1 : rentContract.getRenewCount() + 1);
        rentContract.setDepositElectricAmount(null);
        rentContract.setDepositAmount(null);
        rentContract.setRental(null);
        rentContract.setRenMonths(null);
        rentContract.setDepositMonths(null);
        rentContract.setStartDate(null);
        rentContract.setExpiredDate(null);
        rentContract.setSignDate(null);
        rentContract.setRemindTime(null);
        int currContractNum = 1;
        List<RentContract> allContracts = rentContractService.findAllValidRentContracts();
        if (CollectionUtils.isNotEmpty(allContracts)) {
            currContractNum = currContractNum + allContracts.size();
        }
        rentContract.setContractCode(rentContract.getContractCode().split("-")[0] + "-" + currContractNum + "-" + rentContract.getContractCode().split("-")[2]);
        rentContract.setLiveList(rentContractService.findLiveTenant(rentContract));
        rentContract.setTenantList(rentContractService.findTenant(rentContract));
        commonInit2("projectList", "buildingList", "houseList", "roomList", model, rentContract.getPropertyProject(), rentContract.getBuilding(), rentContract.getHouse(), rentContract.getRoom());
        model.addAttribute("tenantList", tenantService.findList(new Tenant()));
        model.addAttribute("renew", "1");
        model.addAttribute("partnerList", partnerService.findList(new Partner()));
        rentContract.setId(null);
        rentContract.setAgreementId(null);// 防止对某些定金转的合同，再进行续签，导致最终续签合同还持有agreementId
        model.addAttribute("rentContract", rentContract);
        return "modules/contract/rentContractForm";
    }

    /**
     * 牵涉到后台合同的新增、修改；以及APP端合同的修改保存
     */
    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "save")
    public String save(RentContract rentContract, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, rentContract) && ValidatorFlagEnum.SAVE.getValue().equals(rentContract.getValidatorFlag())) {
            return form(rentContract, model, request);
        }
        if (rentContract.getIsNewRecord()) {
            String[] codeArr = rentContract.getContractCode().split("-");
            rentContract.setContractCode(codeArr[0] + "-" + (rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
        }
        if (StringUtils.isNotEmpty(rentContract.getDataSource()) && DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) {
        } else {
            rentContract.setDataSource(DataSourceEnum.BACK_SYSTEM.getValue());
        }
        if (rentContract.getHasFree() == null) {
            rentContract.setHasFree(AwardRentAmtTypeEnum.N.getValue());
            rentContract.setFreeMonths(0);
        }
        int result = rentContractService.saveContract(rentContract);
        if (result == -3) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "系统异常，保存失败，请联系管理员！");
        } else if (result == -2) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "出租合同结束日期不能晚于承租合同截止日期,保存失败，请重试！");
        } else if (result == -1) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房源已出租，保存失败，请重试！");
        } else {
            if (StringUtils.isNotEmpty(rentContract.getDataSource()) && DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) {// APP订单后台保存
                try {
                    Message message = new Message();
                    message.setContent("您的签约申请已被管家确认,请联系管家!");
                    message.setTitle("签约提醒");
                    message.setType("签约提醒");
                    Tenant tenant = rentContract.getTenantList().get(0);
                    message.setReceiver(tenantService.get(tenant).getCellPhone());
                    messageService.addMessage(message, true);
                } catch (Exception e) {
                    logger.error("签约推送异常:", e);
                }
            }
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "出租合同保存成功！");
        }
        if (StringUtils.isNotBlank(rentContract.getAgreementId())) {
            return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
        } else {
            return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
        }
    }

    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "saveAdditional")
    public String saveAdditional(AgreementChange agreementChange, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, agreementChange)) {
            RentContract rentContract = new RentContract();
            rentContract.setId(agreementChange.getContractId());
            return changeContract(rentContract, model);
        }
        rentContractService.saveAdditional(agreementChange);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存变更协议成功 ！");
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    /**
     * 点击“正常退租”按钮触发的业务
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "returnContract")
    public String returnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
        if (paymentTransService.checkNotSignedPaymentTrans(rentContract.getId())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "有款项未到账,不能正常退租!");
            return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
        }
        String returnDateStr = rentContract.getReturnDateStr();
        rentContract = rentContractService.get(rentContract.getId());
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.NORMAL_RETURN_ACCOUNT.getValue());
        if (StringUtils.isNotEmpty(returnDateStr)) {
            rentContract.setReturnDate(DateUtils.parseDate(returnDateStr));
        }
        rentContractService.save(rentContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "正常退租成功，接下来请进行正常退租核算！");
        feeReportService.deleteFeeReportByRentContractId(rentContract.getId());
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    /**
     * 把出租合同业务状态从“正常退租待核算”回滚为 “有效”
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "rollbackToNormal")
    public String rollbackContractToValidFromNormalReturn(RentContract rentContract, RedirectAttributes redirectAttributes) {
        rentContract = rentContractService.get(rentContract.getId());
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.VALID.getValue());
        rentContractService.save(rentContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "已恢复为有效合同！");
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    /**
     * 点击“提前退租”按钮触发的业务
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "earlyReturnContract")
    public String earlyReturnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
        String rentContractId = rentContract.getId();
        String returnDateStr = rentContract.getReturnDateStr();
        paymentTransService.deleteNotSignPaymentTrans(rentContractId);
        rentContract = rentContractService.get(rentContractId);
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.EARLY_RETURN_ACCOUNT.getValue());
        if (StringUtils.isNotEmpty(returnDateStr)) {
            rentContract.setReturnDate(DateUtils.parseDate(returnDateStr));
        }
        rentContractService.save(rentContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "提前退租成功，接下来请进行提前退租核算！");
        feeReportService.deleteFeeReportByRentContractId(rentContract.getId());
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    /**
     * 把出租合同业务状态从“提前退租待核算”回滚为 “有效”
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "rollbackFromPreturnToNormal")
    public String rollbackContractToValidFromPrereturn(RentContract rentContract, RedirectAttributes redirectAttributes) {
        String rentContractId = rentContract.getId();
        paymentTransService.rollbackDeleteNotSignPaymentTrans(rentContractId);
        rentContract = rentContractService.get(rentContractId);
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.VALID.getValue());
        rentContractService.save(rentContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "已恢复为有效合同！");
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    /**
     * 点击“逾期退租”按钮触发的业务
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "lateReturnContract")
    public String lateReturnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
        if (paymentTransService.checkNotSignedPaymentTrans(rentContract.getId())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "有款项未到账,不能逾期退租!");
            return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
        }
        String returnDateStr = rentContract.getReturnDateStr();
        rentContract = rentContractService.get(rentContract.getId());
        if (StringUtils.isNotEmpty(returnDateStr)) {
            rentContract.setReturnDate(DateUtils.parseDate(returnDateStr));
        }
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.LATE_RETURN_ACCOUNT.getValue());
        rentContractService.save(rentContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "逾期退租成功，接下来请进行逾期退租核算！");
        feeReportService.deleteFeeReportByRentContractId(rentContract.getId());
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    /**
     * 点击“特殊退租”按钮触发的业务
     */
    @RequiresPermissions("contract:rentContract:specialreturn")
    @RequestMapping(value = "specialReturnContract")
    public String specialReturnContract(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
        String returnDateStr = rentContract.getReturnDateStr();// 用户指定的实际退租日期
        String contractId = rentContract.getId();
        rentContract = rentContractService.get(contractId);
        rentContract.setReturnDateStr(returnDateStr);
        rentContract.setTradeType(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue());
        List<Accounting> outAccountList = genOutAccountListBack(rentContract, false);// 应出核算项列表
        List<Accounting> inAccountList = genInAccountListBack(rentContract, false, false);// 应收核算项列表
        model.addAttribute("dates", DateUtils.getDistanceOfTwoDate(rentContract.getStartDate(), DateUtils.parseDate(rentContract.getReturnDateStr())));// 已住天数
        model.addAttribute("totalFee", commonCalculateTotalAmount(rentContract, PaymentTransTypeEnum.RENT_AMOUNT.getValue()));
        model.addAttribute("rental", rentContract.getRental());
        model.addAttribute("outAccountList", outAccountList);
        model.addAttribute("outAccountSize", outAccountList.size());
        model.addAttribute("accountList", inAccountList);
        model.addAttribute("accountSize", inAccountList.size());
        model.addAttribute("rentContract", rentContract);
        model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
        feeReportService.deleteFeeReportByRentContractId(rentContract.getId());
        return "modules/contract/rentContractCheck";
    }

    @RequiresPermissions("contract:rentContract:change")
    @RequestMapping(value = "changeContract")
    public String changeContract(RentContract rentContract, Model model) {
        AgreementChange agreementChange = new AgreementChange();
        agreementChange.setContractId(rentContract.getId());
        model.addAttribute("agreementChange", agreementChange);
        List<Tenant> tenantList = tenantService.findList(new Tenant());
        model.addAttribute("tenantList", tenantList);
        return "modules/contract/additionalContract";
    }

    /**
     * 点击“正常退租核算”按钮触发的业务
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "toReturnCheck")
    public String toReturnCheck(RentContract rentContract, Model model) {
        rentContract = rentContractService.get(rentContract.getId());
        List<Accounting> outAccountList = genOutAccountListBack(rentContract, false);// 应出核算项列表
        List<Accounting> inAccountList = genInAccountListBack(rentContract, false, false);// 应收核算项列表
        model.addAttribute("accountList", inAccountList);
        model.addAttribute("accountSize", inAccountList.size());
        model.addAttribute("outAccountList", outAccountList);
        model.addAttribute("outAccountSize", outAccountList.size());
        rentContract.setTradeType(TradeTypeEnum.NORMAL_RETURN_RENT.getValue());
        model.addAttribute("rentContract", rentContract);
        model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
        return "modules/contract/rentContractCheck";
    }

    /**
     * 点击“提前退租核算”按钮触发的业务
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "toEarlyReturnCheck")
    public String toEarlyReturnCheck(RentContract rentContract, Model model) {
        rentContract = rentContractService.get(rentContract.getId());
        List<Accounting> outAccountList = genOutAccountListBack(rentContract, true);// 应出核算项列表
        List<Accounting> inAccountList = genInAccountListBack(rentContract, true, false);// 应收核算项列表
        model.addAttribute("returnRental", "1");// 显示计算公式的标识
        model.addAttribute("totalFee", commonCalculateTotalAmount(rentContract, PaymentTransTypeEnum.RENT_AMOUNT.getValue()));// 已缴纳房租总金额
        model.addAttribute("rental", rentContract.getRental());
        model.addAttribute("dates", DateUtils.getDistanceOfTwoDate(rentContract.getStartDate(), new Date()));// 已住天数
        model.addAttribute("accountList", inAccountList);
        model.addAttribute("accountSize", inAccountList.size());
        model.addAttribute("outAccountList", outAccountList);
        model.addAttribute("outAccountSize", outAccountList.size());
        rentContract.setTradeType(TradeTypeEnum.ADVANCE_RETURN_RENT.getValue());
        model.addAttribute("rentContract", rentContract);
        model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
        return "modules/contract/rentContractCheck";
    }

    /**
     * 点击“逾期退租核算”按钮触发的业务
     */
    @RequiresPermissions("contract:rentContract:return")
    @RequestMapping(value = "toLateReturnCheck")
    public String toLateReturnCheck(RentContract rentContract, Model model) {
        rentContract = rentContractService.get(rentContract.getId());
        List<Accounting> outAccountList = genOutAccountListBack(rentContract, false);// 应出核算项列表
        List<Accounting> inAccountList = genInAccountListBack(rentContract, false, true);// 应收核算项列表
        model.addAttribute("dates", DateUtils.getDistanceOfTwoDate(rentContract.getExpiredDate(), new Date()) - 1);// 已住天数
        model.addAttribute("rental", rentContract.getRental());
        model.addAttribute("outAccountList", outAccountList);
        model.addAttribute("outAccountSize", outAccountList.size());
        model.addAttribute("accountList", inAccountList);
        model.addAttribute("accountSize", inAccountList.size());
        rentContract.setTradeType(TradeTypeEnum.OVERDUE_RETURN_RENT.getValue());
        model.addAttribute("rentContract", rentContract);
        model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
        return "modules/contract/rentContractCheck";
    }

    /**
     * 计算各种退款的应退或应收的总金额
     */
    private String calculateTatalRefundAmt(List<Accounting> outAccountList, List<Accounting> inAccountList) {
        double totalOutAmt = calculateTatalAmt(outAccountList); // 应出总金额
        double totalInAmt = calculateTatalAmt(inAccountList);// 应收总金额
        return new BigDecimal(totalInAmt - totalOutAmt).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    }

    private double calculateTatalAmt(List<Accounting> accountList) {
        double totalAmt = 0;
        if (CollectionUtils.isNotEmpty(accountList)) {
            for (Accounting outAcct : accountList) {
                totalAmt += outAcct.getFeeAmount();
            }
        }
        return totalAmt;
    }

    /**
     * 在退租核算页面，点保存按钮
     */
    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "returnCheck")
    public String returnCheck(RentContract rentContract, RedirectAttributes redirectAttributes) {
        rentContractService.returnCheck(rentContract, rentContract.getTradeType());
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "操作成功！");
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "delete")
    public String delete(RentContract rentContract, RedirectAttributes redirectAttributes) {
        rentContract.preUpdate();
        rentContractService.delete(rentContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除出租合同成功");
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    /**
     * 应出退租核算项列表
     */
    private List<Accounting> genOutAccountListBack(RentContract rentContract, boolean isPre) {
        List<Accounting> outAccountings = new ArrayList<>();
        // 应退水电费押金
        Accounting eaccounting = new Accounting();
        eaccounting.setFeeType(PaymentTransTypeEnum.WATER_ELECT_DEPOSIT.getValue());
        if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType())) {// 如果是续签合同，需要退被续签合同的水电费押金+水电费押金差额,需做递归处理
            eaccounting.setFeeAmount(calculateContinueContractAmount(rentContract, "2"));
        } else {// 如果是新签合同则直接退水电费押金
            eaccounting.setFeeAmount(rentContract.getDepositElectricAmount());
        }
        outAccountings.add(eaccounting);
        // 应退房租押金
        Accounting accounting = new Accounting();
        accounting.setFeeType(PaymentTransTypeEnum.RENT_DEPOSIT.getValue());
        if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType())) {// 如果是续签合同，需要退被续签合同的房租押金+房租押金差额,需做递归处理
            accounting.setFeeAmount(calculateContinueContractAmount(rentContract, "4"));
        } else {// 如果是新签合同则直接退房租押金
            accounting.setFeeAmount(rentContract.getDepositAmount());
        }
        outAccountings.add(accounting);
        // 应退房租(提前退租或特殊退租)
        Date paidExpiredDate = paymentTransService.analysisMaxIncomedTransDate(rentContract);
        String returnDateStr = rentContract.getReturnDateStr();// 如果是特殊退租，用户指定的退租日期
        if (isPre || (StringUtils.isNotEmpty(returnDateStr) && paidExpiredDate != null && paidExpiredDate.after(DateUtils.parseDate(returnDateStr)))) {
            Accounting preBackRentalAcc = new Accounting();
            preBackRentalAcc.setFeeType(PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue());
            preBackRentalAcc.setFeeAmount(commonCalculateBackAmount(rentContract, PaymentTransTypeEnum.RENT_AMOUNT.getValue(), rentContract.getRental()));
            outAccountings.add(preBackRentalAcc);
        }
        // 应退智能电表剩余电费，整租不装智能电表，只有合租会装智能电表
        if (RentModelTypeEnum.JOINT_RENT.getValue().equals(rentContract.getRentMode())) {
            Room room = roomService.get(rentContract.getRoom());
            if (room != null && StringUtils.isNotEmpty(room.getMeterNo())) {// 合租且电表号不为空
                Accounting elctrBackAcc = new Accounting();
                elctrBackAcc.setFeeType(PaymentTransTypeEnum.ELECT_SURPLUS_AMOUNT.getValue());
                Map<Integer, String> resultMap = electricFeeService.getMeterFee(rentContract.getId(), DateUtils.firstDateOfCurrentMonth(), DateUtils.lastDateOfCurrentMonth());
                Double feeAmount = 0D;
                if (MapUtils.isNotEmpty(resultMap)) {
                    String remainedTotalEle = resultMap.get(3);// 剩余总电量
                    String personElePrice = resultMap.get(4);// 个人电量单价
                    if (StringUtils.isNotEmpty(remainedTotalEle) && StringUtils.isNotEmpty(personElePrice)) {
                        Double price = Double.valueOf(remainedTotalEle) * Double.valueOf(personElePrice);
                        if (price > 0) {
                            feeAmount = new BigDecimal(price).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue(); // 保留两位小数
                        }
                    }
                }
                elctrBackAcc.setFeeAmount(feeAmount);
                outAccountings.add(elctrBackAcc);
            }
        }
        if ("0".equals(rentContract.getChargeType())) {
            // 预付 ---应退 水费剩余金额
            if (rentContract.getWaterFee() != null && rentContract.getWaterFee() > 0) {
                Accounting waterAcc = new Accounting();
                waterAcc.setFeeType(PaymentTransTypeEnum.WATER_SURPLUS_AMOUNT.getValue());
                waterAcc.setFeeAmount(commonCalculateBackAmount(rentContract, PaymentTransTypeEnum.WATER_AMOUNT.getValue(), rentContract.getWaterFee()));
                outAccountings.add(waterAcc);
            }
            // 预付 ---应退 燃气费
            if (rentContract.getGasFee() != null && rentContract.getGasFee() > 0) {
                Accounting gasAcc = new Accounting();
                gasAcc.setFeeType(PaymentTransTypeEnum.GAS_REFUND_AMOUNT.getValue());
                gasAcc.setFeeAmount(commonCalculateBackAmount(rentContract, PaymentTransTypeEnum.GAS_AMOUNT.getValue(), rentContract.getGasFee()));
                outAccountings.add(gasAcc);
            }
            // 预付 ---应退 电视费
            if ("1".equals(rentContract.getHasTv()) && null != rentContract.getTvFee() && rentContract.getTvFee() > 0) {
                Accounting tvAcc = new Accounting();
                tvAcc.setFeeType(PaymentTransTypeEnum.TV_SURPLUS_AMOUNT.getValue());
                tvAcc.setFeeAmount(commonCalculateBackAmount(rentContract, PaymentTransTypeEnum.TV_AMOUNT.getValue(), rentContract.getTvFee()));
                outAccountings.add(tvAcc);
            }
            // 应退宽带费
            if ("1".equals(rentContract.getHasNet()) && null != rentContract.getNetFee() && rentContract.getNetFee() > 0) {
                Accounting netAcc = new Accounting();
                netAcc.setFeeType(PaymentTransTypeEnum.NET_SURPLUS_AMOUNT.getValue());
                netAcc.setFeeAmount(commonCalculateBackAmount(rentContract, PaymentTransTypeEnum.NET_AMOUNT.getValue(), rentContract.getNetFee()));
                outAccountings.add(netAcc);
            }
            // 应退服务费
            if (null != rentContract.getServiceFee() && rentContract.getServiceFee() > 0) {
                Accounting servAcc = new Accounting();
                servAcc.setFeeType(PaymentTransTypeEnum.SERVICE_SURPLUS_AMOUNT.getValue());
                servAcc.setFeeAmount(commonCalculateBackAmount(rentContract, PaymentTransTypeEnum.SERVICE_AMOUNT.getValue(), rentContract.getServiceFee()));
                outAccountings.add(servAcc);
            }
        }
        return outAccountings;
    }

    /**
     * 如果当前合同是续签合同，则计算出当前合同前所有的被续签合同的押金总额
     *
     * @param depositType 为4=则计算所有房租押金金额；为2=则计算所有水电押金金额
     */
    private double calculateContinueContractAmount(RentContract currentContract, String depositType) {
        Double totalAmount = 0d;
        if ("4".equals(depositType)) {// 房租押金金额
            totalAmount = currentContract.getDepositAmount();
        }
        if ("2".equals(depositType)) {// 水电押金金额
            totalAmount = currentContract.getDepositElectricAmount();
        }
        RentContract tempContract = currentContract;// 防止修改原合同数据
        while (StringUtils.isNotEmpty(tempContract.getContractId())) {
            RentContract tempOriRentContract = rentContractService.get(tempContract.getContractId());
            if (tempOriRentContract != null) {
                if ("4".equals(depositType)) {// 房租押金金额
                    totalAmount = totalAmount + tempOriRentContract.getDepositAmount();
                }
                if ("2".equals(depositType)) {// 水电押金金额
                    totalAmount = totalAmount + tempOriRentContract.getDepositElectricAmount();
                }
                tempContract = tempOriRentContract;
            } else {
                logger.warn("can not find oriRentContract by contractID:[" + tempContract.getContractId() + "]");
                break;
            }
        }
        return new BigDecimal(totalAmount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取合同下账务交易类型为“新签合同”、“续签合同”的，审核通过的账务交易关联的所有款项列表
     */
    private List<PaymentTrans> genPaymentTrades(RentContract rentContract) {
        List<PaymentTrans> allPTs = Lists.newArrayList();
        // 获取到所有的新签、续签合同审批成功的账务交易记录
        TradingAccounts ta = new TradingAccounts();
        ta.setTradeId(rentContract.getId());
        ta.setTradeStatus(TradingAccountsStatusEnum.AUDIT_PASS.getValue());
        if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(rentContract.getSignType())) {
            ta.setTradeType(TradeTypeEnum.SIGN_NEW_CONTRACT.getValue());
        }
        if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType())) {
            ta.setTradeType(TradeTypeEnum.NORMAL_RENEW.getValue());
        }
        List<TradingAccounts> tradingAccounts = tradingAccountsService.findList(ta);
        if (CollectionUtils.isNotEmpty(tradingAccounts)) {
            for (TradingAccounts taccount : tradingAccounts) {
                PaymentTrade paymentTrade = new PaymentTrade();
                paymentTrade.setTradeId(taccount.getId());
                List<PaymentTrade> pts = paymentTradeService.findList(paymentTrade);// 账务-款项关联表
                if (CollectionUtils.isNotEmpty(pts)) {
                    for (PaymentTrade pt : pts) {
                        PaymentTrans paymentTran = paymentTransService.get(pt.getTransId());
                        if (paymentTran != null) {
                            allPTs.add(paymentTran);
                        }
                    }
                }
            }
        }
        return allPTs;
    }

    /**
     * 通用的计算应退房租、水费、电视费、燃气费、宽带费、服务费已成功缴纳且到账的总金额
     *
     * @param paymentType 款项类型
     */
    private Double commonCalculateTotalAmount(RentContract rentContract, String paymentType) {
        Double totalAmount = 0d;// 租客总共已经成功缴纳的费用金额
        List<PaymentTrans> rentalPaymentTrans = genPaymentTrades(rentContract);
        if (CollectionUtils.isNotEmpty(rentalPaymentTrans)) {
            for (PaymentTrans pt : rentalPaymentTrans) {
                if (pt.getTradeAmount() != null && paymentType.equals(pt.getPaymentType())) {
                    // 对于某些定金转合同 并且 定金金额不足一个月房租的情况，此处应除去房租里定金转的部分 ，因为后续会单独把定金金额加上。
                    if (pt.getTransferDepositAmount() != null && pt.getTransferDepositAmount() > 0) {
                        totalAmount = totalAmount + (pt.getTradeAmount() - pt.getTransferDepositAmount());
                    } else {
                        totalAmount = totalAmount + pt.getTradeAmount();
                    }
                }
            }
        }
        // 特殊的，当合同为定金转过来的时候，用户已经支付的定金金额需要额外补充计算
        if (PaymentTransTypeEnum.RENT_AMOUNT.getValue().equals(paymentType)) {// 应退房租
            if (!StringUtils.isBlank(rentContract.getAgreementId())) {
                DepositAgreement depositAgreement = depositAgreementService.get(rentContract.getAgreementId());
                if (depositAgreement.getDepositAmount() > 0d) {
                    totalAmount = totalAmount + depositAgreement.getDepositAmount();
                }
            }
        }
        return totalAmount;
    }

    /**
     * 通用的计算应退房租、水费、电视费、燃气费、宽带费、服务费金额的方法
     *
     * @param paymentType    款项类型
     * @param monthFeeAmount 每月费用金额
     */
    private Double commonCalculateBackAmount(RentContract rentContract, String paymentType, double monthFeeAmount) {
        Double totalAmount = commonCalculateTotalAmount(rentContract, paymentType);
        Date endDate = new Date();
        if (StringUtils.isNotEmpty(rentContract.getReturnDateStr())) {// 特殊退租时
            endDate = DateUtils.parseDate(rentContract.getReturnDateStr());
        }
        double dates = DateUtils.getDistanceOfTwoDate(rentContract.getStartDate(), endDate);// 实际入住天数
        double dailyFee = monthFeeAmount * 12 / 365;// 平摊到每天的费用金额
        double hasLivedAmount = dates * dailyFee;
        Double refundAmount = totalAmount - hasLivedAmount;// 提前退租应退金额
        if (refundAmount < 0) {
            return 0d;
        } else {
            return new BigDecimal(refundAmount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    /**
     * 应收退租核算项列表
     *
     * @param isPre  是否提前退租
     * @param isLate 是否逾期退租
     */
    private List<Accounting> genInAccountListBack(RentContract rentContract, boolean isPre, boolean isLate) {
        List<Accounting> inAccountings = new ArrayList<Accounting>();
        if (isPre) {// 应收---早退违约金
            Accounting earlyDepositAcc = new Accounting();
            earlyDepositAcc.setFeeType(PaymentTransTypeEnum.LEAVE_EARLY_DEPOSIT.getValue());
            if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType())) {// 如果是续签合同，需要退被续签合同的水电费押金+水电费押金差额,需做递归处理
                earlyDepositAcc.setFeeAmount(calculateContinueContractAmount(rentContract, "4"));
            } else {// 如果是新签合同则直接退水电费押金
                earlyDepositAcc.setFeeAmount(rentContract.getDepositAmount());
            }
            inAccountings.add(earlyDepositAcc);
        }
        if (isLate) {// 应收---逾赔房租
            Accounting lateAcc = new Accounting();
            lateAcc.setFeeType(PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue());
            double dates = DateUtils.getDistanceOfTwoDate(rentContract.getExpiredDate(), new Date()) - 1;// 逾期天数
            double dailyRental = rentContract.getRental() * 12 / 365;// 每天房租租金
            double tental = (dates < 0 ? 0 : dates) * dailyRental;
            lateAcc.setFeeAmount(new BigDecimal(tental).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            inAccountings.add(lateAcc);
        }

        // 应收---损坏赔偿金
        Accounting pay4BrokeAcc = new Accounting();
        pay4BrokeAcc.setFeeType(PaymentTransTypeEnum.DAMAGE_COMPENSATE.getValue());
        pay4BrokeAcc.setFeeAmount(0D);
        inAccountings.add(pay4BrokeAcc);

        // 应收---退租补偿税金
        Accounting backSuppAcc = new Accounting();
        backSuppAcc.setFeeType(PaymentTransTypeEnum.RETURN_SUPPLY_TAX.getValue());
        backSuppAcc.setFeeAmount(0D);
        inAccountings.add(backSuppAcc);

        // 应收---电费自用金额
        Accounting elSelAcc = new Accounting();
        elSelAcc.setFeeType(PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue());
        elSelAcc.setFeeAmount(0D);// 人工计算
        inAccountings.add(elSelAcc);

        // 应收---电费分摊金额
        Accounting elCommAcc = new Accounting();
        elCommAcc.setFeeType(PaymentTransTypeEnum.ELECT_SHARE_AMOUNT.getValue());
        elCommAcc.setFeeAmount(0D);// 人工计算
        inAccountings.add(elCommAcc);

        // 应收---水费金额
        Accounting waterSelAcc = new Accounting();
        waterSelAcc.setFeeType(PaymentTransTypeEnum.WATER_AMOUNT.getValue());
        waterSelAcc.setFeeAmount(0D);// 人工计算
        inAccountings.add(waterSelAcc);

        // 应收---有线电视费
        Accounting tvAcc = new Accounting();
        tvAcc.setFeeType(PaymentTransTypeEnum.TV_AMOUNT.getValue());
        tvAcc.setFeeAmount(0D);// 人工计算
        inAccountings.add(tvAcc);

        // 应收---宽带费
        Accounting netAcc = new Accounting();
        netAcc.setFeeType(PaymentTransTypeEnum.NET_AMOUNT.getValue());
        netAcc.setFeeAmount(0D);// 人工计算
        inAccountings.add(netAcc);

        // 应收---服务费
        Accounting servAcc = new Accounting();
        servAcc.setFeeType(PaymentTransTypeEnum.SERVICE_AMOUNT.getValue());
        servAcc.setFeeAmount(0D);// 人工计算
        inAccountings.add(servAcc);
        return inAccountings;
    }

    /**
     * 动态修改出租合同的入住人列表
     */
    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "changeLiveList")
    public void changeLiveList(HttpServletRequest request, HttpServletResponse response) {
        String rentContractId = request.getParameter("rentContractId");
        String liveIds = request.getParameter("liveIds");
        if (StringUtils.isNotBlank(rentContractId)) {
            contractTenantService.updateLiveList(rentContractId, convertToList(liveIds));
        }
    }

    /**
     * 动态修改出租合同的承租人列表
     */
    @RequiresPermissions("contract:rentContract:edit")
    @RequestMapping(value = "changeTenantList")
    public void changeTenantList(HttpServletRequest request, HttpServletResponse response) {
        String rentContractId = request.getParameter("rentContractId");
        String tenantIds = request.getParameter("tenantIds");
        if (StringUtils.isNotBlank(rentContractId)) {
            contractTenantService.updateTenantList(rentContractId, convertToList(tenantIds));
        }
    }

    private List<Tenant> convertToList(String ids) {
        List<Tenant> tenants = new ArrayList();
        if (StringUtils.isNotBlank(ids)) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                Tenant t = new Tenant();
                t.setId(id);
                tenants.add(t);
            }
        }
        return tenants;
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = "initPublicBasicFeeInfo")
    public String initPublicBasicFeeInfo(RentContract rentContract, Model model, HttpServletRequest request, HttpServletResponse response) {
        initContractSearchConditions(rentContract, model, request, response, false);
        return "modules/fee/contractInitFeeMgt";
    }

    @RequiresPermissions("contract:rentContract:view")
    @RequestMapping(value = "queryPublicBasicFeeInfo")
    public String queryPublicBasicFeeInfo(RentContract rentContract, Model model, HttpServletRequest request, HttpServletResponse response) {
        initContractSearchConditions(rentContract, model, request, response, true);
        return "modules/fee/contractInitFeeMgt";
    }

    /**
     * 导向修改合同公共基础费用的页面
     */
    @RequiresPermissions("contract:rentContract:editContractFeeInfo")
    @RequestMapping(value = "basicFeeForm")
    public String basicFeeform(RentContract rentContract, Model model, HttpServletRequest request) {
        return "modules/fee/basicFeeForm";
    }

    /**
     * 导向修改合同公共基础费用的页面,保存
     */
    @RequiresPermissions("contract:rentContract:editContractFeeInfo")
    @RequestMapping(value = "basicFeeSave")
    public String basicFeeSave(RentContract rentContract, Model model, HttpServletRequest request, HttpServletResponse response) {
        rentContractService.save(rentContract);
        return queryPublicBasicFeeInfo(rentContract, model, request, response);
    }

    /**
     * 出租合同的审核状态为审核通过，同时出租合同业务状态为【退租核算完成到账收据待登记4、退租款项待审核5、退租款项审核拒绝6】的后门撤销
     */
    @RequiresPermissions("contract:superRentContract:edit")
    @RequestMapping(value = "backCancelRetreat")
    public String backCancelRetreat(RentContract rentContract, RedirectAttributes redirectAttributes) {
        if (ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(rentContract.getContractStatus())) {
            backCancelRetreatDeleteBusi(rentContract, rentContract.getContractBusiStatus());
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "该出租合同的退租核算数据已被清空，合同已恢复为有效合同！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "该出租合同不是到账收据审核通过的!");
        }
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }

    private void backCancelRetreatDeleteBusi(RentContract rentContract, String contractBusiStatus) {
        if (ContractBusiStatusEnum.ACCOUNT_DONE_TO_SIGN.getValue().equals(contractBusiStatus) || ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue().equals(contractBusiStatus)
                || ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue().equals(contractBusiStatus)) {
            List<String> tradeTypeList = new ArrayList<String>();
            tradeTypeList.add(TradeTypeEnum.ADVANCE_RETURN_RENT.getValue());
            tradeTypeList.add(TradeTypeEnum.NORMAL_RETURN_RENT.getValue());
            tradeTypeList.add(TradeTypeEnum.OVERDUE_RETURN_RENT.getValue());
            tradeTypeList.add(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue());
            accountingService.delRentContractAccountings(rentContract);
            paymentTransService.deleteTransList(rentContract.getId(), tradeTypeList, null);
            rentContract.setContractBusiStatus(ContractBusiStatusEnum.VALID.getValue());
            rentContractService.save(rentContract);
            if (ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue().equals(contractBusiStatus) || ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue().equals(contractBusiStatus)) {
                TradingAccounts ta = new TradingAccounts();
                ta.setTradeId(rentContract.getId());
                ta.setTradeTypeList(tradeTypeList);
                List<TradingAccounts> tradingAccounts = tradingAccountsService.findList(ta);
                List<String> tradingAccountsIds = new ArrayList<String>();
                if (CollectionUtils.isNotEmpty(tradingAccounts)) {
                    for (TradingAccounts tempTa : tradingAccounts) {
                        tradingAccountsIds.add(tempTa.getId());
                    }
                }
                paymentTradeService.deleteByTradeIds(tradingAccountsIds);
                if (ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue().equals(contractBusiStatus)) {
                    receiptService.deleteByTradeIds(tradingAccountsIds);
                    attachmentService.deleteByTradeIds(tradingAccountsIds);
                    ta.preUpdate();
                    tradingAccountsService.delete(ta);
                }
            }
        }
    }

//    @RequestMapping(value = "testProcess")
//    public void testProcess() {
//        // 提前退租、特殊退租
//        RentContract refundrc = new RentContract();
//        List<String> refundStatusList = new ArrayList<>();
//        refundStatusList.add(ContractBusiStatusEnum.ACCOUNT_DONE_TO_SIGN.getValue());
//        refundStatusList.add(ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue());
//        refundStatusList.add(ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue());
//        refundStatusList.add(ContractBusiStatusEnum.EARLY_RETURN.getValue());
//        refundrc.setContractBusiStatusList(refundStatusList);
//        List<RentContract> refundrcList = rentContractService.findList(refundrc);
//        for (RentContract rc : refundrcList) {
//            String paymentTransId = "";
//            PaymentTrans pt = new PaymentTrans();
//            pt.setTransId(rc.getId());
//            pt.setPaymentType(PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue());
//            pt.setTradeDirection(TradeDirectionEnum.OUT.getValue());
//            List<String> tradeTypeList = new ArrayList<String>();
//            tradeTypeList.add(TradeTypeEnum.ADVANCE_RETURN_RENT.getValue());
//            pt.setTradeTypeList(tradeTypeList);
//            List<PaymentTrans> pts = paymentTransService.findList(pt);
//            if (CollectionUtils.isNotEmpty(pts)) {
//                paymentTransId = pts.get(0).getId();
//                Date feeDate;
//                Double feeAmt;
//                Accounting accounting = new Accounting();
//                accounting.setRentContractId(rc.getId());
//                accounting.setFeeDirection(TradeDirectionEnum.OUT.getValue());
//                accounting.setFeeType(PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue());
//                List<String> accountingList = new ArrayList<String>();
//                accountingList.add(AccountingTypeEnum.ADVANCE_RETURN_ACCOUNT.getValue());
//                accounting.setAccountingTypeList(accountingList);
//                List<Accounting> ats = accountingService.findList(accounting);
//                if (CollectionUtils.isNotEmpty(ats)) {
//                    feeDate = ats.get(0).getFeeDate();
//                    feeAmt = ats.get(0).getFeeAmount();
//                    if (StringUtils.isEmpty(ats.get(0).getPaymentTransId())) {
//                        Accounting accounting2 = new Accounting();
//                        accounting2.setId(ats.get(0).getId());
//                        accounting2.setPaymentTransId(paymentTransId);
//                        accountingService.updatePaymentTransId(accounting2);
//                    }
//                    if (feeDate != null && feeAmt > 0d) {
//                        rentContractService.shareRetirementAmt(feeDate, feeAmt, rc, paymentTransId, TradeDirectionEnum.OUT.getValue());
//                    }
//                }
//            }
//        }
//
//        // 特殊退
//        RentContract srefundrc = new RentContract();
//        List<String> srefundStatusList = new ArrayList<>();
//        srefundStatusList.add(ContractBusiStatusEnum.ACCOUNT_DONE_TO_SIGN.getValue());
//        srefundStatusList.add(ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue());
//        srefundStatusList.add(ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue());
//        srefundStatusList.add(ContractBusiStatusEnum.SPECIAL_RETURN.getValue());
//        srefundrc.setContractBusiStatusList(srefundStatusList);
//        List<RentContract> srefundrcList = rentContractService.findList(srefundrc);
//        for (RentContract rc : srefundrcList) {
//            String paymentTransId = "";
//            PaymentTrans pt = new PaymentTrans();
//            pt.setTransId(rc.getId());
//            List<String> paymentTypeList = new ArrayList<String>();
//            paymentTypeList.add(PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue());
//            paymentTypeList.add(PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue());
//            pt.setPaymentTypeList(paymentTypeList);
//            List<String> tradeTypeList = new ArrayList<String>();
//            tradeTypeList.add(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue());
//            pt.setTradeTypeList(tradeTypeList);
//            List<PaymentTrans> pts = paymentTransService.findList(pt);
//            if (CollectionUtils.isNotEmpty(pts)) {
//                paymentTransId = pts.get(0).getId();
//                if (PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue().equals(pts.get(0).getPaymentType())) {
//                    Date feeDate = null;
//                    Double feeAmt = 0d;
//                    Accounting accounting = new Accounting();
//                    accounting.setRentContractId(rc.getId());
//                    accounting.setFeeDirection(TradeDirectionEnum.OUT.getValue());
//                    accounting.setFeeType(PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue());
//                    List<String> accountingList = new ArrayList<String>();
//                    accountingList.add(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
//                    accounting.setAccountingTypeList(accountingList);
//                    List<Accounting> ats = accountingService.findList(accounting);
//                    if (CollectionUtils.isNotEmpty(ats)) {
//                        feeDate = ats.get(0).getFeeDate();
//                        feeAmt = ats.get(0).getFeeAmount();
//                        if (StringUtils.isEmpty(ats.get(0).getPaymentTransId())) {
//                            Accounting accounting2 = new Accounting();
//                            accounting2.setId(ats.get(0).getId());
//                            accounting2.setPaymentTransId(paymentTransId);
//                            accountingService.updatePaymentTransId(accounting2);
//                        }
//                        if (feeDate != null && feeAmt > 0d) {
//                            rentContractService.shareRetirementAmt(feeDate, feeAmt, rc, paymentTransId, TradeDirectionEnum.OUT.getValue());
//                        }
//                    }
//                }
//                if (PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue().equals(pts.get(0).getPaymentType())) {
//                    Date feeDate = null;
//                    Double feeAmt = 0d;
//                    Accounting accounting = new Accounting();
//                    accounting.setRentContractId(rc.getId());
//                    accounting.setFeeDirection(TradeDirectionEnum.IN.getValue());
//                    accounting.setFeeType(PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue());
//                    List<String> accountingList = new ArrayList<String>();
//                    accountingList.add(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
//                    accounting.setAccountingTypeList(accountingList);
//                    List<Accounting> ats = accountingService.findList(accounting);
//                    if (CollectionUtils.isNotEmpty(ats)) {
//                        feeDate = ats.get(0).getFeeDate();
//                        feeAmt = ats.get(0).getFeeAmount();
//                        if (StringUtils.isEmpty(ats.get(0).getPaymentTransId())) {
//                            Accounting accounting2 = new Accounting();
//                            accounting2.setId(ats.get(0).getId());
//                            accounting2.setPaymentTransId(paymentTransId);
//                            accountingService.updatePaymentTransId(accounting2);
//                        }
//                        if (feeDate != null && feeAmt > 0d) {
//                            rentContractService.shareOverdueAmt(feeDate, paymentTransService.analysisMaxIncomedTransDate(rc), feeAmt, rc, paymentTransId, TradeDirectionEnum.IN.getValue());
//                        }
//                    }
//                }
//            }
//        }
//
//        // 逾期退租
//        RentContract supplyrc = new RentContract();
//        List<String> supplyStatusList = new ArrayList<>();
//        supplyStatusList.add(ContractBusiStatusEnum.ACCOUNT_DONE_TO_SIGN.getValue());
//        supplyStatusList.add(ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue());
//        supplyStatusList.add(ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue());
//        supplyStatusList.add(ContractBusiStatusEnum.LATE_RETURN.getValue());
//        supplyrc.setContractBusiStatusList(supplyStatusList);
//        List<RentContract> supplyrcList = rentContractService.findList(supplyrc);
//        for (RentContract rc : supplyrcList) {
//            String paymentTransId = "";
//            PaymentTrans pt = new PaymentTrans();
//            pt.setTransId(rc.getId());
//            pt.setPaymentType(PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue());
//            pt.setTradeDirection(TradeDirectionEnum.IN.getValue());
//            List<String> tradeTypeList = new ArrayList<String>();
//            tradeTypeList.add(TradeTypeEnum.OVERDUE_RETURN_RENT.getValue());
//            pt.setTradeTypeList(tradeTypeList);
//            List<PaymentTrans> pts = paymentTransService.findList(pt);
//            if (CollectionUtils.isNotEmpty(pts)) {
//                paymentTransId = pts.get(0).getId();
//                Date feeDate = null;
//                Double feeAmt = 0d;
//                Accounting accounting = new Accounting();
//                accounting.setRentContractId(rc.getId());
//                accounting.setFeeDirection(TradeDirectionEnum.IN.getValue());
//                accounting.setFeeType(PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue());
//                List<String> accountingList = new ArrayList<String>();
//                accountingList.add(AccountingTypeEnum.LATE_RETURN_ACCOUNT.getValue());
//                accounting.setAccountingTypeList(accountingList);
//                List<Accounting> ats = accountingService.findList(accounting);
//                if (CollectionUtils.isNotEmpty(ats)) {
//                    feeDate = ats.get(0).getFeeDate();
//                    feeAmt = ats.get(0).getFeeAmount();
//                    if (StringUtils.isEmpty(ats.get(0).getPaymentTransId())) {
//                        Accounting accounting2 = new Accounting();
//                        accounting2.setId(ats.get(0).getId());
//                        accounting2.setPaymentTransId(paymentTransId);
//                        accountingService.updatePaymentTransId(accounting2);
//                    }
//                    if (feeDate != null && feeAmt > 0d) {
//                        rentContractService.shareOverdueAmt(feeDate, rc.getExpiredDate(), feeAmt, rc, paymentTransId, TradeDirectionEnum.IN.getValue());
//                    }
//                }
//            }
//        }
//    }

    @RequiresPermissions("contract:rentContract:deleteContract")
    @RequestMapping(value = "deleteContract")
    public String deleteContract(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
        accountingService.delRentContractAccountings(rentContract);
        agreementChangeService.delRentContract(rentContract);
        attachmentService.delRentContract(rentContract);
        auditService.deleteRentContract(rentContract.getId());
        auditHisService.delete(rentContract.getId());
        contractTenantService.delRentContract(rentContract);
        paymentTransService.deletePaymentTransAndTradingAcctouns(rentContract.getId());
        postpaidFeeService.delRentContract(rentContract.getId());
        feeReportService.deleteFeeReportByRentContractId(rentContract.getId());
        rentContractService.updateHouseRendStatusByContractId(rentContract.getId());
        rentContract.preUpdate();
        rentContractService.delete(rentContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除成功！");
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/";
    }

}
