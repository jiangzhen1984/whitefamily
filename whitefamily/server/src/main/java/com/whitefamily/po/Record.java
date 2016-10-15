package com.whitefamily.po;

import java.util.Date;

import com.whitefamily.po.customer.User;

public abstract class Record {
	
	protected long id;
	
	protected Date datetime;
	
	
	protected User operator;
	
	protected Long parentId;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Date getDatetime() {
		return datetime;
	}


	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}


	public User getOperator() {
		return operator;
	}


	public void setOperator(User operator) {
		this.operator = operator;
	}


	public long getParentId() {
		return parentId == null? 0 : parentId;
	}


	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	

}
