package com.example.demo.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "cat")


public class Cat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false,length = 255)
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "type",length = 255)
    private String type;

    @Column(name="health")
    private String health;

    @Column(name = "jueyu",length = 255)
    private Boolean jueyu;

    @Column(name ="adopted")
    private Boolean adopted;

    @Column(name = "fed")
    private Boolean feed;

    public enum Health {
        HEALTHY, SICK
    }
    


    protected Cat() {
    }

    public Cat( String name, int age, String type, String health, Boolean jueyu, Boolean adopted) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.health = health;
        this.jueyu = jueyu;
        this.adopted = adopted;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getType() {
        return type;
    }

    public String getHealth() {
        return health;
    }

    public Boolean getJueyu() {
        return jueyu;
    }

    public Boolean getAdopted() {
        return adopted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public void setJueyu(Boolean jueyu) {
        this.jueyu = jueyu;
    }

    public void setAdopted(Boolean adopted) {
        this.adopted = adopted;
    }

    public void feed() {
        feed=true;
    }

}
