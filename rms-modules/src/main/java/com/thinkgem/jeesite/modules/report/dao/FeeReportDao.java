/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.FeeReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用报名DAO接口
 * 
 * @author xiao
 */
@MyBatisDao
public interface FeeReportDao extends CrudDao<FeeReport> {

    List<FeeReport> getFeeReportByRoomIdList(@Param("roomIdList")List<String> roomIdList, @Param("feeType")String feeType);

    public List<FeeReport> getFeeReportList(@Param("size")int size);

}
