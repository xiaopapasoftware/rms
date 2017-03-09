/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import com.thinkgem.jeesite.modules.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 管家投拆Entity
 * @author daniel
 * @version 2016-07-07
 */
public class ServiceUserComplain extends DataEntity<ServiceUserComplain> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 投拆人
    private String userPhone;		// 投拆人
    private String userName;		// 投拆人
	private User serviceUser;		// 被投拆人,服务管家
	private String content;		// 投拆内容
	
	public ServiceUserComplain() {
		super();
	}

	public ServiceUserComplain(String id){
		super(id);
	}

	@Length(min=0, max=64, message="投拆人长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public User getServiceUser() {
		return serviceUser;
	}

	public void setServiceUser(User serviceUser) {
		this.serviceUser = serviceUser;
	}
	
	@Length(min=0, max=500, message="投拆内容长度必须介于 0 和 500 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}