/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 常见问题Entity
 * @author daniel
 * @version 2016-05-10
 */
public class Questions extends DataEntity<Questions> {
	
	private static final long serialVersionUID = 1L;
	private String question;		// 问题
	private String answer;		// 回答
	private String type;		// 类型
	private Integer sort;		// 排序
	
	public Questions() {
		super();
	}

	public Questions(String id){
		super(id);
	}

	@Length(min=0, max=2000, message="问题长度必须介于 0 和 2000 之间")
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	@Length(min=0, max=2000, message="回答长度必须介于 0 和 2000 之间")
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	@Length(min=0, max=1, message="类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}