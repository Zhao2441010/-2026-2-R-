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

    public List<TU> findByuid(Long uid);
    public List<TU> findBytid(Long tid);

    @Modifying
    public void deleteById(Long id);

    @Modifying
    @Query("delete from TU t where t.taskId =:tid and t.userId =: uid")
    public void deleteByTU(@Param("tid") Long tid, @Param("uid") Long uid);

    @Query("select count(t) from TU t where t.taskId=:taskid")
    public int countVolunteerByTaskId(Long taskid);

}
