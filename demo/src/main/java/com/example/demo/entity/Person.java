package com.example.demo.entity;


import jakarta.persistence.*;



@Entity   //JPA注解，表示这是一个实体类
@Table(name = "person")  //指定对应数据库表名为person
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING) 
//指定鉴别列的名称为person_type，类型为字符串，用于区分不同的子类


public abstract class Person {
    
    
    @Id //JPA注解，表示这是实体类的主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键生成策略，使用数据库自增
    //使用数据库的自增功能来生成主键值
    private Long id; //主键字段，类型为Long


    @Column(name="password",nullable = false,length = 255) //指定对应数据库表中的列名为password
    private String password; //密码字段，类型为String，不能为空，长度最大为255

    @Column(name = "username",nullable = false,length=255)
    private String username; //用户名字段，类型为String，不能为空，长度最大为255
    
    @Column(name = "realname",nullable=true) //指定对应数据库表中的列名为name
    private String realname; //姓名字段，类型为String


    @Column(name = "gender") //指定对应数据库表中的列名为gender
    private String gender; //性别字段，类型为String


    @Column(name = "age") //指定对应数据库表中的列名为age
    private int age; //年龄字段，类型为int


    @Column(name = "phone_number",nullable = false,unique=true,length=20) //指定对应数据库表中的列名为phone_number
    //指定不能为空,也不能重复
    private String phoneNumber; //电话号码字段，类型为String


    @Column(name = "address",nullable=true,length = 255) //指定对应数据库表中的列名为address
    private String address; //地址字段，类型为String    


    public enum Gender {
        MALE,FEMALE
    }

    protected Person() {
    }

    public Person( String username, String realname, String gender, int age, String phoneNumber, String address) {
        this.username = username;
        this.realname = realname;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }



}
