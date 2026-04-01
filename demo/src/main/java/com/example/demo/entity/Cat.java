package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "cat")


public class Cat {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter@Setter
    @Column(name = "name",nullable = false,length = 255)
    private String name;

    @Getter@Setter
    @Column(name = "age")
    private int age;

    @Getter
    @Column(name = "type")
    private String type;

    @Getter@Setter
    @Column(name="health")
    private String health;

    @Setter @Getter
    @Column(name = "jueyu",length = 255)
    private Boolean jueyu;

    @Getter@Setter
    @Column(name ="adopted")
    private Boolean adopted;

    @Getter@Setter
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



}
