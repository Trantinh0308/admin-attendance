package com.example.attendance.dto;

// class use show realtime
public class AttendanceDTO2 {
    private String imageCode;
    private String employeeName;
    private String department;
    private String time;

    public AttendanceDTO2() {
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
