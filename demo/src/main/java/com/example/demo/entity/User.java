package com.example.demo.entity;


import jakarta.persistence.*;
@Entity
@DiscriminatorValue("USER")
//标识实体类和鉴别列即可

public class User extends Person{

    protected User() {
    }

    public User(String username, String realname, String gender, int age, String phoneNumber, String address) {
        super(username, realname, gender, age, phoneNumber, address);
    }

    public User(String username, String gender, String phonenumber,String password){
        this.setUsername(username);
        this.setGender(gender);
        this.setPassword(password);
        this.setPhoneNumber(phonenumber);
    }

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
    public Long getId(){
        return super.getId();
    }

    public String getPassword() {
        return super.getPassword();
    }
    
}

