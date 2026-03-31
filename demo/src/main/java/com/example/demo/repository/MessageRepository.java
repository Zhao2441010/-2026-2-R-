package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.example.demo.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Modifying
    @Query("delete from Message m where m.id = :id")
    void deleteById(@Param("id") Long id);


}
