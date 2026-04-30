package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 从 Session 中获取当前登录用户的 ID
     * UserController 中使用的是 "currentUser" 作为 key
     */
    private Long getCurrentUserId(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    /**
     * 获取当前登录用户的未读消息数量
     */
    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(HttpSession session) {
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return ResponseEntity.ok(Map.of("count", 0));
        }
        int count = messageService.countUncheckMessage(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 获取当前登录用户的所有消息
     * 可选参数：status=all/unread/read 筛选状态
     */
    @GetMapping("/list")
    public ResponseEntity<?> getMessages(
            @RequestParam(defaultValue = "all") String status,
            HttpSession session) {

        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "请先登录"));
        }

        List<Message> messages;
        switch (status) {
            case "unread":
                messages = messageService.showUncheckMessage(userId);
                break;
            case "read":
                messages = messageService.showAllMessages(userId).stream()
                        .filter(Message::getSeen)
                        .toList();
                break;
            default:
                messages = messageService.showAllMessages(userId);
                break;
        }

        return ResponseEntity.ok(Map.of("success", true, "data", messages));
    }

    /**
     * 将消息标记为已读
     */
    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, HttpSession session) {
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "请先登录"));
        }

        messageService.setSeen(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    /**
     * 删除消息
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id, HttpSession session) {
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "请先登录"));
        }

        messageService.deleteMessage(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
}