package com.whitefamily.po.artifact;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "WF_ARTI_PRODUCT")
public class ArtifactProduct {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToMany
	@JoinColumn(name="WF_ARTI_ID")
	protected Set<ArtifactStaff> staffs;
	
	

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Set<ArtifactStaff> getStaffs() {
		return staffs;
	}


	public void setStaffs(Set<ArtifactStaff> staffs) {
		this.staffs = staffs;
	}
	
	
	public void addStaffs(ArtifactStaff as) {
		if (this.staffs == null) {
			this.staffs = new HashSet<ArtifactStaff>();
		}
		this.staffs.add(as);
	}
	
	
}
