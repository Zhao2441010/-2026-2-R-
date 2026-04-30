package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * 管理员登录页 / 后台首页
     */
    @GetMapping({"", "/", "/index"})
    public String adminPage() {
        return "admin";
    }

    /**
     * 猫咪管理页面
     */
    @GetMapping("/cats")
    public String catManagement() {
        return "admin-cats";
    }

    /**
     * 志愿活动管理页面
     */
    @GetMapping("/tasks")
    public String volunteerManagement() {
        return "admin-tasks";
    }

    /**
     * 申请信息管理页面
     */
    @GetMapping("/logs")
    public String applicationManagement() {
        return "admin-logs";
    }
}