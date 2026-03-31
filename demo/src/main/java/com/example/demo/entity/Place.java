package com.example.demo.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "Place")
public class Place {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cat_id")
    private Long catId;

    @Column(name="place")
    private String place;

    protected Place() {
    }

    public Place(Long catId, String place) {
        this.catId = catId;
        this.place = place;
    }
}
