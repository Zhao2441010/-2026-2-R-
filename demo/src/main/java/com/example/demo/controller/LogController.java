package com.example.demo.controller;

import com.example.demo.entity.Log;
import com.example.demo.entity.User;
import com.example.demo.service.LogService;
import com.example.demo.service.TUService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")  // 允许跨域，生产环境请配置具体域名
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private TUService tuService;

    /**
     * 获取所有日志
     */
    @GetMapping("/all")
    public ResponseEntity<List<Log>> getAllLogs() {
        List<Log> logs = logService.querryAllProcesses();
        return ResponseEntity.ok(logs);
    }

    /**
     * 获取未处理的日志
     */
    @GetMapping("/unsolved")
    public ResponseEntity<List<Log>> getUnsolvedLogs() {
        List<Log> logs = logService.querryUnsolvedProcesses();
        return ResponseEntity.ok(logs);
    }

    /**
     * 根据用户ID查询日志
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Log>> getLogsByUser(@PathVariable Long userId) {
        List<Log> logs = logService.querryMySubmissions(userId);
        return ResponseEntity.ok(logs);
    }

    /**
     * 根据ID查询单条日志状态
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getLogStatus(@PathVariable Long id) {
        String status = logService.querryMyProcesses(id);
        return ResponseEntity.ok(status);
    }

    /**
     * 设置日志为已通过（APPROVE）
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveLog(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {

        Date solveDate = new Date(); // 默认当前时间
        if (body != null && body.containsKey("solveDate")) {
            // 如果前端传了时间，可以在这里解析
        }

        logService.setApprove(id, solveDate);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "已标记为已通过");
        result.put("id", id);
        result.put("status", "APPROVE");
        result.put("solveDate", solveDate);

        return ResponseEntity.ok(result);
    }

    /**
     * 设置日志为已拒绝（REJECT）
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectLog(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {

        Date solveDate = new Date(); // 默认当前时间

        logService.setReject(id, solveDate);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "已标记为已拒绝");
        result.put("id", id);
        result.put("status", "REJECT");
        result.put("solveDate", solveDate);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/suggestion")
    public ResponseEntity<Map<String, Object>> submitSuggestion(
            @RequestBody Map<String, String> body,
            HttpSession session) {

        // 从 Session 获取当前登录用户
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "请先登录后再提交建议");
            return ResponseEntity.status(401).body(result);
        }

        Long userId = user.getId();
        String username = user.getUsername();
        String suggestion = body.get("suggestion");

        Date today = new Date();
        today.setTime(today.getTime());

        // 检查今天是否已经上传过
        boolean checkResult = logService.checkUserTodayUpload(userId,today);
        if (checkResult) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "今天已经上传，请明天再来");
            return ResponseEntity.ok(result);
        }

        if (suggestion == null || suggestion.trim().isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "建议内容不能为空");
            return ResponseEntity.badRequest().body(result);
        }

        // 限制建议长度
        if (suggestion.length() > 500) {
            suggestion = suggestion.substring(0, 500);
        }

        Date submitDate = new Date();
        String description = "【用户建议】" + (username != null ? "来自 " + username + "：" : "：") + suggestion;

        // 保存为 Log，catId 设为 -1 表示这是建议类型日志
        logService.saveLog(userId, -1L, submitDate, null, description, "SUGGESTION");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "感谢您的建议！我们会认真考虑 💕");
        result.put("submitDate", submitDate);

        return ResponseEntity.ok(result);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", logService.countAllProcesses());
        stats.put("pending", logService.countUnsolvedProcesses());
        stats.put("approve", logService.countAllProcesses() - logService.countUnsolvedProcesses()); // 近似值
        // 更精确的做法是在 Service 层增加 countByStatus 方法
        return ResponseEntity.ok(stats);
    }

    /**
     * 清理过期日志（30天前）
     */
    @DeleteMapping("/expired")
    public ResponseEntity<Map<String, Object>> clearExpiredLogs() {
        logService.clearExpiredInfo();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "已清理过期日志");

        return ResponseEntity.ok(result);
    }
}