package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "health")
public class Health {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "cat_id")
    private Long catId;

    @Getter@Setter
    @Column(name = "health_status")
    private String healthStatus;

    @Getter@Setter
    @Column(name = "check_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date checkDate;

    @Getter@Setter
    @Column(name="hospital")
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
