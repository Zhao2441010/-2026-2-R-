package com.example.demo.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "picture")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cat_id")
    private Long catId;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "display")
    private Boolean display=false;

    protected Picture() {
    }

    public Picture(Long catId, String url) {
        this.catId = catId;
        this.url = url;
        this.display = false;
    }

    public String getUrl() {
        return url;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

}
