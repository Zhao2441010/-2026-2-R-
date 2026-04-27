package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.service.TaskService;
import com.example.demo.service.TUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TUService tuService;

    // ========== 查询接口 ==========

    /**
     * 获取前10个活跃任务（首页展示）
     */
    @GetMapping("/active/top10")
    public ResponseEntity<List<Map<String, Object>>> getTop10ActiveTasks() {
        List<Task> tasks = taskService.findTop10Active();
        List<Map<String, Object>> result = tasks.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有活跃任务
     */
    @GetMapping("/active/all")
    public ResponseEntity<List<Map<String, Object>>> getAllActiveTasks() {
        List<Task> tasks = taskService.findActive();
        List<Map<String, Object>> result = tasks.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有任务（包含已结束）
     */
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllTasks() {
        List<Task> tasks = taskService.querryAllEvent();
        List<Map<String, Object>> result = tasks.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取单个任务详情
     */
    @GetMapping("/{tid}")
    public ResponseEntity<Map<String, Object>> getTaskDetail(@PathVariable Long tid) {
        Task task = taskService.findTaskById(tid); // 假设 TaskService 有 findById 方法
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToMap(task));
    }

    /**
     * 检查当前用户是否已报名该任务，并返回当前报名人数
     */
    @GetMapping("/{tid}/check-register")
    public ResponseEntity<Map<String, Object>> checkRegister(
            @PathVariable Long tid,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        Long uid = user != null ? user.getId() : null;

        // 获取当前报名人数
        int currentNum = tuService.countVolunteerByTaskId(tid);
        result.put("currentNum", currentNum);

        if (uid == null) {
            result.put("registered", false);
            return ResponseEntity.ok(result);
        }

        boolean registered = tuService.checkRegister(uid, tid);
        result.put("registered", registered);
        return ResponseEntity.ok(result);
    }

    // ========== 报名/取消报名接口 ==========

    /**
     * 报名任务
     */
    @PostMapping("/{tid}/register")
    public ResponseEntity<Map<String, Object>> registerTask(
            @PathVariable Long tid,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        Long uid = user != null ? user.getId() : null;

        if (uid == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        // 检查是否已报名
        if (tuService.checkRegister(uid, tid)) {
            result.put("success", false);
            result.put("message", "您已经报名过该任务了");
            return ResponseEntity.badRequest().body(result);
        }

        // 检查是否已满
        Task task = taskService.findTaskById(tid);
        if (task == null) {
            result.put("success", false);
            result.put("message", "任务不存在");
            return ResponseEntity.badRequest().body(result);
        }

        int currentNum = tuService.countVolunteerByTaskId(tid);
        if (task.getNeed() <= currentNum) {
            result.put("success", false);
            result.put("message", "该任务报名人数已满");
            return ResponseEntity.badRequest().body(result);
        }

        // 执行报名
        try {
            tuService.uidRegisterFortid(uid, tid);
            result.put("success", true);
            result.put("message", "报名成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "报名失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 取消报名
     */
    @DeleteMapping("/{tid}/cancel")
    public ResponseEntity<Map<String, Object>> cancelTask(
            @PathVariable Long tid,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        Long uid = user != null ? user.getId() : null;

        if (uid == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        // 检查是否已报名
        if (!tuService.checkRegister(uid, tid)) {
            result.put("success", false);
            result.put("message", "您尚未报名该任务");
            return ResponseEntity.badRequest().body(result);
        }

        // 执行取消报名
        try {
            tuService.deleteByTU(tid, uid);
            result.put("success", true);
            result.put("message", "取消报名成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "取消报名失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // ========== 辅助方法 ==========

    /**
     * 将 Task 实体转换为前端需要的 Map 格式
     */
    private Map<String, Object> convertToMap(Task task) {
        Map<String, Object> map = new HashMap<>();
        map.put("tid", task.getId());
        map.put("tname", task.getDescription());
        map.put("need", task.getNeed());
        map.put("taskTime", task.getEventdate());;
        map.put("description", task.getDescription());
        map.put("ended", task.isEnded()); // 假设 Task 有 isEnded 方法

        // 计算当前报名人数
        int currentNum = tuService.countVolunteerByTaskId(task.getId());
        map.put("currentNum", currentNum);

        return map;
    }
}