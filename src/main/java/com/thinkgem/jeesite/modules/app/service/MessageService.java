/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.service;

import java.util.List;

import com.thinkgem.jeesite.modules.app.util.MessagePushUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.dao.MessageDao;

/**
 * 消息Service
 * @author huangsc
 * @version 2015-12-14
 */
@Service
@Transactional(readOnly = true)
public class MessageService extends CrudService<MessageDao, Message> {

	public Message get(String id) {
		return super.get(id);
	}
	
	public List<Message> findList(Message message) {
		return super.findList(message);
	}
	
	public Page<Message> findPage(Page<Message> page, Message message) {
		return super.findPage(page, message);
	}
	
	@Transactional(readOnly = false)
	public void save(Message message) {
		super.save(message);
	}
	
	@Transactional(readOnly = false)
	public void delete(Message message) {
		super.delete(message);
	}

    //添加消息
    public void addMessage(Message message, boolean isPush){
        super.save(message);
        if(isPush){
            push(message);
        }
    }
    public void push(Message message){
        MessagePushUtil.pushAccount(message.getTitle(), message.getContent(), message.getReceiver());
    }
}