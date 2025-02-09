package com.example.attendance.dto;

public class EmployeeDTO {
    private String fullName;
    private String gender;
    private String phoneNumber;
    private String department;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String fullName, String gender, String phoneNumber, String department) {
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.department = department;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
