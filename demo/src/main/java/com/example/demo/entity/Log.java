package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "log")
public class Log {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "cat_id")
    private Long catId;

    @Getter
    @Column(name = "user_id")
    private Long userId;

    @Getter
    @Column(name = "submit_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date submitDate;

    @Getter@Setter
    @Column(name = "solve_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date solveDate;

    @Getter
    @Column(name="description")
    private String description;

    @Getter@Setter
    @Column(name="solved")
    private Boolean solved;

    @Getter@Setter
    @Column(name="status")
    private String status;
 
    protected Log() { 
    }

    public Log(Long uid,Long cid,String description, java.util.Date submitDate, java.util.Date solveDate,String status) {
        this.userId = uid;
        this.catId = cid;
        this.description = description;
        this.submitDate = submitDate;
        this.solveDate = solveDate;
        this.solved = false ;
        this.status = status;
    }

}
