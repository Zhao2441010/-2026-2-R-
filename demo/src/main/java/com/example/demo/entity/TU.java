package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Entity
public class TU {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name="user_id")
    private Long userId;

    @Getter
    @Column(name="task_id")
    private Long taskId;

    protected TU(){}

    public TU(Long tid,Long uid) {
        this.taskId=tid;
        this.userId=uid;
    }

}
