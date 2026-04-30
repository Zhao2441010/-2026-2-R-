package com.example.demo.controller;

import com.example.demo.service.CatService;
import com.example.demo.service.LogService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    // 注入你的 Service
     @Autowired private CatService catService;
     @Autowired private TaskService taskService;
     @Autowired private LogService logService;

    /**
     * 获取当前猫咪数量
     */
    @GetMapping("/cats/count")
    public Map<String, Object> getCatCount() {
        Map<String, Object> result = new HashMap<>();
         result.put("count", catService.countAll());

        return result;
    }

    /**
     * 获取当前志愿活动数量
     */
    @GetMapping("/volunteers/count")
    public Map<String, Object> getVolunteerCount() {
        Map<String, Object> result = new HashMap<>();
         result.put("count", taskService.countAll());
        return result;
    }

    /**
     * 获取待处理申请数量
     */
    @GetMapping("/applications/pending-count")
    public Map<String, Object> getPendingApplicationCount() {
        Map<String, Object> result = new HashMap<>();
         result.put("count", logService.countUnsolvedProcesses());
        return result;
    }
}