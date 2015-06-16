/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 居委会Entity
 * 
 * @author huangsc
 * @version 2015-06-03
 */
public class Neighborhood extends DataEntity<Neighborhood> {

	private static final long serialVersionUID = 1L;
	private String neighborhoodName; // 居委会名称
	private String neighborhoodAddr; // 居委会地址

	public Neighborhood() {
		super();
	}

	public Neighborhood(String id) {
		super(id);
	}

	@Length(min = 1, max = 100, message = "居委会名称长度必须介于 1 和 100 之间")
	public String getNeighborhoodName() {
		return neighborhoodName;
	}

	public void setNeighborhoodName(String neighborhoodName) {
		this.neighborhoodName = neighborhoodName;
	}

	@Length(min = 1, max = 100, message = "居委会地址长度必须介于 1 和 100 之间")
	public String getNeighborhoodAddr() {
		return neighborhoodAddr;
	}

	public void setNeighborhoodAddr(String neighborhoodAddr) {
		this.neighborhoodAddr = neighborhoodAddr;
	}

}