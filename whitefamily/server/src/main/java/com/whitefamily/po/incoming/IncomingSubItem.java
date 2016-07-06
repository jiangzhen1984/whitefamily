package com.whitefamily.po.incoming;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="WF_INCOMING_SUB_ITEM")
public class IncomingSubItem {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="WF_INCMOING_NAME", columnDefinition="VARCHAR(500)")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_INCOMING_ITEM_ID", insertable = true, updatable = true, nullable = false)
	protected IncomingItem parent;
	
	
	@Enumerated(EnumType.ORDINAL)
	private IncomingOperator operator;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public IncomingItem getParent() {
		return parent;
	}


	public void setParent(IncomingItem parent) {
		this.parent = parent;
	}


	public IncomingOperator getOperator() {
		return operator;
	}


	public void setOperator(IncomingOperator operator) {
		this.operator = operator;
	}
	
	
	
	
	

}
