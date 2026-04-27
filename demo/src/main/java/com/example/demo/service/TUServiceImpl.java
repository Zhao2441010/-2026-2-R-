package com.example.demo.service;


import com.example.demo.entity.TU;
import com.example.demo.repository.TURepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TUServiceImpl implements TUService {

    private final TURepository tuRepository;
    public TUServiceImpl(TURepository tuRepository) {
        this.tuRepository = tuRepository;
    }


    @Override
    @Transactional
    public void uidRegisterFortid(Long uid, Long tid) {
        tuRepository.save(new TU(tid, uid));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        tuRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByTU(Long tid,Long uid) {
        tuRepository.deleteByTU(tid,uid);
    }

    @Override
    public List<TU> findTUByUserId(Long uid) {
        return List.of();
    }

    @Override
    public List<TU> findTUByTaskId(Long tid) {
        return List.of();
    }


    @Override
    public int countVolunteerByTaskId(Long taskid) {
        return tuRepository.countVolunteerByTaskId(taskid);
    }

    public boolean checkRegister(Long uid,Long tid){
        return tuRepository.findBytidAnduid(tid,uid)!=null;
    }

}
