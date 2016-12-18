package com.whitefamily.po.artifact;

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
@Table(name = "WF_ARTI_STAFF")
public class ArtifactStaff {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Enumerated(EnumType.ORDINAL)
	private ArtifactStaffType type;
	
	@Column(name = "WF_PRO_ID")
	private long productId;
	
	@Column(name = "WF_UNIT", columnDefinition = "NUMERIC(10,3)")
	protected float unit;
	
	@Column(name = "WF_MINAL_PRODUCE_UNIT", columnDefinition = "VARCHAR(200)")
	protected String minalProduceUnit;
	
	
	@Column(name = "WF_STYLE", columnDefinition = "VARCHAR(200)")
	protected String style;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_ARTI_ID", insertable = true, updatable = true, nullable = false)
	protected ArtifactProduct artifact;
	
	@Column(name = "WF_DESC", columnDefinition = "VARCHAR(400)")
	protected String desc;
	
	
	

	public ArtifactStaff() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArtifactStaffType getType() {
		return type;
	}

	public void setType(ArtifactStaffType type) {
		this.type = type;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public ArtifactProduct getArtifact() {
		return artifact;
	}

	public void setArtifact(ArtifactProduct artifact) {
		this.artifact = artifact;
	}

	public float getUnit() {
		return unit;
	}

	public void setUnit(float unit) {
		this.unit = unit;
	}

	public ArtifactStaff(ArtifactStaffType type, long productId) {
		super();
		this.type = type;
		this.productId = productId;
	}

	public String getMinalProduceUnit() {
		return minalProduceUnit;
	}

	public void setMinalProduceUnit(String minalProduceUnit) {
		this.minalProduceUnit = minalProduceUnit;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
	
}
