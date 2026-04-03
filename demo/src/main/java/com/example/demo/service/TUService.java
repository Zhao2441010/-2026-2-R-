package com.example.demo.service;

import com.example.demo.entity.TU;

import java.util.List;

public interface TUService {

    uidRegisterFortid(Long uid,Long tid)

    public List<TU> findTUByUserId(Long uid);
    public List<TU> findTUByTaskId(Long tid);

    public void deleteById(Long id);

    public void deleteByTU(Long tid, Long uid);

    public int countVolunteerByTaskId(Long taskid);

}
