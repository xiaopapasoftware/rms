package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.filter.search.builder.SortBuilder;
import com.thinkgem.jeesite.common.support.MessageSupport;
import com.thinkgem.jeesite.modules.entity.Dict;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.report.service.ReportComponentService;
import com.thinkgem.jeesite.modules.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wangganggang on 17/3/4.
 *
 * @author wangganggang
 * @date 2017/03/04
 */
@Controller
@RequestMapping(value = "${adminPath}/report/component/")
public class ReportComponentController {

    @Autowired
    private ReportComponentService reportComponentService;

    @RequestMapping("dict")
    @ResponseBody
    public Object queryDict(HttpServletRequest request) {
        String type = request.getParameter("type");
        List<Dict> dicts = DictUtils.getDictList(type);
        return MessageSupport.successDataMsg(dicts, "查询成功");
    }

    @RequestMapping("project")
    @ResponseBody
    public Object queryProject() {
        List<Sort> sorts = SortBuilder.create().addAsc("project_name").end();
        List<PropertyProject> propertyProjects = reportComponentService.queryProject(null, sorts);
        return MessageSupport.successDataMsg(propertyProjects, "查询成功");
    }


}
