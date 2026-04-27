package com.example.demo.repository;

import com.example.demo.entity.TU;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TURepository extends JpaRepository<TU,Long> {

    @Query("select t from TU t where t.userId=:uid")
    public List<TU> findByuid(@Param("uid") Long uid);

    @Query("select t from TU t where t.taskId=:tid")
    public List<TU> findBytid(@Param("tid") Long tid);

    @Modifying
    @Query("delete from TU t where t.taskId =:tid and t.userId =:uid")
    public void deleteByTU(@Param("tid") Long tid, @Param("uid") Long uid);

    @Modifying
    public void deleteById(@Param("id") Long id);

    @Query("select count(t) from TU t where t.taskId=:taskid")
    public int countVolunteerByTaskId(Long taskid);

    @Query("select t from TU t where t.taskId=:tid and t.userId=:uid")
    public TU findBytidAnduid(@Param("tid") Long tid, @Param("uid") Long uid);

}
