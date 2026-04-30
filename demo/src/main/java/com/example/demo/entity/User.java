package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("USER")
public class User extends Person {

    @Column(name = "last_upload_date")
    private LocalDate lastUploadDate;

    @Column(name = "daily_upload_count")
    private Integer dailyUploadCount = 0;

    protected User() {
    }

    public User(String username, String realname, String gender, int age, String phoneNumber, String address) {
        super(username, realname, gender, age, phoneNumber, address);
    }

    public User(String username, String gender, String phonenumber, String password) {
        this.setUsername(username);
        this.setGender(gender);
        this.setPassword(password);
        this.setPhoneNumber(phonenumber);
    }

    // ========== 新增 getter/setter ==========
    public LocalDate getLastUploadDate() {
        return lastUploadDate;
    }

    public void setLastUploadDate(LocalDate lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    public Integer getDailyUploadCount() {
        return dailyUploadCount;
    }

    public void setDailyUploadCount(Integer dailyUploadCount) {
        this.dailyUploadCount = dailyUploadCount;
    }

    // 辅助方法：检查是否是 ADMIN
    public boolean isAdmin() {
        return this instanceof Admin;
    }

    // 原有方法...
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public void setRealname(String realname) {
        super.setRealname(realname);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public void setAddress(String address) {
        super.setAddress(address);
    }

    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public String getPassword() {
        return super.getPassword();
    }
}