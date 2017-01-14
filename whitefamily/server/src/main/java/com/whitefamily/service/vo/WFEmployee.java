package com.whitefamily.service.vo;

public class WFEmployee extends WFUser {

	private float salary;
	
	private float bonus;
	
	private String desc;
	
	private float desc1;
	
	
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
	
	
	
	
	
}
