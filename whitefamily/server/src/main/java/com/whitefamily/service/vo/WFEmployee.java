package com.whitefamily.service.vo;

public class WFEmployee extends WFUser {

	private float salary;
	
	
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
	
	
	
}
