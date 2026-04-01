package com.example.demo.service;

import com.example.demo.entity.TU;

import java.util.List;

public interface TUService {

    public List<TU> findEventByUserId(Long uid);
    public List<TU> findVolunteerByTaskId(Long tid);

    public void deleteById(Long id);

    public void deleteByTU(Long tid, Long uid);

    public int countVolunteerByTaskId(Long taskid);

}
