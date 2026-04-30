package com.example.demo.service;

import java.util.List;
import java.util.Date;
import com.example.demo.entity.Log;

public interface LogService {
//我自己的log主要是记录应该通知管理员并追溯解决状态的事件


    public void saveLog(Long userId,Long catId,Date submitDate,Date SolvedDate,String descriptoin,String process);

//  发起请求
    void logFor(Long user_id,Long cat_id,String message,Date submitDate);

    void logForEnroll(Long user_id,Long picture_id,Date submitDate);

 
//  处理请求
    
    void setApprove(Long adopt_id,Date solveDate);
    void setReject(Long adopt_id,Date solveDate);


//  查看进程

    List<Log> querryMySubmissions(Long user_id);

    String querryMyProcesses(Long task_id);

    List<Log> querryUnsolvedProcesses();
    List<Log> querryAllProcesses();

    boolean checkUserTodayUpload(Long uid,java.util.Date date);

//  警告类
//  1.某只猫当天没有人喂
//  2.领养者一周以上没有上传最近动态

    void notifyforFeed(Long cat_id,String message,Date submitDate);

    void notifyforLongTimeNoUpload(Long user_id,Long cat_id,String message,Date submitDate);


    int countUnsolvedProcesses();
    int countAllProcesses();


//  清理过期信息
    void clearExpiredInfo();



}
