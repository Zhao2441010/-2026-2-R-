package com.example.demo.service;

public interface LogService {
//我自己的log主要是记录应该通知管理员并追溯解决状态的事件


//  发起请求
    void logforAdopt(Long user_id,Long cat_id,String message);

    void logforUpload(Long user_id,Long cat_id,String message);

    void logforEnroll(Long user_id,Long picture_id,String message);

 
//  处理请求
    
    void approveforAdopt(Long adopt_id,String message);
    void rejectforAdopt(Long adopt_id,String message);


    void approveforUpload(Long upload_id,String message);
    void rejectforUpload(Long upload_id,String message);

    void approveforEnroll(Long enroll_id,String message);
    void rejectforEnroll(Long enroll_id,String message);


//  处理审核的通知
    void notifyforApplication(Long adopt_id,String message);

//  查看进程

    void querryMySubmissions(Long user_id);

    void querryMyProcesses(Long task_id);

    void querryUnsolvedProcesses();


//  警告类
//  1.某只猫当天没有人喂
//  2.领养者一周以上没有上传最近动态

    void notifyforFeed(Long cat_id,String message);

    void notifyforLongTimeNoUpload(Long user_id,Long cat_id,String message);


//  清理过期信息
    void clearExpiredInfo();



}
