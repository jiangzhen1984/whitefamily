package com.whitefamily.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WF_BRAND")
public class Brand {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	
	@Column(name="WF_BR_NAME", columnDefinition="VARCHAR(500)")
	protected String name;
	
	@Column(name="WF_BR_STYLE", columnDefinition="VARCHAR(500)")
	protected String style;
	
	@Column(name="WF_BR_UNIT", columnDefinition="VARCHAR(100)")
	protected String unit;

	
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


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;
	}



	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


}
