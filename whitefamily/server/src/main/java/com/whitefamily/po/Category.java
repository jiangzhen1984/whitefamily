package com.whitefamily.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WF_CATEGORY")
public class Category {
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	@Column(name="WF_CTE_NAME", columnDefinition="VARCHAR(500)")
	protected String name;
	
	
	@Column(name="WF_CTE_LEVEL", columnDefinition="smallint")
	protected int level;
	
	@Column(name="WF_CTE_PARENT_ID", columnDefinition="bigint")
	protected long parentId;
	
	@Column(name="WF_CTE_TYPE", columnDefinition="tinyint")
	protected int type;
	
	@Column(name="WF_CTE_ORDER", columnDefinition="smallint default 0")
	protected int order;
	
	

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	
	
	
}
