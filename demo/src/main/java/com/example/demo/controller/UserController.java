package com.example.demo.controller;

import com.example.demo.entity.Admin;
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
     * 参数: phonenumber, password
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

        // 判断角色：Admin 子类返回 ADMIN，否则 USER
        String role = (user instanceof Admin) ? "ADMIN" : "USER";

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
        response.put("role", role);           // ← 新增：返回角色
        response.put("user", userInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // 1. 检查登录状态
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "未登录");
            return ResponseEntity.status(401).body(response);
        }

        // 2. 权限校验：只能修改自己的信息，ADMIN 可以修改任意用户
        boolean isAdmin = currentUser instanceof Admin;
        if (!isAdmin && !currentUser.getId().equals(id)) {
            response.put("success", false);
            response.put("message", "无权修改他人信息");
            return ResponseEntity.status(403).body(response);
        }

        // 3. 根据ID从数据库查询用户
        User targetUser = userService.getUserById(id);
        if (targetUser == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return ResponseEntity.badRequest().body(response);
        }

        // 4. 获取并验证参数
        String username = (String) request.get("username");
        String realname = (String) request.get("realname");
        String gender = (String) request.get("gender");
        Integer age = request.get("age") != null ? Integer.valueOf(request.get("age").toString()) : null;
        String address = (String) request.get("address");

        // 基础校验
        if (username == null || username.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "用户名不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 5. 更新用户信息（根据ID更新，手机号不可修改）
            targetUser.setUsername(username);
            if (realname != null) targetUser.setRealname(realname);
            if (gender != null) targetUser.setGender(gender);
            if (age != null) targetUser.setAge(age);
            if (address != null) targetUser.setAddress(address);

            // 保存到数据库
            userService.updateUser(targetUser);

            // 6. 如果修改的是当前登录用户，同步更新 Session
            if (currentUser.getId().equals(id)) {
                session.setAttribute("currentUser", targetUser);
            }

            response.put("success", true);
            response.put("message", "修改成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "修改失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
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

        // 判断角色
        String role = (user instanceof Admin) ? "ADMIN" : "USER";

        response.put("success", true);        // ← 新增：统一返回格式
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("phoneNumber", user.getPhoneNumber());
        response.put("gender", user.getGender());
        response.put("age", user.getAge());
        response.put("address", user.getAddress());
        response.put("realname", user.getRealname());
        response.put("role", role);           // ← 新增：返回角色
        response.put("dailyUploadCount",user.getDailyUploadCount());
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