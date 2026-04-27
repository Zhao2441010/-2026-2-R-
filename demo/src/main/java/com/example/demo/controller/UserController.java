package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * POST /api/user/register
     * 参数: username, phonenumber, password
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        String username = request.get("username");
        String phonenumber = request.get("phonenumber");
        String password = request.get("password");

        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "用户名不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        if (phonenumber == null || phonenumber.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "手机号不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        if (password == null || password.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "密码不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        // 检查用户名是否已存在
        if (userService.existsByPhonenumber(phonenumber)) {
            response.put("success", false);
            response.put("message", "手机号已被使用");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            userService.saveUser(username,"MALE",phonenumber,password);
            response.put("success", true);
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 用户登录
     * POST /api/user/login
     * 参数: username, password
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request,
                                                     HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String phonenumber = request.get("phonenumber");
        String password = request.get("password");

        if (phonenumber == null || password == null) {
            response.put("success", false);
            response.put("message", "手机号和密码不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        // 验证用户
        User user = userService.getUserByPhonenumber(phonenumber);
        if (user == null) {
            response.put("success", false);
            response.put("message", "手机号或密码错误");
            return ResponseEntity.status(401).body(response);
        }

        // 验证密码
        if (!password.equals(user.getPassword())) {
            response.put("success", false);
            response.put("message", "手机号或密码错误");
            return ResponseEntity.status(401).body(response);
        }

        // 登录成功，将用户信息存入 Session
        session.setAttribute("currentUser", user);

        // 返回用户信息（不包含密码）
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("phoneNumber", user.getPhoneNumber());
        userInfo.put("gender", user.getGender());
        userInfo.put("age", user.getAge());
        userInfo.put("address", user.getAddress());
        userInfo.put("realname", user.getRealname());

        response.put("success", true);
        response.put("message", "登录成功");
        response.put("user", userInfo);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取当前登录用户
     * GET /api/user/current
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.put("success", false);
            response.put("message", "未登录");
            return ResponseEntity.status(401).body(response);
        }

        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("phoneNumber", user.getPhoneNumber());
        response.put("gender", user.getGender());
        response.put("age", user.getAge());
        response.put("address", user.getAddress());
        response.put("realname", user.getRealname());
        return ResponseEntity.ok(response);
    }

    /**
     * 用户退出登录
     * POST /api/user/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        session.invalidate();
        response.put("success", true);
        response.put("message", "退出成功");
        return ResponseEntity.ok(response);
    }
}
