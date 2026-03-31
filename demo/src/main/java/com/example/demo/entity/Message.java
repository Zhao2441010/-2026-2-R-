package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "user_id")
    private Long userId;


    @Column(name = "content", length = 255)
    private String content;

    protected Message() {
    }

    public Message( Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public Long setUserId(Long userId) {
        this.userId = userId;
        return userId;
    }

    public String setContent(String content) {
        this.content = content;
        return content;
    }

}
