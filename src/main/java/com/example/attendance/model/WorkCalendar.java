package com.example.attendance.model;

public class WorkCalendar {
	private String day;
	private String dayOfWeek;

	public WorkCalendar(String day, String dayOfWeek) {
		this.day = day;
		this.dayOfWeek = dayOfWeek;
	}

	public String getDay() {
		return day;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	@Override
	public String toString() {
		return dayOfWeek + ", " + day;  // Ví dụ: "T3, 17"
	}
}