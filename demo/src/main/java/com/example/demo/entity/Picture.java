package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "picture")
public class Picture {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter@Setter
    @Column(name = "cat_id")
    private Long catId;

    @Getter@Setter
    @Column(name = "url", length = 255)
    private String url;

    @Getter@Setter
    @Column(name = "display")
    private Boolean display=false;

    protected Picture() {
    }

    public Picture(Long catId, String url) {
        this.catId = catId;
        this.url = url;
        this.display = false;
    }

}
