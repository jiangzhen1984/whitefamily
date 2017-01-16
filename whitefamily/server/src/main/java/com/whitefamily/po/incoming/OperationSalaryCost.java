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
	
	@Column(name = "WF_BONUS", columnDefinition = "NUMERIC(10,3) default 0")
	private float bonus;
	
	@Column(name = "WF_FEE", columnDefinition = "NUMERIC(10,3) default 0")
	private float fee;
	
	@Column(name = "WF_SENIORITY_ALLOWANCE", columnDefinition = "NUMERIC(10,3) default 0")
	private float seniorityAllowance;
	
	@Column(name = "WF_PERFECT_ATTENDENCE", columnDefinition = "NUMERIC(10,3) default 0")
	private float perfectAttendence;
	
	@Column(name = "WF_COMPENSATION", columnDefinition = "NUMERIC(10,3) default 0")
	private float compensation;
	
	@Column(name = "WF_ABSENCE", columnDefinition = "NUMERIC(10,3) default 0")
	private float absence;
	
	@Column(name = "WF_ILLNESS", columnDefinition = "NUMERIC(10,3) default 0")
	private float illness;
	
	@Column(name = "WF_DEPOSIT", columnDefinition = "NUMERIC(10,3) default 0")
	private float deposit;
	
	@Column(name = "WF_FINE", columnDefinition = "NUMERIC(10,3) default 0")
	private float fine;
	
	@Column(name = "WF_REAL", columnDefinition = "NUMERIC(10,3) default 0")
	private float real;

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

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}

	public float getFee() {
		return fee;
	}

	public void setFee(float fee) {
		this.fee = fee;
	}

	public float getSeniorityAllowance() {
		return seniorityAllowance;
	}

	public void setSeniorityAllowance(float seniorityAllowance) {
		this.seniorityAllowance = seniorityAllowance;
	}

	public float getPerfectAttendence() {
		return perfectAttendence;
	}

	public void setPerfectAttendence(float perfectAttendence) {
		this.perfectAttendence = perfectAttendence;
	}

	public float getCompensation() {
		return compensation;
	}

	public void setCompensation(float compensation) {
		this.compensation = compensation;
	}

	public float getAbsence() {
		return absence;
	}

	public void setAbsence(float absence) {
		this.absence = absence;
	}

	public float getIllness() {
		return illness;
	}

	public void setIllness(float illness) {
		this.illness = illness;
	}

	public float getDeposit() {
		return deposit;
	}

	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}

	public float getFine() {
		return fine;
	}

	public void setFine(float fine) {
		this.fine = fine;
	}

	public float getReal() {
		return real;
	}

	public void setReal(float real) {
		this.real = real;
	}
	
	
	

}
