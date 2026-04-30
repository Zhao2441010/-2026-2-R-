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
    private Boolean display = false;

    // ========== 新增：上传者ID ==========
    @Getter@Setter
    @Column(name = "uploader_id")
    private Long uploaderId;

    protected Picture() {
    }

    public Picture(Long catId, String url) {
        this.catId = catId;
        this.url = url;
        this.display = false;
    }

    // ========== 新增：带上传者的构造方法 ==========
    public Picture(Long catId, String url, Long uploaderId) {
        this.catId = catId;
        this.url = url;
        this.uploaderId = uploaderId;
        this.display = false;
    }
}