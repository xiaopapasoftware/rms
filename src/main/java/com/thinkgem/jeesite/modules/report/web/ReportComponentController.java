package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.filter.search.MatchType;
import com.thinkgem.jeesite.common.filter.search.PropertyType;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.filter.search.builder.PropertyFilterBuilder;
import com.thinkgem.jeesite.common.filter.search.builder.SortBuilder;
import com.thinkgem.jeesite.common.support.MessageSupport;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.report.service.ReportComponentSrervice;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
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
    private ReportComponentSrervice reportComponentSrervice;

    @RequestMapping("dict")
    @ResponseBody
    public Object queryDict(HttpServletRequest request) {
        PropertyFilterBuilder propertyFilterBuilder = PropertyFilterBuilder.create();
        String type = request.getParameter("type");
        if (StringUtils.isNotBlank(type)) {
            propertyFilterBuilder.matchTye(MatchType.EQ).propertyType(PropertyType.S)
                    .add("type", StringUtils.trimToEmpty(type));
        }
        List<Sort> sorts = SortBuilder.create().addAsc("sort").end();

        List<Dict> dicts = reportComponentSrervice.queryDict(propertyFilterBuilder.end(), sorts);
        return MessageSupport.successDataMsg(dicts, "查询成功");
    }
}
