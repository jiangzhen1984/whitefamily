package com.whitefamily.po.delivery;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.whitefamily.po.Record;
import com.whitefamily.po.customer.User;


@Entity
@Table(name = "WF_INTERNAL_DELIVERY_RECORD")
public class InternalDeliveryRecord extends Record {
	
	@Column(name = "WF_OPER_ID")
	private Long userId;

	@Column(name = "WF_USER_NAME", columnDefinition = "VARCHAR(100)")
	private String userName;

	@Transient
	protected InternalDeliveryRecord parent;
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Override
	public long getId() {
		return super.getId();
	}

	@Override
	public void setId(long id) {
		super.setId(id);
	}

	@Column(name = "WF_OPT_TIMESTAMP", columnDefinition = "timestamp")
	@Override
	public Date getDatetime() {
		return super.getDatetime();
	}

	@Override
	public void setDatetime(Date datetime) {
		super.setDatetime(datetime);
	}

	@Override
	@Transient
	public User getOperator() {
		if (operator == null) {
			operator = new User();
			operator.setId(this.userId);
			operator.setUsername(this.userName);
		}
		return super.getOperator();
	}

	@Override
	public void setOperator(User operator) {
		super.setOperator(operator);
		if (operator != null) {
			this.userId = operator.getId();
			this.userName = operator.getName();
		}
	}
	
	
	@Override
	@Column(name = "WF_PARENT_REC_ID")
	public long getParentId() {
		return super.getParentId();
	}

	@Override
	public void setParentId(Long parentId) {
		super.setParentId(parentId);
	}
	
	@Transient
	public InternalDeliveryRecord getParent() {
		return parent;
	}
}
