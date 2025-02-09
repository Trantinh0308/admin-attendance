package com.example.attendance.dto;

import java.sql.Date;
import java.sql.Time;

//class use send request
public class AttendanceDTO {
    private Long employeeId;
    private String attendanceDevice;
    private String imageCode;
    private Date date;
    private Time time;

    public AttendanceDTO() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getAttendanceDevice() {
        return attendanceDevice;
    }

    public void setAttendanceDevice(String attendanceDevice) {
        this.attendanceDevice = attendanceDevice;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
