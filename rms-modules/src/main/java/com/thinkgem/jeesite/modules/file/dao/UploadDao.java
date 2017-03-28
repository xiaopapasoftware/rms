package com.thinkgem.jeesite.modules.file.dao;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * @author wangganggang
 * @date 2017/03/26
 */
@MyBatisDao
public interface UploadDao {

    List<Map> queryAttachment(Criterion criterion);

}
