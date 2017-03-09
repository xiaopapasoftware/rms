/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.company.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.company.entity.Document;
import com.thinkgem.jeesite.modules.company.dao.DocumentDao;

/**
 * 办公文件管理Service
 * @author huangsc
 * @version 2015-09-12
 */
@Service
@Transactional(readOnly = true)
public class DocumentService extends CrudService<DocumentDao, Document> {

	public Document get(String id) {
		return super.get(id);
	}
	
	public List<Document> findList(Document document) {
		return super.findList(document);
	}
	
	public Page<Document> findPage(Page<Document> page, Document document) {
		return super.findPage(page, document);
	}
	
	@Transactional(readOnly = false)
	public void save(Document document) {
		super.save(document);
	}
	
	@Transactional(readOnly = false)
	public void delete(Document document) {
		super.delete(document);
	}
	
}