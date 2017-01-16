package com.whitefamily.service.vo;

public class WFEmployee extends WFUser {

	private float salary;
	
	private float bonus;
	
	private String desc;
	
	private float desc1;
	
	
	private float fee;
	
	private float seniorityAllowance;
	
	private float perfectAttendence;
	
	private float compensation;
	
	private float absence;
	
	private float illness;
	
	private float deposit;
	
	private float fine;
	
	private float real;
	
	
	public WFEmployee() {
		
	}
	
	public WFEmployee(String name, float salary) {
		this.name = name;
		this.salary = salary;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getDesc1() {
		return desc1;
	}

	public void setDesc1(float desc1) {
		this.desc1 = desc1;
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
