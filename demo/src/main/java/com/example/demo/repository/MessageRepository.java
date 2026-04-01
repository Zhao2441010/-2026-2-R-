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

    @Query("select m from Message m where m.userId = :receiverId order by m.time DESC")
    java.util.List<Message> findAllByReceiverId(@Param("receiverId") Long receiverId);


    @Query("select m from Message m where m.userId = :receiverId and m.seen=false order by m.time DESC")
    java.util.List<Message> findUnseenByReceiverId(@Param("receiverId") Long receiverId);

    @Query("select count(m) from Message m where m.userId = :receiverId and m.seen=false")
    int countUncheckMessage(@Param("receiverId") Long receiverId);

    @Query("select count(m) from Message m where m.userId = :receiverId")
    int countAllMessage(@Param("receiverId") Long receiverId);

}
