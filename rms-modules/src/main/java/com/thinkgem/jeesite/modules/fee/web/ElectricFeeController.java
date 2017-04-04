package com.thinkgem.jeesite.modules.fee.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.ElectricChargeStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.FeeSettlementStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFeeUseInfo;
import com.thinkgem.jeesite.modules.fee.entity.PostpaidFee;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;
import com.thinkgem.jeesite.modules.fee.service.PostpaidFeeService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;

/**
 * 电费管理
 * 
 * @author wangshujin
 */
@Controller
@RequestMapping(value = "${adminPath}/fee/electricFee")
public class ElectricFeeController extends BaseController {

  @Autowired
  private RoomService roomServie;
  @Autowired
  private ElectricFeeService electricFeeService;
  @Autowired
  private PostpaidFeeService postpaidFeeService;
  @Autowired
  private RentContractService rentContractService;
  @Autowired
  private TradingAccountsService tradingAccountsService;

  @ModelAttribute
  public ElectricFee get(@RequestParam(required = false) String id) {
    ElectricFee entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = electricFeeService.get(id);
    }
    if (entity == null) {
      entity = new ElectricFee();
    }
    return entity;
  }

  // @RequiresPermissions("fee:electricFee:view")
  @RequestMapping(value = {"list", ""})
  public String list(ElectricFee electricFee, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<ElectricFee> page = electricFeeService.findPage(new Page<ElectricFee>(request, response), electricFee);
    model.addAttribute("page", page);
    return "modules/fee/electricFeeList";
  }

  // @RequiresPermissions("fee:electricFee:view")
  @RequestMapping(value = "form")
  public String form(ElectricFee electricFee, Model model) {
    model.addAttribute("electricFee", electricFee);
    return "modules/fee/electricFeeForm";
  }

  @RequestMapping(value = "viewUseInfo")
  public String viewUseInfo(ElectricFeeUseInfo electricFeeUseInfo, Model model) {
    if (StringUtils.isNotEmpty(electricFeeUseInfo.getContractCode())) {
      RentContract resultRentContract = rentContractService.findContractByCode(electricFeeUseInfo.getContractCode());
      if (resultRentContract != null) {
        String startDate = electricFeeUseInfo.getStartDate();
        String endDate = electricFeeUseInfo.getEndDate();
        Map<Integer, String> resultMap = electricFeeService.getMeterFee(resultRentContract.getId(), startDate, endDate);
        if (MapUtils.isNotEmpty(resultMap)) {
          List<ElectricFeeUseInfo> list = new ArrayList<ElectricFeeUseInfo>();
          ElectricFeeUseInfo ele = new ElectricFeeUseInfo();
          ele.setContractCode(resultRentContract.getContractCode());
          ele.setEndDate(endDate);
          ele.setPersonalPrice("0".equals(resultMap.get(4)) ? "" : resultMap.get(4));
          if (StringUtils.isNotEmpty(resultMap.get(4)) && StringUtils.isNotEmpty(resultMap.get(1))) {
            double value = Double.valueOf(resultMap.get(4)) * Double.valueOf(resultMap.get(1));
            ele.setPersonalUseAmount(new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
          } else {
            ele.setPersonalUseAmount("");
          }
          ele.setPersonalUseEle("0".equals(resultMap.get(1)) ? "" : resultMap.get(1));
          ele.setPublicPrice("0".equals(resultMap.get(5)) ? "" : resultMap.get(5));
          if (StringUtils.isNotEmpty(resultMap.get(5)) && StringUtils.isNotEmpty(resultMap.get(2))) {
            double value = Double.valueOf(resultMap.get(5)) * Double.valueOf(resultMap.get(2));
            ele.setPublicUseAmount(new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
          } else {
            ele.setPublicUseAmount("");
          }
          ele.setPublicUseEle("0".equals(resultMap.get(2)) ? "" : resultMap.get(2));
          ele.setRemainedEle("0".equals(resultMap.get(3)) ? "" : resultMap.get(3));
          if (StringUtils.isNotEmpty(resultMap.get(3)) && StringUtils.isNotEmpty(resultMap.get(4))) {
            double value = Double.valueOf(resultMap.get(4)) * Double.valueOf(resultMap.get(3));
            ele.setRemainedEleAmount(new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
          } else {
            ele.setRemainedEleAmount("");
          }
          ele.setStartDate(startDate);
          ele.setReturnValue(resultMap.get(0));
          list.add(ele);
          model.addAttribute("electricFeeUseInfo", electricFeeUseInfo);
          model.addAttribute("list", list);
        }
      }
    }
    return "modules/fee/electricFeeUseInfo";
  }

  // @RequiresPermissions("fee:electricFee:edit")
  @RequestMapping(value = "save")
  public String save(ElectricFee electricFee, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, electricFee)) {
      return form(electricFee, model);
    }
    String rentContractId = electricFee.getRentContractId();
    RentContract rc = rentContractService.get(rentContractId);
    if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rc.getRentMode())) {
      return setValues(model, rc, electricFee.getChargeAmount().intValue(), "整租不能充值电费！");
    } else {
      Room room = roomServie.get(rc.getRoom().getId());
      if (room != null) {
        if (StringUtils.isEmpty(room.getMeterNo())) {
          return setValues(model, rc, electricFee.getChargeAmount().intValue(), "该房间的电表号还没有进行设置，请先设置电表号!");
        }
      }
    }
    electricFeeService.save(electricFee);
    addMessage(redirectAttributes, "电费充值申请成功提交！请等待审核结果。");
    return "redirect:" + Global.getAdminPath() + "/fee/electricFee/?repage";
  }

  // @RequiresPermissions("fee:electricFee:edit")
  @RequestMapping(value = "delete")
  public String delete(ElectricFee electricFee, RedirectAttributes redirectAttributes) {
    electricFeeService.delete(electricFee);
    addMessage(redirectAttributes, "删除电费结算成功");
    return "redirect:" + Global.getAdminPath() + "/fee/electricFee/?repage";
  }

  @RequestMapping(value = "retryFail")
  public String retryFail(ElectricFee electricFee, RedirectAttributes redirectAttributes) {
    ElectricFee fee = electricFeeService.get(electricFee.getId());
    if (fee != null) {
      RentContract rc = rentContractService.get(fee.getRentContractId());
      if (rc != null) {
        String meterNo = roomServie.get(rc.getRoom().getId()).getMeterNo();
        logger.info("begin RetryFail! Call recharge service! electric meter no is:{},chargeAmount is:{}", meterNo, fee.getChargeAmount());
        String id = tradingAccountsService.charge(meterNo, new DecimalFormat("0").format(fee.getChargeAmount()));
        if (StringUtils.isNotBlank(id) && !"0".equals(id)) {
          Pattern pattern = Pattern.compile("[0-9]*");
          Matcher isNum = pattern.matcher(id);
          if (isNum.matches()) {
            fee.setChargeId(id);
            fee.setSettleStatus(FeeSettlementStatusEnum.AUDIT_PASSED.getValue());
            fee.setChargeStatus(ElectricChargeStatusEnum.SUCCESSED.getValue());
            fee.preUpdate();
            electricFeeService.update(fee);
            addMessage(redirectAttributes, "充值成功！");
          } else {
            fee.setSettleStatus(FeeSettlementStatusEnum.AUDIT_REFUSED.getValue());
            fee.setChargeStatus(ElectricChargeStatusEnum.FAILED.getValue());
            fee.preUpdate();
            electricFeeService.update(fee);
            addMessage(redirectAttributes, "充值失败！");
          }
        } else {
          fee.setSettleStatus(FeeSettlementStatusEnum.AUDIT_REFUSED.getValue());
          fee.setChargeStatus(ElectricChargeStatusEnum.FAILED.getValue());
          fee.preUpdate();
          electricFeeService.update(fee);
          addMessage(redirectAttributes, "充值失败！");
        }
      }
    }
    return "redirect:" + Global.getAdminPath() + "/fee/electricFee/?repage";
  }

  @RequestMapping(value = {"postpaidFeeList"})
  public String postpaidFeeList(PostpaidFee postpaidFee, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<PostpaidFee> page = postpaidFeeService.findPage(new Page<PostpaidFee>(request, response), postpaidFee);
    model.addAttribute("page", page);
    return "modules/fee/postpaidFeeList";
  }

  @RequestMapping(value = "postpaidFeeForm")
  public String postpaidFeeForm(PostpaidFee postpaidFee, Model model) {
    model.addAttribute("postpaidFee", postpaidFee);
    return "modules/fee/postpaidFeeForm";
  }

  @RequestMapping(value = "postpaidFeeSave")
  public String postpaidFeeSave(PostpaidFee postpaidFee, Model model) {
    if (!beanValidator(model, postpaidFee)) {
      return postpaidFeeForm(postpaidFee, model);
    }
    postpaidFeeService.saveBusiPostpaidFee(postpaidFee);
    return "redirect:" + Global.getAdminPath() + "/fee/electricFee/postpaidFeeList";
  }

  private String setValues(Model model, RentContract rc, int chargeAmount, String message) {
    model.addAttribute("message", message);
    model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
    model.addAttribute("rentContractId", rc.getId());
    model.addAttribute("contractName", rc.getContractName());
    model.addAttribute("chargeAmount", chargeAmount);
    return "modules/fee/electricFeeForm";
  }

}
