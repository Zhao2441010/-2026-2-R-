package com.example.demo.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id", insertable = false, updatable = false)
    private Cat cat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "submit_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date submitDate;

    @Column(name = "solve_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date solveDate;

    @Column(name="description")
    private String description;


    @Column(name="process")
    private String process;

 
    protected Log() { 
    }

    public Log(String description, java.util.Date submitDate, java.util.Date solveDate, String process) {
        this.description = description;
        this.submitDate = submitDate;
        this.solveDate = solveDate;
        this.process = process;
    }



}
