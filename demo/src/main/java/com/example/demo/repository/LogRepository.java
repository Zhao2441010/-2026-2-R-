package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

    @Modifying
    @Query("UPDATE Log SET process = :process , solveDate = :solveDate WHERE id = :id")
    public void updateProcess(
        @Param("id") Long id,
        @Param("process") String process,
        @Param("solveDate") java.util.Date solveDate
    );




}