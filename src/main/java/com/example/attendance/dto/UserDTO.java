package com.example.attendance.dto;

public class UserDTO {
    String username;
    String password;
    String mobileId;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String mobileId) {
        this.username = username;
        this.password = password;
        this.mobileId = mobileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }
}
