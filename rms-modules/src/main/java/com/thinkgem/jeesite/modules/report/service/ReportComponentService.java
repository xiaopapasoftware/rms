package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.utils.MapKeyHandle;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.entity.Dict;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.dao.ReportComponentDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangganggang on 17/3/4.
 *
 * @author wangganggang
 * @date 2017/03/04
 */
@Service
public class ReportComponentService {

    @Autowired
    private ReportComponentDao reportComponentDao;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Autowired
    private RoomService roomService;

    public List<Dict> queryDict(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return reportComponentDao.queryDict(new Criterion(propertyFilters, sorts));
    }

    public List<PropertyProject> queryProject(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return propertyProjectService.findList(new PropertyProject());
    }

    public List<Map> queryTenant(Map map) {
        return reportComponentDao.queryTenant(map);
    }


    /**
     * 填充合同日期
     **/
    public List<Map> fillTenantInfo(List<Map> maps) {
        return maps.parallelStream().map(map -> {
            List<Map> tenants = queryTenant(map);
            final StringBuffer cellPhone = new StringBuffer();
            final StringBuffer tenantName = new StringBuffer();
            final StringBuffer tenantIdNo = new StringBuffer();
            final StringBuffer tenantNameLead = new StringBuffer();
            final StringBuffer cellPhoneLead = new StringBuffer();
            if (tenants != null && tenants.size() > 0) {
                Map finalMap = map;
                tenants.forEach(t -> {
                    if (StringUtils.equals(MapUtils.getString(t, "contract_id"), MapUtils.getString(finalMap, "contract_id"))) {
                        cellPhone.append(MapUtils.getString(t, "cell_phone")).append(";");
                        tenantName.append(MapUtils.getString(t, "tenant_name")).append(";");
                        tenantIdNo.append(MapUtils.getString(t, "id_no")).append(";");
                    }
                    if (StringUtils.equals(MapUtils.getString(t, "lease_contract_id"), MapUtils.getString(finalMap, "contract_id"))) {
                        tenantNameLead.append(MapUtils.getString(t, "tenant_name")).append(";");
                        cellPhoneLead.append(MapUtils.getString(t, "cell_phone")).append(";");
                    }
                });
            }
            map.put("tenant_name", StringUtils.substringBeforeLast(tenantName.toString(), ";"));
            map.put("cell_phone", StringUtils.substringBeforeLast(cellPhone.toString(), ";"));
            map.put("id_no", StringUtils.substringBeforeLast(tenantIdNo.toString(), ";"));
            map.put("tenant_name_lead", StringUtils.substringBeforeLast(tenantNameLead.toString(), ";"));
            map.put("cell_phone_lead", StringUtils.substringBeforeLast(cellPhoneLead.toString(), ";"));
            if (RentModelTypeEnum.JOINT_RENT.getValue().equals(MapUtils.getString(map, "rent_mode"))) {
                map.put("room_num", 1);
            } else {
                map.put("room_num", roomService.findRoomListByHouseId(MapUtils.getString(map, "house_id")).size());
            }
            return MapKeyHandle.keyToJavaProperty(map);
        }).collect(Collectors.toList());
    }
}
