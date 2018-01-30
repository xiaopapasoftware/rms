package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.enums.LeaseContractAuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.service.AuditHisService;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.inventory.service.HouseOwnerService;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.entity.Remittancer;
import com.thinkgem.jeesite.modules.person.service.OwnerService;
import com.thinkgem.jeesite.modules.person.service.RemittancerService;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 承租合同
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/leaseContract")
public class LeaseContractController extends CommonBusinessController {
    @Autowired
    private LeaseContractService leaseContractService;
    @Autowired
    private RemittancerService remittancerService;
    @Autowired
    private AuditHisService auditHisService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private HouseOwnerService houseOwnerService;

    @ModelAttribute
    public LeaseContract get(@RequestParam(required = false) String id) {
        LeaseContract entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = leaseContractService.get(id);
        }
        if (entity == null) {
            entity = new LeaseContract();
        }
        return entity;
    }

    @RequiresPermissions("contract:leaseContract:view")
    @RequestMapping(value = {"list"})
    public String listQuery(LeaseContract leaseContract, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<LeaseContract> page = leaseContractService.findPage(new Page<>(request, response), leaseContract);
        page.getList().forEach(contract -> {
            contract.setOwnerList(ownerService.findByHouse(contract.getHouse()));
        });
        model.addAttribute("page", page);
        commonInit("projectList", "buildingList", "houseList", "roomList", model, leaseContract.getPropertyProject(), leaseContract.getBuilding(), null);
        return "modules/contract/leaseContractList";
    }

    @RequiresPermissions("contract:leaseContract:view")
    @RequestMapping(value = {""})
    public String listNoQuery(LeaseContract leaseContract, Model model) {
        commonInit("projectList", "buildingList", "houseList", "roomList", model, leaseContract.getPropertyProject(), leaseContract.getBuilding(), null);
        return "modules/contract/leaseContractList";
    }

    @RequestMapping(value = "audit")
    public String audit(AuditHis auditHis, RedirectAttributes redirectAttributes) {
        leaseContractService.audit(auditHis);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "操作成功！");
        return "redirect:" + Global.getAdminPath() + "/contract/leaseContract/?repage";
    }

    @RequestMapping(value = "auditHis")
    public String auditHis(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<AuditHis> page = auditHisService.findPage(new Page<>(request, response), auditHis);
        model.addAttribute("page", page);
        return "modules/contract/auditHis";
    }

    @RequiresPermissions("contract:leaseContract:view")
    @RequestMapping(value = "form")
    public String form(LeaseContract leaseContract, Model model) {
        model.addAttribute("leaseContract", leaseContract);
        if (leaseContract.getIsNewRecord()) {
            leaseContract.setContractCode((leaseContractService.getTotalValidLeaseContractCounts() + 1) + "-" + "SF");
        }
        commonInit("projectList", "buildingList", "houseList", "roomList", model, leaseContract.getPropertyProject(), leaseContract.getBuilding(), null);
        model.addAttribute("remittancerList", remittancerService.findList(new Remittancer()));
        leaseContract.setOwnerList(ownerService.findByHouse(leaseContract.getHouse()));
        return "modules/contract/leaseContractForm";
    }

    @RequiresPermissions("contract:leaseContract:edit")
    @RequestMapping(value = "save")
    public String save(LeaseContract leaseContract, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, leaseContract)) {
            return form(leaseContract, model);
        }
        LeaseContract tmpLeaseContract = new LeaseContract();
        tmpLeaseContract.setPropertyProject(leaseContract.getPropertyProject());
        tmpLeaseContract.setBuilding(leaseContract.getBuilding());
        tmpLeaseContract.setHouse(leaseContract.getHouse());
        List<LeaseContract> list = leaseContractService.findList(tmpLeaseContract);
        if (CollectionUtils.isNotEmpty(list)) {
            tmpLeaseContract = list.get(0);
            if (LeaseContractAuditStatusEnum.TO_BE_AUDIT.getValue().equals(tmpLeaseContract.getContractStatus())
                    || LeaseContractAuditStatusEnum.PASS.getValue().equals(tmpLeaseContract.getContractStatus())) {
                if ("add".equals(leaseContract.getType())) {
                    addMessage(model, ViewMessageTypeEnum.ERROR, "该房屋已签承租合同！");
                    return form(leaseContract, model);
                } else {
                    if (!leaseContract.getPropertyProject().getId().equals(tmpLeaseContract.getPropertyProject().getId()) || !leaseContract.getBuilding().getId().equals(tmpLeaseContract.getBuilding().getId())
                            || !leaseContract.getHouse().getId().equals(tmpLeaseContract.getHouse().getId())) {
                        addMessage(model, ViewMessageTypeEnum.ERROR, "该房屋已签承租合同！");
                        return form(leaseContract, model);
                    }
                }
            }
        }
        if (leaseContract.getIsNewRecord()) {
            String[] codeArr = leaseContract.getContractCode().split("-");
            leaseContract.setContractCode(codeArr[0] + "-" + (leaseContractService.getTotalValidLeaseContractCounts() + 1) + "-" + "SF");
        }
        leaseContractService.save(leaseContract);
        houseOwnerService.processHouseAndOwner(leaseContract.getHouse().getId(), leaseContract.getOwnerList());// 房屋业主关系信息
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存承租合同成功！");
        return "redirect:" + Global.getAdminPath() + "/contract/leaseContract/?repage";
    }

    @RequiresPermissions("contract:leaseContract:edit")
    @RequestMapping(value = "delete")
    public String delete(LeaseContract leaseContract, RedirectAttributes redirectAttributes) {
        leaseContractService.delete(leaseContract);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除承租合同成功");
        return "redirect:" + Global.getAdminPath() + "/contract/leaseContract/?repage";
    }

    @RequestMapping(value = "ajaxQueryOwners")
    @ResponseBody
    public String ajaxQueryOwners(String id) {
        if (StringUtils.isNotBlank(id)) {
            List<Owner> ownerList = ownerService.findByHouse(leaseContractService.get(id).getHouse());
            if (CollectionUtils.isNotEmpty(ownerList)) {
                return getSelectOptionResult(ownerList);
            }
        }
        return "";
    }

    private String getSelectOptionResult(List<Owner> ownerList) {
        StringBuffer optionResultBuf = new StringBuffer();
        if (CollectionUtils.isNotEmpty(ownerList)) {
            ownerList.forEach(owner -> optionResultBuf.append("<option selected value='").append(owner.getId()).append("'>").append(owner.getName()).append("-").append(owner.getSocialNumber()).append("-")
                    .append(owner.getCellPhone()).append(StringUtils.isEmpty(owner.getSecondCellPhone()) ? "" : "/" + owner.getSecondCellPhone()).append("</option>"));
        }
        return optionResultBuf.toString();
    }

}
