package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

import java.util.List;

/**
 * Created by wangganggang on 17/3/4.
 *
 * @author wangganggang
 * @date 2017/03/04
 */
@MyBatisDao
public interface ReportComponentDao {

    List<Dict> queryDict(Criterion criterion);

    List<PropertyProject> queryProject(Criterion criterion);

}
