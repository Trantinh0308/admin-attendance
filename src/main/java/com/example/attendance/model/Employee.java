package com.example.attendance.model;

import jakarta.persistence.*;

@Entity
@Table(name="employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    @Column(name = "name")
    private String fullName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "department")
    private String department;

    public Employee() {
    }

    public Employee(Long id, Account account, String fullName, String gender,
                    String phoneNumber, String department) {
        this.id = id;
        this.account = account;
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
