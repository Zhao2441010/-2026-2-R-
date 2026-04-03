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
    public void deleteById(Long id);

    @Modifying
    @Query("update Task t set t.eventdate = :eventdate where t.Id = :id")
    public void updateEventdateById(Long id, Date eventdate);

    @Modifying
    @Query("update Task t set t.have = t.have+1 where t.Id = :id")
    void volunteerRegister(Long id);

    @Modifying
    @Query("update Task t set t.have = t.have-1 where t.Id = :id")
    void volunteerUnregister(Long id);


    public List<Task> findByEventdate(Date today);

    @Query("select t from Task t where t.eventdate > :today order by t.eventdate asc")
    public List<Task> findFutureEvent(Date today);

    @Query("select t from Task t order by t.eventdate desc")
    public List<Task> findAllEvent();

    Task findById(Long id);
}
