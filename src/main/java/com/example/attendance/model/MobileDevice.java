package com.example.attendance.model;

import jakarta.persistence.*;

@Entity
@Table(name="mobile_device")
public class MobileDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "mobile_id")
    private String mobileId;

    public MobileDevice() {
    }
    public MobileDevice(Long id, Account account, String mobileId) {
        this.id = id;
        this.account = account;
        this.mobileId = mobileId;
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

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }
}
