package com.example.demo.entity;


import jakarta.persistence.*;
@Entity
@DiscriminatorValue("USER")
//标识实体类和鉴别列即可

public class User extends Person{

    @Column(name = "username")
    private String username;

    protected User() {
    }

    public User(String password,String username,String realname, String gender, int age, String phoneNumber, String address) {
        super(password,realname, gender, age, phoneNumber, address);
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return username;
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


    
}

