package com.example.attendance.dto;

import java.sql.Date;
import java.sql.Time;
// class use get history
public class AttendanceDTO3 {
    private String employeeImage;
    private Long employeeCode;
    private String employeeName;
    private String departmentName;
    private Date attendanceDate;
    private Time attendanceTime;
    private String status;
    private String deviceName;
    public AttendanceDTO3() {}

    public AttendanceDTO3(String employeeImage, Long employeeCode, String employeeName,
                          String departmentName, Date attendanceDate, Time attendanceTime,
                          String status, String deviceName) {
        this.employeeImage = employeeImage;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.attendanceDate = attendanceDate;
        this.attendanceTime = attendanceTime;
        this.status = status;
        this.deviceName = deviceName;
    }

    public String getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        this.employeeImage = employeeImage;
    }

    public Long getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(Long employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Time getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(Time attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
