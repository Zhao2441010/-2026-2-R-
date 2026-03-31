package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "health")
public class Health {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cat_id")
    private Long catId;

    @Column(name = "health_status")
    private String healthStatus;

    @Column(name = "check_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date checkDate;

    @Column(name="hospital",length = 255)
    private String hospital;

    protected Health() {
    }

    public Health(Long catId, String healthStatus, java.util.Date checkDate, String hospital) {
        this.catId = catId;
        this.healthStatus = healthStatus;
        this.checkDate = checkDate;
        this.hospital = hospital;
    }

}
