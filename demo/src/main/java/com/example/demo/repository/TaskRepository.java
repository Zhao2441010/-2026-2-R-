package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Modifying
    public void deleteById(Integer id);

    @Modifying
    @Query("update Task t set t.eventdate = :eventdate where t.id = :id")
    public void updateEventdateById(Integer id, String eventdate);

    @Modifying
    @Query("update Task t set t.have = t.have+1 where t.id = :id")
    void volunteerRegister(Long id);

    @Modifying
    @Query("update Task t set t.have = t.have-1 where t.id = :id")
    void volunteerUnregister(Long id);


    public List<Task> findByEventdate(Date today);

    @Query("select t from Task t where t.eventdate > :today")
    public List<Task> findFutureEvent(Date today);

    @Query("select t from Task t")
    public List<Task> findAllEvent();

}
