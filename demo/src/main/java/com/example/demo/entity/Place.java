package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Place")
public class Place {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "cat_id")
    private Long catId;

    @Getter
    @Column(name="place")
    private String place;

    protected Place() {
    }

    public Place(Long catId, String place) {
        this.catId = catId;
        this.place = place;
    }
}
