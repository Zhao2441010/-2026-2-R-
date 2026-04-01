package com.example.demo.service;

import com.example.demo.entity.Log;
import com.example.demo.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(Long userId,Long catId,Date submitDate,Date solvedDate,String descriptoin,String status){
        Log l=new Log(userId,catId,descriptoin,submitDate,solvedDate,status);
        logRepository.save(l);
    }

    @Override
    @Transactional
    public void logFor(Long user_id, Long cat_id, String message,Date submitDate) {
        saveLog(user_id,cat_id,submitDate,null,message,"PENDING");
    }

    @Override
    @Transactional
    public void logForEnroll(Long user_id, Long picture_id,Date submitDate) {
        saveLog(user_id,picture_id,submitDate,null,"Apply for enroll with pictureId", "PENDING");
    }

    @Override
    @Transactional
    public void setApprove(Long adopt_id, Date solveDate) {
        logRepository.setStatus(adopt_id, "APPROVE", solveDate);
    }

    @Override
    @Transactional
    public void setReject(Long adopt_id, Date solveDate) {
        logRepository.setStatus(adopt_id, "REJECT", solveDate);
    }




    @Override
    public List<Log> querryMySubmissions(Long user_id) {
        return logRepository.findByUserId(user_id);
    }

    @Override
    public String querryMyProcesses(Long task_id) {
        return logRepository.findById(task_id).map(Log::getStatus).orElse("Task not found");
    }

    @Override
    public List<Log> querryUnsolvedProcesses() {
        return logRepository.findUnsolvedLogs();
    }

    @Override
    public List<Log> querryAllProcesses() {
        return logRepository.findAllLogs();
    }

    @Override
    @Transactional
    public void notifyforFeed(Long cat_id,String message,Date submitDate) {
        saveLog(null,cat_id,submitDate,null,message,"PENDING");
    }

    @Override
    @Transactional
    public void notifyforLongTimeNoUpload(Long user_id, Long cat_id, String message,Date submitDate) {
        saveLog(user_id,cat_id,submitDate,null,message,"PENDING");
    }

    @Override
    @Transactional
    public void clearExpiredInfo() {
        Date cutoffDate = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000); // 30天前
        logRepository.deleteOldLogs(cutoffDate);
    }

    
    @Override
    public int countUnsolvedProcesses(){
        return logRepository.countUnsolvedLogs();
    }

    @Override
    public int countAllProcesses(){
        return logRepository.countAllLogs();
    }

}