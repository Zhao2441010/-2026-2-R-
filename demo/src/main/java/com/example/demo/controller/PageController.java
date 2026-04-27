package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 首页
    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }

    // 全部猫咪页
    @GetMapping("/all-cats")
    public String allCats() {
        return "redirect:/all-cats";
    }
}