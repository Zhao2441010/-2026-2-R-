package com.example.demo.entity;


import jakarta.persistence.*;
import java.util.Date;


import com.example.demo.entity.UCid;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "adopt")
public class Adopt {
    
    @EmbeddedId
    private UCid uc;


    @Setter@Getter
    @Column(name = "adopt_date",nullable = false)
    @Temporal(TemporalType.DATE)
    //注解存储精度 date日期, time时间, timestamp日期和时间
    private Date adoptDate;



    protected Adopt() {
    }

    public Adopt( Long userId, Long catId,Date adoptDate) {
        this.uc = new UCid(userId,catId);
        this.adoptDate = adoptDate;

    }

    public Long getUserId() {
        return uc.getUserid();
    }

    public Long getCatId() {
        return uc.getCatid();
    }



}
