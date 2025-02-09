package com.example.attendance.model;

import java.util.ArrayList;
import java.util.List;

public class Timekeeping {
	private Employee employee;
	private List<Double> listTimekeeping;
	private double sum;
	
	public Timekeeping() {
		super();
	}

	public Timekeeping(Employee employee) {
		super();
		listTimekeeping = new ArrayList<>();
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Double> getListTimekeeping() {
		return listTimekeeping;
	}

	public void setListTimekeeping(List<Double> listTimekeeping) {
		this.listTimekeeping = listTimekeeping;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(List<Double>list) {
		double k = 0;
		for(int i=0;i<list.size();i++) {
			k+=list.get(i);
		}
		this.sum = k;
	}

	@Override
	public String toString() {
		return "Timekeeping [employee=" + employee + ", listTimekeeping=" + listTimekeeping + ", sum=" + sum + "]";
	}
}
