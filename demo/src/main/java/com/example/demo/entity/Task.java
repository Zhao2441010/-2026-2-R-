package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
public class Task {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Getter@Setter
    @Column(name="description")
    private String description;

    @Getter@Setter
    @Column(name="eventdate")
    @Temporal(TemporalType.DATE)
    private Date eventdate;


    @Getter@Setter
    @Column(name="need")
    private Long need;

    protected Task() {
    }

    public Task(String description, Date eventdate,Long need) {
        this.description = description;
        this.eventdate=eventdate;
        this.need=need;
    }



}
