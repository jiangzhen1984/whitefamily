package com.whitefamily.po.incoming;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.whitefamily.po.Shop;

@Entity
@Table(name="WF_OPERATION_SALARY_COST")
public class OperationSalaryCost {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_SHOP_ID", insertable = true, updatable = true, nullable = false)
	private Shop shop;
	
	@Column(name = "WF_DATE", columnDefinition = "date")
	@Type(type="date")
	private Date date;
	
	@Column(name = "WF_DATE_STR", columnDefinition = "VARCHAR(7)")
	private String dateStr;
	
	@Column(name = "WF_DESC", columnDefinition = "VARCHAR(400)")
	private String desc;
	
	@Column(name = "WF_EMPLOYEE_NAME", columnDefinition = "VARCHAR(100)")
	private String employee;
	
	@Column(name = "WF_SALARY", columnDefinition = "NUMERIC(10,3) default 0")
	private float salary;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}
	
	
	

}
