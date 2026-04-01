package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Getter
    @Column(name = "user_id")
    private Long userId;

    @Getter
    @Column(name = "content")
    private String content;

    @Getter
    @Column(name="time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Getter
    @Setter
    @Column(name="seen")
    private Boolean seen;

    protected Message() {
    }

    public Message( Long userId, String content, Date time) {
        this.userId = userId;
        this.content = content;
        this.time = time;
        this.seen = false;

    }



}
