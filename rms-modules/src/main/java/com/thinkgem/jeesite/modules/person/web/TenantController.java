/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.ContractTenantService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.contract.web.CommonBusinessController;
import com.thinkgem.jeesite.modules.entity.User;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.entity.Company;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.CompanyService;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.service.SystemService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "${adminPath}/person/tenant")
public class TenantController extends CommonBusinessController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ContractTenantDao contractTenantDao;

    @Autowired
    private RentContractService rentContractService;

    @Autowired
    private ContractTenantService contractTenantService;

    @ModelAttribute
    public Tenant get(@RequestParam(required = false) String id) {
        Tenant entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tenantService.get(id);
        }
        if (entity == null) {
            entity = new Tenant();
        }
        return entity;
    }

    @RequestMapping(value = {"syncAjaxQuery"})
    @ResponseBody
    public String syncAjaxQuery(String q) {
        List<Tenant> allTenants = new ArrayList<Tenant>();
        Tenant parameterT1 = new Tenant();
        parameterT1.setTenantName(q);
        List<Tenant> tenantListByName = tenantService.findList(parameterT1);
        Tenant parameterT2 = new Tenant();
        parameterT2.setCellPhone(q);
        List<Tenant> tenantListByCellNo = tenantService.findList(parameterT2);
        Tenant parameterT3 = new Tenant();
        parameterT3.setIdNo(q);
        List<Tenant> tenantListByIdNo = tenantService.findList(parameterT3);
        allTenants.addAll(tenantListByName);
        allTenants.addAll(tenantListByCellNo);
        allTenants.addAll(tenantListByIdNo);
        List<HashMap<String, String>> resultMapList = new ArrayList<HashMap<String, String>>();
        if (CollectionUtils.isNotEmpty(allTenants)) {
            for (Tenant t : allTenants) {
                HashMap<String, String> tempMap = new HashMap<String, String>();
                tempMap.put("id", t.getId());
                String text = "";
                String companyName = "";
                if (t.getCompany() != null && StringUtils.isNotEmpty(t.getCompany().getId())) {
                    Company c = companyService.get(t.getCompany().getId());
                    companyName = c.getCompanyName();
                }
                if (StringUtils.isNotEmpty(companyName)) {
                    text = t.getTenantName().concat("-").concat(t.getIdNo()).concat("-").concat(t.getCellPhone()).concat("-").concat(companyName);
                } else {
                    text = t.getTenantName().concat("-").concat(t.getIdNo()).concat("-").concat(t.getCellPhone());
                }
                tempMap.put("text", text);
                resultMapList.add(tempMap);
            }
        }
        return JsonMapper.toJsonString(resultMapList);
    }


    // @RequiresPermissions("person:tenant:view")
    @RequestMapping(value = {"list"})
    public String listQuery(Tenant tenant, HttpServletRequest request, HttpServletResponse response, Model model) {
        buildTenant(tenant);
        Page<Tenant> page = tenantService.findPage(new Page<Tenant>(request, response), tenant);
        model.addAttribute("page", page);
        model.addAttribute("listUser", systemService.findUser(new User()));
        model.addAttribute("listCompany", companyService.findList(new Company()));
        commonInit("projectList", "buildingList", "houseList", "roomList", model, tenant.getPropertyProject(), tenant.getBuilding(), tenant.getHouse());
        return "modules/person/tenantList";
    }

    private void buildTenant(Tenant tenant) {
        if (StringUtils.isNotBlank(tenant.getContractCode()) || StringUtils.isNotBlank(tenant.getContractName())
                || StringUtils.isNotBlank(tenant.getPropertyProject().getId()) || StringUtils.isNotBlank(tenant.getBuilding().getId())
                || StringUtils.isNotBlank(tenant.getHouse().getId()) || StringUtils.isNotBlank(tenant.getRoom().getId())) {
            RentContract rentContract = new RentContract();
            rentContract.setContractCode(tenant.getContractCode());
            rentContract.setContractName(tenant.getContractName());
            rentContract.setPropertyProject(new PropertyProject(tenant.getPropertyProject().getId()));
            rentContract.setBuilding(new Building(tenant.getBuilding().getId()));
            rentContract.setHouse(new House(tenant.getHouse().getId()));
            rentContract.setRoom(new Room(tenant.getRoom().getId()));
            List<RentContract> rentContractList = rentContractService.getByRentContract(rentContract);
            if (CollectionUtils.isNotEmpty(rentContractList)) {
                List<ContractTenant> contractTenantList = contractTenantService.getTenantListByContractIdList(rentContractList.stream().map(RentContract::getId).collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(contractTenantList)) {
                    tenant.setIdList(contractTenantList.stream().map(ContractTenant::getTenantId).collect(Collectors.toList()));
                }
            } else {
                //置空用
                tenant.setIdList(Collections.singletonList("test"));
            }
        }
    }

    @RequestMapping(value = {""})
    public String listNoQuery(Tenant tenant, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("listUser", systemService.findUser(new User()));
        model.addAttribute("listCompany", companyService.findList(new Company()));
        model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
        return "modules/person/tenantList";
    }

    // @RequiresPermissions("person:tenant:view")
    @RequestMapping(value = "form")
    public String form(Tenant tenant, Model model) {
        model.addAttribute("tenant", tenant);
        model.addAttribute("listUser", systemService.findUser(new User()));
        model.addAttribute("listCompany", companyService.findList(new Company()));
        return "modules/person/tenantForm";
    }

    @RequestMapping(value = "add")
    public String add(Tenant tenant, Model model) {
        model.addAttribute("tenant", tenant);
        model.addAttribute("listUser", systemService.findUser(new User()));
        model.addAttribute("listCompany", companyService.findList(new Company()));
        model.addAttribute("type", tenant.getType());
        return "modules/person/tenantAddDialog";
    }

    // @RequiresPermissions("person:tenant:edit")
    @RequestMapping(value = "save")
    public String save(Tenant tenant, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, tenant)) {
            return form(tenant, model);
        }
        List<Tenant> tenants = tenantService.findTenantByIdTypeAndNo(tenant);
        if (!tenant.getIsNewRecord()) {// 是更新
            if (CollectionUtils.isNotEmpty(tenants)) {
                tenant.setId(tenants.get(0).getId());
            }
            tenantService.save(tenant);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改租客信息成功");
            return "redirect:" + Global.getAdminPath() + "/person/tenant/?repage";
        } else {// 是新增
            if (CollectionUtils.isNotEmpty(tenants)) {
                addMessage(model, ViewMessageTypeEnum.WARNING, "该证件类型租客的证件号码或手机号已被占用，不能重复添加");
                model.addAttribute("listCompany", companyService.findList(new Company()));
                model.addAttribute("listUser", systemService.findUser(new User()));
                return "modules/person/tenantForm";
            } else {
                tenantService.save(tenant);
                addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存租客信息成功");
                return "redirect:" + Global.getAdminPath() + "/person/tenant/?repage";
            }
        }
    }

    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(Tenant tenant, Model model, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = new JSONObject();
        List<Tenant> tenants = tenantService.findTenantByIdTypeAndNo(tenant);
        if (CollectionUtils.isNotEmpty(tenants)) {
            model.addAttribute("listCompany", companyService.findList(new Company()));
            model.addAttribute("listUser", systemService.findUser(new User()));
            addMessage(jsonObject, ViewMessageTypeEnum.WARNING, "该证件类型租客的证件号码或手机号已被占用，不能重复添加!");
        } else {
            tenantService.save(tenant);
            jsonObject.put("id", tenant.getId());
            String companyName = "";
            if (tenant.getCompany() != null && StringUtils.isNotEmpty(tenant.getCompany().getId())) {
                Company c = companyService.get(tenant.getCompany().getId());
                companyName = c.getCompanyName();
            }
            if (StringUtils.isNotEmpty(companyName)) {
                jsonObject.put("name", tenant.getLabel().concat("-").concat(companyName));
            } else {
                jsonObject.put("name", tenant.getLabel());
            }
        }
        return jsonObject.toString();
    }

    // @RequiresPermissions("person:tenant:edit")
    @RequestMapping(value = "delete")
    public String delete(Tenant tenant, RedirectAttributes redirectAttributes) {
        ContractTenant ct = new ContractTenant();
        ct.setTenantId(tenant.getId());
        List<ContractTenant> cts = contractTenantDao.findList(ct);
        if (CollectionUtils.isNotEmpty(cts)) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "租客已预付定金或生成合同，不能删除");
        } else {
            tenantService.delete(tenant);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除租客信息成功");
        }
        return "redirect:" + Global.getAdminPath() + "/person/tenant/?repage";
    }

}
