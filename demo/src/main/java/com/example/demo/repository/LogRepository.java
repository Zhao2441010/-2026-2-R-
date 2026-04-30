package com.example.demo.repository;

import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Log;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Modifying
    @Query("UPDATE Log SET solved = true ,status=:status, solveDate = :solveDate WHERE id = :id")
    public void setStatus(
        @Param("id") Long id,
        @Param("status")  String status,
        @Param("solveDate") java.util.Date solveDate
    );

    @Query("select l from Log l where l.solved = false order by l.submitDate ASC")
    List<Log> findUnsolvedLogs();

    @Query("select l from Log l order by l.submitDate DESC")
    List<Log> findAllLogs();

    @Query("select count(l) from Log l where l.solved = false")
    int countUnsolvedLogs();

    @Query("select count(l) from Log l")
    int countAllLogs();

    @Query("select count(l) from Log l where l.userId=:uid and l.submitDate=:date")
    int checkUserTodayUpload(@Param("uid") Long uid, @Param("date") java.util.Date date);

    List<Log> findByUserId(Long userId);

    List<Log> findByCatId(Long catId);

    @Modifying
    @Query("delete from Log l where l.solved = true and l.submitDate < :cutoffDate")
    void deleteOldLogs(@Param("cutoffDate") Date cutoffDate);



}