package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.List;

import com.whitefamily.po.incoming.OperationCost;

public class WFOperationCost extends OperationCost {
	
	//饮料
	private float monthlyYl;
	
	//水费
	private float monthlySf;
	
	//电费
	private float monthlyDf;
	
	//房费
	private float monthlyFf;
	
	//燃气费
	private float monthlyRqf;
	
	private List<WFEmployee> employeesCost;
	
	
	public void addEmployeeCost(String name, float salary, float bonus, String desc, float fee) {
		WFEmployee wfe = new WFEmployee(name, salary);
		wfe.setBonus(bonus);
		wfe.setDesc(desc);
		wfe.setDesc1(fee);
		addEmployeeCost(wfe);
	}
	
	
	public void addEmployeeCost(WFEmployee e) {
		if (e == null) {
			return;
		}
		if (employeesCost == null) {
			employeesCost = new ArrayList<WFEmployee>();
		}
		employeesCost.add(e);
	}

	public float getMonthlyYl() {
		return monthlyYl;
	}

	public void setMonthlyYl(float monthlyYl) {
		this.monthlyYl = monthlyYl;
	}

	public float getMonthlySf() {
		return monthlySf;
	}

	public void setMonthlySf(float monthlySf) {
		this.monthlySf = monthlySf;
	}

	public float getMonthlyDf() {
		return monthlyDf;
	}

	public void setMonthlyDf(float monthlyDf) {
		this.monthlyDf = monthlyDf;
	}

	public float getMonthlyFf() {
		return monthlyFf;
	}

	public void setMonthlyFf(float monthlyFf) {
		this.monthlyFf = monthlyFf;
	}

	public float getMonthlyRqf() {
		return monthlyRqf;
	}

	public void setMonthlyRqf(float monthlyRqf) {
		this.monthlyRqf = monthlyRqf;
	}


	public List<WFEmployee> getEmployeesCost() {
		return employeesCost;
	}
	
	public float getSalary() {
		float sum = 0;
		for (int i = 0; employeesCost != null && i < employeesCost.size(); i++) {
			WFEmployee wfe = employeesCost.get(i);
			sum += wfe.getSalary()+ wfe.getBonus()+wfe.getDesc1() ;
		}
		return sum;
	}
	
	
}
