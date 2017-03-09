/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.device.dao.DevicesHisDao;
import com.thinkgem.jeesite.modules.device.entity.DevicesHis;

/**
 * 设备变更信息Service
 * 
 * @author huangsc
 * @version 2015-06-28
 */
@Service
@Transactional(readOnly = true)
public class DevicesHisService extends CrudService<DevicesHisDao, DevicesHis> {

}