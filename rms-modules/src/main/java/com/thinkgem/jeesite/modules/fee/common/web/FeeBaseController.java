package com.thinkgem.jeesite.modules.fee.common.web;

import com.thinkgem.jeesite.common.RespConstants;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author wangganggang
 * @date 2017年09月28日 下午11:05
 */
public class FeeBaseController extends BaseController {

    @Autowired
    protected SelectItemService selectItemService;

    @Autowired
    protected FeeCommonService feeCommonService;

    @RequestMapping(value = "getSubOrgList")
    public Object getSubOrgList(SelectItemCondition condition) {
        List<SelectItem> selectItems = selectItemService.getSelectListByBusinessCode(condition);
        return ResponseData.success().data(selectItems);
    }

    @RequestMapping(value = "getArea")
    public Object getArea() {
        List<SelectItem> selectItems = feeCommonService.getAreaWithAuth();
        return ResponseData.success().data(selectItems);
    }

    @RequestMapping(value = "houseInfo")
    public Object houseInfo(String accountNum,String type) {
        return ResponseData.success().data(feeCommonService.getHouseByQueryWhereAndType(accountNum, type));
    }

    @RequestMapping(value = "roomInfo")
    public Object roomInfo(String houseId) {
        return ResponseData.success().data(feeCommonService.getRoomByHouseId(houseId));
    }

    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    public Object runtimeException(RuntimeException e){
        return ResponseData.failure(RespConstants.ERROR_CODE_101).message(e.getMessage());
    }
}
