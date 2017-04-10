package com.thinkgem.jeesite.modules.fee.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.entity.PostpaidFee;

/**
 * 后付费公共事业费DAO接口
 * 
 * @author wangshujin
 */
@MyBatisDao
public interface PostpaidFeeDao extends CrudDao<PostpaidFee> {

}
